package the_dark_jumper.cannontracer.gui;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.IModuleBehaviour;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class OnscreenGUI extends Screen{
	public final GuiManager guiManager;
	
	public GetterAndSetter<Double> xOffsetGNS = new GetterAndSetter<Double>(0d);
	public GetterAndSetter<Double> yOffsetGNS = new GetterAndSetter<Double>(0d);
	
	public OnscreenGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public void renderScreen() {
		int baseX = 4 + (int)(xOffsetGNS.get() * minecraft.mainWindow.getScaledWidth());
		int baseY = 4 + (int)(yOffsetGNS.get() * minecraft.mainWindow.getScaledHeight());
		int count = 0;
		
		double configFontHeight = guiManager.fontHeightGNS.get();
		if(configFontHeight == 0) {
			return;
		}
		baseX /= configFontHeight;
		baseY /= configFontHeight;
		GlStateManager.pushMatrix();
		GlStateManager.scaled(configFontHeight, configFontHeight, configFontHeight);
		
		try {
			for(Iterator<IModule> it = guiManager.main.moduleManager.getModules().iterator(); it.hasNext();) {
				IModule module = it.next();
				if(module == null) {
					continue;
				}
				if(!module.getRender()) {
					continue;
				}
				
				IModuleBehaviour behaviour = module.getBehaviour();
				if(behaviour == null) {
					continue;
				}
				behaviour.renderText(this, minecraft.fontRenderer, baseX, baseY + (10 * count));
				
				count++;
			}
		}
		catch(ConcurrentModificationException e) {
			System.out.println("threads are weird, I don't give enough of a fuck, no gui for you this frame");
		}
		
		GlStateManager.popMatrix();
	}
}
