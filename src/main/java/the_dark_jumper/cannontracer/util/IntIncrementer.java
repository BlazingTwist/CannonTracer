package the_dark_jumper.cannontracer.util;

import javax.annotation.Nullable;
import jumpercommons.GetterAndSetter;

public class IntIncrementer extends LoopingIncrementer {
	private final Runnable onValueChanged;
	private final GetterAndSetter<Integer> getterAndSetter;
	private final Integer offset;
	private final Integer minValue;
	private final Integer maxValue;

	public IntIncrementer(Runnable onValueChanged, GetterAndSetter<Integer> getterAndSetter, Integer offset, @Nullable Integer minValue, @Nullable Integer maxValue) {
		this.onValueChanged = onValueChanged;
		this.getterAndSetter = getterAndSetter;
		this.offset = offset;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	protected void doIncrement() {
		setValue(getterAndSetter.get() + offset);
	}

	@Override
	protected void doDecrement() {
		setValue(getterAndSetter.get() - offset);
	}

	private void setValue(int value) {
		if (maxValue != null && value > maxValue) {
			value = maxValue;
		}
		if (minValue != null && value < minValue) {
			value = minValue;
		}
		getterAndSetter.set(value);
		onValueChanged.run();
	}
}
