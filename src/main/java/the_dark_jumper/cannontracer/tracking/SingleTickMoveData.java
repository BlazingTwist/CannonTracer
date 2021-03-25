package the_dark_jumper.cannontracer.tracking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.HashMap;
import java.util.Optional;
import jumpercommons.SimpleLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import org.lwjgl.opengl.GL11;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.configsaving.Color;
import the_dark_jumper.cannontracer.configsaving.TrackingDataEntry;
import the_dark_jumper.cannontracer.settings.SinglePlayerSettings;

public class SingleTickMoveData {
	@JsonIgnore
	public final EntityTracker entityTracker;

	//contains whether tick was an explosion-tick or not
	@JsonProperty("tickData")
	public HashMap<String, HashMap<Integer, Boolean>> tickData = new HashMap<>();

	@JsonProperty("pos1")
	public SimpleLocation pos1;

	@JsonProperty("pos2")
	public SimpleLocation pos2;

	@JsonProperty("tickOffset")
	public int tickOffset = 0;

	/**
	 * Jackson constructor
	 */
	private SingleTickMoveData() {
		entityTracker = Main.getInstance().entityTracker;
	}

	public SingleTickMoveData(EntityTracker entityTracker, SimpleLocation pos1, SimpleLocation pos2) {
		this.entityTracker = entityTracker;
		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	public SingleTickMoveData copy() {
		SingleTickMoveData moveData = new SingleTickMoveData(entityTracker, pos1.copy(), pos2.copy());
		HashMap<String, HashMap<Integer, Boolean>> copy = new HashMap<>();
		for (String key : tickData.keySet()) {
			HashMap<Integer, Boolean> copyMap = new HashMap<>();
			HashMap<Integer, Boolean> originalMap = tickData.get(key);
			for (int tickKey : originalMap.keySet()) {
				copyMap.put(tickKey, originalMap.get(tickKey));
			}
			copy.put(key, copyMap);
		}
		moveData.tickData = copy;
		return moveData;
	}

	public boolean isSameData(SimpleLocation a, SimpleLocation b) {
		return pos1.equals(a) && pos2.equals(b);
	}

	public void addTick(String entityName, int tick, boolean isLastTick) {
		if (!tickData.containsKey(entityName)) {
			tickData.put(entityName, new HashMap<>());
		}
		HashMap<Integer, Boolean> ticks = tickData.get(entityName);
		if (!ticks.containsKey(tick)) {
			ticks.put(tick, isLastTick);
		}
	}

	public void setupDrawingBuffer(BufferBuilder bufferBuilder, TrackingDataEntry trackingData, String entityName, boolean renderLines) {
		GL11.glLineWidth(trackingData.getThickness());
		if(renderLines){
			handleDrawLines(bufferBuilder, trackingData);
		}
		handleDrawHitBox(bufferBuilder, trackingData, entityName);
	}

	private void handleDrawLines(BufferBuilder bufferBuilder, TrackingDataEntry trackingData) {
		Color color = trackingData.getColor();

		//always y1->y2 first
		drawPosTransparent(bufferBuilder, pos1.x, pos1.y, pos1.z);
		drawPos(bufferBuilder, pos1.x, pos2.y, pos1.z, color);

		//x1->x2 next if difference is greater, otherwise z1->z2
		if (Math.abs(pos2.x - pos1.x) >= Math.abs(pos2.z - pos1.z)) {
			drawPos(bufferBuilder, pos2.x, pos2.y, pos1.z, color);
		} else {
			drawPos(bufferBuilder, pos1.x, pos2.y, pos2.z, color);
		}
		drawPos(bufferBuilder, pos2.x, pos2.y, pos2.z, color);
	}

	private void handleDrawHitBox(BufferBuilder bufferBuilder, TrackingDataEntry trackingData, String entityName) {
		Optional<Boolean> isExplosionTick;
		switch (entityTracker.main.moduleManager.state) {
			case SINGLEPLAYER: {
				SinglePlayerSettings spSettings = entityTracker.main.singlePlayerSettings;
				if (!spSettings.renderBoxesGNS.get()) {
					return;
				}
				if (spSettings.modeGNS.get() == 2) { // Last X Seconds - mode
					int targetTick = spSettings.renderTickGNS.get() + tickOffset;
					isExplosionTick = Optional.ofNullable(tickData.get(entityName))
							.map(entityTickData -> entityTickData.get(targetTick));
				} else {
					isExplosionTick = Optional.of(false);
				}
				break;
			}
			case MULTIPLAYER: {
				int targetTick = entityTracker.main.multiPlayerSettings.renderTickGNS.get() + tickOffset;
				isExplosionTick = Optional.ofNullable(tickData.get(entityName))
						.map(entityTickData -> entityTickData.get(targetTick));
				break;
			}
			default:
				return;
		}

		if (!isExplosionTick.isPresent()) {
			return;
		}

		Color color = trackingData.getColor();
		if (isExplosionTick.get()) {
			color = color.copy().invert();
		}
		drawHitBox(bufferBuilder, pos2, trackingData.getHitBoxRadius(), color);
	}

	private static void drawHitBox(BufferBuilder bufferBuilder, SimpleLocation location, double radius, Color color) {
		double x0 = location.x - radius;
		double x1 = location.x + radius;
		double y0 = location.y - radius;
		double y1 = location.y + radius;
		double z0 = location.z - radius;
		double z1 = location.z + radius;

		drawHalfHitBox(bufferBuilder, color, x1, x0, y1, y0, z0, z1);
		drawHalfHitBox(bufferBuilder, color, x0, x1, y0, y1, z0, z1);
	}

	private static void drawHalfHitBox(BufferBuilder bufferBuilder, Color color, double x0, double x1, double y0, double y1, double z0, double z1) {
		drawPosTransparent(bufferBuilder, x1, y1, z0);
		drawPos(bufferBuilder, x0, y1, z0, color);
		drawPos(bufferBuilder, x0, y1, z1, color);
		drawPos(bufferBuilder, x1, y1, z1, color);

		drawPosTransparent(bufferBuilder, x1, y0, z0);
		drawPos(bufferBuilder, x1, y1, z0, color);
		drawPos(bufferBuilder, x1, y1, z1, color);
		drawPos(bufferBuilder, x1, y0, z1, color);
	}

	private static void drawPosTransparent(BufferBuilder bufferBuilder, double x, double y, double z) {
		bufferBuilder.pos(x, y, z).color(0, 0, 0, 0).endVertex();
	}

	private static void drawPos(BufferBuilder bufferBuilder, double x, double y, double z, Color color) {
		bufferBuilder.pos(x, y, z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
	}

	public void renderAxisText(EntityRendererManager rendererManager, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
		SimpleLocation delta = SimpleLocation.sub(pos2, pos1);
		renderText(rendererManager, "" + delta.y, matrixStack, buffer, pos1.x, pos1.y + (delta.y / 2), pos1.z);

		if (Math.abs(pos2.x - pos1.x) >= Math.abs(pos2.z - pos1.z)) {
			renderText(rendererManager, "" + delta.x, matrixStack, buffer, pos1.x + (delta.x / 2), pos2.y, pos1.z);
			renderText(rendererManager, "" + delta.z, matrixStack, buffer, pos2.x, pos2.y, pos1.z + (delta.z / 2));
		} else {
			renderText(rendererManager, "" + delta.z, matrixStack, buffer, pos1.x, pos2.y, pos1.z + (delta.z / 2));
			renderText(rendererManager, "" + delta.x, matrixStack, buffer, pos1.x + (delta.x / 2), pos2.y, pos2.z);
		}
	}

	private static void renderText(EntityRendererManager renderManager, String displayText, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, double x, double y, double z) {
		double cameraDistance = renderManager.getDistanceToCamera(x, y, z);
		if (cameraDistance <= 4096.0D) {
			matrixStackIn.push();
			matrixStackIn.translate(x, y, z);
			matrixStackIn.rotate(renderManager.getCameraOrientation());
			matrixStackIn.scale(-0.025f, -0.025f, 0.025f);
			Matrix4f matrix = matrixStackIn.getLast().getMatrix();
			float backgroundOpacity = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25f);
			int backgroundColor = (int) (backgroundOpacity * 255f) << 24;
			FontRenderer fontRenderer = renderManager.getFontRenderer();
			fontRenderer.renderString(displayText, 0, 0, 0x20FFFFFF, false, matrix, bufferIn, false, backgroundColor, 0);
			fontRenderer.renderString(displayText, 0, 0, 0xFFFFFFFF, false, matrix, bufferIn, false, 0, 0);
			matrixStackIn.pop();
		}
	}
}
