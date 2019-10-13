package the_dark_jumper.cannonTracer.gui.guiElements.interfaces;

import net.minecraftforge.client.event.InputEvent;

public interface FocusableFrame {
	public void focused();
	
	public void focusLost();
	
	public boolean getFocused();
	
	public void keyEvent(InputEvent.KeyInputEvent event);
}
