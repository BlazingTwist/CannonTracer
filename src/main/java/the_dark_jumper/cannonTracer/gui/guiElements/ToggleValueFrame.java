package the_dark_jumper.cannonTracer.gui.guiElements;

import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGui;
import the_dark_jumper.cannonTracer.gui.JumperGui.FrameConfig;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;

public class ToggleValueFrame extends DoubleSegmentFrame{
	public final GetterAndSetter<Boolean> source;
	
	public ToggleValueFrame(Main main, JumperGui parent, FrameConfig config, FrameColors colors, String text, GetterAndSetter<Boolean> source) {
		super(main, parent, text, Boolean.toString(source.getter.get()), (source.getter.get() ? colors.colorOn : colors.colorOff), config, colors);
		this.source = source;
	}
	
	@Override
	public void onClicked() {
		ignoreInput = true;
		boolean result = !source.getter.get();
		source.setter.accept(result);
		super.valueColor = (result ? colors.colorOn : colors.colorOff);
		super.value = Boolean.toString(result);
	}
}
