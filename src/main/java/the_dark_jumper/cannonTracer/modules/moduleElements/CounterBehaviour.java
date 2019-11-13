package the_dark_jumper.cannontracer.modules.moduleelements;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class CounterBehaviour implements IModuleAxisBehaviour{
	private ModuleAxis parent;
	@Override public IModule getParent() {return parent;}
	
	private Timer timer = null;
	public boolean incrementLoopIsRunning = false;
	public boolean decrementLoopIsRunning = false;
	
	public Supplier<Integer> max = null;
	public int maxNum;
	public Supplier<Integer> min = null;
	public int minNum;
	
	public int step;
	public GetterAndSetter<Integer> valueGNS;
	
	public CounterBehaviour(ModuleAxis parent, int step, GetterAndSetter<Integer> valueGNS) {
		this.parent = parent;
		this.step = step;
		this.valueGNS = valueGNS;
	}
	
	public CounterBehaviour setMax(int max) {
		this.maxNum = max;
		return this;
	}
	public CounterBehaviour setMax(Supplier<Integer> max) {
		this.max = max;
		return this;
	}
	
	public CounterBehaviour setMin(int min) {
		this.minNum = min;
		return this;
	}
	public CounterBehaviour setMin(Supplier<Integer> min) {
		this.min = min;
		return this;
	}
	
	private int getMax() {
		if(max != null) {
			return max.get();
		}
		return maxNum;
	}
	
	private int getMin() {
		if(min != null) {
			return min.get();
		}
		return minNum;
	}
	
	@Override
	public void renderText(Screen screen, FontRenderer fontRenderer, int x, int y){
		screen.drawString(fontRenderer, parent.name + " : " + valueGNS.get(), x, y, 0x7fffffff);
	}
	
	@Override
	public void onTriggerChanged(boolean isTriggered) {
		if(isTriggered) {
			decrementLoopIsRunning = false;
			if(incrementLoopIsRunning) {
				return;
			}
			increment();
			incrementLoopIsRunning = true;
			startTimer(new Increment(), 300);
		}else {
			incrementLoopIsRunning = false;
		}
	}
	
	@Override
	public void onOtherTriggerChanged(boolean isTriggered) {
		if(isTriggered) {
			incrementLoopIsRunning = false;
			if(decrementLoopIsRunning) {
				return;
			}
			decrement();
			decrementLoopIsRunning = true;
			startTimer(new Decrement(), 300);
		}else {
			decrementLoopIsRunning = false;
		}
	}
	
	private void increment() {
		int value = valueGNS.get();
		int max = getMax();
		if(value == max){
			return;
		}
		value += step;
		if(value > max) {
			value = max;
		}
		valueGNS.set(value);
	}
	
	private void decrement() {
		int value = valueGNS.get();
		int min = getMin();
		if(value == min){
			return;
		}
		value -= step;
		if(value < min) {
			value = min;
		}
		valueGNS.set(value);
	}
	
	private void onIncrementLoop(){
		if(!incrementLoopIsRunning) {
			return;
		}
		increment();
		startTimer(new Increment(), 50);
	}
	
	private void onDecrementLoop(){
		if(!decrementLoopIsRunning) {
			return;
		}
		decrement();
		startTimer(new Decrement(), 50);
	}
	
	private void startTimer(ValueChanger vc, int delay) {
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
	
	private interface ValueChanger{
		public void changeValue();
	}
	
	private class Increment implements ValueChanger{
		public void changeValue() {
			onIncrementLoop();
		}
	}
	
	private class Decrement implements ValueChanger{
		public void changeValue() {
			onDecrementLoop();
		}
	}
}
