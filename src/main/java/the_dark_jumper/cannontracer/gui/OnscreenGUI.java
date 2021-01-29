package the_dark_jumper.cannontracer.gui;

import java.util.ConcurrentModificationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.opengl.GL11;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.IModuleBehaviour;

public class OnscreenGUI extends Screen{
	public final GuiManager guiManager;
	
	public OnscreenGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public void renderScreen() {
		if(minecraft == null){
			System.out.println("minecraft was null?");
			return;
		}

		int baseX = 4 + (int)(guiManager.getGuiConfig().getXOffset() * minecraft.getMainWindow().getScaledWidth());
		//int baseX = 4 + (int)(xOffsetGNS.get() * minecraft.func_228018_at_().getScaledWidth());
		//int baseX = 4 + (int)(xOffsetGNS.get() * minecraft.mainWindow.getScaledWidth());

		int baseY = 4 + (int)(guiManager.getGuiConfig().getYOffset() * minecraft.getMainWindow().getScaledHeight());
		//int baseY = 4 + (int)(yOffsetGNS.get() * minecraft.func_228018_at_().getScaledHeight());
		//int baseY = 4 + (int)(yOffsetGNS.get() * minecraft.mainWindow.getScaledHeight());

		int count = 0;
		
		double configFontHeight = guiManager.getGuiConfig().getFontHeight();
		if(configFontHeight == 0) {
			return;
		}
		baseX /= configFontHeight;
		baseY /= configFontHeight;
		GL11.glPushMatrix();
		//GlStateManager.pushMatrix();
		GL11.glScaled(configFontHeight, configFontHeight, configFontHeight);
		//GlStateManager.scaled(configFontHeight, configFontHeight, configFontHeight);
		
		try {
			for (IModule module : guiManager.main.moduleManager.getModules()) {
				if (module == null || !module.isRender()) {
					continue;
				}

				IModuleBehaviour behaviour = module.getBehaviour();
				if (behaviour == null) {
					continue;
				}
				behaviour.renderText(this, minecraft.fontRenderer, baseX, baseY + (10 * count));

				count++;
			}
		}
		catch(ConcurrentModificationException e) {
			System.out.println("threads are weird, and I don't care enough, no gui for you this frame");
		}

		GL11.glPopMatrix();
		//GlStateManager.popMatrix();
	}
}
