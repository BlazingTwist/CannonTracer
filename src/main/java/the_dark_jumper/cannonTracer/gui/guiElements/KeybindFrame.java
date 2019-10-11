package the_dark_jumper.cannonTracer.gui.guiElements;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.ConfigGUI;
import the_dark_jumper.cannonTracer.gui.JumperGui;
import the_dark_jumper.cannonTracer.util.GetterAndSetter_INT;

public class KeybindFrame extends DoubleSegmentFrame implements Tickable, Focusable{
	public final GetterAndSetter_INT keybind;
	public boolean isFocused = false;
	
	
	public KeybindFrame(Main main, Screen parent, int x, int y, int xEnd, int yEnd, int borderThickness, int innerColor,
			int innerColor2, int borderColor, int colorHover, int colorHover2, String keybindName, GetterAndSetter_INT keybind) {
		super(main, parent, keybindName, main.keyLibrary.getKeyContent(keybind.getter.get()).keyName, x, y, xEnd, yEnd,
				borderThickness, innerColor, innerColor2, borderColor, 0xfff1f1f1, colorHover, colorHover2);
		this.keybind = keybind;
	}

	@Override
	public void tick(JumperGui gui) {
		if(gui instanceof ConfigGUI) {
			boolean leftDown = ((ConfigGUI)gui).leftDown;
			if(isFocused && !this.ignoreInput && leftDown && !this.hovered &&
					(!main.keyPressListener.pressedKeys.contains(42) && !main.keyPressListener.pressedKeys.contains(54))) {
				focusLost();
			}
		}
	}

	@Override
	public void onClicked() {
		System.out.println("onclicked!");
		ignoreInput = true;
		focused();
	}
	
	public void focused() {
		System.out.println("focused!");
		isFocused = true;
		this.value = "";
	}
	
	public void focusLost() {
		System.out.println("focus lost!");
		isFocused = false;
		if(this.value.equals("")) {
			this.value = main.keyLibrary.getKeyContent(keybind.getter.get()).keyName;
		}
	}
	
	public void keyEvent(InputEvent.KeyInputEvent event) {
		System.out.println("receiving keyevent!");
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
