package the_dark_jumper.cannontracer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.InputEvent;

public interface IJumperGUI {
	boolean getLeftDown();
	
	Minecraft getMinecraft();

	void drawCenteredString(FontRenderer fontRenderer, String text, int i, int height, int j);
	
	void keyEvent(InputEvent.KeyInputEvent event);
	
	void mousePressEvent(boolean isLeftDown);
}
