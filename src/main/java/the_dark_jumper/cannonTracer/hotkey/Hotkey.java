package the_dark_jumper.cannontracer.hotkey;

import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannontracer.Main;

public class Hotkey {	
	public String command = "";
	public void setCommand(String command) {this.command = command;}
	public String getCommand() {return this.command;}
	
	public LinkedList<KeybindData> keybinds = new LinkedList<>();
	private boolean ignoreInput;
	public int keybindCount = 0;
	
	public Hotkey() {}
	public Hotkey(String command, LinkedList<KeybindData> keybinds) {
		this.command = command;
		this.keybinds = keybinds;
		this.keybindCount = keybinds.size();
	}
	
	public Hotkey setKeybinds(LinkedList<KeybindData> keybinds) {
		this.keybinds = keybinds;
		return this;
	}
	
	public Hotkey addKeybind(int keycode, boolean triggerState) {
		keybinds.add(new KeybindData(keycode, triggerState));
		return this;
	}
	
	public void checkHotkeyState() {
		if(allKeysSatisfied()) {
			if(!ignoreInput) {
				onTriggeredChanged(true);
			}
		}else {
			if(ignoreInput) {
				onTriggeredChanged(false);
			}
		}
	}
	
	/*
	*returns true if the key is pressed and the triggerState is true
	*or if the key is not pressed and the trigger state is false
	*/
	private boolean isConditionSatisfied(int keycode, boolean triggerState) {
		return Main.getInstance().keyPressListener.pressedKeys.contains(keycode) == triggerState;
	}
	
	private boolean allKeysSatisfied() {
		for(KeybindData keybind : keybinds) {
			if(!isConditionSatisfied(keybind.keycode, keybind.triggerState)) {
				return false;
			}
		}
		return true;
	}
	
	private void onTriggeredChanged(boolean isTriggered) {
		ignoreInput = isTriggered;
		if(isTriggered) {
			Minecraft.getInstance().player.sendChatMessage(command);
		}
	}
}
