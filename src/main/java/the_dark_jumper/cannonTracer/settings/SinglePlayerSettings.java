package the_dark_jumper.cannontracer.settings;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.tracking.SingleTickMoveData;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class SinglePlayerSettings {
	public final Main main;
	
	private int mode = 0;
	public GetterAndSetter<Integer> modeGNS = new GetterAndSetter<Integer>(this::getMode, this::setMode);
	public GetterAndSetter<Integer> renderTickGNS = new GetterAndSetter<Integer>(0);
	public GetterAndSetter<Boolean> xRayTraceGNS = new GetterAndSetter<Boolean>(false);
	public GetterAndSetter<Boolean> renderBoxesGNS = new GetterAndSetter<Boolean>(false);
	private boolean renderMenu = false;
	public GetterAndSetter<Boolean> renderMenuGNS = new GetterAndSetter<Boolean>(this::getRenderMenu, this::setRenderMenu);
	public GetterAndSetter<Boolean> bLogGNS = new GetterAndSetter<Boolean>(false);
	
	public SinglePlayerSettings(Main main) {
		this.main = main;
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
	
	public void loadLastSeconds(boolean b) {
		if(!b) {
			return;
		}
		if(mode != 2) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
		for(SingleTickMoveData moveData : main.entityTracker.lastSecond) {
			SingleTickMoveData copiedMoveData = moveData.copy();
			copiedMoveData.tickOffset = main.entityTracker.currentTick - main.moduleManager.getMaxDisplayTickSP();
			main.entityTracker.tracingHistory.add(copiedMoveData);
		}
	}
	
	public void clearHistory(boolean b) {
		if(!b) {
			return;
		}
		if(mode != 1 && mode != 2) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
	}
}
