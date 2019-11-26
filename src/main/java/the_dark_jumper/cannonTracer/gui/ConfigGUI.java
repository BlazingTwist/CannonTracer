package the_dark_jumper.cannontracer.gui;

import java.util.ArrayList;
import java.util.HashMap;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.Update;
import the_dark_jumper.cannontracer.gui.guielements.BasicTextFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.ToggleValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.ValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IKeyEventRepeaterFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.ITickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FormatData;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;
import the_dark_jumper.cannontracer.modules.ModuleManager.State;
import the_dark_jumper.cannontracer.modules.ModuleTableEntry;
import the_dark_jumper.cannontracer.modules.moduleelements.IModule;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleAxis;
import the_dark_jumper.cannontracer.modules.moduleelements.ModuleBasic;
import the_dark_jumper.cannontracer.tracking.TrackingData;

public class ConfigGUI extends Screen implements IJumperGUI{	
	public final GuiManager guiManager;
	public ArrayList<IRenderableFrame> guiComponents = new ArrayList<>();
	private ScrollableTable moduleKeybindTable;
	private ScrollableTable trackingTable;
	
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
		FrameColors headerColors = new FrameColors();
		headerColors.innerColor = 0x778f8f8f;
		headerColors.borderColor = 0xff7f7f7f;
		generateCommonScreenComponents(config, colors, headerColors);
		
		//headline
		config.init(8, 10, 21, 14, 8);
		guiComponents.add(new BasicTextFrame(this, "Config-Screen", config.duplicate(), headerColors));
		config.init(47, 10, 62, 14, 8);
		guiComponents.add(new ButtonFrame(this, "Update", config.duplicate(), colors, this::onUpdateButton));
		config.init(63, 10, 78, 14, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "displayTick", guiManager.main.singlePlayerSettings.renderTickGNS, Integer.class));
		config.init(79, 10, 94, 14, 8);
		guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "logIDs", guiManager.main.singlePlayerSettings.bLogGNS));
		
		//keybinds
		config.init(8, 35, 21, 39, 8);
		guiComponents.add(new BasicTextFrame(this, "Keybinds", config.duplicate(), headerColors));
		config.init(80, 35, 94, 39, 8);
		guiComponents.add(new ButtonFrame(this, "open Hotkey Menu", config.duplicate(), colors, this::openHotkeyScreen));
		config.init(6, 40, 94, 59, 8);
		generateModuleKeybindTable(config, colors, Main.getInstance().moduleManager.singlePlayerModules);
		
		//tracing entries
		config.init(8, 65, 21, 69, 8);
		guiComponents.add(new BasicTextFrame(this, "Tracked Entities", config.duplicate(), headerColors));
		config.init(6, 70, 94, 94, 8);
		generateTrackingTable(Main.getInstance().entityTracker.observedEntityIDSP, config, colors);
		//generateTrackingScreenComponents(guiManager.main.entityTracker.observedEntityIDSP, config, colors, 6, 70, 94, 5, 8);
	}
	
	public void generateMultiplayerScreenComponents() {
		guiComponents.clear();
		FrameConfig config = new FrameConfig();
		FrameColors colors = new FrameColors();
		FrameColors headerColors = new FrameColors();
		headerColors.innerColor = 0;
		headerColors.borderColor = 0xff7f7f7f;
		generateCommonScreenComponents(config, colors, headerColors);
		
		//headline
		config.init(8, 10, 21, 14, 8);
		guiComponents.add(new BasicTextFrame(this, "Config-Screen", config.duplicate(), headerColors));
		config.init(47, 10, 62, 14, 8);
		guiComponents.add(new ButtonFrame(this, "Update", config.duplicate(), colors, this::onUpdateButton));
		config.init(63, 10, 78, 14, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "displayTick", guiManager.main.multiPlayerSettings.renderTickGNS, Integer.class));
		config.init(79, 10, 94, 14, 8);
		guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "logIDs", guiManager.main.multiPlayerSettings.bLogGNS));
		
		//keybinds
		config.init(8, 35, 21, 39, 8);
		guiComponents.add(new BasicTextFrame(this, "Keybinds", config.duplicate(), headerColors));
		config.init(80, 35, 94, 39, 8);
		guiComponents.add(new ButtonFrame(this, "open Hotkey Menu", config.duplicate(), colors, this::openHotkeyScreen));
		config.init(6, 40, 94, 59, 8);
		generateModuleKeybindTable(config, colors, Main.getInstance().moduleManager.multiPlayerModules);
		
		//tracing entries
		config.init(8, 65, 21, 69, 8);
		guiComponents.add(new BasicTextFrame(this, "Tracked Entities", config.duplicate(), headerColors));
		config.init(6, 70, 94, 94, 8);
		generateTrackingTable(Main.getInstance().entityTracker.observedEntityIDMP, config, colors);
		//generateTrackingScreenComponents(guiManager.main.entityTracker.observedEntityIDMP, config, colors, 6, 70, 94, 5, 8);
	}
	
	private void generateCommonScreenComponents(FrameConfig config, FrameColors colors, FrameColors headerColors) {
		//alpha outliner
		config.init(5, 5, 95, 95, 8);
		FrameColors backGroundColors = new FrameColors();
		backGroundColors.innerColor = backGroundColors.borderColor = 0x55000000;
		guiComponents.add(new BasicTextFrame(this, "", config.duplicate(), backGroundColors));
		
		//gui config
		config.init(8, 20, 21, 24, 8);
		guiComponents.add(new BasicTextFrame(this, "GUI Config", config.duplicate(), headerColors));
		config.init(47, 20, 62, 24, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "x-offset", guiManager.onscreenGUI.xOffsetGNS, Double.class));
		config.init(63, 20, 78, 24, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "y-offset", guiManager.onscreenGUI.yOffsetGNS, Double.class));
		config.init(79, 20, 94, 24, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "fontHeight", guiManager.fontHeightGNS, Double.class));
		config.init(6, 25, 49, 29, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "cfg_path", Main.getInstance().dataManager.configPathGNS, String.class));
		config.init(50, 25, 94, 29, 8);
		guiComponents.add(new ValueFrame(this, config.duplicate(), colors, "update_path", Main.getInstance().dataManager.updatePathGNS, String.class));
	}
	
	private void generateModuleKeybindTable(FrameConfig config, FrameColors colors, ArrayList<IModule> modules) {
		moduleKeybindTable = new ScrollableTable(this, config.duplicate(), colors);
		moduleKeybindTable.setUniformColFormat(false, 9, 1);
		moduleKeybindTable.setColFormat(false,
				new FormatData(2, 1),
				new FormatData(9, 2)
				);
		moduleKeybindTable.setUniformRowFormat(false, 4, 1);
		moduleKeybindTable.addRow(
				null,
				new BasicTextFrame(this, "KeybindName", null, colors),
				new BasicTextFrame(this, "triggerState", null, colors),
				new BasicTextFrame(this, "keybind", null, colors)
				);
		generateKeybinds(moduleKeybindTable, modules);
		moduleKeybindTable.generateScrollbars(true, 10, true, moduleKeybindTable.matchWidthToHeight(10));
		moduleKeybindTable.updateScrollbarRanges();
		guiComponents.add(moduleKeybindTable);
	}
	
	private void generateKeybinds(ScrollableTable table, ArrayList<IModule> modules){
		int limit = modules.size();
		for(int i = 0; i < limit; i++) {
			IModule module = modules.get(i);
			if(module instanceof ModuleBasic) {
				table.addRow(new ModuleTableEntry(this, module.getName(), ((ModuleBasic)module).keybinds, table, i).generateRow());
			}else if(module instanceof ModuleAxis) {
				table.addRow(new ModuleTableEntry(this, module.getName()+"+", ((ModuleAxis)module).positiveKeybinds, table, i).generateRow());
				//increment limit because module axis splits into two keybind sets
				limit++;
				i++;
				table.addRow(new ModuleTableEntry(this, module.getName()+"-", ((ModuleAxis)module).negativeKeybinds, table, i).generateRow());
			}
		}
	}
	
	private void generateTrackingTable(HashMap<String, TrackingData> entities, FrameConfig config, FrameColors colors) {
		trackingTable = new ScrollableTable(this, config.duplicate(), colors);
		trackingTable.setUniformColFormat(true, 11, 1);
		trackingTable.setColFormat(true,
				new FormatData(16, 1)
				);
		trackingTable.setUniformRowFormat(false, 4, 1);
		for(String key : entities.keySet()) {
			generateTrackingRow(trackingTable, key, entities.get(key));
		}
		trackingTable.generateScrollbars(false, 0, true, 1);
		trackingTable.updateScrollbarRanges();
		guiComponents.add(trackingTable);
	}
	
	private void generateTrackingRow(ScrollableTable table, String entityName, TrackingData trackingData) {
		System.out.println("adding key: "+entityName);
		table.addRow(
				new BasicTextFrame(this, entityName, null, table.colors),
				new ValueFrame(this, null, table.colors, "time", trackingData.timeGNS, Float.class),
				new ValueFrame(this, null, table.colors, "thickness", trackingData.thicknessGNS, Float.class),
				new ValueFrame(this, null, table.colors, "red", trackingData.redGNS, Integer.class),
				new ValueFrame(this, null, table.colors, "green", trackingData.greenGNS, Integer.class),
				new ValueFrame(this, null, table.colors, "blue", trackingData.blueGNS, Integer.class),
				new ValueFrame(this, null, table.colors, "alpha", trackingData.alphaGNS, Integer.class),
				new ToggleValueFrame(this, null, table.colors, "render", trackingData.renderGNS)
				);
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
	
	@Override public void keyEvent(InputEvent.KeyInputEvent event) {
		if(Main.getInstance().keyPressListener.pressedKeys.contains(1)) {
			if(Main.getInstance().moduleManager.state == State.MULTIPLAYER) {
				Main.getInstance().moduleManager.menuMP.behaviour.onTriggerChanged(true);
			}else if(Main.getInstance().moduleManager.state == State.SINGLEPLAYER) {
				Main.getInstance().moduleManager.menuSP.behaviour.onTriggerChanged(true);
			}
			return;
		}
		for(IRenderableFrame renderable : guiComponents) {
			if(renderable instanceof IFocusableFrame) {
				if(((IFocusableFrame)renderable).getFocused()) {
					((IFocusableFrame)renderable).keyEvent(event);
				}
			}else if(renderable instanceof IKeyEventRepeaterFrame) {
				((IKeyEventRepeaterFrame)renderable).keyEvent(event);
			}
		}
	}
	
	public boolean leftDown = false;
	public boolean queueLeftUpdate = false;
	@Override public boolean getLeftDown() {
		return leftDown;
	}
	@Override public void mousePressEvent(boolean leftDown) {
		if(this.leftDown != leftDown) {
			this.leftDown = leftDown;
			queueLeftUpdate = true;
		}
	}
	
	@Override
	public void drawCenteredString(FontRenderer fontRenderer, String text, int xPos, int height, int color) {
		double configFontHeight = guiManager.fontHeightGNS.get();
		if(configFontHeight == 0) {
			return;
		}
		height -= (fontRenderer.FONT_HEIGHT * configFontHeight) / 2;
		xPos /= configFontHeight;
		height /= configFontHeight;
		
		GlStateManager.pushMatrix();
		GlStateManager.scaled(configFontHeight, configFontHeight, configFontHeight);
		super.drawCenteredString(fontRenderer, text, xPos, height, color);
		GlStateManager.popMatrix();
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
