package the_dark_jumper.cannontracer.gui.guielements.interfaces;

import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.Main;

public interface IFocusableFrame extends IClickableFrame{	
	@Override
	default void mouseOver(int x, int y, int scaledScreenWidth, int scaledScreenHeight, boolean mouseLeftDown, boolean queueLeftUpdate) {
		if(getIsClicked() && !mouseLeftDown) {
			setIsClicked(false);
		}
		int x1 = getPercentValue(scaledScreenWidth, this.getConfig().x);
		int x2 = getPercentValue(scaledScreenWidth, this.getConfig().xEnd);
		int y1 = getPercentValue(scaledScreenHeight, this.getConfig().y);
		int y2 = getPercentValue(scaledScreenHeight, this.getConfig().yEnd);
		if(x > x1 && x < x2 && y > y1 && y < y2) {
			setHovered(true);
			if(queueLeftUpdate && mouseLeftDown) {
				setIsClicked(true);
				onFocusChange(true);
			}
		}else {
			setHovered(false);
			//lose focus if mouse is pressed outside of frame and neither of the shift keys are pressed
			if(getFocused() && mouseLeftDown && queueLeftUpdate && (!Main.getInstance().keyPressListener.pressedKeys.contains(42) && !Main.getInstance().keyPressListener.pressedKeys.contains(54))) {
				onFocusChange(false);
			}
		}
	}
	
	boolean getFocused();
	void setFocused(boolean focused);

	boolean getPreserveOnclick();
	void setPreserveOnclick(boolean preserveOnclick);
	
	String getValue();
	void setValue(String value);
	
	default void onFocusChange(boolean isFocused) {
		System.out.println("Focus Changed!");
		if(isFocused == getFocused()) {
			return;
		}
		setFocused(isFocused);
		if(isFocused) {
			if(!getPreserveOnclick()){
				setValue("");
			}
		}else {
			parseInput();
		}
	}
	
	void parseInput();
	
	void keyEvent(InputEvent.KeyInputEvent event);
}
