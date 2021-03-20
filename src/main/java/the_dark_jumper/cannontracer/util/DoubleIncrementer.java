package the_dark_jumper.cannontracer.util;

import javax.annotation.Nullable;
import jumpercommons.GetterAndSetter;

public class DoubleIncrementer extends LoopingIncrementer {
	private final Runnable onValueChanged;
	private final GetterAndSetter<Double> getterAndSetter;
	private final Double offset;
	private final Double minValue;
	private final Double maxValue;

	public DoubleIncrementer(Runnable onValueChanged, GetterAndSetter<Double> getterAndSetter, Double offset, @Nullable Double minValue, @Nullable Double maxValue) {
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

	private void setValue(double value) {
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
