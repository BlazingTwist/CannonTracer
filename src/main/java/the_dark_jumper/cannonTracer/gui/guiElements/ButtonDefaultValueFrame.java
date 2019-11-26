package the_dark_jumper.cannontracer.gui.guielements;

import java.util.function.Consumer;

import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class ButtonDefaultValueFrame <T> extends BasicTextFrame implements IClickableFrame{
	public T value;
	public final Consumer<T> onPressed;
	
	private boolean isClicked = false;
	@Override public boolean getIsClicked() {return isClicked;}
	@Override public void setIsClicked(boolean isClicked) {
		this.isClicked = isClicked;
		if(onPressed != null) {
			onPressed.accept(value);
		}
	}
	
	public boolean hovered = false;
	@Override public boolean getHovered() {return hovered;}
	@Override public void setHovered(boolean hovered) {this.hovered = hovered;}
	
	public ButtonDefaultValueFrame(IJumperGUI parent, String text, FrameConfig config, FrameColors colors, T value, Consumer<T> onPressed) {
		super(parent, text, config, colors);
		this.value = value;
		this.onPressed = onPressed;
	}
}
