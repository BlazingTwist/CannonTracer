package the_dark_jumper.cannonTracer.gui.guiElements;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGUI;
import the_dark_jumper.cannonTracer.gui.JumperGUI.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.FocusableFrame;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;

public class KeybindFrame extends DoubleSegmentFrame implements FocusableFrame{
	public final GetterAndSetter<Integer> keybind;
	
	public boolean isFocused = false;
	@Override public boolean getFocused() {return isFocused;}
	@Override public void setFocused(boolean isFocused) {this.isFocused = isFocused;}
	
	public KeybindFrame(JumperGUI parent, FrameConfig config, FrameColors colors, String keybindName, GetterAndSetter<Integer> keybind) {
		super(parent, keybindName, Main.getInstance().keyLibrary.getKeyContent(keybind.getter.get().intValue()).keyName, config, colors);
		this.keybind = keybind;
	}
	
	@Override
	public void parseInput() {
		if(this.value.equals("")) {
			this.value = Main.getInstance().keyLibrary.getKeyContent(keybind.getter.get().intValue()).keyName;
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
			keybind.setter.accept(event.getScanCode());
			onFocusChange(false);
		}
	}
}
