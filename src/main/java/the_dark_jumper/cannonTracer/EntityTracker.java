package the_dark_jumper.cannonTracer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
import the_dark_jumper.cannonTracer.util.TracingData;
import the_dark_jumper.cannonTracer.util.TrackingData;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.modules.ModuleManager;

public class EntityTracker {
	public final Main main;
	Set<Entity> trackedEntities = Sets.<Entity>newHashSet();
	public HashMap<String, TrackingData> observedEntityIDSP = new HashMap<String, TrackingData>();
	public HashMap<String, TrackingData> observedEntityIDMP = new HashMap<String, TrackingData>();
	public ArrayList<TracingData> tracingHistory = new ArrayList<TracingData>();
	public ArrayList<TracingData> lastSecond=new ArrayList<TracingData>();
	
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
		if(main.guiSettings.bLog) {
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
	
	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event) {
		Entity a_e;
		TrackingData track_data;
		try {
			if(trackedEntities != null && trackedEntities.size() > 0) {
				for(Iterator<Entity>it = trackedEntities.iterator(); it.hasNext();) {
					a_e = it.next();
					if(!a_e.isAlive()) {
						it.remove();
						continue;
					}
					if(a_e.ticksExisted <= 0) {
						continue;
					}
					track_data = observedEntityIDSP.get(a_e.getClass().getSimpleName());
					boolean newData = true;
					double x1 = a_e.prevPosX, x2 = a_e.posX, y1 = a_e.prevPosY, y2 = a_e.posY, z1 = a_e.prevPosZ, z2 = a_e.posZ;
					TracingData trace_data;
					if(main.singlePlayerSettings.mode == 2) {
						for(Iterator<TracingData> iter = lastSecond.iterator(); iter.hasNext();) {
							trace_data=iter.next();
							if(System.currentTimeMillis() - trace_data.timeOfCreation >= 5000) {
								iter.remove();
							}else if(!trace_data.isNewData(x1, x2, y1, y2, z1, z2)){
								newData = false;
								long time = System.currentTimeMillis();
								trace_data.timeOfCreation = time;
								trace_data.ticksAlive.add(time);
								break;
							}
						}
						if(newData) {
							lastSecond.add(new TracingData(this, a_e.getClass().getSimpleName(), a_e.prevPosX, a_e.posX, a_e.prevPosY, a_e.posY, a_e.prevPosZ, a_e.posZ));
							System.out.println("added trace to lastsecond, new length: "+lastSecond.size());
						}
					}else {
						for(Iterator<TracingData> iter=tracingHistory.iterator(); iter.hasNext();) {
							trace_data=iter.next();
							if(!trace_data.isNewData(x1, x2, y1, y2, z1, z2)) {
								newData=false;
								trace_data.timeOfCreation=System.currentTimeMillis();
								//System.out.println("detected duplicate data");
								break;
							}
						}
						if(newData) {
							tracingHistory.add(new TracingData(this, a_e.getClass().getSimpleName(), a_e.prevPosX, a_e.posX, a_e.prevPosY, a_e.posY, a_e.prevPosZ, a_e.posZ));
							System.out.println("added trace to history, new length: "+tracingHistory.size());
						}
					}
				}
			}
		}catch (ConcurrentModificationException e){
			System.out.println("Good...I wanted it to crash");
		}
	
		if(tracingHistory!=null&&tracingHistory.size()>0) {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if(player == null) {
				return;
			}
			Vec3d player_pos = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
			GlStateManager.pushMatrix();
			GlStateManager.disableTexture();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			if(main.singlePlayerSettings.xRayTrace) {
				GlStateManager.disableDepthTest();
			}
			GlStateManager.translated(-player_pos.x, -player_pos.y, -player_pos.z);
			final Tessellator tessellator = Tessellator.getInstance();
			final BufferBuilder bufferBuilder = tessellator.getBuffer();
			//GL11.glEnable(GL11.GL_LINE_SMOOTH);
			bufferBuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

			TracingData trace_data;
			for(Iterator<TracingData> it=tracingHistory.iterator(); it.hasNext();) {
				trace_data = it.next();
				track_data=observedEntityIDSP.get(trace_data.ID);
				if(track_data.renderGNS.getter.get()) {
					trace_data.setupDrawingBuffer(bufferBuilder, track_data);
				}
				if(System.currentTimeMillis() - trace_data.timeOfCreation >= (track_data.getTime() * 1000) && main.singlePlayerSettings.mode == 0) {
					//rendered time >= max time
					it.remove();
				}
			}

			tessellator.draw();
			if(main.singlePlayerSettings.xRayTrace) {
				GlStateManager.enableDepthTest();
			}
			GlStateManager.lineWidth(1.0f);
			GlStateManager.disableBlend();
			GlStateManager.enableTexture();
			GlStateManager.popMatrix();
		}
	}
}