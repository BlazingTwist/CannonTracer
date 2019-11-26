package the_dark_jumper.cannontracer.modules.moduleelements.behaviours;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBasic;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class StateMachineBehaviour implements IModuleBehaviour{
	private ModuleBasic parent;
	@Override public IModule getParent() {return parent;}
	
	public String stateNames[];
	public GetterAndSetter<Integer> valueGNS;
	
	public StateMachineBehaviour(ModuleBasic parent, int currentState, String[] stateNames, GetterAndSetter<Integer> valueGNS) {
		this.parent = parent;
		valueGNS.set(currentState);
		this.stateNames = stateNames;
		this.valueGNS = valueGNS;
	}
	
	@Override
	public void renderText(Screen screen, FontRenderer fontRenderer, int x, int y) {
		screen.drawString(fontRenderer, parent.name + " : " + stateNames[valueGNS.get()], x, y, 0x7fffffff);
	}

	@Override
	public void onTriggerChanged(boolean isTriggered) {
		if(isTriggered) {
			nextState();
		}
	}
	
	private void nextState() {
		int targetState = (valueGNS.get() + 1) % stateNames.length;
		valueGNS.set(targetState);
	}
	
}
