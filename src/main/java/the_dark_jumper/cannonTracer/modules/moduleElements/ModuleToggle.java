package the_dark_jumper.cannontracer.modules.moduleelements;

import java.util.function.Consumer;

public class ModuleToggle extends ModuleBase{
	public boolean key1Pressed = false, key2Pressed = false;
	public boolean state;
	public Consumer<Boolean> valueChanged;
	
	public ModuleToggle(String name, boolean render, boolean isGlobal, int keybind1, int keybind2) {
		super(name, render, isGlobal, keybind1, keybind2);
		initialize(false, null);
	}
	
	public ModuleToggle(String name, boolean render, boolean isGlobal, boolean state, Consumer<Boolean> valueChanged, int keybind1, int keybind2) {
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
	
	public void checkAllPressed() {
		if(key1Pressed && key2Pressed) {
			toggle();
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
			checkAllPressed();
		}
	}
	
	public void toggle() {
		state = !state;
		System.out.println(name + " toggles to state " + state);
		if(valueChanged != null) {
			valueChanged.accept(state);
		}
	}
}
