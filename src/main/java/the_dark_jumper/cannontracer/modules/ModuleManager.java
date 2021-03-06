package the_dark_jumper.cannontracer.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.configsaving.MultiPlayerKeybinds;
import the_dark_jumper.cannontracer.configsaving.SinglePlayerKeybinds;
import the_dark_jumper.cannontracer.configsaving.TrackingDataEntry;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleAxis;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBasic;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.ButtonBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.CounterBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.StateMachineBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.ToggleBehaviour;

public class ModuleManager {
	Main main;
	public State state = State.MENU;

	public ArrayList<IModule> singlePlayerModules = new ArrayList<>();
	public ArrayList<IModule> multiPlayerModules = new ArrayList<>();

	public ModuleBasic tracerModeSP;
	public ModuleBasic xRayTracesSP;
	public ModuleBasic menuSP;
	public ModuleBasic renderBoxesSP;
	public ModuleBasic loadLastSecondSP;
	public ModuleBasic clearHistorySP;
	public ModuleAxis displayTickSP;
	public ModuleBasic showFirstTickSP;
	public ModuleBasic showLastTickSP;

	public ModuleBasic xRayTracesMP;
	public ModuleBasic renderLinesMP;
	public ModuleBasic menuMP;
	public ModuleBasic pullDataMP;
	public ModuleBasic clearDataMP;
	public ModuleAxis displayTickMP;
	public ModuleBasic findNextDespawnTickMP;
	public ModuleBasic findPreviousDespawnTickMP;
	public ModuleBasic showFirstTickMP;
	public ModuleBasic showLastTickMP;

	private HashSet<IFocusableFrame> focusedFrames = new HashSet<>();

	public enum State {
		SINGLEPLAYER,
		MULTIPLAYER,
		MENU
	}

	public ModuleManager(Main main) {
		this.main = main;

		// prepare default values
		generateSinglePlayerModules();
		generateMultiPlayerModules();
	}

	public void onFrameFocusChanged(IFocusableFrame frame, boolean focus) {
		if (focus) {
			focusedFrames.add(frame);
		} else {
			focusedFrames.remove(frame);
		}
	}

	public void focusReleaseAllFrames(){
		List<IFocusableFrame> focusedFramesCopy = new ArrayList<>(focusedFrames);
		for (IFocusableFrame focusedFrame : focusedFramesCopy) {
			focusedFrame.onFocusChange(false);
		}
	}

	public void generateSinglePlayerModules() {
		tracerModeSP = new ModuleBasic("TracerModeSP", true, false);
		tracerModeSP.setBehaviour(new StateMachineBehaviour(tracerModeSP, 0, new String[]{"Timed Render", "Permanent Render", "Last x Seconds"}, main.singlePlayerSettings.modeGNS));
		singlePlayerModules.add(tracerModeSP);

		xRayTracesSP = new ModuleBasic("XRayTracesSP", true, false);
		xRayTracesSP.setBehaviour(new ToggleBehaviour(xRayTracesSP, false, main.singlePlayerSettings.xRayTraceGNS));
		singlePlayerModules.add(xRayTracesSP);

		menuSP = new ModuleBasic("MenuSP", false, true);
		menuSP.setBehaviour(new ToggleBehaviour(menuSP, false, main.singlePlayerSettings.renderMenuGNS));
		singlePlayerModules.add(menuSP);

		renderBoxesSP = new ModuleBasic("RenderBoxesSP", true, false);
		renderBoxesSP.setBehaviour(new ToggleBehaviour(renderBoxesSP, false, main.singlePlayerSettings.renderBoxesGNS));
		singlePlayerModules.add(renderBoxesSP);

		loadLastSecondSP = new ModuleBasic("LoadLastSecondSP", false, false);
		loadLastSecondSP.setBehaviour(new ButtonBehaviour(loadLastSecondSP, false, main.singlePlayerSettings::loadLastSeconds));
		singlePlayerModules.add(loadLastSecondSP);

		clearHistorySP = new ModuleBasic("ClearHistorySP", false, false);
		clearHistorySP.setBehaviour(new ButtonBehaviour(clearHistorySP, false, main.singlePlayerSettings::clearHistory));
		singlePlayerModules.add(clearHistorySP);

		displayTickSP = new ModuleAxis("DisplayTickSP", true, false);
		displayTickSP.setBehaviour(
				new CounterBehaviour(displayTickSP, 1, main.singlePlayerSettings.renderTickGNS)
						.setMax(this::getMaxDisplayTickSP)
		);
		singlePlayerModules.add(displayTickSP);

		showFirstTickSP = new ModuleBasic("ShowFirstTickSP", false, false);
		showFirstTickSP.setBehaviour(new ButtonBehaviour(showFirstTickSP, false, main.singlePlayerSettings::showFirstTick));
		singlePlayerModules.add(showFirstTickSP);

		showLastTickSP = new ModuleBasic("ShowLastTickSP", false, false);
		showLastTickSP.setBehaviour(new ButtonBehaviour(showLastTickSP, false, main.singlePlayerSettings::showLastTick));
		singlePlayerModules.add(showLastTickSP);
	}

	public void generateMultiPlayerModules() {
		xRayTracesMP = new ModuleBasic("XRayTracesMP", true, false);
		xRayTracesMP.setBehaviour(new ToggleBehaviour(xRayTracesMP, false, main.multiPlayerSettings.xRayTraceGNS));
		multiPlayerModules.add(xRayTracesMP);

		renderLinesMP = new ModuleBasic("RenderLinesMP", true, false);
		renderLinesMP.setBehaviour(new ToggleBehaviour(renderLinesMP, true, main.multiPlayerSettings.renderLinesGNS));
		multiPlayerModules.add(renderLinesMP);

		menuMP = new ModuleBasic("MenuMP", false, true);
		menuMP.setBehaviour(new ToggleBehaviour(menuMP, false, main.multiPlayerSettings.renderMenuGNS));
		multiPlayerModules.add(menuMP);

		pullDataMP = new ModuleBasic("PullDataMP", false, false);
		pullDataMP.setBehaviour(new ButtonBehaviour(pullDataMP, false, main.multiPlayerSettings::pullData));
		multiPlayerModules.add(pullDataMP);

		clearDataMP = new ModuleBasic("ClearDataMP", false, false);
		clearDataMP.setBehaviour(new ButtonBehaviour(clearDataMP, false, main.multiPlayerSettings::clearData));
		multiPlayerModules.add(clearDataMP);

		displayTickMP = new ModuleAxis("DisplayTickMP", true, false);
		displayTickMP.setBehaviour(
				new CounterBehaviour(displayTickMP, 1, main.multiPlayerSettings.renderTickGNS)
						.setMax(this::getMaxDisplayTickMP)
		);
		multiPlayerModules.add(displayTickMP);

		findNextDespawnTickMP = new ModuleBasic("DisplayNextDespawnTickMP", false, false);
		findNextDespawnTickMP.setBehaviour(new ButtonBehaviour(findNextDespawnTickMP, false, main.multiPlayerSettings::findNextDespawnTick));
		multiPlayerModules.add(findNextDespawnTickMP);

		findPreviousDespawnTickMP = new ModuleBasic("DisplayPrevDespawnTickMP", false, false);
		findPreviousDespawnTickMP.setBehaviour(new ButtonBehaviour(findPreviousDespawnTickMP, false, main.multiPlayerSettings::findPreviousDespawnTick));
		multiPlayerModules.add(findPreviousDespawnTickMP);

		showFirstTickMP = new ModuleBasic("ShowFirstTickMP", false, false);
		showFirstTickMP.setBehaviour(new ButtonBehaviour(showFirstTickMP, false, main.multiPlayerSettings::showFirstTick));
		multiPlayerModules.add(showFirstTickMP);

		showLastTickMP = new ModuleBasic("ShowLastTickMP", false, false);
		showLastTickMP.setBehaviour(new ButtonBehaviour(showLastTickMP, false, main.multiPlayerSettings::showLastTick));
		multiPlayerModules.add(showLastTickMP);
	}

	public void reloadConfig() {
		SinglePlayerKeybinds keysSP = main.dataManager.getTracerConfig().getSinglePlayerConfig().getKeybinds();
		tracerModeSP.setKeybind(keysSP.getTracerModeSP());
		xRayTracesSP.setKeybind(keysSP.getxRayTracesSP());
		menuSP.setKeybind(keysSP.getMenuSP());
		renderBoxesSP.setKeybind(keysSP.getRenderBoxesSP());
		loadLastSecondSP.setKeybind(keysSP.getLoadLastSecondSP());
		clearHistorySP.setKeybind(keysSP.getClearHistorySP());
		displayTickSP.setPositiveKeybind(keysSP.getDisplayTickSPAdd());
		displayTickSP.setNegativeKeybind(keysSP.getDisplayTickSPSub());
		showFirstTickSP.setKeybind(keysSP.getShowFirstTickSP());
		showLastTickSP.setKeybind(keysSP.getShowLastTickSP());

		MultiPlayerKeybinds keysMP = main.dataManager.getTracerConfig().getMultiPlayerConfig().getKeybinds();
		xRayTracesMP.setKeybind(keysMP.getxRayTracesMP());
		renderLinesMP.setKeybind(keysMP.getRenderLinesMP());
		menuMP.setKeybind(keysMP.getMenuMP());
		pullDataMP.setKeybind(keysMP.getPullDataMP());
		clearDataMP.setKeybind(keysMP.getClearDataMP());
		displayTickMP.setPositiveKeybind(keysMP.getDisplayTickMPAdd());
		displayTickMP.setNegativeKeybind(keysMP.getDisplayTickMPSub());
		findNextDespawnTickMP.setKeybind(keysMP.getDisplayDespawnTickMP());
		findPreviousDespawnTickMP.setKeybind(keysMP.getDisplayPreviousDespawnTickMP());
		showFirstTickMP.setKeybind(keysMP.getShowFirstTickMP());
		showLastTickMP.setKeybind(keysMP.getShowLastTickMP());
	}

	public ArrayList<IModule> getModules() {
		if (state == State.SINGLEPLAYER) {
			return singlePlayerModules;
		} else if (state == State.MULTIPLAYER) {
			return multiPlayerModules;
		}
		return new ArrayList<>();
	}

	public int getMaxDisplayTickSP() {
		float max = 0;
		for (TrackingDataEntry trackingData : main.dataManager.getTrackingDataSP().values()) {
			if (trackingData.getTime() > max) {
				max = trackingData.getTime();
			}
		}
		// + 1 as a safety net regarding truncating
		return (int) ((max + 1) * 20);
	}

	public int getMaxDisplayTickMP() {
		float max = 0;
		for (TrackingDataEntry trackingData : main.dataManager.getTrackingDataMP().values()) {
			if (trackingData.getTime() > max) {
				max = trackingData.getTime();
			}
		}
		// + 1 as a safety net regarding truncating
		return (int) ((max + 1) * 20);
	}

	public void keyPressed(int key, boolean screenActive) {
		if (!focusedFrames.isEmpty()) { // ignore even global keybinds when textField is focused
			if (!screenActive) {
				// TODO this really doesn't belong here, but eh
				for (IFocusableFrame focusedFrame : focusedFrames) {
					focusedFrame.onFocusChange(false);
				}
			} else {
				return;
			}
		}

		for (IModule module : getModules()) {
			if (screenActive && !module.isGlobal()) {
				continue;
			}
			module.onKeyEvent(key);
		}
	}

	public void keyReleased(int key) {
		for (IModule module : getModules()) {
			module.onKeyEvent(key);
		}
	}
}