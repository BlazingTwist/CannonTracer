package the_dark_jumper.cannontracer.hotkey;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannontracer.Main;

public class HotkeyManager {
	Main main;
	
	public ArrayList<Hotkey> activeHotkeys = new ArrayList<>();
	
	public HotkeyManager(Main main) {
		this.main = main;
	}
	
	public ArrayList<Hotkey> getHotkeys(){
		return activeHotkeys;
	}
	
	public void addHotkey(Hotkey hk) {
		activeHotkeys.add(hk);
	}
	
	public void clearHotkeys() {
		activeHotkeys.clear();
	}
	
	public void onPressedKeysChanged() {
		if(Minecraft.getInstance().currentScreen != null) {
			return;
		}
		for(Hotkey hotkey : activeHotkeys) {
			hotkey.checkHotkeyState();
		}
	}
}
