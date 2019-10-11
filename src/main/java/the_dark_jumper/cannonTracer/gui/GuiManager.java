package the_dark_jumper.cannonTracer.gui;

import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.modules.ModuleManager;

public class GuiManager {
	public final Main main;
	public final IngameGUI ingameGUI;
	public final ConfigGUI configGUI;
	
	public GuiManager(Main main) {
		this.main = main;
		ingameGUI = new IngameGUI(this);
		configGUI = new ConfigGUI(this);
	}
	
	public void renderGUIs() {
		if(main.moduleManager.state == ModuleManager.State.SINGLEPLAYER) {
			ingameGUI.renderScreen();
		}else if(main.moduleManager.state == ModuleManager.State.MULTIPLAYER) {
			ingameGUI.renderScreen();
		}
	}
}
