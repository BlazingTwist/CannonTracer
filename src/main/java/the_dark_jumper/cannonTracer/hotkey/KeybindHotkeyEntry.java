package the_dark_jumper.cannontracer.hotkey;

import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class KeybindHotkeyEntry {
	public HotkeyTableEntry parent;
	public KeybindData keybindData;
	public GetterAndSetter<Boolean> triggerGNS;
	public GetterAndSetter<Integer> keycodeGNS;
	
	{
		triggerGNS = new GetterAndSetter<Boolean>(this::getTriggerState, this::setTriggerState);
		keycodeGNS = new GetterAndSetter<Integer>(this::getKeybind, this::setKeybind);
	}
	
	public KeybindHotkeyEntry(HotkeyTableEntry parent) {
		init(parent, new KeybindData(1, true));
	}
	
	public KeybindHotkeyEntry(HotkeyTableEntry parent, KeybindData keybindData) {
		init(parent, keybindData);
	}
	
	private void init(HotkeyTableEntry parent, KeybindData keybindData) {
		this.parent = parent;
		this.keybindData = keybindData;
	}
	
	public boolean getTriggerState() {
		return keybindData.triggerState;
	}
	
	public void setTriggerState(boolean triggerState) {
		keybindData.triggerState = triggerState;
	}
	
	public int getKeybind() {
		return keybindData.keycode;
	}
	
	public void setKeybind(int keycode) {
		keybindData.keycode = keycode;
	}
}
