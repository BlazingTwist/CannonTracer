package the_dark_jumper.cannonTracer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_dark_jumper.cannonTracer.modules.ModuleManager;
import the_dark_jumper.cannonTracer.util.SimpleLocation;
import the_dark_jumper.cannonTracer.util.SingleTickMoveData;
import the_dark_jumper.cannonTracer.util.TrackingData;

public class EntityTracker {
	public final Main main;
	Set<Entity> trackedEntities = Sets.<Entity>newHashSet();
	public HashMap<String, TrackingData> observedEntityIDSP = new HashMap<>();
	public HashMap<String, TrackingData> observedEntityIDMP = new HashMap<>();
	public ArrayList<SingleTickMoveData> tracingHistory = new ArrayList<>();
	public ArrayList<SingleTickMoveData> lastSecond=new ArrayList<>();
	
	public EntityTracker(Main main){
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void entitySpawnEvent(EntityJoinWorldEvent event) {
		if(main.moduleManager.state != ModuleManager.State.SINGLEPLAYER) {
			return;
		}
		Entity entity = event.getEntity();
		if(entity == null) {
			return;
		}
		if(main.singlePlayerSettings.bLogGNS.getter.get()) {
			try {
				FileWriter out=new FileWriter("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\The_Dark_Jumper_Cannon_Tracer\\log.cue", true);
				BufferedWriter BWout=new BufferedWriter(out);
				BWout.write("Entity detected! ClassName: "+entity.getClass().getSimpleName());
				BWout.newLine();
				BWout.close();
			}catch(Exception e){
				System.out.println("thrown error while saving");
				e.printStackTrace();
			}
		}
		if(observedEntityIDSP.containsKey(entity.getClass().getSimpleName()) && observedEntityIDSP.get(entity.getClass().getSimpleName()).getRender()) {
			trackedEntities.add(entity);
		}
	}
	
	public void removeTrackedEntities(Entity entity) {
		trackedEntities.remove(entity);
	}
	
	public void doSinglePlayerSpecificRender() {
		if(main.singlePlayerSettings.modeGNS.getter.get() == 2) {
			removeOutdatedEntities(lastSecond);
		}else if(main.singlePlayerSettings.modeGNS.getter.get() == 0){
			removeOutdatedEntities(tracingHistory);
		}
		checkTrackedEntities();
	}
	//singleplayer
	public void checkTrackedEntities() {
		if(trackedEntities == null || trackedEntities.size() <= 0) {
			return;
		}
		for(Iterator<Entity> it = trackedEntities.iterator(); it.hasNext(); ) {
			Entity entity = it.next();
			if(!entity.isAlive()) {
				it.remove();
				continue;
			}
			if(entity.ticksExisted <= 0) {
				continue;
			}
			String entityName = entity.getClass().getSimpleName();
			SimpleLocation pos1 = new SimpleLocation(entity.prevPosX, entity.prevPosY + 0.49, entity.prevPosZ);
			SimpleLocation pos2 = new SimpleLocation(entity.posX, entity.posY + 0.49, entity.posZ);
			if(main.singlePlayerSettings.modeGNS.getter.get() == 2) {
				checkNewEntities(lastSecond, pos1, pos2, entityName);
			}else {
				checkNewEntities(tracingHistory, pos1, pos2, entityName);
			}
		}
	}
	public void removeOutdatedEntities(ArrayList<SingleTickMoveData> source) {
		for(Iterator<SingleTickMoveData> it = source.iterator(); it.hasNext(); ) {
			SingleTickMoveData moveData = it.next();
			for(Iterator<String> keyIT = moveData.tickData.keySet().iterator(); keyIT.hasNext(); ) {
				String key = keyIT.next();
				for(Iterator<Integer> tickIT = moveData.tickData.get(key).iterator(); tickIT.hasNext(); ) {
					int tick = tickIT.next();
					if(System.currentTimeMillis() - (moveData.timeOfCreation + (tick * 50)) >= 5000) {
						tickIT.remove();
						continue;
					}
				}
				if(moveData.tickData.get(key).isEmpty()) {
					keyIT.remove();
					continue;
				}
			}
			if(moveData.tickData.isEmpty()) {
				it.remove();
				continue;
			}
		}
	}
	public void checkNewEntities(ArrayList<SingleTickMoveData> source, SimpleLocation pos1, SimpleLocation pos2, String entityName) {
		for(Iterator<SingleTickMoveData> it = source.iterator(); it.hasNext(); ) {
			SingleTickMoveData moveData = it.next();
			if(!moveData.isNewData(pos1,  pos2)) {
				//is old data
				moveData.addTick(entityName);
				return;
			}
		}
		//is new data
		SingleTickMoveData newData = new SingleTickMoveData(this, pos1, pos2);
		newData.addTick(entityName);
		source.add(newData);
	}
	
	public boolean getXray() {
		if(main.moduleManager.state == ModuleManager.State.SINGLEPLAYER) {
			return main.singlePlayerSettings.xRayTraceGNS.getter.get();
		}else if(main.moduleManager.state == ModuleManager.State.MULTIPLAYER) {
			return main.multiPlayerSettings.xRayTraceGNS.getter.get();
		}
		return false;
	}
	
	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event) {
		if(main.moduleManager.state == ModuleManager.State.MENU) {
			return;
		}
		boolean singleplayer = false;
		if(main.moduleManager.state == ModuleManager.State.SINGLEPLAYER) {
			doSinglePlayerSpecificRender();
			singleplayer = true;
		}
		if(tracingHistory == null || tracingHistory.size() <= 0) {
			return;
		}
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if(player == null) {
			return;
		}
		Vec3d player_pos = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
		GlStateManager.pushMatrix();
		GlStateManager.disableTexture();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if(getXray()) {
			GlStateManager.disableDepthTest();
		}
		GlStateManager.translated(-player_pos.x, -player_pos.y, -player_pos.z);
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		
		for(SingleTickMoveData moveData : tracingHistory) {
			for(String key : moveData.tickData.keySet()) {
				if(singleplayer) {
					TrackingData trackData = observedEntityIDSP.get(key);
					if(trackData.renderGNS.getter.get()) {
						moveData.setupDrawingBuffer(bufferBuilder, trackData, key);
					}
				}else {
					TrackingData trackData = observedEntityIDMP.get(key);
					if(trackData.renderGNS.getter.get()) {
						moveData.setupDrawingBuffer(bufferBuilder, trackData, key);
					}
				}
			}
		}
		tessellator.draw();
		if(getXray()) {
			GlStateManager.enableDepthTest();
		}
		GlStateManager.lineWidth(1.0f);
		GlStateManager.disableBlend();
		GlStateManager.enableTexture();
		GlStateManager.popMatrix();
	}
}