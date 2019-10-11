package the_dark_jumper.cannonTracer.util;

import java.util.ArrayList;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.BufferBuilder;
import the_dark_jumper.cannonTracer.EntityTracker;

public class TracingData {
	public final EntityTracker entityTracker;
	public String ID;
	public long timeOfCreation;
	public ArrayList<Long> ticksAlive=new ArrayList<Long>();
	public double x1, x2, y1, y2, z1, z2;
	
	public void initialize(String ID, double x1, double x2, double y1, double y2, double z1, double z2) {
		this.ID = ID;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = (y1 + 0.49);
		this.y2 = (y2 + 0.49);
		this.z1 = z1;
		this.z2 = z2;
		//System.out.println("initializing ID: "+ID+" | x1="+x1+" | x2="+x2+" | y1="+y1+" | y2="+y2+" | z1="+z1+" | z2="+z2);
	}
	
	public TracingData(EntityTracker entityTracker, String ID, double x1, double x2, double y1, double y2, double z1, double z2){
		this.entityTracker = entityTracker;
		initialize(ID, x1, x2, y1, y2, z1, z2);
		this.ticksAlive.clear();
		long time = System.currentTimeMillis();
		this.ticksAlive.add(time);
		this.timeOfCreation = time;
	}
	public TracingData(EntityTracker entityTracker, String ID, double x1, double x2, double y1, double y2, double z1, double z2, ArrayList<Long> ticksAlive){
		this.entityTracker = entityTracker;
		initialize(ID, x1, x2, y1, y2, z1, z2);
		this.ticksAlive.clear();
		for(long tick : ticksAlive) {
			this.ticksAlive.add(tick);
		}
	}
	
	public boolean isNewData(double x1, double x2, double y1, double y2, double z1, double z2) {
		if(this.x1 == x1 && this.x2 == x2 && this.y1 == (y1 + 0.49) && this.y2 == (y2 + 0.49) && this.z1 == z1 && this.z2 == z2) {
			//System.out.println("detected old data");
			return false;
		}
		//System.out.println("Detected new Data! | new Data x1: "+x1+" | x2: "+x2+" | y1: "+y1+" | y2: "+y2+" | z1: "+z1+" | z2: "+z2+" | compared x1: "+this.x1+" | x2: "+this.x2+" | y1: "+this.y1+" | y2: "+this.y2+" | z1: "+this.z1+" | z2: "+this.z2);
		return true;
	}
	
	public void setupDrawingBuffer(BufferBuilder bufferBuilder ,TrackingData td) {
		//System.out.println("tracing tnt data: x1="+x1+" | x2="+x2+" | y1="+y1+" | y2="+y2+" | z1="+z1+" | z2="+z2+" | red="+td.getRed()+" | green="+td.getGreen()+" | blue="+td.getBlue()+" | alpha="+td.getAlpha());
		GlStateManager.lineWidth(td.getThickness());
		//y1->y2 | x1->x2 | z1->z2
		bufferBuilder.pos(x1, y1, z1).color(0,0,0,0).endVertex();
		bufferBuilder.pos(x1, y2, z1).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos(x2, y2, z1).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos(x2, y2, z2).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		if(!entityTracker.main.singlePlayerSettings.renderBoxes) {
			return;
		}
		if(entityTracker.main.singlePlayerSettings.mode==2&&!(ticksAlive.contains(entityTracker.main.singlePlayerSettings.renderTick))) {
			return;
		}
		bufferBuilder.pos((x2-0.49), (y2-0.49), (z2-0.49)).color(0,0,0,0).endVertex();
		bufferBuilder.pos((x2+0.49), (y2-0.49), (z2-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2+0.49), (y2-0.49), (z2+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2-0.49), (y2-0.49), (z2+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2-0.49), (y2-0.49), (z2-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2-0.49), (y2+0.49), (z2-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2+0.49), (y2+0.49), (z2-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2+0.49), (y2+0.49), (z2+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2-0.49), (y2+0.49), (z2+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2-0.49), (y2-0.49), (z2+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		
		//fill missing 3 lines
		bufferBuilder.pos((x2-0.49), (y2+0.49), (z2+0.49)).color(0,0,0,0).endVertex();
		bufferBuilder.pos((x2-0.49), (y2+0.49), (z2-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2+0.49), (y2-0.49), (z2-0.49)).color(0,0,0,0).endVertex();
		bufferBuilder.pos((x2+0.49), (y2+0.49), (z2-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((x2+0.49), (y2-0.49), (z2+0.49)).color(0,0,0,0).endVertex();
		bufferBuilder.pos((x2+0.49), (y2+0.49), (z2+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
	}
}