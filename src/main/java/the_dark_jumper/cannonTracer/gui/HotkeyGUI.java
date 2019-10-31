package the_dark_jumper.cannontracer.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.gui.guielements.BasicTextFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable.FormatData;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.ITickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

//TODO implement consistent datagrid and outsource scrollbars to via consumers

public class HotkeyGUI extends Screen implements IJumperGUI{	
	public final GuiManager guiManager;
	public ArrayList<IRenderableFrame> guiComponents = new ArrayList<>();
	
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
		table.setColFormat(true,
				new FormatData(0, 24),
				new FormatData(25, 49),
				new FormatData(50, 74),
				new FormatData(75, 99),
				new FormatData(100, 124)
				);
		table.setRowFormat(true,
				new FormatData(0, 24),
				new FormatData(25, 49),
				new FormatData(50, 74),
				new FormatData(75, 99),
				new FormatData(100, 124)
				);
		table.addRow(
				null,
				new ButtonFrame(this, "Column1", null, colors, null),
				new ButtonFrame(this, "Column2", null, colors, null),
				new ButtonFrame(this, "Column3", null, colors, null),
				new ButtonFrame(this, "Column4", null, colors, null)
				);
		table.addRow(
				new ButtonFrame(this, "Row1", null, colors, null),
				new ButtonFrame(this, "Value1_1", null, colors, null),
				new ButtonFrame(this, "Value1_2", null, colors, null),
				new ButtonFrame(this, "Value1_3", null, colors, null),
				new ButtonFrame(this, "Value1_4", null, colors, null)
				);
		table.addRow(
				new ButtonFrame(this, "Row2", null, colors, null),
				new ButtonFrame(this, "Value2_1", null, colors, null),
				new ButtonFrame(this, "Value2_2", null, colors, null),
				new ButtonFrame(this, "Value2_3", null, colors, null),
				new ButtonFrame(this, "Value2_4", null, colors, null)
				);
		table.addRow(
				new ButtonFrame(this, "Row3", null, colors, null),
				new ButtonFrame(this, "Value3_1", null, colors, null),
				new ButtonFrame(this, "Value3_2", null, colors, null),
				new ButtonFrame(this, "Value3_3", null, colors, null),
				new ButtonFrame(this, "Value3_4", null, colors, null)
				);
		table.addRow(
				new ButtonFrame(this, "Row4", null, colors, null),
				new ButtonFrame(this, "Value4_1", null, colors, null),
				new ButtonFrame(this, "Value4_2", null, colors, null),
				new ButtonFrame(this, "Value4_3", null, colors, null),
				new ButtonFrame(this, "Value4_4", null, colors, null)
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
		for(IRenderableFrame renderable : guiComponents) {
			if(renderable instanceof IFocusableFrame) {
				if(((IFocusableFrame)renderable).getFocused()) {
					((IFocusableFrame)renderable).keyEvent(event);
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
