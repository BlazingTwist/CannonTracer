package the_dark_jumper.cannontracer.configsaving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.prefs.Preferences;

import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.gui.GuiManager;
import the_dark_jumper.cannontracer.gui.OnscreenGUI;
import the_dark_jumper.cannontracer.hotkey.Hotkey;
import the_dark_jumper.cannontracer.hotkey.HotkeyManager;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleAxis;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBasic;
import the_dark_jumper.cannontracer.tracking.TrackingData;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class DataManager {
	public final Main main;
	
	public GetterAndSetter<String> configPathGNS = new GetterAndSetter<String>("");
	public GetterAndSetter<String> updatePathGNS = new GetterAndSetter<String>(""); 
	
	public DataManager(Main main) {
		this.main = main;
	}
	
	//C:\Users\Admin\Documents\The_Dark_Jumper_Cannon_Tracer
	public void Save() {
		if(configPathGNS.get().equals("")) {
			return;
		}
		
		Preferences prefs = Preferences.userNodeForPackage(DataManager.class);
		prefs.put("TracerConfigPath", configPathGNS.get());
		prefs.put("TracerUpdatePath", updatePathGNS.get());
		
		try {		
			/*FileWriter out = new FileWriter("C:\\Users\\" + System.getProperty("user.name") +
					"\\Documents\\The_Dark_Jumper_Cannon_Tracer\\tracer.config");*/
			FileWriter out = new FileWriter(configPathGNS.get());
			BufferedWriter bwout = new BufferedWriter(out);
			writeSinglePlayerConfig(bwout);			
			writeMultiPlayerConfig(bwout);
			writeHotkeys(bwout);
			writeGUIConfig(bwout);
			bwout.close();
		}catch(Exception e){
			System.out.println("thrown error while saving");
			e.printStackTrace();
		}
	}
	
	public void writeSinglePlayerConfig(BufferedWriter bwout) {
		writeServerSpecificConfig(bwout, "SinglePlayer", main.entityTracker.observedEntityIDSP, main.moduleManager.singlePlayerModules);
	}
	
	public void writeMultiPlayerConfig(BufferedWriter bwout) {
		writeServerSpecificConfig(bwout, "MultiPlayer", main.entityTracker.observedEntityIDMP, main.moduleManager.multiPlayerModules);
	}
	
	public void writeHotkeys(BufferedWriter bwout) {
		Header header = new Header("Hotkeys", "", 1);
		header.write(bwout);
		//command | trigger , keycode | trigger , keycode ...
		header.init("Hotkey Entry", "", 2);
		for(Hotkey hotkey : Main.getInstance().hotkeyManager.getHotkeys()) {
			header.content = new KeybindContent().setCommand(hotkey.commandGNS.get()).setKeybinds(hotkey.keybinds).buildContent();
			header.write(bwout);
		}
	}
	
	public void writeGUIConfig(BufferedWriter bwout) {
		Header header = new Header("guiConfig", "", 1);
		header.write(bwout);
		header.init("configEntry", "", 2);
		OnscreenGUI onscreenGUI = Main.getInstance().guiManager.onscreenGUI;
		GuiManager guiManager = Main.getInstance().guiManager;
		header.content = new DataTypes.ConfigDouble("xOffset", onscreenGUI.xOffsetGNS.get()).buildString();
		header.write(bwout);
		header.content = new DataTypes.ConfigDouble("yOffset", onscreenGUI.yOffsetGNS.get()).buildString();
		header.write(bwout);
		header.content = new DataTypes.ConfigDouble("fontHeight", guiManager.fontHeightGNS.get()).buildString();
		header.write(bwout);
	}
	
	public void writeServerSpecificConfig(BufferedWriter bwout, String headerName,
			HashMap<String, TrackingData> observerList, ArrayList<IModule> keybindList) {
		Header header = new Header(headerName, "", 1);
		header.write(bwout);
		//output observed entities
		//id | render | time | thickness | r | g | b | a
		header.init("Tracking Data", "", 2);
		for(Iterator<String> it = observerList.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			TrackingData trackingData = observerList.get(key);
			header.content = new TrackingDataContent(key, trackingData).buildContent();
			header.write(bwout);
		}
		//output singleplayer keybinds
		header.init("Keybind", "", 2);
		for(IModule module : keybindList) {
			if(module instanceof ModuleBasic) {
				ModuleBasic moduleBase = (ModuleBasic)module;
				header.content = new KeybindContent().setCommand(moduleBase.name).setKeybinds(moduleBase.keybinds).buildContent();
				header.write(bwout);
			}else if(module instanceof ModuleAxis) {
				ModuleAxis moduleAxis = (ModuleAxis)module;
				header.content = new KeybindContent().setCommand(moduleAxis.name + "+").setKeybinds(moduleAxis.positiveKeybinds).buildContent();
				header.write(bwout);
				header.content = new KeybindContent().setCommand(moduleAxis.name + "-").setKeybinds(moduleAxis.negativeKeybinds).buildContent();
				header.write(bwout);
			}
		}
	}
	
	public void Load() {
		Preferences prefs = Preferences.userNodeForPackage(DataManager.class);
		configPathGNS.set(prefs.get("TracerConfigPath", ""));
		updatePathGNS.set(prefs.get("TracerUpdatePath", ""));
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(configPathGNS.get())));
			ArrayList<String> lines = new ArrayList<>();
			for(String line; (line = br.readLine())!=null;) {
				lines.add(line);
			}
			loadServerSpecificConfig(lines, "SinglePlayer", main.entityTracker.observedEntityIDSP, main.moduleManager.singlePlayerModules);
			loadServerSpecificConfig(lines, "MultiPlayer", main.entityTracker.observedEntityIDMP, main.moduleManager.multiPlayerModules);
			loadHotkeys(lines);
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadServerSpecificConfig(ArrayList<String> lines, String headerName,
			HashMap<String, TrackingData> observerList, ArrayList<IModule> keybindList) {
		Header header = new Header(null, null, 0);
		TrackingDataContent trackingDataContent = new TrackingDataContent(observerList);
		KeybindContent keybindContent = new KeybindContent();
		boolean foundSection = false;
		for(Iterator<String> it = lines.iterator(); it.hasNext();) {
			String line = it.next();
			if(!header.readHeader(line)) {
				continue;
			}
			if(!foundSection) {
				if(header.level == 1 && header.name.equals(headerName)) {
					foundSection = true;
				}
				continue;
			}
			if(header.level == 1) {
				//reached next section, read (past tense) all lines of current section
				return;
			}
			if(header.level == 2 && header.content != null) {
				if(header.name.equals("Tracking Data")) {
					if(!trackingDataContent.readContent(header.content)) {
						//couldn't read tracking data... okay then, guess we're ignoring this one?
					}
				}else if(header.name.equals("Keybind")) {
					if(!keybindContent.readContent(header.content)) {
						//couldn't read keybind data... okay then, guess we're ignoring this one?
					}else {
						updateModule(keybindList, keybindContent);
					}
				}else {
					//unknown header, next
				}
				continue;
			}
		}
	}
	
	public void updateModule(ArrayList<IModule> modules, KeybindContent keybindContent) {
		for(IModule module : modules) {
			String command = keybindContent.command;
			char lastChar = command.charAt(command.length() - 1);
			if(lastChar == '+') {
				if(!(module.getName() + '+').equals(keybindContent.command)) {
					continue;
				}
				if(module instanceof ModuleAxis) {
					((ModuleAxis)module).setPositiveKeybinds(keybindContent.keybinds);
				}
			}else if(lastChar == '-') {
				if(!(module.getName() + '-').equals(keybindContent.command)) {
					continue;
				}
				if(module instanceof ModuleAxis) {
					((ModuleAxis)module).setNegativeKeybinds(keybindContent.keybinds);
				}
			}else {
				if(!module.getName().equals(keybindContent.command)) {
					continue;
				}
				if(module instanceof ModuleBasic) {
					((ModuleBasic)module).setKeybinds(keybindContent.keybinds);
				}
			}
		}
	}
	
	public void loadHotkeys(ArrayList<String> lines) {
		HotkeyManager hotkeyManager = Main.getInstance().hotkeyManager;
		Header header = new Header(null, null, 0);
		KeybindContent hotkeyContent = new KeybindContent();
		boolean foundSection = false;
		for(Iterator<String> it = lines.iterator(); it.hasNext(); ) {
			String line = it.next();
			if(!header.readHeader(line)) {
				continue;
			}
			if(!foundSection) {
				if(header.level == 1 && header.name.equals("Hotkeys")) {
					foundSection = true;
				}
				continue;
			}
			if(header.level == 1) {
				//reached next section, read (past tense) all lines of current section
				return;
			}
			if(header.level == 2 && header.content != null) {
				if(header.name.equals("Hotkey Entry")) {
					if(!hotkeyContent.readContent(header.content)) {
						//couldn't read hotkey... okay then, guess we're ignoring this one?
					}else {
						hotkeyManager.addHotkey(new Hotkey(hotkeyContent.command, hotkeyContent.keybinds));
						hotkeyContent = new KeybindContent();
					}
				}
			}
		}
	}
	
	public void loadGUIConfig(ArrayList<String> lines) {
		GuiManager guiManager = Main.getInstance().guiManager;
		OnscreenGUI onscreenGUI = Main.getInstance().guiManager.onscreenGUI;
		Header header = new Header(null, null, 0);
		DataTypes.ConfigString configString = new DataTypes.ConfigString();
		boolean foundSection = false;
		for(Iterator<String> it = lines.iterator(); it.hasNext(); ) {
			String line = it.next();
			if(!header.readHeader(line)) {
				continue;
			}
			if(!foundSection) {
				if(header.level == 1 && header.name.equals("guiConfig")) {
					foundSection = true;
				}
				continue;
			}
			if(header.level == 1) {
				//reached next section, read (past tense) all lines of current section
				return;
			}
			if(header.level == 2 && header.content != null) {
				if(header.name.equals("configEntry")) {
					if(!configString.readValue(header.content)) {
						//couldn't read offsets... okay then, guess we're ignoring this one?
					}else {
						if(configString.name.equals("xOffset")) {
							onscreenGUI.xOffsetGNS.set(Double.parseDouble(configString.value));
						}else if(configString.name.equals("yOffset")) {
							onscreenGUI.yOffsetGNS.set(Double.parseDouble(configString.value));
						}else if(configString.name.equals("fontHeight")) {
							guiManager.fontHeightGNS.set(Double.parseDouble(configString.value));
						}
					}
				}
			}
		}
	}
}