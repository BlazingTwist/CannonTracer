package the_dark_jumper.cannontracer.configsaving;

import java.util.LinkedList;

import the_dark_jumper.cannontracer.hotkey.KeybindData;

public class HotkeyContent implements Header.Content{
	public String command;
	public LinkedList<KeybindData> keybinds;
	
	public HotkeyContent setCommand(String command) {
		this.command = command;
		return this;
	}
	
	public HotkeyContent setKeybinds(LinkedList<KeybindData> keybinds) {
		this.keybinds = keybinds;
		return this;
	}
	
	public String buildContent() {
		String out = new DataTypes.ConfigString("command", command).buildString();
		for(KeybindData keybind : keybinds) {
			out += " | " + new DataTypes.ConfigString("keybind", keybind.triggerState + " , " + keybind.keycode).buildString();
		}
		return out;
	}
	
	public boolean readContent(String content) {
		String data[] = content.split(" \\| ");
		command = null;
		keybinds = new LinkedList<>();
		DataTypes.ConfigString configString = new DataTypes.ConfigString();
		for(String str : data) {
			if(!configString.readValue(str)) {
				return false;
			}
			if(configString.name.equals("command")) {
				command = configString.value;
			}else if(configString.name.equals("keybind")) {
				String keys[] = configString.value.split(" , ");
				keybinds.add(new KeybindData(Integer.parseInt(keys[1]), Boolean.parseBoolean(keys[0])));
			}
		}
		if(command == null) {
			return false;
		}
		return true;
	}
}
