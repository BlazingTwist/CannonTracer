package the_dark_jumper.cannonTracer.settings;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;
import the_dark_jumper.cannonTracer.util.TracingData;

public class SinglePlayerSettings {
	public final Main main;
	
	private int mode = 0;
	public GetterAndSetter<Integer> modeGNS;
	private int renderTick = 0;
	public GetterAndSetter<Integer> renderTickGNS;
	private boolean xRayTrace = false;
	public GetterAndSetter<Boolean> xRayTraceGNS;
	private boolean renderBoxes = false;
	public GetterAndSetter<Boolean> renderBoxesGNS;
	private boolean renderMenu = false;
	public GetterAndSetter<Boolean> renderMenuGNS;
	private boolean bLog = false;
	public GetterAndSetter<Boolean> bLogGNS;
	
	public SinglePlayerSettings(Main main) {
		this.main = main;
		setupAccessors();
	}
	
	private void setupAccessors() {
		modeGNS = new GetterAndSetter<Integer>(this::getMode, this::setMode);
		renderTickGNS = new GetterAndSetter<Integer>(this::getRenderTick, this::setRenderTick);
		xRayTraceGNS = new GetterAndSetter<Boolean>(this::getXRayTrace, this::setXRayTrace);
		renderBoxesGNS = new GetterAndSetter<Boolean>(this::getRenderBoxes, this::setRenderBoxes);
		renderMenuGNS = new GetterAndSetter<Boolean>(this::getRenderMenu, this::setRenderMenu);
		bLogGNS = new GetterAndSetter<Boolean>(this::getBLog, this::setBLog);
	}
	
	private int getMode() {
		return this.mode;
	}
	private void setMode(int mode) {
		this.mode = mode;
		if(mode != 1) {
			main.entityTracker.tracingHistory.clear();
			main.entityTracker.lastSecond.clear();
		}
	}
	
	private int getRenderTick() {
		return this.renderTick;
	}
	private void setRenderTick(int renderTick) {
		this.renderTick = renderTick;
	}
	
	private boolean getXRayTrace() {
		return this.xRayTrace;
	}
	private void setXRayTrace(boolean xRayTrace) {
		this.xRayTrace = xRayTrace;
	}
	
	private boolean getRenderBoxes() {
		return this.renderBoxes;
	}
	private void setRenderBoxes(boolean renderBoxes) {
		this.renderBoxes = renderBoxes;
	}
	
	private boolean getRenderMenu() {
		return this.renderMenu;
	}
	private void setRenderMenu(boolean renderMenu) {
		if(renderMenu) {
			main.guiManager.configGUI.generateSingleplayerScreenComponents();
			Minecraft.getInstance().displayGuiScreen(main.guiManager.configGUI);
		}else{
			main.guiManager.configGUI.onClose();
			main.dataManager.Save();
		}
		this.renderMenu = renderMenu;
	}
	
	private boolean getBLog() {
		return this.bLog;
	}
	private void setBLog(boolean b) {
		this.bLog = b;
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
