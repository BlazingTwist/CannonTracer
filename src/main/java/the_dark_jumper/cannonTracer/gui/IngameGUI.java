package the_dark_jumper.cannontracer.gui;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBase;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleCounter;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleOnOff;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleStateMachine;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleToggle;

public class IngameGUI extends Screen{
	public final GuiManager guiManager;
	
	public IngameGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public void renderScreen() {
		int count = 0;
		ModuleBase module;
		try {
			for(Iterator<ModuleBase> it = guiManager.main.moduleManager.getModules().iterator(); it.hasNext();) {
				module = it.next();
				if(module == null) {
					return;
				}
				if(!module.render) {
					continue;
				}
				if(module instanceof ModuleOnOff) {
					ModuleOnOff moo = (ModuleOnOff)module;
					drawString(minecraft.fontRenderer, moo.name + " :" + moo.state, 4, 4 + (10 * count), moo.state ? 0x7f00ff00 : 0x7fff0000);
				}else if(module instanceof ModuleToggle) {
					ModuleToggle mt = (ModuleToggle)module;
					drawString(minecraft.fontRenderer, mt.name + " :" + mt.state, 4, 4 + (10 * count), mt.state ? 0x7f00ff00 : 0x7fff0000);
				}else if(module instanceof ModuleCounter) {
					ModuleCounter mc = (ModuleCounter)module;
					drawString(minecraft.fontRenderer, mc.name + " :" + mc.valueGNS.getter.get(), 4, 4 + (10 * count), 0x7fffffff);
				}else if(module instanceof ModuleStateMachine) {
					ModuleStateMachine msm = (ModuleStateMachine)module;
					drawString(minecraft.fontRenderer, msm.name + " :" + msm.stateNames[msm.currentState], 4, 4 + (10 * count), 0x7fffffff);
				}
				count++;
			}
		}
		catch(ConcurrentModificationException e) {
			System.out.println("threads are weird, I don't give enough of a fuck, no gui for you this frame");
		}
	}
}
