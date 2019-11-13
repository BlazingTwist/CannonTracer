package the_dark_jumper.cannontracer.modules.moduleelements;

import java.util.function.Consumer;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

public class StateMachineBehaviour implements IModuleBehaviour{
	private ModuleBase parent;
	@Override public IModule getParent() {return parent;}
	
	public int currentState;
	public String stateNames[];
	public Consumer<Integer> onStateChanged;
	
	public StateMachineBehaviour(ModuleBase parent, int currentState, String[] stateNames, Consumer<Integer> callback) {
		this.parent = parent;
		this.currentState = currentState;
		this.stateNames = stateNames;
		this.onStateChanged = callback;
	}
	
	@Override
	public void renderText(Screen screen, FontRenderer fontRenderer, int x, int y) {
		screen.drawString(fontRenderer, parent.name + " : " + currentState, x, y, 0x7fffffff);
	}

	@Override
	public void onTriggerChanged(boolean isTriggered) {
		if(isTriggered) {
			nextState();
		}
	}
	
	private void nextState() {
		currentState = (currentState + 1) % stateNames.length;
		onStateChanged.accept(currentState);
	}
	
}
