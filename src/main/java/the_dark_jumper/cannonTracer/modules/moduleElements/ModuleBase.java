package the_dark_jumper.cannontracer.modules.moduleelements;

import java.util.LinkedList;

import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.util.KeybindData;

public class ModuleBase implements IModule{
	public String name;
	@Override public String getName() {return name;}
	
	public LinkedList<KeybindData> keybinds = new LinkedList<>();
	
	public IModuleBehaviour behaviour = null;
	@Override public IModuleBehaviour getBehaviour() {return behaviour;}
	
	private boolean render;
	@Override public boolean getRender() {return render;}
	private boolean isGlobal;
	@Override public boolean getIsGlobal() {return isGlobal;}
	
	private boolean ignoreInput = false;
	
	public ModuleBase(String name, boolean render, boolean isGlobal) {
		this.name = name;
		this.render = render;
		this.isGlobal = isGlobal;
	}
	
	public ModuleBase setKeybinds(LinkedList<KeybindData> keybinds) {
		this.keybinds = keybinds;
		return this;
	}
	
	public ModuleBase addKeybind(boolean triggerState, int keycode) {
		keybinds.add(new KeybindData(triggerState, keycode));
		return this;
	}
	
	public ModuleBase setBehaviour(IModuleBehaviour behaviour) {
		this.behaviour = behaviour;
		return this;
	}
	
	private boolean isKeyRelevant(int keycode) {
		for(KeybindData keybind : keybinds) {
			if(keybind.keycodeGNS.get() == keycode) {
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
		if(allKeysSatisfied()) {
			if(!ignoreInput) {
				ignoreInput = true;
				if(behaviour != null) {
					behaviour.onTriggerChanged(true);
				}
			}
		}else {
			if(ignoreInput) {
				ignoreInput = false;
				if(behaviour != null) {
					behaviour.onTriggerChanged(false);
				}
			}
		}
	}
	
	private boolean isConditionSatisfied(int keycode, boolean triggerState) {
		return Main.getInstance().keyPressListener.pressedKeys.contains(keycode) == triggerState;
	}
	
	private boolean allKeysSatisfied() {
		for(KeybindData keybind : keybinds) {
			if(!isConditionSatisfied(keybind.keycodeGNS.get(), keybind.triggerGNS.get())) {
				return false;
			}
		}
		return true;
	}
	
	/*public void updateKeybind(int index, int keybind) {
		if(index == 0) {
			keybind1 = keybind;
		}else if(index == 1) {
			keybind2 = keybind;
		}
	}
	
	public void keyPressed(int key) {}
	
	public void keyReleased(int key) {}*/
}