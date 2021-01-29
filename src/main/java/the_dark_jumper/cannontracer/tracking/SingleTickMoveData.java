package the_dark_jumper.cannontracer.tracking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import net.minecraft.client.renderer.BufferBuilder;
import org.lwjgl.opengl.GL11;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.configsaving.Color;
import the_dark_jumper.cannontracer.configsaving.TrackingDataEntry;
import the_dark_jumper.cannontracer.modules.ModuleManager;
import the_dark_jumper.cannontracer.util.SimpleLocation;

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
	private SingleTickMoveData(){
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

	public void setupDrawingBuffer(BufferBuilder bufferBuilder, TrackingDataEntry trackingData, String entityName) {
		GL11.glLineWidth(trackingData.getThickness());

		Color color = trackingData.getColor();

		int targetRed = color.getRed();
		int targetGreen = color.getGreen();
		int targetBlue = color.getBlue();
		int targetAlpha = color.getAlpha();

		//GlStateManager.lineWidth(td.getThickness());
		//y1->y2 | x1->x2 | z1->z2
		//always y1->y2 first
		//bufferBuilder.func_225582_a_(pos1.x, pos1.y, pos1.z).func_225586_a_(0,0,0,0).endVertex();
		bufferBuilder.pos(pos1.x, pos1.y, pos1.z).color(0, 0, 0, 0).endVertex();
		//bufferBuilder.func_225582_a_(pos1.x, pos2.y, pos1.z).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos(pos1.x, pos2.y, pos1.z).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		//x1->x2 next if difference is greater, otherwise z1->z2
		if (Math.abs(pos2.x - pos1.x) >= Math.abs(pos2.z - pos1.z)) {
			//bufferBuilder.func_225582_a_(pos2.x, pos2.y, pos1.z).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
			bufferBuilder.pos(pos2.x, pos2.y, pos1.z).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		} else {
			//bufferBuilder.func_225582_a_(pos1.x, pos2.y, pos2.z).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
			bufferBuilder.pos(pos1.x, pos2.y, pos2.z).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		}
		//bufferBuilder.func_225582_a_(pos2.x, pos2.y, pos2.z).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos(pos2.x, pos2.y, pos2.z).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();

		int targetTick;
		boolean isExplosionTick = false;
		if (entityTracker.main.moduleManager.state == ModuleManager.State.SINGLEPLAYER) {
			if (!entityTracker.main.singlePlayerSettings.renderBoxesGNS.get()) {
				return;
			}
			if (entityTracker.main.singlePlayerSettings.modeGNS.get() == 2) {
				if (!tickData.containsKey(entityName)) {
					return;
				}
				targetTick = entityTracker.main.singlePlayerSettings.renderTickGNS.get() + tickOffset;
				if (!tickData.get(entityName).containsKey(targetTick)) {
					return;
				}
				isExplosionTick = tickData.get(entityName).get(targetTick);
			}
		} else if (entityTracker.main.moduleManager.state == ModuleManager.State.MULTIPLAYER) {
			//not checking renderboxes, because always active in multiplayer
			if (!tickData.containsKey(entityName)) {
				return;
			}
			targetTick = entityTracker.main.multiPlayerSettings.renderTickGNS.get() + tickOffset;
			if (!tickData.get(entityName).containsKey(targetTick)) {
				return;
			}
			isExplosionTick = tickData.get(entityName).get(targetTick);
		}

		if (isExplosionTick) {
			targetRed = 255 - targetRed;
			targetGreen = 255 - targetGreen;
			targetBlue = 255 - targetBlue;
		}

		/*bufferBuilder.func_225582_a_((pos2.x-0.49), (pos2.y-0.49), (pos2.z-0.49)).func_225586_a_(0,0,0,0).endVertex();
		bufferBuilder.func_225582_a_((pos2.x+0.49), (pos2.y-0.49), (pos2.z-0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x+0.49), (pos2.y-0.49), (pos2.z+0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x-0.49), (pos2.y-0.49), (pos2.z+0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x-0.49), (pos2.y-0.49), (pos2.z-0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x-0.49), (pos2.y+0.49), (pos2.z-0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x+0.49), (pos2.y+0.49), (pos2.z-0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x+0.49), (pos2.y+0.49), (pos2.z+0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x-0.49), (pos2.y+0.49), (pos2.z+0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x-0.49), (pos2.y-0.49), (pos2.z+0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();

		//fill missing 3 lines
		bufferBuilder.func_225582_a_((pos2.x-0.49), (pos2.y+0.49), (pos2.z+0.49)).func_225586_a_(0,0,0,0).endVertex();
		bufferBuilder.func_225582_a_((pos2.x-0.49), (pos2.y+0.49), (pos2.z-0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x+0.49), (pos2.y-0.49), (pos2.z-0.49)).func_225586_a_(0,0,0,0).endVertex();
		bufferBuilder.func_225582_a_((pos2.x+0.49), (pos2.y+0.49), (pos2.z-0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.func_225582_a_((pos2.x+0.49), (pos2.y-0.49), (pos2.z+0.49)).func_225586_a_(0,0,0,0).endVertex();
		bufferBuilder.func_225582_a_((pos2.x+0.49), (pos2.y+0.49), (pos2.z+0.49)).func_225586_a_(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();*/

		bufferBuilder.pos((pos2.x - 0.49), (pos2.y - 0.49), (pos2.z - 0.49)).color(0, 0, 0, 0).endVertex();
		bufferBuilder.pos((pos2.x + 0.49), (pos2.y - 0.49), (pos2.z - 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x + 0.49), (pos2.y - 0.49), (pos2.z + 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x - 0.49), (pos2.y - 0.49), (pos2.z + 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x - 0.49), (pos2.y - 0.49), (pos2.z - 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x - 0.49), (pos2.y + 0.49), (pos2.z - 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x + 0.49), (pos2.y + 0.49), (pos2.z - 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x + 0.49), (pos2.y + 0.49), (pos2.z + 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x - 0.49), (pos2.y + 0.49), (pos2.z + 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x - 0.49), (pos2.y - 0.49), (pos2.z + 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();

		//fill missing 3 lines
		bufferBuilder.pos((pos2.x - 0.49), (pos2.y + 0.49), (pos2.z + 0.49)).color(0, 0, 0, 0).endVertex();
		bufferBuilder.pos((pos2.x - 0.49), (pos2.y + 0.49), (pos2.z - 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x + 0.49), (pos2.y - 0.49), (pos2.z - 0.49)).color(0, 0, 0, 0).endVertex();
		bufferBuilder.pos((pos2.x + 0.49), (pos2.y + 0.49), (pos2.z - 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
		bufferBuilder.pos((pos2.x + 0.49), (pos2.y - 0.49), (pos2.z + 0.49)).color(0, 0, 0, 0).endVertex();
		bufferBuilder.pos((pos2.x + 0.49), (pos2.y + 0.49), (pos2.z + 0.49)).color(targetRed, targetGreen, targetBlue, targetAlpha).endVertex();
	}
}
