package the_dark_jumper.cannontracer.gui.guielements;

import jumpercommons.GetterAndSetter;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class ToggleKeybindFrame extends KeybindFrame implements IFocusableFrame {
	public final GetterAndSetter<Boolean> toggle;

	public boolean isFocused = false;

	public ToggleKeybindFrame(IJumperGUI parent, FrameConfig config, FrameColors colors, String keybindName, GetterAndSetter<Integer> keybind, GetterAndSetter<Boolean> toggle) {
		super(parent, config, colors, keybindName, keybind);
		this.toggle = toggle;
		updateColor();
	}

	@Override
	public void mouseOver(int x, int y, int scaledScreenWidth, int scaledScreenHeight, boolean mouseLeftDown, boolean queueLeftUpdate) {
		if (getIsClicked() && !mouseLeftDown) {
			setIsClicked(false);
		}
		int x1 = getPercentValue(scaledScreenWidth, this.getConfig().x);
		int x2 = getPercentValue(scaledScreenWidth, this.getEstimateValueBorder(this.getConfig().x, this.getConfig().xEnd));
		int x3 = getPercentValue(scaledScreenWidth, this.getConfig().xEnd);
		int y1 = getPercentValue(scaledScreenHeight, this.getConfig().y);
		int y2 = getPercentValue(scaledScreenHeight, this.getConfig().yEnd);
		if (x > x1 && x < x3 && y > y1 && y < y2) {
			setHovered(true);
			if (x < x2) {
				// handle toggle click
				if (queueLeftUpdate && mouseLeftDown) {
					setIsClicked(true);
					onFocusChange(false);
					toggle.set(!toggle.get());
					updateColor();
				}
			} else {
				// handle keybind change click
				if (queueLeftUpdate && mouseLeftDown) {
					setIsClicked(true);
					onFocusChange(true);
				}
			}
		} else {
			setHovered(false);
			//lose focus if mouse is pressed outside of frame and neither of the shift keys are pressed
			if (getFocused() && mouseLeftDown && queueLeftUpdate && (!Main.getInstance().keyPressListener.pressedKeys.contains(42) && !Main.getInstance().keyPressListener.pressedKeys.contains(54))) {
				onFocusChange(false);
			}
		}
	}

	private void updateColor() {
		this.valueColor = toggle.get() ? colors.colorOn : colors.colorOff;
	}
}
