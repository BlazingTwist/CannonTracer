package the_dark_jumper.cannontracer.modules;

import java.util.ArrayList;

import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.modules.moduleelements.ButtonBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.CounterBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleAxis;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBase;
import the_dark_jumper.cannontracer.modules.moduleelements.StateMachineBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.ToggleBehaviour;
import the_dark_jumper.cannontracer.util.TrackingData;

public class ModuleManager {
	Main main;
	public State state = State.MENU;
	
	public ArrayList<IModule> singlePlayerModules = new ArrayList<IModule>();
	public ArrayList<IModule> multiPlayerModules = new ArrayList<IModule>();
	
	public ModuleBase tracerModeSP;
	public ModuleBase xRayTracesSP;
	public ModuleBase menuSP;
	public ModuleBase renderBoxesSP;
	public ModuleBase loadLastSecondSP;
	public ModuleBase clearHistorySP;
	public ModuleAxis displayTickSP;
	
	public ModuleBase xRayTracesMP;
	public ModuleBase menuMP;
	public ModuleBase renderBoxesMP;
	public ModuleBase pullDataMP;
	public ModuleBase clearDataMP;
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
		tracerModeSP = new ModuleBase("TracerModeSP", true, false);
		tracerModeSP.setBehaviour(new StateMachineBehaviour(tracerModeSP, 0, new String[] {"Timed Render", "Permanent Render", "Last x Seconds"}, main.singlePlayerSettings.modeGNS.setter));
		tracerModeSP.addKeybind(true, 20).addKeybind(true, 42);
		singlePlayerModules.add(tracerModeSP);
		
		xRayTracesSP = new ModuleBase("XRayTracesSP", true, false);
		xRayTracesSP.setBehaviour(new ToggleBehaviour(xRayTracesSP, false, main.singlePlayerSettings.xRayTraceGNS.setter));
		xRayTracesSP.addKeybind(true, 45).addKeybind(true, 42);
		singlePlayerModules.add(xRayTracesSP);
		
		menuSP = new ModuleBase("MenuSP", false, true);
		menuSP.setBehaviour(new ToggleBehaviour(menuSP, false, main.singlePlayerSettings.renderMenuGNS.setter));
		menuSP.addKeybind(true, 46).addKeybind(true, 42);
		singlePlayerModules.add(menuSP);
		
		renderBoxesSP = new ModuleBase("RenderBoxesSP", true, false);
		renderBoxesSP.setBehaviour(new ToggleBehaviour(renderBoxesSP, false, main.singlePlayerSettings.renderBoxesGNS.setter));
		renderBoxesSP.addKeybind(true, 48).addKeybind(true, 42);
		singlePlayerModules.add(renderBoxesSP);
		
		loadLastSecondSP = new ModuleBase("LoadLastSecondSP", false, false);
		loadLastSecondSP.setBehaviour(new ButtonBehaviour(loadLastSecondSP, false, main.singlePlayerSettings::loadLastSeconds));
		loadLastSecondSP.addKeybind(true, 19).addKeybind(false, 42);
		singlePlayerModules.add(loadLastSecondSP);
		
		clearHistorySP = new ModuleBase("ClearLastSecondSP", false, false);
		clearHistorySP.setBehaviour(new ButtonBehaviour(clearHistorySP, false, main.singlePlayerSettings::clearHistory));
		clearHistorySP.addKeybind(true, 19).addKeybind(true, 42);
		singlePlayerModules.add(clearHistorySP);
		
		displayTickSP = new ModuleAxis("DisplayTickSP", true, false);
		displayTickSP.setBehaviour(new CounterBehaviour(displayTickSP, 1, main.singlePlayerSettings.renderTickGNS).setMax(this::getMaxDisplayTickSP));
		displayTickSP.addPositiveKeybind(true, 333).addNegativeKeybind(true, 331);
		singlePlayerModules.add(displayTickSP);
	}
	
	public void generateMultiPlayerModules() {
		xRayTracesMP = new ModuleBase("XRayTracesMP", true, false);
		xRayTracesMP.setBehaviour(new ToggleBehaviour(xRayTracesMP, false, main.multiPlayerSettings.xRayTraceGNS.setter));
		xRayTracesMP.addKeybind(true, 45).addKeybind(true, 42);
		multiPlayerModules.add(xRayTracesMP);
		
		menuMP = new ModuleBase("MenuMP", false, true);
		menuMP.setBehaviour(new ToggleBehaviour(menuMP, false, main.multiPlayerSettings.renderMenuGNS.setter));
		menuMP.addKeybind(true, 46).addKeybind(true, 42);
		multiPlayerModules.add(menuMP);
		
		renderBoxesMP = new ModuleBase("RenderBoxesMP", true, false);
		renderBoxesMP.setBehaviour(new ToggleBehaviour(renderBoxesMP, false, main.multiPlayerSettings.renderBoxesGNS.setter));
		renderBoxesMP.addKeybind(true, 48).addKeybind(true, 42);
		multiPlayerModules.add(renderBoxesMP);
		
		pullDataMP = new ModuleBase("PullDataMP", false, false);
		pullDataMP.setBehaviour(new ButtonBehaviour(pullDataMP, false, main.multiPlayerSettings::pullData));
		pullDataMP.addKeybind(true, 19).addKeybind(false, 42);
		multiPlayerModules.add(pullDataMP);
		
		clearDataMP = new ModuleBase("ClearDataMP", false, false);
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
		return null;
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
	
	/*synchronized public void registerSinglePlayerModules() {
		if(activeModules.size() != 0) {
			return;
		}
		if(lastSecondSP == null) {
			lastSecondSP = new ModuleOnOff("Last Second", false, false, false, main.singlePlayerSettings::lastSeconds, main.keybindManagerSP.lastSecond1, main.keybindManagerSP.lastSecond2);
		}
		if(displayTickSP == null) {
			displayTickSP = new ModuleCounter("Display Tick", true, false, 0, this::getMaxDisplayTickSP, 1, main.singlePlayerSettings.renderTickGNS, main.keybindManagerSP.prevTick, main.keybindManagerSP.nextTick);
		}
		activeModules.add(tracerModeSP);
		activeModules.add(xRayTracesSP);
		activeModules.add(menuSP);
		activeModules.add(renderBoxesSP);
		activeModules.add(lastSecondSP);
		activeModules.add(displayTickSP);
	}*/
	
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
	
	/*synchronized public void registerMultiplayerModules() {
		if(activeModules.size() != 0) {
			return;
		}
		if(xRayTracesMP == null) {
			xRayTracesMP = new ModuleToggle("XRay Traces", true, false, false, main.multiPlayerSettings.xRayTraceGNS.setter, main.keybindManagerMP.xRayTraces1, main.keybindManagerMP.xRayTraces2);
		}
		if(menuMP == null) {
			menuMP = new ModuleToggle("Menu", false, true, false, main.multiPlayerSettings.renderMenuGNS.setter, main.keybindManagerMP.menu1, main.keybindManagerMP.menu2);
		}
		if(renderBoxesMP == null) {
			renderBoxesMP = new ModuleToggle("Render Boxes", true, false, false, main.multiPlayerSettings.renderBoxesGNS.setter, main.keybindManagerMP.rendBox1, main.keybindManagerMP.rendBox2);
		}
		if(pullDataMP == null) {
			pullDataMP = new ModuleOnOff("pullData", false, false, false, main.multiPlayerSettings::pullData, main.keybindManagerMP.pullData1, main.keybindManagerMP.pullData2);
		}
		if(displayTickMP == null) {
			displayTickMP = new ModuleCounter("Display Tick", true, false, 0, this::getMaxDisplayTickMP, 1, main.multiPlayerSettings.renderTickGNS, main.keybindManagerMP.prevTick, main.keybindManagerMP.nextTick);
		}
		activeModules.add(xRayTracesMP);
		activeModules.add(menuMP);
		activeModules.add(renderBoxesMP);
		activeModules.add(pullDataMP);
		activeModules.add(displayTickMP);
	}*/
	
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