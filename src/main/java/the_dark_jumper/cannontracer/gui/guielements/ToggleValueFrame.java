package the_dark_jumper.cannontracer.gui.guielements;

import jumpercommons.GetterAndSetter;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class ToggleValueFrame extends DoubleSegmentFrame{
	public final GetterAndSetter<Boolean> source;
	
	public ToggleValueFrame(IJumperGUI parent, FrameConfig config, FrameColors colors, String text, GetterAndSetter<Boolean> source) {
		super(parent, text, Boolean.toString(source.get()), (source.get() ? colors.colorOn : colors.colorOff), config, colors);
		this.source = source;
	}
	
	@Override
	public void setIsClicked(boolean isClicked) {
		if(isClicked) {
			boolean result = !source.get();
			source.set(result);
			super.valueColor = (result ? colors.colorOn : colors.colorOff);
			super.value = Boolean.toString(result);
		}
	}
}
