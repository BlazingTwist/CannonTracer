package the_dark_jumper.cannontracer.tracking;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.configsaving.TrackingDataEntry;
import the_dark_jumper.cannontracer.modules.ModuleManager;
import the_dark_jumper.cannontracer.util.SimpleLocation;

public class EntityTracker {
	public final Main main;
	Set<Entity> trackedEntities = Sets.newHashSet();
	public Queue<SingleTickMoveData> tracingHistory = new ConcurrentLinkedQueue<>();
	public Queue<SingleTickMoveData> lastSecond = new ConcurrentLinkedQueue<>();
	public int currentTick = 0;

	public EntityTracker(Main main) {
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void entitySpawnEvent(EntityJoinWorldEvent event) {
		//multiplayer tracking is handled by the companion plugin
		if (main.moduleManager.state != ModuleManager.State.SINGLEPLAYER) {
			return;
		}

		Entity entity = event.getEntity();
		if (entity == null) {
			return;
		}

		if (main.singlePlayerSettings.bLogGNS.get() && Minecraft.getInstance().player != null) {
			Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("Entity detected! ClassName: '" + entity.getClass().getSimpleName() + "'"));
		}

		String entityName = entity.getClass().getSimpleName();
		if (main.dataManager.getTrackingDataSP() != null
				&& main.dataManager.getTrackingDataSP().containsKey(entityName)
				&& main.dataManager.getTrackingDataSP().get(entityName).isRender()) {
			trackedEntities.add(entity);
		}
	}

	@SubscribeEvent
	public void onTick(WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		if (main.moduleManager.state != ModuleManager.State.SINGLEPLAYER) {
			return;
		}
		currentTick++;
		doSinglePlayerSpecificTracking();
	}

	public void doSinglePlayerSpecificTracking() {
		if (main.singlePlayerSettings.modeGNS.get() == 2) {
			removeOutdatedEntities(lastSecond);
		} else if (main.singlePlayerSettings.modeGNS.get() == 0) {
			removeOutdatedEntities(tracingHistory);
		}
		checkTrackedEntities();
	}

	public void checkTrackedEntities() {
		if (trackedEntities == null || trackedEntities.size() <= 0) {
			return;
		}

		for (Iterator<Entity> it = trackedEntities.iterator(); it.hasNext(); ) {
			Entity entity = it.next();
			if (!entity.isAlive()) {
				it.remove();
				continue;
			}
			if (entity.ticksExisted <= 0) {
				continue;
			}
			String entityName = entity.getClass().getSimpleName();
			SimpleLocation pos1 = new SimpleLocation(entity.prevPosX, entity.prevPosY + 0.49, entity.prevPosZ);
			Vec3d currentPositionVector = entity.getPositionVector();
			SimpleLocation pos2 = new SimpleLocation(currentPositionVector.x, currentPositionVector.y + 0.49, currentPositionVector.z);
			if (main.singlePlayerSettings.modeGNS.get() == 2) {
				checkNewEntities(lastSecond, pos1, pos2, entityName);
			} else {
				checkNewEntities(tracingHistory, pos1, pos2, entityName);
			}
		}
	}

	public void removeOutdatedEntities(Queue<SingleTickMoveData> source) {
		for (Iterator<SingleTickMoveData> it = source.iterator(); it.hasNext(); ) {
			SingleTickMoveData moveData = it.next();
			for (Iterator<String> keyIT = moveData.tickData.keySet().iterator(); keyIT.hasNext(); ) {
				String key = keyIT.next();
				TrackingDataEntry trackingData = main.dataManager.getTrackingDataSP().get(key);
				if(trackingData == null){
					keyIT.remove();
				}else{
					int maxTime = (int) (trackingData.getTime() * 1000);
					moveData.tickData.get(key).keySet().removeIf(tick -> ((currentTick - tick) * 50) > maxTime);
					if (moveData.tickData.get(key).isEmpty()) {
						keyIT.remove();
					}
				}
			}
			if (moveData.tickData.isEmpty()) {
				it.remove();
			}
		}
	}

	public void checkNewEntities(Queue<SingleTickMoveData> source, SimpleLocation pos1, SimpleLocation pos2, String entityName) {
		for (SingleTickMoveData moveData : source) {
			if (!moveData.isNewData(pos1, pos2)) {
				//is old data
				moveData.addTick(entityName, currentTick, false);
				return;
			}
		}
		//is new data
		SingleTickMoveData newData = new SingleTickMoveData(this, pos1, pos2);
		newData.addTick(entityName, currentTick, false);
		source.add(newData);
	}

	public boolean getXray() {
		if (main.moduleManager.state == ModuleManager.State.SINGLEPLAYER) {
			return main.singlePlayerSettings.xRayTraceGNS.get();
		} else if (main.moduleManager.state == ModuleManager.State.MULTIPLAYER) {
			return main.multiPlayerSettings.xRayTraceGNS.get();
		}
		return false;
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event) {
		if (main.moduleManager.state == ModuleManager.State.MENU) {
			return;
		}
		boolean isSinglePlayer = (main.moduleManager.state == ModuleManager.State.SINGLEPLAYER);

		if (tracingHistory == null || tracingHistory.size() <= 0) {
			return;
		}

		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}

		GL11.glPushMatrix();
		//GlStateManager.pushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		//GlStateManager.disableTexture();
		GL11.glEnable(GL11.GL_BLEND);
		//GlStateManager.enableBlend();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (getXray()) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			//GlStateManager.disableDepthTest();
		}

		//1.15 fix that I found online... don't touch it, it's magic
		ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
		Vec3d projectedView = renderInfo.getProjectedView();
		GL11.glRotatef(renderInfo.getPitch(), 1, 0, 0);
		GL11.glRotatef(renderInfo.getYaw() + 180, 0, 1, 0);
		GL11.glTranslated(-projectedView.x, -projectedView.y, -projectedView.z);
		//GlStateManager.translated(-player_pos.x, -player_pos.y, -player_pos.z);


		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

		for (SingleTickMoveData moveData : tracingHistory) {
			for (String key : moveData.tickData.keySet()) {
				TrackingDataEntry trackData = isSinglePlayer ? main.dataManager.getTrackingDataSP().get(key) : main.dataManager.getTrackingDataMP().get(key);
				if (trackData != null && trackData.isRender()) {
					moveData.setupDrawingBuffer(bufferBuilder, trackData, key);
				}
			}
		}

		tessellator.draw();
		if (getXray()) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			//GlStateManager.enableDepthTest();
		}
		GL11.glLineWidth(1.0f);
		//GlStateManager.lineWidth(1.0f);
		GL11.glDisable(GL11.GL_BLEND);
		//GlStateManager.disableBlend();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		//GlStateManager.enableTexture();
		GL11.glPopMatrix();
		//GlStateManager.popMatrix();
	}
}