package the_dark_jumper.cannontracer.settings;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class MultiPlayerSettings {
	public final Main main;
	
	public GetterAndSetter<Integer> renderTickGNS = new GetterAndSetter<Integer>(0);
	public GetterAndSetter<Boolean> xRayTraceGNS = new GetterAndSetter<Boolean>(false);
	public GetterAndSetter<Boolean> renderBoxesGNS = new GetterAndSetter<Boolean>(false);
	private boolean renderMenu = false;
	public GetterAndSetter<Boolean> renderMenuGNS = new GetterAndSetter<Boolean>(this::getRenderMenu, this::setRenderMenu);
	public GetterAndSetter<Boolean> bLogGNS = new GetterAndSetter<Boolean>(false);
	
	public MultiPlayerSettings(Main main) {
		this.main = main;
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
