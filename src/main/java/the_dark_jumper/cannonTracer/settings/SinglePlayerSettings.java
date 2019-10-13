package the_dark_jumper.cannonTracer.settings;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;
import the_dark_jumper.cannonTracer.util.TracingData;

public class SinglePlayerSettings {
	public final Main main;
	
	public int mode = 0;
	public GetterAndSetter<Integer> modeGNS;
	public int renderTick = 0;
	public GetterAndSetter<Integer> renderTickGNS;
	public boolean xRayTrace = false;
	public GetterAndSetter<Boolean> xRayTraceGNS;
	public boolean renderBoxes = false;
	public GetterAndSetter<Boolean> renderBoxesGNS;
	public boolean renderMenu = false;
	public GetterAndSetter<Boolean> renderMenuGNS;
	
	public SinglePlayerSettings(Main main) {
		this.main = main;
		setupAccessors();
	}
	
	public void setupAccessors() {
		modeGNS = new GetterAndSetter<Integer>(this::getMode, this::setMode);
		renderTickGNS = new GetterAndSetter<Integer>(this::getRenderTick, this::setRenderTick);
		xRayTraceGNS = new GetterAndSetter<Boolean>(this::getXRayTrace, this::setXRayTrace);
		renderBoxesGNS = new GetterAndSetter<Boolean>(this::getRenderBoxes, this::setRenderBoxes);
		renderMenuGNS = new GetterAndSetter<Boolean>(this::getRenderMenu, this::setRenderMenu);
	}
	
	public int getMode() {
		return this.mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
		if(mode != 1) {
			main.entityTracker.tracingHistory.clear();
			main.entityTracker.lastSecond.clear();
		}
	}
	
	public int getRenderTick() {
		return this.renderTick;
	}
	public void setRenderTick(int renderTick) {
		this.renderTick = renderTick;
	}
	
	public boolean getXRayTrace() {
		return this.xRayTrace;
	}
	public void setXRayTrace(boolean xRayTrace) {
		this.xRayTrace = xRayTrace;
	}
	
	public boolean getRenderBoxes() {
		return this.renderBoxes;
	}
	public void setRenderBoxes(boolean renderBoxes) {
		this.renderBoxes = renderBoxes;
	}
	
	public boolean getRenderMenu() {
		return this.renderMenu;
	}
	public void setRenderMenu(boolean renderMenu) {
		if(renderMenu) {
			main.guiManager.configGUI.generateSingleplayerScreenComponents();
			Minecraft.getInstance().displayGuiScreen(main.guiManager.configGUI);
		}else{
			main.guiManager.configGUI.onClose();
			main.dataManager.Save();
		}
		this.renderMenu = renderMenu;
	}
	
	public void lastSeconds(boolean b) {
		if(b) {
			if(mode != 2) {
				return;
			}
			main.entityTracker.tracingHistory.clear();
			TracingData tracingData;
			long time = System.currentTimeMillis();
			ArrayList<Long> tickList = new ArrayList<Long>();
			for(Iterator<TracingData> it = main.entityTracker.lastSecond.iterator(); it.hasNext();) {
				tracingData = it.next();
				for(long tick : tracingData.ticksAlive) {
					tickList.add((tick + 5000 - time) / 50);
				}
				main.entityTracker.tracingHistory.add(new TracingData(main.entityTracker, tracingData.ID,
						tracingData.x1,
						tracingData.x2,
						tracingData.y1 - 0.49,
						tracingData.y2 - 0.49,
						tracingData.z1,
						tracingData.z2,
						tickList));
				tickList.clear();
			}
		}else {
			if(mode == 0) {
				return;
			}
			main.entityTracker.tracingHistory.clear();
		}
	}
}
