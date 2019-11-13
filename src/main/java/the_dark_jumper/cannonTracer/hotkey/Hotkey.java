package the_dark_jumper.cannontracer.hotkey;

import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.util.GetterAndSetter;
import the_dark_jumper.cannontracer.util.KeybindData;

public class Hotkey {
	public GetterAndSetter<String> commandGNS = new GetterAndSetter<String>("");
	
	public LinkedList<KeybindData> keybinds = new LinkedList<>();
	private boolean ignoreInput;
	
	public Hotkey() {}
	public Hotkey(String command, LinkedList<KeybindData> keybinds) {
		commandGNS.set(command);
		this.keybinds = keybinds;
	}
	
	public Hotkey setKeybinds(LinkedList<KeybindData> keybinds) {
		this.keybinds = keybinds;
		return this;
	}
	
	public Hotkey addKeybind(boolean triggerState, int keycode) {
		keybinds.add(new KeybindData(triggerState, keycode));
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
	
	private boolean isConditionSatisfied(int keycode, boolean triggerState) {
		return Main.getInstance().keyPressListener.pressedKeys.contains(keycode) == triggerState;
	}
	
	private boolean allKeysSatisfied() {
		for(KeybindData keybind : keybinds) {
			if(!isConditionSatisfied(keybind.keycodeGNS.get(), keybind.triggerGNS.get())) {
				return false;
			}
		}
		return true;
	}
	
	private void onTriggeredChanged(boolean isTriggered) {
		ignoreInput = isTriggered;
		if(isTriggered) {
			Minecraft.getInstance().player.sendChatMessage(commandGNS.get());
		}
	}
}
