package the_dark_jumper.cannontracer.modules.moduleelements;

import java.util.function.Consumer;

public class ModuleOnOff extends ModuleBase{
	public boolean key1Pressed = false, key2Pressed = false;
	public boolean state;
	public Consumer<Boolean> valueChanged;
	
	public ModuleOnOff(String name, boolean render, boolean isGlobal, int keybind1, int keybind2) {
		super(name, render, isGlobal, keybind1, keybind2);
		initialize(false, null);
	}
	
	public ModuleOnOff(String name, boolean render, boolean isGlobal, boolean state, Consumer<Boolean> valueChanged, int keybind1, int keybind2) {
		super(name, render, isGlobal, keybind1, keybind2);
		initialize(state, valueChanged);
	}

	public void initialize(boolean state, Consumer<Boolean> valueChanged) {
		this.state = state;
		this.valueChanged = valueChanged;
	}
	
	public void keyPressed(int key) {
		setKeyPressed(key, true);
	}
	
	public void keyReleased(int key) {
		setKeyPressed(key, false);
	}
	
	public void checkPresses() {
		if(key1Pressed) {
			if(key2Pressed) {
				state = false;
			}else {
				state = true;
			}
			System.out.println(name + " switches to state " + state);
			if(valueChanged != null) {
				valueChanged.accept(state);
			}
		}
	}
	
	public void setKeyPressed(int key, boolean isPressed) {
		if(key == keybind1) {
			key1Pressed = isPressed;
		}else if(key == keybind2) {
			key2Pressed = isPressed;
		}else {
			return;
		}
		if(isPressed) {
			checkPresses();
		}
	}
}
