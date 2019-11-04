package the_dark_jumper.cannontracer.gui.guielements;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class KeybindFrame extends DoubleSegmentFrame implements IFocusableFrame{
	public final GetterAndSetter<Integer> keybind;
	
	public boolean isFocused = false;
	@Override public boolean getFocused() {return isFocused;}
	@Override public void setFocused(boolean isFocused) {this.isFocused = isFocused;}
	
	public KeybindFrame(IJumperGUI parent, FrameConfig config, FrameColors colors, String keybindName, GetterAndSetter<Integer> keybind) {
		super(parent, keybindName, Main.getInstance().keyLibrary.getKeyContent(keybind.get().intValue()).keyName, config, colors);
		this.keybind = keybind;
	}
	
	@Override
	public void parseInput() {
		if(this.value.equals("")) {
			this.value = Main.getInstance().keyLibrary.getKeyContent(keybind.get().intValue()).keyName;
		}
	}
	
	@Override
	public void keyEvent(InputEvent.KeyInputEvent event) {
		if(event.getAction() == GLFW.GLFW_PRESS) {
			if(event.getScanCode() == 1) {
				//escape
				onFocusChange(false);
				return;
			}
			this.value = Main.getInstance().keyLibrary.getKeyContent(event.getScanCode()).keyName;
			keybind.set(event.getScanCode());
			onFocusChange(false);
		}
	}
}
