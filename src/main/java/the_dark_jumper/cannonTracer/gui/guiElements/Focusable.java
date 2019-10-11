package the_dark_jumper.cannonTracer.gui.guiElements;

import net.minecraftforge.client.event.InputEvent;

public interface Focusable {
	public void focused();
	
	public void focusLost();
	
	public boolean getFocused();
	
	public void keyEvent(InputEvent.KeyInputEvent event);
}
