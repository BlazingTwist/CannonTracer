package the_dark_jumper.cannonTracer.gui.guiElements;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGui;
import the_dark_jumper.cannonTracer.gui.JumperGui.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.FocusableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.TickableFrame;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;

public class KeybindFrame extends DoubleSegmentFrame implements TickableFrame, FocusableFrame{
	public final GetterAndSetter<Integer> keybind;
	public boolean isFocused = false;
	
	
	public KeybindFrame(Main main, JumperGui parent, FrameConfig config, FrameColors colors, String keybindName, GetterAndSetter<Integer> keybind) {
		super(main, parent, keybindName, main.keyLibrary.getKeyContent(keybind.getter.get().intValue()).keyName, config, colors);
		this.keybind = keybind;
	}

	@Override
	public void tick(JumperGui gui) {
		if(isFocused && !this.ignoreInput && gui.getLeftDown() && !this.hovered
				&& (!main.keyPressListener.pressedKeys.contains(42) && !main.keyPressListener.pressedKeys.contains(54))) {
			focusLost();
		}
	}

	@Override
	public void onClicked() {
		ignoreInput = true;
		focused();
	}
	
	public void focused() {
		isFocused = true;
		this.value = "";
	}
	
	public void focusLost() {
		isFocused = false;
		if(this.value.equals("")) {
			this.value = main.keyLibrary.getKeyContent(keybind.getter.get().intValue()).keyName;
		}
	}
	
	public void keyEvent(InputEvent.KeyInputEvent event) {
		if(event.getAction() == GLFW.GLFW_PRESS) {
			if(event.getScanCode() == 1) {
				//escape
				focusLost();
				return;
			}
			this.value = main.keyLibrary.getKeyContent(event.getScanCode()).keyName;
			keybind.setter.accept(event.getScanCode());
			focusLost();
		}
	}
	
	public boolean getFocused() {
		return isFocused;
	}
}
