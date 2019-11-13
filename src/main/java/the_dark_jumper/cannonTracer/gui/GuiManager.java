package the_dark_jumper.cannontracer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.modules.ModuleManager;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class GuiManager {
	public final Main main;
	public final IngameGUI ingameGUI;
	public final ConfigGUI configGUI;
	public final HotkeyGUI hotkeyGUI;
	
	public GetterAndSetter<Double> fontHeightGNS = new GetterAndSetter<Double>(1d);
	
	public GuiManager(Main main) {
		this.main = main;
		ingameGUI = new IngameGUI(this);
		configGUI = new ConfigGUI(this);
		hotkeyGUI = new HotkeyGUI(this);
	}
	
	public void renderGUIs() {
		if(main.moduleManager.state == ModuleManager.State.SINGLEPLAYER) {
			ingameGUI.renderScreen();
		}else if(main.moduleManager.state == ModuleManager.State.MULTIPLAYER) {
			ingameGUI.renderScreen();
		}
	}
	
	public void keyEvent(InputEvent.KeyInputEvent event) {
		Screen screen = Minecraft.getInstance().currentScreen;
		if(screen != null && screen instanceof IJumperGUI) {
			((IJumperGUI)screen).keyEvent(event);
		}
	}
	
	public void mousePressEvent(boolean isLeftDown) {
		Screen screen = Minecraft.getInstance().currentScreen;
		if(screen != null && screen instanceof IJumperGUI) {
			((IJumperGUI)screen).mousePressEvent(isLeftDown);
		}
	}
}
