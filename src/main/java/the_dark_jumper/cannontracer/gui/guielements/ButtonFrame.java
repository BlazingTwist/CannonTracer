package the_dark_jumper.cannontracer.gui.guielements;

import java.util.function.Consumer;

import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class ButtonFrame extends BasicTextFrame implements IClickableFrame{
	public final Consumer<Boolean> onPressed;
	
	private boolean isClicked = false;
	@Override public boolean getIsClicked() {return isClicked;}
	@Override public void setIsClicked(boolean isClicked) {
		this.isClicked = isClicked;
		if(onPressed != null) {
			onPressed.accept(isClicked);
		}
	}
	
	public boolean hovered = false;
	@Override public boolean getHovered() {return hovered;}
	@Override public void setHovered(boolean hovered) {this.hovered = hovered;}
	
	public ButtonFrame(IJumperGUI parent, String text, FrameConfig config, FrameColors colors, Consumer<Boolean> onPressed) {
		super(parent, text, config, colors);
		this.onPressed = onPressed;
	}
}
