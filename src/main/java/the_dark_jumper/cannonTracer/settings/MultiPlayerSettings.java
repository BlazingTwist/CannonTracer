package the_dark_jumper.cannontracer.settings;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class MultiPlayerSettings {
	public final Main main;
	
	private int renderTick = 0;
	public GetterAndSetter<Integer> renderTickGNS = new GetterAndSetter<Integer>(this::getRenderTick, this::setRenderTick);
	private boolean xRayTrace = false;
	public GetterAndSetter<Boolean> xRayTraceGNS = new GetterAndSetter<Boolean>(this::getXRayTrace, this::setXRayTrace);
	private boolean renderBoxes = false;
	public GetterAndSetter<Boolean> renderBoxesGNS = new GetterAndSetter<Boolean>(this::getRenderBoxes, this::setRenderBoxes);
	private boolean renderMenu = false;
	public GetterAndSetter<Boolean> renderMenuGNS = new GetterAndSetter<Boolean>(this::getRenderMenu, this::setRenderMenu);
	private boolean bLog = false;
	public GetterAndSetter<Boolean> bLogGNS = new GetterAndSetter<Boolean>(this::getBLog, this::setBLog);
	
	public MultiPlayerSettings(Main main) {
		this.main = main;
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
			main.guiManager.configGUI.generateMultiplayerScreenComponents();
			Minecraft.getInstance().displayGuiScreen(main.guiManager.configGUI);
		}else{
			main.guiManager.configGUI.onClose();
			main.dataManager.Save();
			main.serverChatListener.sendConfigMessage();
		}
		this.renderMenu = renderMenu;
	}
	
	private boolean getBLog() {
		return this.bLog;
	}
	private void setBLog(boolean b) {
		this.bLog = b;
	}
	
	public void pullData(boolean b) {
		if(!b) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
		main.serverChatListener.requestTracingData();
	}
	
	public void clearData(boolean b) {
		if(!b) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
	}
}
