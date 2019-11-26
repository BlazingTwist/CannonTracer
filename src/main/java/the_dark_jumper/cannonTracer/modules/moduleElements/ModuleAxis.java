package the_dark_jumper.cannontracer.modules.moduleelements;

import java.util.LinkedList;

import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.IModuleAxisBehaviour;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.IModuleBehaviour;
import the_dark_jumper.cannontracer.util.KeybindData;

public class ModuleAxis implements IModule{
	public String name;
	@Override public String getName() {return name;}
	
	public LinkedList<KeybindData> positiveKeybinds = new LinkedList<>();
	public LinkedList<KeybindData> negativeKeybinds = new LinkedList<>();
	
	public IModuleAxisBehaviour behaviour = null;
	@Override public IModuleBehaviour getBehaviour() {return behaviour;}
	
	private boolean render;
	@Override public boolean getRender() {return render;}
	private boolean isGlobal;
	@Override public boolean getIsGlobal() {return isGlobal;}
	
	private boolean ignorePositiveInput = false;
	private boolean ignoreNegativeInput = false;
	
	public ModuleAxis(String name, boolean render, boolean isGlobal) {
		this.name = name;
		this.render = render;
		this.isGlobal = isGlobal;
	}
	
	public ModuleAxis setPositiveKeybinds(LinkedList<KeybindData> keybinds) {
		this.positiveKeybinds = keybinds;
		return this;
	}
	
	public ModuleAxis setNegativeKeybinds(LinkedList<KeybindData> keybinds) {
		this.negativeKeybinds = keybinds;
		return this;
	}
	
	public ModuleAxis addPositiveKeybind(boolean triggerState, int keycode) {
		positiveKeybinds.add(new KeybindData(triggerState, keycode));
		return this;
	}
	
	public ModuleAxis addNegativeKeybind(boolean triggerState, int keycode) {
		negativeKeybinds.add(new KeybindData(triggerState, keycode));
		return this;
	}
	
	public ModuleAxis setBehaviour(IModuleAxisBehaviour behaviour) {
		this.behaviour = behaviour;
		return this;
	}
	
	private boolean isKeyRelevant(int keycode) {
		for(KeybindData keybindData : positiveKeybinds) {
			if(keybindData.keycodeGNS.get() == keycode) {
				return true;
			}
		}
		for(KeybindData keybindData : negativeKeybinds) {
			if(keybindData.keycodeGNS.get() == keycode) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onKeyEvent(int keycode) {
		if(!isKeyRelevant(keycode)) {
			return;
		}
		checkState();
	}
	
	private void checkState() {
		if(allPositiveKeysSatisfied()) {
			if(!ignorePositiveInput) {
				ignorePositiveInput = true;
				if(behaviour != null) {
					behaviour.onTriggerChanged(true);
				}
			}
		}else {
			if(ignorePositiveInput) {
				ignorePositiveInput = false;
				if(behaviour != null) {
					behaviour.onTriggerChanged(false);
				}
			}
		}
		
		if(allNegativeKeysSatisfied()) {
			if(!ignoreNegativeInput) {
				ignoreNegativeInput = true;
				if(behaviour != null) {
					behaviour.onOtherTriggerChanged(true);
				}
			}
		}else {
			if(ignoreNegativeInput) {
				ignoreNegativeInput = false;
				if(behaviour != null) {
					behaviour.onOtherTriggerChanged(false);
				}
			}
		}
	}
	
	private boolean isConditionSatisfied(int keycode, boolean triggerState) {
		return Main.getInstance().keyPressListener.pressedKeys.contains(keycode) == triggerState;
	}
	
	private boolean allPositiveKeysSatisfied() {
		for(KeybindData keybind : positiveKeybinds) {
			if(!isConditionSatisfied(keybind.keycodeGNS.get(), keybind.triggerGNS.get())) {
				return false;
			}
		}
		return true;
	}
	
	private boolean allNegativeKeysSatisfied() {
		for(KeybindData keybind : negativeKeybinds) {
			if(!isConditionSatisfied(keybind.keycodeGNS.get(), keybind.triggerGNS.get())) {
				return false;
			}
		}
		return true;
	}
}
