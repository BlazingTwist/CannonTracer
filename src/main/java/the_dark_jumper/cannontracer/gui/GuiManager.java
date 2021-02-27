package the_dark_jumper.cannontracer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.configsaving.GuiConfig;
import the_dark_jumper.cannontracer.modules.ModuleManager;

public class GuiManager {
	public final Main main;
	public final OnscreenGUI onscreenGUI;
	public final ConfigGUI configGUI;
	public final HotkeyGUI hotkeyGUI;
	public final TestCannonGUI testCannonGUI;
	
	public GuiManager(Main main) {
		this.main = main;
		onscreenGUI = new OnscreenGUI(this);
		configGUI = new ConfigGUI(this);
		hotkeyGUI = new HotkeyGUI(this);
		testCannonGUI = new TestCannonGUI(this);
	}

	public GuiConfig getGuiConfig(){
		return main.dataManager.getTracerConfig().getGuiConfig();
	}
	
	public void renderGUIs() {
		if(main.moduleManager.state == ModuleManager.State.SINGLEPLAYER) {
			onscreenGUI.renderScreen();
		}else if(main.moduleManager.state == ModuleManager.State.MULTIPLAYER) {
			onscreenGUI.renderScreen();
		}
	}
	
	public void keyEvent(InputEvent.KeyInputEvent event) {
		Screen screen = Minecraft.getInstance().currentScreen;
		if(screen instanceof IJumperGUI) {
			((IJumperGUI)screen).keyEvent(event);
		}
	}
	
	public void mousePressEvent(boolean isLeftDown) {
		Screen screen = Minecraft.getInstance().currentScreen;
		if(screen instanceof IJumperGUI) {
			((IJumperGUI)screen).mousePressEvent(isLeftDown);
		}
	}
}
