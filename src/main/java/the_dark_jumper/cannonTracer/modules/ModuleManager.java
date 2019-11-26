package the_dark_jumper.cannontracer.modules;

import java.util.ArrayList;

import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleAxis;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBasic;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.ButtonBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.CounterBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.StateMachineBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.ToggleBehaviour;
import the_dark_jumper.cannontracer.tracking.TrackingData;

public class ModuleManager {
	Main main;
	public State state = State.MENU;
	
	public ArrayList<IModule> singlePlayerModules = new ArrayList<IModule>();
	public ArrayList<IModule> multiPlayerModules = new ArrayList<IModule>();
	
	public ModuleBasic tracerModeSP;
	public ModuleBasic xRayTracesSP;
	public ModuleBasic menuSP;
	public ModuleBasic renderBoxesSP;
	public ModuleBasic loadLastSecondSP;
	public ModuleBasic clearHistorySP;
	public ModuleAxis displayTickSP;
	
	public ModuleBasic xRayTracesMP;
	public ModuleBasic menuMP;
	public ModuleBasic renderBoxesMP;
	public ModuleBasic pullDataMP;
	public ModuleBasic clearDataMP;
	public ModuleAxis displayTickMP;
	
	public enum State{
		SINGLEPLAYER,
		MULTIPLAYER,
		MENU
	}
	
	public ModuleManager(Main main) {
		this.main = main;
		generateSinglePlayerModules();
		generateMultiPlayerModules();
	}
	
	public void generateSinglePlayerModules() {
		tracerModeSP = new ModuleBasic("TracerModeSP", true, false);
		tracerModeSP.setBehaviour(new StateMachineBehaviour(tracerModeSP, 0, new String[] {"Timed Render", "Permanent Render", "Last x Seconds"}, main.singlePlayerSettings.modeGNS));
		tracerModeSP.addKeybind(true, 20).addKeybind(true, 42);
		singlePlayerModules.add(tracerModeSP);
		
		xRayTracesSP = new ModuleBasic("XRayTracesSP", true, false);
		xRayTracesSP.setBehaviour(new ToggleBehaviour(xRayTracesSP, false, main.singlePlayerSettings.xRayTraceGNS));
		xRayTracesSP.addKeybind(true, 45).addKeybind(true, 42);
		singlePlayerModules.add(xRayTracesSP);
		
		menuSP = new ModuleBasic("MenuSP", false, true);
		menuSP.setBehaviour(new ToggleBehaviour(menuSP, false, main.singlePlayerSettings.renderMenuGNS));
		menuSP.addKeybind(true, 46).addKeybind(true, 42);
		singlePlayerModules.add(menuSP);
		
		renderBoxesSP = new ModuleBasic("RenderBoxesSP", true, false);
		renderBoxesSP.setBehaviour(new ToggleBehaviour(renderBoxesSP, false, main.singlePlayerSettings.renderBoxesGNS));
		renderBoxesSP.addKeybind(true, 48).addKeybind(true, 42);
		singlePlayerModules.add(renderBoxesSP);
		
		loadLastSecondSP = new ModuleBasic("LoadLastSecondSP", false, false);
		loadLastSecondSP.setBehaviour(new ButtonBehaviour(loadLastSecondSP, false, main.singlePlayerSettings::loadLastSeconds));
		loadLastSecondSP.addKeybind(true, 19).addKeybind(false, 42);
		singlePlayerModules.add(loadLastSecondSP);
		
		clearHistorySP = new ModuleBasic("ClearLastSecondSP", false, false);
		clearHistorySP.setBehaviour(new ButtonBehaviour(clearHistorySP, false, main.singlePlayerSettings::clearHistory));
		clearHistorySP.addKeybind(true, 19).addKeybind(true, 42);
		singlePlayerModules.add(clearHistorySP);
		
		displayTickSP = new ModuleAxis("DisplayTickSP", true, false);
		displayTickSP.setBehaviour(new CounterBehaviour(displayTickSP, 1, main.singlePlayerSettings.renderTickGNS).setMax(this::getMaxDisplayTickSP));
		displayTickSP.addPositiveKeybind(true, 333).addNegativeKeybind(true, 331);
		singlePlayerModules.add(displayTickSP);
	}
	
	public void generateMultiPlayerModules() {
		xRayTracesMP = new ModuleBasic("XRayTracesMP", true, false);
		xRayTracesMP.setBehaviour(new ToggleBehaviour(xRayTracesMP, false, main.multiPlayerSettings.xRayTraceGNS));
		xRayTracesMP.addKeybind(true, 45).addKeybind(true, 42);
		multiPlayerModules.add(xRayTracesMP);
		
		menuMP = new ModuleBasic("MenuMP", false, true);
		menuMP.setBehaviour(new ToggleBehaviour(menuMP, false, main.multiPlayerSettings.renderMenuGNS));
		menuMP.addKeybind(true, 46).addKeybind(true, 42);
		multiPlayerModules.add(menuMP);
		
		renderBoxesMP = new ModuleBasic("RenderBoxesMP", true, false);
		renderBoxesMP.setBehaviour(new ToggleBehaviour(renderBoxesMP, false, main.multiPlayerSettings.renderBoxesGNS));
		renderBoxesMP.addKeybind(true, 48).addKeybind(true, 42);
		multiPlayerModules.add(renderBoxesMP);
		
		pullDataMP = new ModuleBasic("PullDataMP", false, false);
		pullDataMP.setBehaviour(new ButtonBehaviour(pullDataMP, false, main.multiPlayerSettings::pullData));
		pullDataMP.addKeybind(true, 19).addKeybind(false, 42);
		multiPlayerModules.add(pullDataMP);
		
		clearDataMP = new ModuleBasic("ClearDataMP", false, false);
		clearDataMP.setBehaviour(new ButtonBehaviour(clearDataMP, false, main.multiPlayerSettings::clearData));
		clearDataMP.addKeybind(true, 19).addKeybind(true, 42);
		multiPlayerModules.add(clearDataMP);
		
		displayTickMP = new ModuleAxis("DisplayTickMP", true, false);
		displayTickMP.setBehaviour(new CounterBehaviour(displayTickMP, 1, main.multiPlayerSettings.renderTickGNS).setMax(this::getMaxDisplayTickMP));
		displayTickMP.addPositiveKeybind(true, 333).addNegativeKeybind(true, 331);
		multiPlayerModules.add(displayTickMP);
	}
	
	public ArrayList<IModule> getModules(){
		if(state == State.SINGLEPLAYER) {
			return singlePlayerModules;
		}else if(state == State.MULTIPLAYER) {
			return multiPlayerModules;
		}
		return new ArrayList<IModule>();
	}
	
	public int getMaxDisplayTickSP() {
		float max = 0;
		for(TrackingData trackingData : main.entityTracker.observedEntityIDSP.values()) {
			if(trackingData.timeGNS.get() > max) {
				max = trackingData.timeGNS.get();
			}
		}
		// + 1 as a safety net regarding truncating
		return (int)((max + 1) * 20);
	}
	
	public int getMaxDisplayTickMP() {
		float max = 0;
		for(TrackingData trackingData : main.entityTracker.observedEntityIDMP.values()) {
			if(trackingData.timeGNS.get() > max) {
				max = trackingData.timeGNS.get();
			}
		}
		// + 1 as a safety net regarding truncating
		return (int)((max + 1) * 20);
	}
	
	public void keyPressed(int key, boolean screenActive) {
		for(IModule module : getModules()) {
			if(screenActive && !module.getIsGlobal()) {
				continue;
			}
			module.onKeyEvent(key);
		}
	}
	
	public void keyReleased(int key) {
		for(IModule module : getModules()) {
			module.onKeyEvent(key);
		}
	}
}