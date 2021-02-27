package the_dark_jumper.cannontracer.gui;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.opengl.GL11;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.gui.guielements.BasicTextFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IKeyEventRepeaterFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.ITickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FormatData;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;
import the_dark_jumper.cannontracer.hotkey.Hotkey;
import the_dark_jumper.cannontracer.hotkey.HotkeyTableEntry;

public class HotkeyGUI extends Screen implements IJumperGUI{	
	public final GuiManager guiManager;
	public ArrayList<IRenderableFrame> guiComponents = new ArrayList<>();
	private ScrollableTable hotkeyTable = null;
	
	public HotkeyGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public ArrayList<HotkeyTableEntry> hotkeys = new ArrayList<>();
	
	public void addCommandButtonPressed(boolean isPressed) {
		if(isPressed) {
			System.out.println("Triggered new Command Button!");
			Hotkey hotkey = new Hotkey();
			HotkeyTableEntry hotkeyTableEntry = new HotkeyTableEntry(this, hotkey, hotkeyTable, hotkeys.size());
			Main.getInstance().hotkeyManager.addHotkey(hotkey);
			hotkeys.add(hotkeyTableEntry);
			hotkeyTable.addRow(hotkeyTableEntry.generateRow());
			hotkeyTable.updateScrollbarRanges();
		}
	}
	
	public void removeHotkey(int hotkeyIndex) {
		if(hotkeyIndex + 1 >= hotkeyTable.getRows().size()) {
			Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("Couldn't delete Hotkey "+ hotkeyIndex + ", stop spamming that button."));
			return;
		}
		
		//+1 to account for header row
		hotkeyTable.deleteRow(hotkeyIndex + 1);
		hotkeyTable.updateScrollbarRanges();
		
		hotkeys.remove(hotkeyIndex);
		Main.getInstance().hotkeyManager.removeHotkey(hotkeyIndex);
		
		for(HotkeyTableEntry hotkeyTableEntry : hotkeys) {
			if(hotkeyTableEntry.hotkeyIndex > hotkeyIndex) {
				hotkeyTableEntry.hotkeyIndex--;
			}
		}
	}
	
	public void closeButtonPressed(boolean isPressed) {
		if(isPressed) {
			Minecraft.getInstance().displayGuiScreen(guiManager.configGUI);
		}
	}
	
	public void generateScreenComponents() {
		guiComponents.clear();
		FrameConfig config = new FrameConfig();
		//alpha outliner
		config.init(5, 5, 95, 95, 8);
		FrameColors backGroundColors = new FrameColors();
		backGroundColors.innerColor = backGroundColors.borderColor = 0x55000000;
		guiComponents.add(new BasicTextFrame(this, "", config.duplicate(), backGroundColors));
		//headline
		config.init(6, 10, 59, 14, 8);
		FrameColors colors = new FrameColors();
		guiComponents.add(new BasicTextFrame(this, "Hotkey Menu", config.duplicate(), colors));
		config.init(60, 10, 89, 14, 8);
		guiComponents.add(new ButtonFrame(this, "add command", config.duplicate(), colors, this::addCommandButtonPressed));
		config.init(90, 10, 94, 14, 8);
		guiComponents.add(new ButtonFrame(this, "X", config.duplicate(), colors, this::closeButtonPressed));
		//CommandTable
		config.init(6, 20, 94, 84, 4);
		hotkeyTable = new ScrollableTable(this, config.duplicate(), colors);
		/*table.setUniformColFormat(false, new FormatData(6, 28));
		table.setUniformRowFormat(false, new FormatData(20, 25));*/
		hotkeyTable.setUniformColFormat(false, 9, 1);
		hotkeyTable.setColFormat(false,
				new FormatData(4, 1),
				new FormatData(4, 1),
				new FormatData(24, 1)
				);
		hotkeyTable.setUniformRowFormat(false, 4, 1);
		hotkeyTable.addRow(
				new BasicTextFrame(this, "del", null, colors),
				new BasicTextFrame(this, "add", null, colors),
				new BasicTextFrame(this, "command", null, colors),
				new BasicTextFrame(this, "triggers", null, colors)
				);
		generateHotkeys(hotkeyTable, Main.getInstance().hotkeyManager.getHotkeys());
		hotkeyTable.generateScrollbars(true, 3, true, hotkeyTable.matchWidthToHeight(3));
		hotkeyTable.updateScrollbarRanges();
		guiComponents.add(hotkeyTable);
	}
	
	private void generateHotkeys(ScrollableTable table, ArrayList<Hotkey> hotkeys2) {
		hotkeys.clear();
		for(int i = 0; i < hotkeys2.size(); i++) {
			Hotkey hotkey = hotkeys2.get(i);
			HotkeyTableEntry hotkeyEntry = new HotkeyTableEntry(this, hotkey, table, i);
			hotkeys.add(hotkeyEntry);
			table.addRow(hotkeyEntry.generateRow());
		}
	}
	
	boolean detectedAutoGUI = false;
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if(minecraft == null){
			System.out.println("minecraft was null?");
			return;
		}

		int scaledScreenWidth = minecraft.getMainWindow().getScaledWidth();
		//int scaledScreenWidth = minecraft.func_228018_at_().getScaledWidth();
		//int scaledScreenWidth = minecraft.mainWindow.getScaledWidth();

		int scaledScreenHeight = minecraft.getMainWindow().getScaledHeight();
		//int scaledScreenHeight = minecraft.func_228018_at_().getScaledHeight();
		//int scaledScreenHeight = minecraft.mainWindow.getScaledHeight();

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
			closeButtonPressed(true);
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
		double configFontHeight = guiManager.getGuiConfig().getFontHeight();
		if(configFontHeight == 0) {
			return;
		}
		height -= (fontRenderer.FONT_HEIGHT * configFontHeight) / 2;
		xPos /= configFontHeight;
		height /= configFontHeight;

		GL11.glPushMatrix();
		//GlStateManager.pushMatrix();
		GL11.glScaled(configFontHeight, configFontHeight, configFontHeight);
		//GlStateManager.scaled(configFontHeight, configFontHeight, configFontHeight);
		super.drawCenteredString(fontRenderer, text, xPos, height, color);
		GL11.glPopMatrix();
		//GlStateManager.popMatrix();
	}
	
	@Override
	public void onClose() {
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
	}
	
	@Override
	public void renderBackground(int something) {
	}
	
	@Override
	public void renderDirtBackground(int something) {
	}
}
