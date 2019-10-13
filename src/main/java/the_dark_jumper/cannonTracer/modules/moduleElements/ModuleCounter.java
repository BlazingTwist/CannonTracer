package the_dark_jumper.cannonTracer.modules.moduleElements;

import java.util.Timer;
import java.util.TimerTask;

import the_dark_jumper.cannonTracer.util.GetterAndSetter;

public class ModuleCounter extends ModuleBase{
	Timer timer;
	public int min;
	public int max;
	public int step;
	public boolean decrementLoopIsRunning = false;
	public boolean incrementLoopIsRunning = false;
	public GetterAndSetter<Integer> valueGNS;
	
	public ModuleCounter(String name, boolean render, int min, int max, int step, GetterAndSetter<Integer> valueGNS, int keybind1, int keybind2) {
		super(name, render, keybind1, keybind2);
		init(min, max, step, valueGNS);
	}
	
	public void init(int min, int max, int step, GetterAndSetter<Integer> valueGNS) {
		timer = new Timer();
		this.min = min;
		this.max = max;
		this.step = step;
		this.valueGNS = valueGNS;
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
		int value = valueGNS.getter.get();
		if(value == max) {
			return;
		}
		value += step;
		if(value > max) {
			value = max;
		}
		valueGNS.setter.accept(value);
	}
	
	public void decrement() {
		int value = valueGNS.getter.get();
		if(value == min) {
			return;
		}
		value -= step;
		if(value < min) {
			value = min;
		}
		valueGNS.setter.accept(value);
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
