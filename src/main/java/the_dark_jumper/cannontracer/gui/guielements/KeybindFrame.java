package the_dark_jumper.cannontracer.gui.guielements;

import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;
import the_dark_jumper.cannontracer.util.GetterAndSetter;
import the_dark_jumper.cannontracer.util.KeyLibrary;

public class KeybindFrame extends DoubleSegmentFrame implements IFocusableFrame {
	public final GetterAndSetter<Integer> keybind;

	public boolean isFocused = false;

	@Override
	public boolean getFocused() {
		return isFocused;
	}

	@Override
	public void setFocused(boolean isFocused) {
		Main.getInstance().moduleManager.onFrameFocusChanged(this, isFocused);
		this.isFocused = isFocused;
	}

	@Override
	public boolean getPreserveOnclick() {
		return false;
	}

	@Override
	public void setPreserveOnclick(boolean preserveOnclick) {
	}

	public KeybindFrame(IJumperGUI parent, FrameConfig config, FrameColors colors, String keybindName, GetterAndSetter<Integer> keybind) {
		super(parent, keybindName, KeyLibrary.getKeyContent(keybind.get()).keyName, config, colors);
		this.keybind = keybind;
	}

	@Override
	public void parseInput() {
		if (this.value.equals("")) {
			this.value = KeyLibrary.getKeyContent(keybind.get()).keyName;
		}
	}

	@Override
	public void keyEvent(InputEvent.KeyInputEvent event) {
		if (event.getAction() == GLFW.GLFW_PRESS) {
			if (event.getScanCode() == 1) {
				//escape
				onFocusChange(false);
				return;
			}
			this.value = KeyLibrary.getKeyContent(event.getScanCode()).keyName;
			keybind.set(event.getScanCode());
			onFocusChange(false);
		}
	}
}
