package the_dark_jumper.cannontracer.configsaving;

import java.util.ArrayList;

import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBase;

@Deprecated
public class ModuleKeybindContent{
	public String keybindName;
	public ArrayList<ModuleBase> keybindSource;
	
	public ModuleKeybindContent(ArrayList<ModuleBase> keybindSource) {
		init(null, keybindSource);
	}
	
	public ModuleKeybindContent(String bind1, ArrayList<ModuleBase> keybindSource) {
		init(bind1, keybindSource);
	}
	
	public void init(String bind, ArrayList<ModuleBase> keybindSource) {
		this.keybindName = bind;
		this.keybindSource = keybindSource;
	}
	
	/*public String buildContent() {
		//target: 
		String out = new DataTypes.ConfigString(keybindName,
				keybindSource.get(keybindName).accessors[0].get() +
				" , " +
				keybindSource.get(keybindName).accessors[1].get()).buildString();
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
		keybindSource.get(keybindName).accessors[0].set(Integer.parseInt(keys[0]));
		keybindSource.get(keybindName).accessors[1].set(Integer.parseInt(keys[1]));
		return true;
	}*/
}
