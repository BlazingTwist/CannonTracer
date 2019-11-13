package the_dark_jumper.cannontracer.hotkey;

import the_dark_jumper.cannontracer.util.KeybindData;

public class KeybindHotkeyEntry {
	public HotkeyTableEntry parent;
	public KeybindData keybindData;
	
	public KeybindHotkeyEntry(HotkeyTableEntry parent, KeybindData keybindData) {
		this.parent = parent;
		this.keybindData = keybindData;
	}
}
