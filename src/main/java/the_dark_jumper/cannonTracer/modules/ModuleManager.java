package the_dark_jumper.cannonTracer.modules;

import java.util.ArrayList;

import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleBase;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleCounter;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleOnOff;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleStateMachine;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleToggle;
import the_dark_jumper.cannonTracer.util.TrackingData;

public class ModuleManager {
	Main main;
	
	public ArrayList<ModuleBase> activeModules = new ArrayList<ModuleBase>();
	public State state = State.MENU;
	
	public ModuleStateMachine tracerModeSP = null;
	public ModuleToggle xRayTracesSP = null;
	public ModuleToggle menuSP = null;
	public ModuleToggle renderBoxesSP = null;
	public ModuleOnOff lastSecondSP = null;
	public ModuleCounter displayTickSP = null;
	
	public ModuleToggle xRayTracesMP = null;
	public ModuleToggle menuMP = null;
	public ModuleToggle renderBoxesMP = null;
	public ModuleOnOff pullDataMP = null;
	public ModuleCounter displayTickMP = null;
	
	public enum State{
		SINGLEPLAYER,
		MULTIPLAYER,
		MENU
	}
	
	public ModuleManager(Main main) {
		this.main = main;
	}
	
	public ArrayList<ModuleBase> getModules(){
		return activeModules;
	}
	
	public void clearModules() {
		activeModules.clear();
	}
	
	public void registerSinglePlayerModules() {
		if(tracerModeSP == null) {
			tracerModeSP = new ModuleStateMachine("Tracer Mode", true, false, 3, 0, main.singlePlayerSettings.modeGNS.setter, main.keybindManagerSP.tracerBind1, main.keybindManagerSP.tracerBind2,"Timed Render", "Permanent Render", "Last 5 Seconds");
		}
		if(xRayTracesSP == null) {
			xRayTracesSP = new ModuleToggle("XRay Traces", true, false, false, main.singlePlayerSettings.xRayTraceGNS.setter, main.keybindManagerSP.xRayTraces1, main.keybindManagerSP.xRayTraces2);
		}
		if(menuSP == null) {
			menuSP = new ModuleToggle("Menu", false, true, false, main.singlePlayerSettings.renderMenuGNS.setter, main.keybindManagerSP.menu1, main.keybindManagerSP.menu2);
		}
		if(renderBoxesSP == null) {
			renderBoxesSP = new ModuleToggle("Render Boxes", true, false, false, main.singlePlayerSettings.renderBoxesGNS.setter, main.keybindManagerSP.rendBox1, main.keybindManagerSP.rendBox2);
		}
		if(lastSecondSP == null) {
			lastSecondSP = new ModuleOnOff("Last Second", false, false, false, main.singlePlayerSettings::lastSeconds, main.keybindManagerSP.lastSecond1, main.keybindManagerSP.lastSecond2);
		}
		if(displayTickSP == null) {
			displayTickSP = new ModuleCounter("Display Tick", true, false, 0, 100, 1, main.singlePlayerSettings.renderTickGNS, main.keybindManagerSP.prevTick, main.keybindManagerSP.nextTick);
		}
		activeModules.add(tracerModeSP);
		activeModules.add(xRayTracesSP);
		activeModules.add(menuSP);
		activeModules.add(renderBoxesSP);
		activeModules.add(lastSecondSP);
		activeModules.add(displayTickSP);
	}
	
	public int getMaxDisplayTickMP() {
		float max = 0;
		for(TrackingData trackingData : main.entityTracker.observedEntityIDMP.values()) {
			if(trackingData.timeGNS.getter.get() > max) {
				max = trackingData.timeGNS.getter.get();
			}
		}
		// + 1 as a safety net regarding truncating
		return (int)((max + 1) * 20);
	}
	
	public void registerMultiplayerModules() {
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
	}
	
	public void keyPressed(int key, boolean screenActive) {
		for(ModuleBase module : activeModules) {
			if(screenActive && !module.isGlobal) {
				continue;
			}
			module.keyPressed(key);
		}
	}
	
	public void keyReleased(int key) {
		for(ModuleBase module : activeModules) {
			module.keyReleased(key);
		}
	}
}