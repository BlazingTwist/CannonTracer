package the_dark_jumper.cannontracer.hotkey;

import java.util.ArrayList;
import jumpercommons.GetterAndSetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import the_dark_jumper.cannontracer.configsaving.HotkeyEntry;
import the_dark_jumper.cannontracer.listeners.ServerChatListener;

public class Hotkey {
	private HotkeyEntry hotkey = new HotkeyEntry();
	private boolean ignoreInput;

	public Hotkey() {
	}

	public Hotkey(String command, ArrayList<GetterAndSetter<Integer>> trigger, ArrayList<GetterAndSetter<Integer>> exclude) {
		this.hotkey = new HotkeyEntry(command, trigger, exclude);
	}

	public Hotkey(HotkeyEntry hotkey) {
		this.hotkey = hotkey;
	}

	public HotkeyEntry getHotkey() {
		return hotkey;
	}

	public Hotkey setHotkey(HotkeyEntry hotkey) {
		this.hotkey = hotkey;
		return this;
	}

	public void checkHotkeyState() {
		if (hotkey.isSatisfied()) {
			if (!ignoreInput) {
				onTriggeredChanged(true);
			}
		} else {
			if (ignoreInput) {
				onTriggeredChanged(false);
			}
		}
	}

	private void onTriggeredChanged(boolean isTriggered) {
		ignoreInput = isTriggered;
		if (isTriggered) {
			String message = hotkey.getCommand();
			if (ServerChatListener.handleChatMessageSent(message)) {
				return;
			}
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if (player != null) {
				player.sendChatMessage(hotkey.getCommand());
			}
		}
	}
}
