package the_dark_jumper.cannonTracer.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.BufferBuilder;
import the_dark_jumper.cannonTracer.EntityTracker;
import the_dark_jumper.cannonTracer.modules.ModuleManager;

public class SingleTickMoveData {
	public final EntityTracker entityTracker;
	public HashMap<String, ArrayList<Integer>> tickData = new HashMap<>();
	public SimpleLocation pos1, pos2;
	public int tickOffset = 0;
	
	public SingleTickMoveData(EntityTracker entityTracker, SimpleLocation pos1, SimpleLocation pos2) {
		this.entityTracker = entityTracker;
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
	
	public SingleTickMoveData copy() {
		SingleTickMoveData moveData = new SingleTickMoveData(entityTracker, pos1.copy(), pos2.copy());
		HashMap<String, ArrayList<Integer>> copy = new HashMap<>();
		for(String key : tickData.keySet()) {
			ArrayList<Integer> copyList = new ArrayList<Integer>();
			for(int value : tickData.get(key)) {
				copyList.add(value);
			}
			copy.put(new String(key), copyList);
		}
		moveData.tickData = copy;
		return moveData;
	}
	
	public boolean isNewData(SimpleLocation a, SimpleLocation b) {
		return !(pos1.equals(a) && pos2.equals(b));
	}
	
	public void addTick(String entityName, int tick) {
		if(!tickData.containsKey(entityName)) {
			tickData.put(entityName, new ArrayList<Integer>());
		}
		ArrayList<Integer> ticks = tickData.get(entityName);
		if(!ticks.contains(tick)) {
			ticks.add(tick);
		}
	}
	
	public void setupDrawingBuffer(BufferBuilder bufferBuilder, TrackingData td, String entityName) {
		GlStateManager.lineWidth(td.getThickness());
		//y1->y2 | x1->x2 | z1->z2
		bufferBuilder.pos(pos1.x, pos1.y, pos1.z).color(0,0,0,0).endVertex();
		bufferBuilder.pos(pos1.x, pos2.y, pos1.z).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos(pos2.x, pos2.y, pos1.z).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos(pos2.x, pos2.y, pos2.z).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		if(entityTracker.main.moduleManager.state == ModuleManager.State.SINGLEPLAYER) {
			if(!entityTracker.main.singlePlayerSettings.renderBoxesGNS.getter.get()) {
				return;
			}
			if(entityTracker.main.singlePlayerSettings.modeGNS.getter.get() == 2) {
				if(!tickData.containsKey(entityName)) {
					return;
				}
				if(!tickData.get(entityName).contains(entityTracker.main.singlePlayerSettings.renderTickGNS.getter.get() + tickOffset)) {
					return;
				}
			}
		}else if(entityTracker.main.moduleManager.state == ModuleManager.State.MULTIPLAYER) {
			if(!entityTracker.main.multiPlayerSettings.renderBoxesGNS.getter.get()) {
				return;
			}
			if(!tickData.containsKey(entityName)) {
				return;
			}
			if(!tickData.get(entityName).contains(entityTracker.main.multiPlayerSettings.renderTickGNS.getter.get() + tickOffset)){
				return;
			}
		}
		bufferBuilder.pos((pos2.x-0.49), (pos2.y-0.49), (pos2.z-0.49)).color(0,0,0,0).endVertex();
		bufferBuilder.pos((pos2.x+0.49), (pos2.y-0.49), (pos2.z-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x+0.49), (pos2.y-0.49), (pos2.z+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x-0.49), (pos2.y-0.49), (pos2.z+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x-0.49), (pos2.y-0.49), (pos2.z-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x-0.49), (pos2.y+0.49), (pos2.z-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x+0.49), (pos2.y+0.49), (pos2.z-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x+0.49), (pos2.y+0.49), (pos2.z+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x-0.49), (pos2.y+0.49), (pos2.z+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x-0.49), (pos2.y-0.49), (pos2.z+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		
		//fill missing 3 lines
		bufferBuilder.pos((pos2.x-0.49), (pos2.y+0.49), (pos2.z+0.49)).color(0,0,0,0).endVertex();
		bufferBuilder.pos((pos2.x-0.49), (pos2.y+0.49), (pos2.z-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x+0.49), (pos2.y-0.49), (pos2.z-0.49)).color(0,0,0,0).endVertex();
		bufferBuilder.pos((pos2.x+0.49), (pos2.y+0.49), (pos2.z-0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
		bufferBuilder.pos((pos2.x+0.49), (pos2.y-0.49), (pos2.z+0.49)).color(0,0,0,0).endVertex();
		bufferBuilder.pos((pos2.x+0.49), (pos2.y+0.49), (pos2.z+0.49)).color(td.getRed(), td.getGreen(), td.getBlue(), td.getAlpha()).endVertex();
	}
}
