package the_dark_jumper.cannontracer.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.Update;
import the_dark_jumper.cannontracer.gui.guielements.BasicTextFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.KeybindFrame;
import the_dark_jumper.cannontracer.gui.guielements.ToggleValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.ValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.ITickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;
import the_dark_jumper.cannontracer.modules.ModuleManager.State;
import the_dark_jumper.cannontracer.util.KeybindAccessors;
import the_dark_jumper.cannontracer.util.TrackingData;

public class ConfigGUI extends Screen implements IJumperGUI{	
	public final GuiManager guiManager;
	public ArrayList<IRenderableFrame> guiComponents = new ArrayList<>();
	
	public ConfigGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public void openHotkeyScreen(boolean isPressed) {
		if(isPressed) {
			guiManager.hotkeyGUI.generateScreenComponents();
			Minecraft.getInstance().displayGuiScreen(guiManager.hotkeyGUI);
		}
	}
	
	public void onUpdateButton(boolean isPressed) {
		if(isPressed) {
			Update.updateMod();
		}
	}
	
	public void generateSingleplayerScreenComponents() {
		guiComponents.clear();
		FrameConfig config = new FrameConfig();
		FrameColors colors = new FrameColors();
		generateCommonScreenComponents(config, colors);
		
		//headline
		config.init(6, 10, 34, 14, 8);
		guiComponents.add(new BasicTextFrame(this, "Config-Screen", config.duplicate(), colors));
		config.init(35, 10, 49, 14, 8);
		guiComponents.add(new ButtonFrame(this, "Update", config.duplicate(), colors, this::onUpdateButton));
		config.init(50, 10, 71, 14, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "displayTick", guiManager.main.singlePlayerSettings.renderTickGNS, Integer.class));
		config.init(72, 10, 94, 14, 8);
		guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "logIDs", guiManager.main.singlePlayerSettings.bLogGNS));
		
		//keybinds
		config.init(6, 30, 49, 34, 8);
		guiComponents.add(new BasicTextFrame(this, "Keybinds", config.duplicate(), colors));
		config.init(50, 30, 94, 34, 8);
		guiComponents.add(new ButtonFrame(this, "open Hotkey Menu", config.duplicate(), colors, this::openHotkeyScreen));
		generateKeybindScreenComponents(guiManager.main.keybindManagerSP.variables, 0, config, colors, 6, 35, 94, 39, 8);
		generateKeybindScreenComponents(guiManager.main.keybindManagerSP.variables, 1, config, colors, 6, 40, 94, 44, 8);
		
		//tracing entries
		config.init(6, 50, 94, 54, 8);
		guiComponents.add(new BasicTextFrame(this, "Tracked Entities", config.duplicate(), colors));
		generateTrackingScreenComponents(guiManager.main.entityTracker.observedEntityIDSP, config, colors, 6, 55, 94, 5, 8);
	}
	
	public void generateMultiplayerScreenComponents() {
		guiComponents.clear();
		FrameConfig config = new FrameConfig();
		FrameColors colors = new FrameColors();
		generateCommonScreenComponents(config, colors);
		
		//headline
		config.init(6, 10, 34, 14, 8);
		guiComponents.add(new BasicTextFrame(this, "Config-Screen", config.duplicate(), colors));
		config.init(35, 10, 49, 14, 8);
		guiComponents.add(new ButtonFrame(this, "Update", config.duplicate(), colors, this::onUpdateButton));
		config.init(50, 10, 71, 14, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "displayTick", guiManager.main.multiPlayerSettings.renderTickGNS, Integer.class));
		config.init(72, 10, 94, 14, 8);
		guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "logIDs", guiManager.main.multiPlayerSettings.bLogGNS));
		
		//keybinds
		config.init(6, 30, 49, 34, 8);
		guiComponents.add(new BasicTextFrame(this, "Keybinds", config.duplicate(), colors));
		config.init(50, 30, 94, 34, 8);
		guiComponents.add(new ButtonFrame(this, "open Hotkey Menu", config.duplicate(), colors, this::openHotkeyScreen));
		generateKeybindScreenComponents(guiManager.main.keybindManagerMP.variables, 0, config, colors, 6, 35, 94, 39, 8);
		generateKeybindScreenComponents(guiManager.main.keybindManagerMP.variables, 1, config, colors, 6, 40, 94, 44, 8);
		
		//tracing entries
		config.init(6, 50, 94, 54, 8);
		guiComponents.add(new BasicTextFrame(this, "Tracked Entities", config.duplicate(), colors));
		generateTrackingScreenComponents(guiManager.main.entityTracker.observedEntityIDMP, config, colors, 6, 55, 94, 5, 8);
	}
	
	private void generateCommonScreenComponents(FrameConfig config, FrameColors colors) {
		//alpha outliner
		config.init(5, 5, 95, 95, 8);
		FrameColors backGroundColors = new FrameColors();
		backGroundColors.innerColor = backGroundColors.borderColor = 0x55000000;
		guiComponents.add(new BasicTextFrame(this, "", config.duplicate(), backGroundColors));
		
		//gui config
		config.init(6, 20, 34, 24, 8);
		guiComponents.add(new BasicTextFrame(this, "GUI Config", config.duplicate(), colors));
		config.init(35, 20, 44, 24, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "x-offset", guiManager.ingameGUI.xOffsetGNS, Double.class));
		config.init(45, 20, 54, 24, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "y-offset", guiManager.ingameGUI.yOffsetGNS, Double.class));
		config.init(55, 20, 64, 24, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "fontHeight", guiManager.fontHeightGNS, Integer.class));
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
	
	boolean detectedAutoGUI = false;
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		int scaledScreenWidth = minecraft.mainWindow.getScaledWidth();
		int scaledScreenHeight = minecraft.mainWindow.getScaledHeight();
		int guiScale = minecraft.gameSettings.guiScale;
		if(guiScale == 0) {
			if(!detectedAutoGUI) {
				detectedAutoGUI = true;
				Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("please don't use 'auto' as a gui-scale, I don't have internal access to it"));
			}
			return;
		}
		if(detectedAutoGUI) {
			detectedAutoGUI = false;
			Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("thank you for your cooperation."));
		}
		for(IRenderableFrame renderable : guiComponents) {
			if(renderable instanceof IClickableFrame) {
				((IClickableFrame)renderable).mouseOver(mouseX, mouseY, scaledScreenWidth, scaledScreenHeight, this.leftDown, this.queueLeftUpdate);
			}
			if(renderable instanceof ITickableFrame) {
				((ITickableFrame)renderable).tick(this);
			}
			renderable.render(scaledScreenWidth, scaledScreenHeight, guiScale);
		}
		queueLeftUpdate = false;
	}
	
	public void keyEvent(InputEvent.KeyInputEvent event) {
		if(Main.getInstance().keyPressListener.pressedKeys.contains(1)) {
			if(Main.getInstance().moduleManager.state == State.MULTIPLAYER) {
				Main.getInstance().moduleManager.menuMP.toggle();
			}else if(Main.getInstance().moduleManager.state == State.SINGLEPLAYER) {
				Main.getInstance().moduleManager.menuSP.toggle();
			}
			return;
		}
		for(IRenderableFrame renderable : guiComponents) {
			if(renderable instanceof IFocusableFrame && ((IFocusableFrame)renderable).getFocused()) {
				((IFocusableFrame)renderable).keyEvent(event);
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
		if(minecraft.currentScreen != null && minecraft.currentScreen != this && minecraft.currentScreen instanceof IJumperGUI) {
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
