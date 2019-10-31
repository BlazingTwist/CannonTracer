package the_dark_jumper.cannontracer.configsaving;

import java.util.LinkedHashMap;

import the_dark_jumper.cannontracer.util.KeybindAccessors;

public class ModuleKeybindContent implements Header.Content{
	public String keybindName;
	public LinkedHashMap<String, KeybindAccessors> keybindSource;
	
	public ModuleKeybindContent(LinkedHashMap<String, KeybindAccessors> keybindSource) {
		init(null, keybindSource);
	}
	
	public ModuleKeybindContent(String bind1, LinkedHashMap<String, KeybindAccessors> keybindSource) {
		init(bind1, keybindSource);
	}
	
	public void init(String bind, LinkedHashMap<String, KeybindAccessors> keybindSource) {
		this.keybindName = bind;
		this.keybindSource = keybindSource;
	}
	
	public String buildContent() {
		String out = new DataTypes.ConfigString(keybindName,
				keybindSource.get(keybindName).accessors[0].getter.get() +
				" , " +
				keybindSource.get(keybindName).accessors[1].getter.get()).buildString();
		return out;
	}
	
	public boolean readContent(String content) {		
		DataTypes.ConfigString configString = new DataTypes.ConfigString();
		if(!configString.readValue(content)) {
			return false;
		}
		String keybindName = configString.name;
		String keys[] = configString.value.split(" , ");
		this.keybindName = keybindName;
		keybindSource.get(keybindName).accessors[0].setter.accept(Integer.parseInt(keys[0]));
		keybindSource.get(keybindName).accessors[1].setter.accept(Integer.parseInt(keys[1]));
		return true;
	}
}
