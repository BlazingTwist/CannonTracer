package the_dark_jumper.cannonTracer.modules.moduleElements;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ModuleCounter extends ModuleBase{
	Timer timer;
	public int value;
	public int min;
	public int max;
	public int step;
	public boolean decrementLoopIsRunning = false;
	public boolean incrementLoopIsRunning = false;
	Consumer<Integer> valueChanged;
	
	public ModuleCounter(String name, boolean render, int value, int min, int max, int step, int keybind1, int keybind2) {
		super(name, render, keybind1, keybind2);
		init(value, min, max, step, null);
	}
	
	public ModuleCounter(String name, boolean render, int value, int min, int max, int step, Consumer<Integer> valueChanged, int keybind1, int keybind2) {
		super(name, render, keybind1, keybind2);
		init(value, min, max, step, valueChanged);
	}
	
	public void init(int value, int min, int max, int step, Consumer<Integer> valueChanged) {
		timer = new Timer();
		this.value = value;
		this.min = min;
		this.max = max;
		this.step = step;
		this.valueChanged = valueChanged;
	}
	
	public void keyPressed(int key) {
		if(key == keybind2) {
			decrementLoopIsRunning = false;
			if(incrementLoopIsRunning) {
				return;
			}
			increment();
			System.out.println(name + " has been incremented!");
			incrementLoopIsRunning = true;
			startTimer(new Increment(), 300);
		}else if(key == keybind1) {
			incrementLoopIsRunning = false;
			if(decrementLoopIsRunning) {
				return;
			}
			decrement();
			System.out.println(name + " has been decremented!");
			decrementLoopIsRunning = true;
			startTimer(new Decrement(), 300);
		}
	}
	
	public void keyReleased(int key) {
		if(key == keybind2) {
			incrementLoopIsRunning = false;
		}else if(key == keybind1) {
			decrementLoopIsRunning = false;
		}
	}
	
	public void increment() {
		if(value == max) {
			return;
		}
		value += step;
		if(value > max) {
			value = max;
		}
		if(valueChanged != null) {
			valueChanged.accept(value);
		}
	}
	
	public void decrement() {
		if(value == min) {
			return;
		}
		value -= step;
		if(value < min) {
			value = min;
		}
		if(valueChanged != null) {
			valueChanged.accept(value);
		}
	}
	
	public void onIncrementLoop() {
		if(!incrementLoopIsRunning) {
			return;
		}
		increment();
		startTimer(new Increment(), 50);
	}
	
	public void onDecrementLoop() {
		if(!decrementLoopIsRunning) {
			return;
		}
		decrement();
		startTimer(new Decrement(), 50);
	}
	
	public void startTimer(ValueChanger vc, int delay) {
		if(timer != null) {
			timer.cancel();
			timer.purge();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				vc.changeValue();
			}
		}, delay);
	}
	
	public interface ValueChanger{
		public void changeValue();
	}
	
	public class Increment implements ValueChanger{
		public void changeValue() {
			onIncrementLoop();
		}
	}
	
	public class Decrement implements ValueChanger{
		public void changeValue() {
			onDecrementLoop();
		}
	}
}
