package the_dark_jumper.cannonTracer.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannonTracer.gui.guiElements.BasicTextFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.ButtonFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.FrameColors;
import the_dark_jumper.cannonTracer.gui.guiElements.ScrollableTable;
import the_dark_jumper.cannonTracer.gui.guiElements.ScrollableTable.FormatData;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.ClickableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.FocusableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.RenderableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.TickableFrame;

//TODO implement consistent datagrid and outsource scrollbars to via consumers

public class HotkeyGUI extends Screen implements JumperGUI{	
	public final GuiManager guiManager;
	public ArrayList<RenderableFrame> guiComponents = new ArrayList<>();
	
	public HotkeyGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public void generateSingleplayerScreenComponents() {
		guiComponents.clear();
		//FrameConfig config = new FrameConfig();
		//alpha outliner
		
	}
	
	public void addCommandButtonPressed(boolean isPressed) {
		
	}
	
	public void closeButtonPressed(boolean isPressed) {
		if(isPressed) {
			Minecraft.getInstance().displayGuiScreen(guiManager.configGUI);
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
		config.init(6, 10, 59, 14, 8);
		FrameColors colors = new FrameColors();
		guiComponents.add(new BasicTextFrame(this, "Hotkey Menu", config.duplicate(), colors));
		config.init(60, 10, 89, 14, 8);
		guiComponents.add(new ButtonFrame(this, "add command", config.duplicate(), colors, this::addCommandButtonPressed));
		config.init(90, 10, 94, 14, 8);
		guiComponents.add(new ButtonFrame(this, "X", config.duplicate(), colors, this::closeButtonPressed));
		//CommandTable
		config.init(6, 20, 94, 84, 4);
		ScrollableTable table = new ScrollableTable(this, config.duplicate(), colors);
		/*table.setUniformColFormat(false, new FormatData(6, 28));
		table.setUniformRowFormat(false, new FormatData(20, 25));*/
		table.setColFormat(false,
				new FormatData(6, 27),
				new FormatData(28, 49),
				new FormatData(50, 71),
				new FormatData(72, 93),
				new FormatData(94, 116)
				);
		table.setRowFormat(false,
				new FormatData(20, 24),
				new FormatData(25, 29),
				new FormatData(30, 34),
				new FormatData(35, 39)
				);
		table.addRow(
				null,
				new ButtonFrame(this, "Column1", config.duplicate().init(28, 20, 49, 24, 8), colors, null),
				new ButtonFrame(this, "Column2", config.duplicate().init(50, 20, 71, 24, 8), colors, null),
				new ButtonFrame(this, "Column3", config.duplicate().init(72, 20, 93, 24, 8), colors, null),
				new ButtonFrame(this, "Column4", config.duplicate().init(94, 20, 116, 24, 8), colors, null)
				);
		table.addRow(
				new ButtonFrame(this, "Row1", config.duplicate().init(6, 25, 27, 29, 8), colors, null),
				new ButtonFrame(this, "Value1_1", config.duplicate().init(28, 25, 49, 29, 8), colors, null),
				new ButtonFrame(this, "Value1_2", config.duplicate().init(50, 25, 71, 29, 8), colors, null),
				new ButtonFrame(this, "Value1_3", config.duplicate().init(72, 25, 93, 29, 8), colors, null),
				new ButtonFrame(this, "Value1_4", config.duplicate().init(94, 25, 116, 29, 8), colors, null)
				);
		table.addRow(
				new ButtonFrame(this, "Row2", config.duplicate().init(6, 30, 27, 34, 8), colors, null),
				new ButtonFrame(this, "Value2_1", config.duplicate().init(28, 30, 49, 34, 8), colors, null),
				new ButtonFrame(this, "Value2_2", config.duplicate().init(50, 30, 71, 34, 8), colors, null),
				new ButtonFrame(this, "Value2_3", config.duplicate().init(72, 30, 93, 34, 8), colors, null),
				new ButtonFrame(this, "Value2_4", config.duplicate().init(94, 30, 116, 34, 8), colors, null)
				);
		table.addRow(

				new ButtonFrame(this, "Row3", config.duplicate().init(6, 35, 27, 39, 8), colors, null),
				new ButtonFrame(this, "Value3_1", config.duplicate().init(28, 35, 49, 39, 8), colors, null),
				new ButtonFrame(this, "Value3_2", config.duplicate().init(50, 35, 71, 39, 8), colors, null),
				new ButtonFrame(this, "Value3_3", config.duplicate().init(72, 35, 93, 39, 8), colors, null),
				new ButtonFrame(this, "Value3_4", config.duplicate().init(94, 35, 116, 39, 8), colors, null)
				);
		table.generateScrollbars(true, 3, true, table.matchWidthToHeight(3));
		table.updateScrollbarRanges();
		guiComponents.add(table);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
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
