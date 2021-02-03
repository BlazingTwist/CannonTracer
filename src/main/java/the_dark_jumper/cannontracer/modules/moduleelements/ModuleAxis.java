package the_dark_jumper.cannontracer.modules.moduleelements;

import the_dark_jumper.cannontracer.configsaving.KeybindEntry;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.IModuleAxisBehaviour;

public class ModuleAxis implements IModule {
	private final String name;
	private final boolean render;
	private final boolean isGlobal;
	private KeybindEntry positiveKeybind;
	private KeybindEntry negativeKeybind;
	private IModuleAxisBehaviour behaviour;

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

	public KeybindEntry getPositiveKeybind() {
		return positiveKeybind;
	}

	public KeybindEntry getNegativeKeybind() {
		return negativeKeybind;
	}

	@Override
	public IModuleAxisBehaviour getBehaviour() {
		return behaviour;
	}

	private boolean ignorePositiveInput = false;
	private boolean ignoreNegativeInput = false;

	public ModuleAxis(String name, boolean render, boolean isGlobal) {
		this.name = name;
		this.render = render;
		this.isGlobal = isGlobal;
	}

	public ModuleAxis setPositiveKeybind(KeybindEntry positiveKeybind) {
		this.positiveKeybind = positiveKeybind;
		return this;
	}

	public ModuleAxis setNegativeKeybind(KeybindEntry negativeKeybind) {
		this.negativeKeybind = negativeKeybind;
		return this;
	}

	public ModuleAxis addPositiveKeybind(boolean triggerState, int keycode) {
		if (triggerState) {
			positiveKeybind.addTrigger(keycode);
		} else {
			positiveKeybind.addExclude(keycode);
		}
		return this;
	}

	public ModuleAxis addNegativeKeybind(boolean triggerState, int keycode) {
		if (triggerState) {
			negativeKeybind.addTrigger(keycode);
		} else {
			negativeKeybind.addExclude(keycode);
		}
		return this;
	}

	public ModuleAxis setBehaviour(IModuleAxisBehaviour behaviour) {
		this.behaviour = behaviour;
		return this;
	}

	@Override
	public void onKeyEvent(int keycode) {
		if (!ready()) {
			return;
		}

		if (positiveKeybind.isKeyRelevant(keycode) || negativeKeybind.isKeyRelevant(keycode)) {
			checkState();
		}
	}

	private void checkState() {
		if (positiveKeybind.isSatisfied()) {
			if (!ignorePositiveInput) {
				ignorePositiveInput = true;
				if (behaviour != null) {
					behaviour.onTriggerChanged(true);
				}
			}
		} else {
			if (ignorePositiveInput) {
				ignorePositiveInput = false;
				if (behaviour != null) {
					behaviour.onTriggerChanged(false);
				}
			}
		}

		if (negativeKeybind.isSatisfied()) {
			if (!ignoreNegativeInput) {
				ignoreNegativeInput = true;
				if (behaviour != null) {
					behaviour.onOtherTriggerChanged(true);
				}
			}
		} else {
			if (ignoreNegativeInput) {
				ignoreNegativeInput = false;
				if (behaviour != null) {
					behaviour.onOtherTriggerChanged(false);
				}
			}
		}
	}

	private boolean ready() {
		return positiveKeybind != null && positiveKeybind.getTrigger().size() != 0
				&& negativeKeybind != null && negativeKeybind.getTrigger().size() != 0;
	}

	@Override
	public String toString() {
		return "ModuleAxis{" +
				"name='" + name + '\'' +
				", render=" + render +
				", isGlobal=" + isGlobal +
				", positiveKeybind=" + positiveKeybind +
				", negativeKeybind=" + negativeKeybind +
				'}';
	}
}
