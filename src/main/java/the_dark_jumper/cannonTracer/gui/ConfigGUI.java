package the_dark_jumper.cannonTracer.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannonTracer.gui.guiElements.BasicTextFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.Clickable;
import the_dark_jumper.cannonTracer.gui.guiElements.Focusable;
import the_dark_jumper.cannonTracer.gui.guiElements.KeybindFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.RenderableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.Tickable;
import the_dark_jumper.cannonTracer.gui.guiElements.ToggleFrame;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleBase;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleToggle;

public class ConfigGUI extends Screen implements JumperGui{
	public final int borderColor = 0xff444444;
	public final int innerColor = 0xff4a9bce;
	public final int innerColor2 = 0xff3e82ad;
	public final int colorOn = 0xff00ff00;
	public final int colorOff = 0xffff0000;
	public final int colorHover = 0xff4a88ce;
	public final int colorHover2 = 0xff3e72ad;
	
	public boolean leftDown = false;
	
	public final GuiManager guiManager;
	public ArrayList<RenderableFrame> guiComponents = new ArrayList<>();
	
	public ConfigGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public void generateSingleplayerScreenComponents() {
		guiComponents.clear();
		guiComponents.add(new KeybindFrame(guiManager.main, this, 10, 10, 30, 14, 8, innerColor, innerColor2, borderColor,
				colorHover, colorHover2, "Menu1", guiManager.main.keybindManager.variablesSP.get("MenuSP").accessors[0]));
		/*guiComponents.add(new BasicTextFrame(this, "Hello World", 10, 10, 30, 14, 8, innerColor, borderColor));
		for(ModuleBase module : guiManager.main.moduleManager.activeModules) {
			if(module.name.equals("XRay Traces")) {
				guiComponents.add(new ToggleFrame(this, 10, 15, 30, 19, 8, innerColor, innerColor2, borderColor,
						colorOn, colorOff, colorHover, colorHover2, (ModuleToggle) module, 5));
			}
		}*/
		
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
				/*" | guiscale: " + minecraft.gameSettings.guiScale +
				" | mainwindow width: " + minecraft.mainWindow.getWidth() +
				" | mainwindow height: " + minecraft.mainWindow.getHeight() +
				" | mainwindow scaled width: " + minecraft.mainWindow.getScaledWidth() +
				" | mainwindow scaled height: " + minecraft.mainWindow.getScaledHeight());*/
		int scaledScreenWidth = minecraft.mainWindow.getScaledWidth();
		int scaledScreenHeight = minecraft.mainWindow.getScaledHeight();
		int guiScale = minecraft.gameSettings.guiScale;
		for(RenderableFrame renderable : guiComponents) {
			if(renderable instanceof Clickable) {
				((Clickable)renderable).mouseOver(mouseX, mouseY, scaledScreenWidth, scaledScreenHeight, this.leftDown);
			}			
			if(renderable instanceof Tickable) {
				((Tickable)renderable).tick(this);
			}
			renderable.render(scaledScreenWidth, scaledScreenHeight, guiScale);
		}
	}
	
	public void keyEvent(InputEvent.KeyInputEvent event) {
		for(RenderableFrame renderable : guiComponents) {
			if(renderable instanceof Focusable) {
				if(((Focusable)renderable).getFocused()) {
					((Focusable)renderable).keyEvent(event);
				}
			}
		}
	}
	
	public void mousePressEvent(boolean leftDown) {
		this.leftDown = leftDown;
	}
	
	@Override
	public void onClose() {
		this.minecraft.displayGuiScreen((Screen)null);
		guiComponents.clear();
	}
	
	//overrides to avoid stupidness
	@Override
	public String getNarrationMessage() {
		return "";
	}
	
	@Override
	public boolean keyPressed(int noIdea1, int noIdea2, int noIdea3) {
		return false;
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	public void renderBackground() {
		//no, just no
		return;
	}
	
	@Override
	public void renderBackground(int something) {
		//I said no, fuck you!
		return;
	}
	
	@Override
	public void renderDirtBackground(int something) {
		//are you shitting me?
		return;
	}
}
