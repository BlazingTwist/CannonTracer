package the_dark_jumper.cannonTracer.modules;

import java.util.ArrayList;

import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleBase;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleCounter;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleOnOff;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleStateMachine;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleToggle;

public class ModuleManager {
	Main main;
	
	public ArrayList<ModuleBase> activeModules = new ArrayList<ModuleBase>();
	public State state = State.MENU;
	
	public ModuleStateMachine tracerMode = null;
	public ModuleToggle xRayTraces = null;
	public ModuleToggle menu = null;
	public ModuleToggle renderBoxes = null;
	public ModuleOnOff lastSecond = null;
	public ModuleCounter displayTick = null;
	
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
		if(tracerMode == null) {
			tracerMode = new ModuleStateMachine("Tracer Mode", true, 3, 0, main.singlePlayerSettings::setMode, main.keybindManager.tracerBind1, main.keybindManager.tracerBind2,"Timed Render", "Permanent Render", "Last 5 Seconds");
		}
		if(xRayTraces == null) {
			xRayTraces = new ModuleToggle("XRay Traces", true, false, main.singlePlayerSettings::setXRayTrace, main.keybindManager.xRayTraces1, main.keybindManager.xRayTraces2);
		}
		if(menu == null) {
			menu = new ModuleToggle("Menu", false, false, main.singlePlayerSettings::setRenderMenu, main.keybindManager.menu1, main.keybindManager.menu2);
		}
		if(renderBoxes == null) {
			renderBoxes = new ModuleToggle("Render Boxes", true, false, main.singlePlayerSettings::setRenderBoxes, main.keybindManager.rendBox1, main.keybindManager.rendBox2);
		}
		if(lastSecond == null) {
			lastSecond = new ModuleOnOff("Last Second", false, false, main.singlePlayerSettings::lastSeconds, main.keybindManager.lastSecond1, main.keybindManager.lastSecond2);
		}
		if(displayTick == null) {
			displayTick = new ModuleCounter("Display Tick", true, 0, 100, 1, main.singlePlayerSettings.renderTickGNS, main.keybindManager.prevTick, main.keybindManager.nextTick);
		}
		activeModules.add(tracerMode);
		activeModules.add(xRayTraces);
		activeModules.add(menu);
		activeModules.add(renderBoxes);
		activeModules.add(lastSecond);
		activeModules.add(displayTick);
	}
	
	public void registerMultiplayerModules() {
		//no modules yet
	}
	
	public void keyPressed(int key) {
		for(ModuleBase module : activeModules) {
			module.keyPressed(key);
		}
	}
	
	public void keyReleased(int key) {
		for(ModuleBase module : activeModules) {
			module.keyReleased(key);
		}
	}
	
	/*public static void checkKeypresses() {
		for(ModuleBase module : activeModules) {
			if(module.toggle) {
				if(testKeybind(module)) {
					module.toggle();
				}else {
					module.buttonReleased();
				}
			}else if(OldModuleCounter.class.isInstance(module)) {
				if(!KeyPressListener.pressedKeys.contains(module.keybind[0]) && !KeyPressListener.pressedKeys.contains(module.keybind[1])) {
					module.buttonReleased();
				}else if(KeyPressListener.pressedKeys.contains(module.keybind[0]) && !KeyPressListener.pressedKeys.contains(module.keybind[1])) {
					module.activate();
				}else if(KeyPressListener.pressedKeys.contains(module.keybind[1]) && !KeyPressListener.pressedKeys.contains(module.keybind[1])) {
					module.deactivate();
				}
			}else if(KeyPressListener.pressedKeys.contains(module.keybind[0])) {
				if(KeyPressListener.pressedKeys.contains(module.keybind[1])) {
					module.deactivate();
				}else {
					module.activate();
				}
			}else {
				module.buttonReleased();
			}
		}
	}*/
	
	/*public static void onKeyPressed(int Keycode) {
		if(!ModuleManager.renderMenu)
			return;
		for(BaseEmbeddedBox beb : BaseMenu.textboxes) {
			if(beb.getClass()==InteractableTextbox.class) {
				InteractableTextbox itb=(InteractableTextbox)beb;
				if(itb.isFocused) {
					itb.onKeyPressed(Keycode);
				}
			}else if(beb.getClass()==KeybindBox.class) {
				KeybindBox kbb=(KeybindBox)beb;
				if(kbb.isFocused){
					kbb.onKeyPressed(Keycode);
				}
			}else if(beb.getClass()==TextboxAnyFunc.class) {
				TextboxAnyFunc tbaf=(TextboxAnyFunc)beb;
				if(tbaf.isFocused) {
					tbaf.onKeyPressed(Keycode);
				}
			}
		}
	}*/
}