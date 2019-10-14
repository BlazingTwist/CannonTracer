package the_dark_jumper.cannonTracer.configSaving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.util.KeybindAccessors;
import the_dark_jumper.cannonTracer.util.TrackingData;

public class DataManager {
	public final Main main;
	
	public DataManager(Main main) {
		this.main = main;
	}
	
	//C:\Users\Admin\Documents\The_Dark_Jumper_Cannon_Tracer
	public void Save() {
		try {		
			FileWriter out = new FileWriter("C:\\Users\\" + System.getProperty("user.name") +
					"\\Documents\\The_Dark_Jumper_Cannon_Tracer\\tracer.config");
			BufferedWriter bwout = new BufferedWriter(out);
			writeSinglePlayerConfig(bwout);			
			writeMultiPlayerConfig(bwout);		
			bwout.close();
		}catch(Exception e){
			System.out.println("thrown error while saving");
			e.printStackTrace();
		}
	}
	
	public void writeSinglePlayerConfig(BufferedWriter bwout) {
		writeServerSpecificConfig(bwout, "SinglePlayer", main.entityTracker.observedEntityIDSP, main.keybindManagerSP.variables);
	}
	
	public void writeMultiPlayerConfig(BufferedWriter bwout) {
		writeServerSpecificConfig(bwout, "MultiPlayer", main.entityTracker.observedEntityIDMP, main.keybindManagerMP.variables);
	}
	
	public void writeServerSpecificConfig(BufferedWriter bwout, String headerName,
			HashMap<String, TrackingData> observerList, LinkedHashMap<String, KeybindAccessors> keybindList) {
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
		for(Iterator<String> it = keybindList.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			header.content = new ModuleKeybindContent(key, keybindList).buildContent();
			header.write(bwout);
		}
	}
	
	public void Load() {
		try{
			BufferedReader br=new BufferedReader(new FileReader(new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\The_Dark_Jumper_Cannon_Tracer\\tracer.config")));
			ArrayList<String> lines = new ArrayList<>();
			for(String line; (line = br.readLine())!=null;) {
				lines.add(line);
			}
			loadServerSpecificConfig(lines, "SinglePlayer", main.entityTracker.observedEntityIDSP, main.keybindManagerSP.variables);
			loadServerSpecificConfig(lines, "MultiPlayer", main.entityTracker.observedEntityIDMP, main.keybindManagerMP.variables);
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadServerSpecificConfig(ArrayList<String> lines, String headerName,
			HashMap<String, TrackingData> observerList, LinkedHashMap<String, KeybindAccessors> keybindList) {
		Header header = new Header(null, null, 0);
		TrackingDataContent trackingDataContent = new TrackingDataContent(observerList);
		ModuleKeybindContent mkContent = new ModuleKeybindContent(keybindList);
		boolean foundSection = false;
		for(Iterator<String> it = lines.iterator(); it.hasNext();) {
			String line = it.next();
			if(!header.readHeader(line)) {
				continue;
			}
			if(!foundSection && header.level == 1 && header.name.equals(headerName)) {
				foundSection = true;
				continue;
			}
			if(!foundSection) {
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
					if(!mkContent.readContent(header.content)) {
						//couldn't read keybind data... okay then, guess we're ignoring this one?
					}
				}else {
					//unknown header, next
				}
				continue;
			}
		}
	}
}