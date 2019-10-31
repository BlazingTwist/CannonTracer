package the_dark_jumper.cannontracer.modules.moduleelements;

import java.util.function.Consumer;

public class ModuleStateMachine extends ModuleBase{
	public int states;
	public int currentState;
	public String stateNames[];
	public boolean key1Pressed = false, key2Pressed = false;
	public Consumer<Integer> valueChanged;
	
	public ModuleStateMachine(String name, boolean render, boolean isGlobal, int states, int currentState, int keybind1, int keybind2, String... stateNames) {
		super(name, render, isGlobal, keybind1, keybind2);
		initialize(null, states, currentState, stateNames);
	}
	
	public ModuleStateMachine(String name, boolean render, boolean isGlobal, int states, int currentState, Consumer<Integer> valueChanged, int keybind1, int keybind2, String... stateNames) {
		super(name, render, isGlobal, keybind1, keybind2);
		initialize(valueChanged, states, currentState, stateNames);
	}
	
	public void initialize(Consumer<Integer> valueChanged, int states, int currentState, String[] stateNames) {
		this.valueChanged = valueChanged;
		this.states = states;
		this.currentState = currentState;
		this.stateNames = stateNames;
	}
	
	public void keyPressed(int key) {
		setKeyPressed(key, true);
	}
	
	public void keyReleased(int key) {
		setKeyPressed(key, false);
	}
	
	public void checkAllPressed() {
		if(key1Pressed && key2Pressed) {
			nextState();
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
	
	public void nextState() {
		currentState = (currentState + 1) % states;
		System.out.println(name + " toggles to next state");
		if(valueChanged != null) {
			valueChanged.accept(currentState);
		}
	}
}
