package the_dark_jumper.cannonTracer.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannonTracer.gui.guiElements.BasicTextFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.ButtonFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.FrameColors;
import the_dark_jumper.cannonTracer.gui.guiElements.KeybindFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.ToggleValueFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.ValueFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.ClickableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.FocusableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.RenderableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.TickableFrame;
import the_dark_jumper.cannonTracer.util.KeybindAccessors;
import the_dark_jumper.cannonTracer.util.TrackingData;

public class ConfigGUI extends Screen implements JumperGUI{	
	public final GuiManager guiManager;
	public ArrayList<RenderableFrame> guiComponents = new ArrayList<>();
	
	public ConfigGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public void generateSingleplayerScreenComponents() {
		guiComponents.clear();
		FrameConfig config = new FrameConfig();
		//alpha outliner
		config.init(5, 5, 95, 95, 8);
		FrameColors backGroundColors = new FrameColors();
		backGroundColors.innerColor = backGroundColors.borderColor = 0x55000000;
		guiComponents.add(new BasicTextFrame(this, "", config.duplicate(), backGroundColors));
		//headline
		config.init(6, 10, 49, 14, 8);
		FrameColors colors = new FrameColors();
		guiComponents.add(new BasicTextFrame(this, "Config-Screen", config.duplicate(), colors));
		config.init(50, 10, 71, 14, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "displayTick", guiManager.main.singlePlayerSettings.renderTickGNS, Integer.class));
		config.init(72, 10, 94, 14, 8);
		guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "logIDs", guiManager.main.singlePlayerSettings.bLogGNS));
		//keybinds
		config.init(6, 20, 94, 24, 8);
		guiComponents.add(new BasicTextFrame(this, "Keybinds", config.duplicate(), colors));
		generateKeybindScreenComponents(guiManager.main.keybindManagerSP.variables, 0, config, colors, 6, 25, 94, 29, 8);
		generateKeybindScreenComponents(guiManager.main.keybindManagerSP.variables, 1, config, colors, 6, 30, 94, 34, 8);
		//tracing entries
		config.init(6, 40, 94, 44, 8);
		guiComponents.add(new BasicTextFrame(this, "Tracked Entities", config.duplicate(), colors));
		generateTrackingScreenComponents(guiManager.main.entityTracker.observedEntityIDSP, config, colors, 6, 45, 94, 5, 8);		
	}
	
	public void openHotkeyScreenMP(boolean isPressed) {
		if(isPressed) {
			guiManager.hotkeyGUI.generateMultiplayerScreenComponents();
			Minecraft.getInstance().displayGuiScreen(guiManager.hotkeyGUI);
		}
	}
	
	public void generateMultiplayerScreenComponents() {
		guiComponents.clear();
		FrameConfig config = new FrameConfig();
		//alpha outliner
		config.init(5, 5, 95, 95, 8);
		FrameColors backGroundColors = new FrameColors();
		backGroundColors.innerColor = backGroundColors.borderColor = 0x55000000;
		guiComponents.add(new BasicTextFrame(this, "", config.duplicate(), backGroundColors));
		//headline
		config.init(6, 10, 49, 14, 8);
		FrameColors colors = new FrameColors();
		guiComponents.add(new BasicTextFrame(this, "Config-Screen", config.duplicate(), colors));
		config.init(50, 10, 71, 14, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "displayTick", guiManager.main.multiPlayerSettings.renderTickGNS, Integer.class));
		config.init(72, 10, 94, 14, 8);
		guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "logIDs", guiManager.main.multiPlayerSettings.bLogGNS));
		//keybinds
		config.init(6, 20, 49, 24, 8);
		guiComponents.add(new BasicTextFrame(this, "Keybinds", config.duplicate(), colors));
		config.init(50, 20, 94, 24, 8);
		guiComponents.add(new ButtonFrame(this, "open Hotkey Menu", config.duplicate(), colors, this::openHotkeyScreenMP));
		generateKeybindScreenComponents(guiManager.main.keybindManagerMP.variables, 0, config, colors, 6, 25, 94, 29, 8);
		generateKeybindScreenComponents(guiManager.main.keybindManagerMP.variables, 1, config, colors, 6, 30, 94, 34, 8);
		//tracing entries
		config.init(6, 40, 94, 44, 8);
		guiComponents.add(new BasicTextFrame(this, "Tracked Entities", config.duplicate(), colors));
		generateTrackingScreenComponents(guiManager.main.entityTracker.observedEntityIDMP, config, colors, 6, 45, 94, 5, 8);
	}
	
	public void generateKeybindScreenComponents(LinkedHashMap<String, KeybindAccessors> keybindVariables, int accessorIndex, FrameConfig config, FrameColors colors, int x1, int y1, int x2, int y2, int border) {
		int i = 0;
		float steps = (float)(x2 - x1) / keybindVariables.size();
		for(String key : keybindVariables.keySet()) {
			int innerx1 = (int)(x1 + (steps * i));
			config.init(innerx1, y1, (int)(innerx1 + steps - 1), y2, border);
			guiComponents.add(new KeybindFrame(this, config.duplicate(), colors, key, keybindVariables.get(key).accessors[accessorIndex]));
			i++;
		}
	}
	
	public void generateTrackingScreenComponents(HashMap<String, TrackingData> entities, FrameConfig config, FrameColors colors, int x1, int y1, int x2, int height, int border) {
		int i = 0;
		float steps = (x2 - x1) / 8f;
		for(String key : entities.keySet()) {
			int innery1 = y1 + height * i;
			config.init(x1, innery1, (int)(x1 + steps - 1), (innery1 + height - 1), border);
			guiComponents.add(new BasicTextFrame(this, key, config.duplicate(), colors));
			TrackingData trackingData = entities.get(key);
			for(int i2 = 1; i2 <= 7; i2++) {
				int innerx1 = (int)(x1 + (i2 * steps));
				config.init(innerx1, innery1, (int)(innerx1 + steps - 1), (innery1 + height - 1), border);
				if(i2 == 1) {
					guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "time", trackingData.timeGNS, Float.class));
				}else if(i2 == 2) {
					guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "thickness", trackingData.thicknessGNS, Float.class));
				}else if(i2 == 3) {
					guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "red", trackingData.redGNS, Integer.class));
				}else if(i2 == 4) {
					guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "green", trackingData.greenGNS, Integer.class));
				}else if(i2 == 5) {
					guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "blue", trackingData.blueGNS, Integer.class));
				}else if(i2 == 6) {
					guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "alpha", trackingData.alphaGNS, Integer.class));
				}else if(i2 == 7) {
					guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "render", trackingData.renderGNS));
				}
			}
			i++;
		}
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
			if(renderable instanceof ClickableFrame) {
				((ClickableFrame)renderable).mouseOver(mouseX, mouseY, scaledScreenWidth, scaledScreenHeight, this.leftDown, this.queueLeftUpdate);
			}
			if(renderable instanceof TickableFrame) {
				((TickableFrame)renderable).tick(this);
			}
			renderable.render(scaledScreenWidth, scaledScreenHeight, guiScale);
		}
		queueLeftUpdate = false;
	}
	
	public void keyEvent(InputEvent.KeyInputEvent event) {
		for(RenderableFrame renderable : guiComponents) {
			if(renderable instanceof FocusableFrame) {
				if(((FocusableFrame)renderable).getFocused()) {
					((FocusableFrame)renderable).keyEvent(event);
				}
			}
		}
	}
	
	public boolean leftDown = false;
	public boolean queueLeftUpdate = false;
	public boolean getLeftDown() {
		return leftDown;
	}
	public void mousePressEvent(boolean leftDown) {
		if(this.leftDown != leftDown) {
			this.leftDown = leftDown;
			queueLeftUpdate = true;
		}
	}
	
	@Override
	public void onClose() {
		if(minecraft.currentScreen != null && minecraft.currentScreen != this && minecraft.currentScreen instanceof JumperGUI) {
			minecraft.currentScreen.onClose();
		}
		if(minecraft.currentScreen != null) {
			minecraft.displayGuiScreen((Screen)null);
		}
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
