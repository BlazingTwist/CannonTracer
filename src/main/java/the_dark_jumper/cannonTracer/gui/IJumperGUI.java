package the_dark_jumper.cannontracer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.InputEvent;

public interface IJumperGUI {
	public boolean getLeftDown();
	
	public Minecraft getMinecraft();

	public void drawCenteredString(FontRenderer fontRenderer, String text, int i, int height, int j);
	
	public void keyEvent(InputEvent.KeyInputEvent event);
	
	public void mousePressEvent(boolean isLeftDown);
}
