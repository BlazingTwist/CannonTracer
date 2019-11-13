package the_dark_jumper.cannontracer.modules.moduleelements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

public interface IModuleBehaviour {
	public IModule getParent();
	
	public void onTriggerChanged(boolean isTriggered);
	
	public void renderText(Screen screen, FontRenderer fontRenderer, int x, int y);
}
