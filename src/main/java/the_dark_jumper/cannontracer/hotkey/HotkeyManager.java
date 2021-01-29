package the_dark_jumper.cannontracer.hotkey;

import java.util.ArrayList;
import java.util.stream.Collectors;
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
		main.dataManager.getTracerConfig().getHotkeys().add(hk.getHotkey());
	}

	public void removeHotkey(int index){
		Hotkey hotkey = activeHotkeys.remove(index);
		main.dataManager.getTracerConfig().getHotkeys().remove(hotkey.getHotkey());
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

	public void reloadConfig(){
		activeHotkeys.clear();
		activeHotkeys.addAll(main.dataManager.getTracerConfig().getHotkeys().stream().map(Hotkey::new).collect(Collectors.toList()));
	}
}
