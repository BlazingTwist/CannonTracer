package the_dark_jumper.cannontracer.gui;

import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.ParametersAreNonnullByDefault;
import jumpercommons.GetterAndSetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.opengl.GL11;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.Update;
import the_dark_jumper.cannontracer.configsaving.TrackingDataEntry;
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
import the_dark_jumper.cannontracer.util.ChatUtils;

public class ConfigGUI extends Screen implements IJumperGUI {
	public final GuiManager guiManager;
	public ArrayList<IRenderableFrame> guiComponents = new ArrayList<>();
	private ScrollableTable moduleKeybindTable;
	private ScrollableTable trackingTable;

	private boolean queueRefresh = false;

	public ConfigGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}

	public void openHotkeyScreen(boolean isPressed) {
		if (isPressed) {
			guiManager.hotkeyGUI.generateScreenComponents();
			Minecraft.getInstance().displayGuiScreen(guiManager.hotkeyGUI);
		}
	}

	public void onUpdateButton(boolean isPressed) {
		if (isPressed) {
			Update.updateMod();
		}
	}

	private void addTrackingEntity(boolean isPressed) {
		if (isPressed) {
			Main main = Main.getInstance();
			HashMap<String, TrackingDataEntry> trackingData;
			switch (main.moduleManager.state) {
				case SINGLEPLAYER:
					trackingData = main.dataManager.getTrackingDataSP();
					break;
				case MULTIPLAYER:
					trackingData = main.dataManager.getTrackingDataMP();
					break;
				default:
					return;
			}
			String idName = "id_" + trackingData.size();
			TrackingDataEntry entry = new TrackingDataEntry();
			trackingData.put(idName, entry);
			TrackingIDChangeHandler idChangeHandler = new TrackingIDChangeHandler(trackingData, idName);
			TrackingDataDeleteHandler deleteHandler = new TrackingDataDeleteHandler(this, trackingData, idChangeHandler);
			generateTrackingRow(trackingTable, idChangeHandler, deleteHandler, entry);
			trackingTable.updateScrollbarRanges();
		}
	}

	private void reloadConfigButton(boolean isPressed) {
		if (isPressed) {
			this.queueRefresh = true;
			ChatUtils.messagePlayer("Config reloaded.", "", true);
		}
	}

	private void reloadGUI() {
		Main main = Main.getInstance();
		main.dataManager.load();
		guiComponents.clear();
		switch (main.moduleManager.state) {
			case SINGLEPLAYER:
				generateSingleplayerScreenComponents();
				break;
			case MULTIPLAYER:
				generateMultiplayerScreenComponents();
				break;
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
		config.init(47, 10, 54, 14, 8);
		guiComponents.add(new ButtonFrame(this, "Update mod", config.duplicate(), colors, this::onUpdateButton));
		config.init(55, 10, 62, 14, 8);
		guiComponents.add(new ButtonFrame(this, "Reload Config", config.duplicate(), colors, this::reloadConfigButton));
		config.init(63, 10, 78, 14, 8);
		guiComponents.add(new ValueFrame<>(this, config.duplicate(), colors, "displayTick", guiManager.main.singlePlayerSettings.renderTickGNS, Integer.class, false));
		config.init(79, 10, 94, 14, 8);
		guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "logIDs", guiManager.main.singlePlayerSettings.bLogGNS));

		//keybinds
		config.init(8, 35, 21, 39, 8);
		guiComponents.add(new BasicTextFrame(this, "Keybinds", config.duplicate(), headerColors));
		config.init(80, 35, 94, 39, 8);
		guiComponents.add(new ButtonFrame(this, "open Hotkey Menu", config.duplicate(), colors, this::openHotkeyScreen));
		config.init(6, 40, 94, 59, 4);
		generateModuleKeybindTable(config, colors, Main.getInstance().moduleManager.singlePlayerModules);

		//tracing entries
		config.init(8, 65, 21, 69, 8);
		guiComponents.add(new BasicTextFrame(this, "Tracked Entities", config.duplicate(), headerColors));
		config.init(80, 65, 94, 69, 8);
		guiComponents.add(new ButtonFrame(this, "add entity", config.duplicate(), colors, this::addTrackingEntity));
		config.init(6, 70, 94, 94, 4);
		generateTrackingTable(Main.getInstance().dataManager.getTrackingDataSP(), config, colors);
		//generateTrackingScreenComponents(guiManager.main.entityTracker.observedEntityIDSP, config, colors, 6, 70, 94, 5, 8);
	}

	public void generateMultiplayerScreenComponents() {
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
		config.init(47, 10, 55, 14, 8);
		guiComponents.add(new ButtonFrame(this, "Update mod", config.duplicate(), colors, this::onUpdateButton));
		config.init(56, 10, 64, 14, 8);
		guiComponents.add(new ButtonFrame(this, "Reload Config", config.duplicate(), colors, this::reloadConfigButton));
		config.init(65, 10, 74, 14, 8);
		guiComponents.add(new ValueFrame<>(this, config.duplicate(), colors, "displayTick", guiManager.main.multiPlayerSettings.renderTickGNS, Integer.class, false));
		config.init(75, 10, 84, 14, 8);
		guiComponents.add(new ValueFrame<>(this, config.duplicate(), colors, "maxRange", guiManager.main.dataManager.getTracerConfig().getMultiPlayerConfig().getMaxRangeGNS(), Integer.class, false));
		config.init(85, 10, 94, 14, 8);
		guiComponents.add(new ToggleValueFrame(this, config.duplicate(), colors, "logIDs", guiManager.main.multiPlayerSettings.bLogGNS));

		//keybinds
		config.init(8, 35, 21, 39, 8);
		guiComponents.add(new BasicTextFrame(this, "Keybinds", config.duplicate(), headerColors));
		config.init(80, 35, 94, 39, 8);
		guiComponents.add(new ButtonFrame(this, "open Hotkey Menu", config.duplicate(), colors, this::openHotkeyScreen));
		config.init(6, 40, 94, 59, 4);
		generateModuleKeybindTable(config, colors, Main.getInstance().moduleManager.multiPlayerModules);

		//tracing entries
		config.init(8, 65, 21, 69, 8);
		guiComponents.add(new BasicTextFrame(this, "Tracked Entities", config.duplicate(), headerColors));
		config.init(80, 65, 94, 69, 8);
		guiComponents.add(new ButtonFrame(this, "add entity", config.duplicate(), colors, this::addTrackingEntity));
		config.init(6, 70, 94, 94, 4);
		generateTrackingTable(Main.getInstance().dataManager.getTrackingDataMP(), config, colors);
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
		guiComponents.add(new ValueFrame<>(this, config.duplicate(), colors, "x-offset", guiManager.getGuiConfig().getXOffsetGNS(), Double.class, false));
		config.init(63, 20, 78, 24, 8);
		guiComponents.add(new ValueFrame<>(this, config.duplicate(), colors, "y-offset", guiManager.getGuiConfig().getYOffsetGNS(), Double.class, false));
		config.init(79, 20, 94, 24, 8);
		guiComponents.add(new ValueFrame<>(this, config.duplicate(), colors, "fontHeight", guiManager.getGuiConfig().getFontHeightGNS(), Double.class, false));
		config.init(6, 25, 49, 29, 8);
		guiComponents.add(new ValueFrame<>(this, config.duplicate(), colors, "cfg_path", Main.getInstance().dataManager.configPathGNS, String.class, true));
		config.init(50, 25, 94, 29, 8);
		guiComponents.add(new ValueFrame<>(this, config.duplicate(), colors, "update_path", Main.getInstance().dataManager.updatePathGNS, String.class, true));
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
				new BasicTextFrame(this, "keybind", null, colors)
		);
		generateKeybinds(moduleKeybindTable, modules);
		moduleKeybindTable.generateScrollbars(true, 10, true, moduleKeybindTable.matchWidthToHeight(10));
		moduleKeybindTable.updateScrollbarRanges();
		guiComponents.add(moduleKeybindTable);
	}

	private void generateKeybinds(ScrollableTable table, ArrayList<IModule> modules) {
		int tableIndex = 0;
		for (IModule module : modules) {
			if (module instanceof ModuleBasic) {
				table.addRow(new ModuleTableEntry(this, module.getName(), ((ModuleBasic) module).getKeybind(), table, tableIndex).generateRow());
				tableIndex++;
			} else if (module instanceof ModuleAxis) {
				table.addRow(new ModuleTableEntry(this, module.getName() + "+", ((ModuleAxis) module).getPositiveKeybind(), table, tableIndex).generateRow());
				tableIndex++;
				table.addRow(new ModuleTableEntry(this, module.getName() + "-", ((ModuleAxis) module).getNegativeKeybind(), table, tableIndex).generateRow());
				tableIndex++;
			}
		}
	}

	private void generateTrackingTable(HashMap<String, TrackingDataEntry> entities, FrameConfig config, FrameColors colors) {
		trackingTable = new ScrollableTable(this, config.duplicate(), colors);
		trackingTable.setUniformColFormat(true, 10, 1);
		trackingTable.setColFormat(true,
				new FormatData(2, 1),  // Delete-Button
				new FormatData(20, 1), // ID
				new FormatData(7, 1), // TIME
				new FormatData(10, 1), // Thickness
				new FormatData(10, 1), // hitBoxRadius
				new FormatData(7, 1), // Red
				new FormatData(7, 1), // Green
				new FormatData(7, 1), // Blue
				new FormatData(7, 1), // Alpha
				new FormatData(10, 1)  // Render
		);
		trackingTable.setUniformRowFormat(false, 4, 1);
		for (String key : entities.keySet()) {
			TrackingIDChangeHandler idChangeHandler = new TrackingIDChangeHandler(entities, key);
			TrackingDataDeleteHandler deleteHandler = new TrackingDataDeleteHandler(this, entities, idChangeHandler);
			generateTrackingRow(trackingTable, idChangeHandler, deleteHandler, entities.get(key));
		}
		trackingTable.generateScrollbars(false, 0, true, 1);
		trackingTable.updateScrollbarRanges();
		guiComponents.add(trackingTable);
	}

	private void generateTrackingRow(ScrollableTable table, TrackingIDChangeHandler idHandler, TrackingDataDeleteHandler deleteHandler, TrackingDataEntry trackingData) {
		table.addRow(
				new ButtonFrame(this, "x", null, table.colors, deleteHandler::onDelete),
				new ValueFrame<>(this, null, table.colors, "id", new GetterAndSetter<>(idHandler::getID, idHandler::setID), String.class, true),
				new ValueFrame<>(this, null, table.colors, "time", new GetterAndSetter<>(trackingData::getTime, trackingData::setTime), Float.class, false),
				new ValueFrame<>(this, null, table.colors, "thickness", new GetterAndSetter<>(trackingData::getThickness, trackingData::setThickness), Float.class, false),
				new ValueFrame<>(this, null, table.colors, "hitBoxRadius", new GetterAndSetter<>(trackingData::getHitBoxRadius, trackingData::setHitBoxRadius), Double.class, false),
				new ValueFrame<>(this, null, table.colors, "red", new GetterAndSetter<>(trackingData.getColor()::getRed, trackingData.getColor()::setRed), Integer.class, false),
				new ValueFrame<>(this, null, table.colors, "green", new GetterAndSetter<>(trackingData.getColor()::getGreen, trackingData.getColor()::setGreen), Integer.class, false),
				new ValueFrame<>(this, null, table.colors, "blue", new GetterAndSetter<>(trackingData.getColor()::getBlue, trackingData.getColor()::setBlue), Integer.class, false),
				new ValueFrame<>(this, null, table.colors, "alpha", new GetterAndSetter<>(trackingData.getColor()::getAlpha, trackingData.getColor()::setAlpha), Integer.class, false),
				new ToggleValueFrame(this, null, table.colors, "render", new GetterAndSetter<>(trackingData::isRender, trackingData::setRender))
		);
	}

	private static class TrackingIDChangeHandler {
		private final HashMap<String, TrackingDataEntry> entities;
		private String currentID;

		public TrackingIDChangeHandler(HashMap<String, TrackingDataEntry> entities, String currentID) {
			this.entities = entities;
			this.currentID = currentID;
		}

		public void setID(String id) {
			TrackingDataEntry entry = entities.remove(currentID);
			entities.put(id, entry);
			currentID = id;
		}

		public String getID() {
			return currentID;
		}
	}

	private static class TrackingDataDeleteHandler {
		private final ConfigGUI configGUI;
		private final HashMap<String, TrackingDataEntry> entities;
		private final TrackingIDChangeHandler idChangeHandler;

		public TrackingDataDeleteHandler(ConfigGUI configGUI, HashMap<String, TrackingDataEntry> entities, TrackingIDChangeHandler idChangeHandler) {
			this.configGUI = configGUI;
			this.entities = entities;
			this.idChangeHandler = idChangeHandler;
		}

		public void onDelete(boolean isPressed) {
			if (isPressed) {
				String id = idChangeHandler.getID();
				entities.remove(id);
				Main.getInstance().dataManager.save();
				configGUI.queueRefresh = true; // TODO worse performance, but drastically easier than managing row-indices
			}
		}
	}

	boolean detectedAutoGUI = false;

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (minecraft == null) {
			System.out.println("minecraft was null?");
			return;
		}

		if (queueRefresh) {
			reloadGUI();
			queueRefresh = false;
		}

		int scaledScreenWidth = minecraft.getMainWindow().getScaledWidth();
		//int scaledScreenWidth = minecraft.func_228018_at_().getScaledWidth();
		//int scaledScreenWidth = minecraft.mainWindow.getScaledWidth();

		int scaledScreenHeight = minecraft.getMainWindow().getScaledHeight();
		//int scaledScreenHeight = minecraft.func_228018_at_().getScaledHeight();
		//int scaledScreenHeight = minecraft.mainWindow.getScaledHeight();

		int guiScale = minecraft.gameSettings.guiScale;
		if (guiScale == 0) {
			if (!detectedAutoGUI) {
				detectedAutoGUI = true;
				ChatUtils.messagePlayer("please don't use 'auto' as a gui-scale, I don't have internal access to it", "", false);
			}
			return;
		}
		if (detectedAutoGUI) {
			detectedAutoGUI = false;
			ChatUtils.messagePlayer("thank you for your cooperation.", "", true);
		}

		for (IRenderableFrame renderable : guiComponents) {
			if (renderable instanceof IClickableFrame) {
				((IClickableFrame) renderable).mouseOver(mouseX, mouseY, scaledScreenWidth, scaledScreenHeight, this.leftDown, this.queueLeftUpdate);
			}
			if (renderable instanceof ITickableFrame) {
				((ITickableFrame) renderable).tick(this);
			}
			renderable.render(scaledScreenWidth, scaledScreenHeight, guiScale);
		}

		queueLeftUpdate = false;
	}

	@Override
	public void keyEvent(InputEvent.KeyInputEvent event) {
		if (Main.getInstance().keyPressListener.pressedKeys.contains(1)) {
			if (Main.getInstance().moduleManager.state == State.MULTIPLAYER) {
				Main.getInstance().moduleManager.menuMP.getBehaviour().onTriggerChanged(true);
			} else if (Main.getInstance().moduleManager.state == State.SINGLEPLAYER) {
				Main.getInstance().moduleManager.menuSP.getBehaviour().onTriggerChanged(true);
			}
			return;
		}
		for (IRenderableFrame renderable : guiComponents) {
			if (renderable instanceof IFocusableFrame) {
				if (((IFocusableFrame) renderable).getFocused()) {
					((IFocusableFrame) renderable).keyEvent(event);
				}
			} else if (renderable instanceof IKeyEventRepeaterFrame) {
				((IKeyEventRepeaterFrame) renderable).keyEvent(event);
			}
		}
	}

	public boolean leftDown = false;
	public boolean queueLeftUpdate = false;

	@Override
	public boolean getLeftDown() {
		return leftDown;
	}

	@Override
	public void mousePressEvent(boolean leftDown) {
		if (this.leftDown != leftDown) {
			this.leftDown = leftDown;
			queueLeftUpdate = true;
		}
	}

	@Override
	@ParametersAreNonnullByDefault
	public void drawCenteredString(FontRenderer fontRenderer, String text, int xPos, int height, int color) {
		double configFontSize = guiManager.getGuiConfig().getFontHeight();
		if (configFontSize == 0) {
			return;
		}
		height -= (fontRenderer.FONT_HEIGHT * configFontSize) / 2;
		xPos /= configFontSize;
		height /= configFontSize;

		GL11.glPushMatrix();
		//GlStateManager.pushMatrix();
		GL11.glScaled(configFontSize, configFontSize, configFontSize);
		//GlStateManager.scaled(configFontSize, configFontSize, configFontSize);
		super.drawCenteredString(fontRenderer, text, xPos, height, color);
		GL11.glPopMatrix();
		//GlStateManager.popMatrix();
	}

	@Override
	public void onClose() {
		if (minecraft == null) {
			return;
		}
		if (minecraft.currentScreen != null && minecraft.currentScreen != this && minecraft.currentScreen instanceof IJumperGUI) {
			minecraft.currentScreen.onClose();
		}
		if (minecraft.currentScreen != null) {
			minecraft.displayGuiScreen(null);
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
	}

	@Override
	public void renderBackground(int something) {
	}

	@Override
	public void renderDirtBackground(int something) {
	}
}
