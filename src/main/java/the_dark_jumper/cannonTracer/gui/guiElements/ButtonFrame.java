package the_dark_jumper.cannonTracer.gui.guiElements;

import java.util.function.Consumer;

import the_dark_jumper.cannonTracer.gui.JumperGUI;
import the_dark_jumper.cannonTracer.gui.JumperGUI.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.ClickableFrame;

public class ButtonFrame extends BasicTextFrame implements ClickableFrame{
	public final Consumer<Boolean> onPressed;
	
	public boolean isClicked = false;
	@Override public boolean getIsClicked() {return isClicked;}
	@Override public void setIsClicked(boolean isClicked) {
		this.isClicked = isClicked;
		onPressed.accept(isClicked);
	}
	
	public boolean hovered = false;
	@Override public boolean getHovered() {return hovered;}
	@Override public void setHovered(boolean hovered) {this.hovered = hovered;}
	
	public ButtonFrame(JumperGUI parent, String text, FrameConfig config, FrameColors colors, Consumer<Boolean> onPressed) {
		super(parent, text, config, colors);
		this.onPressed = onPressed;
	}
}
