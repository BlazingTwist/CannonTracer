package the_dark_jumper.cannontracer.modules.moduleelements.behaviours;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;

public interface IModuleBehaviour {
	IModule getParent();
	
	void onTriggerChanged(boolean isTriggered);
	
	void renderText(Screen screen, FontRenderer fontRenderer, int x, int y);
}
