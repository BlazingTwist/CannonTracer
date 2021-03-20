package the_dark_jumper.cannontracer.modules.moduleelements.behaviours;

import java.util.function.Supplier;
import jumpercommons.GetterAndSetter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleAxis;
import the_dark_jumper.cannontracer.util.LoopingIncrementer;

public class CounterBehaviour extends LoopingIncrementer implements IModuleAxisBehaviour {
	private ModuleAxis parent;

	@Override
	public IModule getParent() {
		return parent;
	}

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
		if (max != null) {
			return max.get();
		}
		return maxNum;
	}

	private int getMin() {
		if (min != null) {
			return min.get();
		}
		return minNum;
	}

	@Override
	public void renderText(Screen screen, FontRenderer fontRenderer, int x, int y) {
		screen.drawString(fontRenderer, parent.getName() + " : " + valueGNS.get(), x, y, 0x7fffffff);
	}

	@Override
	public void onTriggerChanged(boolean isTriggered) {
		onIncrement(isTriggered);
	}

	@Override
	public void onOtherTriggerChanged(boolean isTriggered) {
		onDecrement(isTriggered);
	}

	@Override
	protected void doIncrement() {
		int value = valueGNS.get();
		int max = getMax();
		if (value == max) {
			return;
		}
		value += step;
		if (value > max) {
			value = max;
		}
		valueGNS.set(value);
	}

	@Override
	protected void doDecrement() {
		int value = valueGNS.get();
		int min = getMin();
		if (value == min) {
			return;
		}
		value -= step;
		if (value < min) {
			value = min;
		}
		valueGNS.set(value);
	}
}
