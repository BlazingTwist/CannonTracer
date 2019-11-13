package the_dark_jumper.cannontracer.modules.moduleelements;

import java.util.function.Consumer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

public class ToggleBehaviour implements IModuleBehaviour{
	private ModuleBase parent;
	@Override public IModule getParent() {return parent;}
	
	public boolean state = false;
	public Consumer<Boolean> onStateChanged;
	
	public ToggleBehaviour(ModuleBase parent, boolean state, Consumer<Boolean> callback) {
		this.parent = parent;
		this.state = state;
		this.onStateChanged = callback;
	}
	
	@Override
	public void renderText(Screen screen, FontRenderer fontRenderer, int x, int y) {
		screen.drawString(fontRenderer, parent.name + " : " + state, x, y, state ? 0x7f00ff00 : 0x7fff0000);
	}
	
	@Override
	public void onTriggerChanged(boolean isTriggered) {
		if(isTriggered) {
			toggle();
		}
	}
	
	public void toggle() {
		state = !state;
		onStateChanged.accept(state);
	}
}
