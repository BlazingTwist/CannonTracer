package the_dark_jumper.cannontracer.modules.moduleelements;

import the_dark_jumper.cannontracer.configsaving.KeybindEntry;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.IModuleBehaviour;

public class ModuleBasic implements IModule {
	private final String name;
	private final boolean render;
	private final boolean isGlobal;
	private KeybindEntry keybind;
	private IModuleBehaviour behaviour;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isRender() {
		return render;
	}

	@Override
	public boolean isGlobal() {
		return isGlobal;
	}

	public KeybindEntry getKeybind() {
		return keybind;
	}

	@Override
	public IModuleBehaviour getBehaviour() {
		return behaviour;
	}

	private boolean ignoreInput = false;

	public ModuleBasic(String name, boolean render, boolean isGlobal) {
		this.name = name;
		this.render = render;
		this.isGlobal = isGlobal;
	}

	public ModuleBasic setKeybind(KeybindEntry keybind) {
		this.keybind = keybind;
		return this;
	}

	public ModuleBasic addKeybind(boolean triggerState, int keycode) {
		if(keybind == null){
			keybind = new KeybindEntry();
		}

		if (triggerState) {
			keybind.addTrigger(keycode);
		} else {
			keybind.addExclude(keycode);
		}
		return this;
	}

	public ModuleBasic setBehaviour(IModuleBehaviour behaviour) {
		this.behaviour = behaviour;
		return this;
	}

	@Override
	public void onKeyEvent(int keycode) {
		if(keybind == null || keybind.getTrigger().size() == 0){
			return;
		}

		if (keybind.isKeyRelevant(keycode)) {
			checkState();
		}
	}

	private void checkState() {
		if (keybind.isSatisfied()) {
			if (!ignoreInput) {
				ignoreInput = true;
				if (behaviour != null) {
					behaviour.onTriggerChanged(true);
				}
			}
		} else {
			if (ignoreInput) {
				ignoreInput = false;
				if (behaviour != null) {
					behaviour.onTriggerChanged(false);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "ModuleBasic{" +
				"name='" + name + '\'' +
				", render=" + render +
				", isGlobal=" + isGlobal +
				", keybind=" + keybind +
				'}';
	}
}