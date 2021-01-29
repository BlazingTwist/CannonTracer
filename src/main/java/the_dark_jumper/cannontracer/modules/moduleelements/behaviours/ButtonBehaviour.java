package the_dark_jumper.cannontracer.modules.moduleelements.behaviours;

import java.util.function.Consumer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBasic;

public class ButtonBehaviour implements IModuleBehaviour {
	private ModuleBasic parent;

	@Override
	public IModule getParent() {
		return parent;
	}

	public boolean isPressed = false;
	public Consumer<Boolean> onPressedChanged;

	public ButtonBehaviour(ModuleBasic parent, boolean isPressed, Consumer<Boolean> callback) {
		this.parent = parent;
		this.isPressed = isPressed;
		this.onPressedChanged = callback;
	}

	@Override
	public void renderText(Screen screen, FontRenderer fontRenderer, int x, int y) {
		screen.drawString(fontRenderer, parent.getName() + " : " + isPressed, x, y, isPressed ? 0x7f00ff00 : 0x7fff0000);
	}

	@Override
	public void onTriggerChanged(boolean isTriggered) {
		isPressed = isTriggered;
		onPressedChanged.accept(isPressed);
	}
}
