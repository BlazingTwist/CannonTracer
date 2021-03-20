package the_dark_jumper.cannontracer.gui.guielements;

import jumpercommons.GetterAndSetter;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class CompactToggleValueFrame extends BasicTextFrame implements IClickableFrame {
	public final GetterAndSetter<Boolean> source;
	public final String enabledText;
	public final String disabledText;

	public boolean isClicked = false;
	@Override public boolean getIsClicked() {return isClicked;}
	@Override
	public void setIsClicked(boolean isClicked) {
		if(isClicked) {
			boolean result = !source.get();
			source.set(result);
			super.text = result ? enabledText : disabledText;
		}
	}

	public boolean hovered = false;
	@Override public boolean getHovered() {return hovered;}
	@Override public void setHovered(boolean hovered) {this.hovered = hovered;}

	public CompactToggleValueFrame(IJumperGUI parent, FrameConfig config, FrameColors colors, String enabledText, String disabledText, GetterAndSetter<Boolean> source) {
		super(parent, source.get() ? enabledText : disabledText, config, colors);
		this.source = source;
		this.enabledText = enabledText;
		this.disabledText = disabledText;
	}

	@Override
	public void drawTexts(float x1, float y1, float x2, float y2) {
		int height = Math.round((y2 + y1) / 2);
		int width = Math.round((x2 + x1) / 2);
		parent.drawCenteredString(minecraft.fontRenderer, text, width, height, source.get() ? colors.colorOn : colors.colorOff);
	}
}
