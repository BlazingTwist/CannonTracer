package the_dark_jumper.cannontracer.gui;

import java.util.ArrayList;
import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.gui.HotkeyGUI.AddKeybind.KeyTriggerPair;
import the_dark_jumper.cannontracer.gui.guielements.BasicTextFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.KeybindFrame;
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
import the_dark_jumper.cannontracer.hotkey.Hotkey;
import the_dark_jumper.cannontracer.hotkey.KeybindData;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

//TODO implement consistent datagrid and outsource scrollbars to via consumers

public class HotkeyGUI extends Screen implements IJumperGUI{	
	public final GuiManager guiManager;
	public ArrayList<IRenderableFrame> guiComponents = new ArrayList<>();
	private ScrollableTable hotkeyTable = null;
	
	public HotkeyGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
	}
	
	public class AddKeybind{
		public class KeyTriggerPair{
			AddKeybind parent;
			int keybindIndex;
			
			public KeyTriggerPair(AddKeybind parent, int keybindIndex) {
				this.parent = parent;
				this.keybindIndex = keybindIndex;
			}
			
			public KeyTriggerPair(AddKeybind parent) {
				this.parent = parent;
				this.keybindIndex = (parent.hotkey.keybindCount++);
				parent.hotkey.keybinds.add(new KeybindData(1, true));
				onAddPressed();
			}
			
			public void setTriggerState(boolean triggerState) {
				System.out.println("setting trigger state: "+triggerState);
				parent.hotkey.keybinds.get(keybindIndex).triggerState = triggerState;
			}
			
			public boolean getTriggerState() {
				System.out.println("getting trigger state: "+parent.hotkey.keybinds.get(keybindIndex).triggerState);
				return parent.hotkey.keybinds.get(keybindIndex).triggerState;
			}
			
			public void setKeybind(int keycode) {
				System.out.println("setting keybind: "+keycode);
				parent.hotkey.keybinds.get(keybindIndex).keycode = keycode;
			}
			
			public int getKeybind() {
				System.out.println("getting keybind: "+parent.hotkey.keybinds.get(keybindIndex).keycode);
				return parent.hotkey.keybinds.get(keybindIndex).keycode;
			}
			
			public void onAddPressed() {
				ArrayList<IRenderableFrame> currentRow = table.getRows().get(rowIndex);
				HotkeyGUI gui = Main.getInstance().guiManager.hotkeyGUI;
				currentRow.add(currentRow.size() - 1, new ToggleValueFrame(gui, null, table.colors, "trigger_" + keybindIndex, new GetterAndSetter<Boolean>(this::getTriggerState, this::setTriggerState)));
				currentRow.add(currentRow.size() - 1, new KeybindFrame(gui, null, table.colors, "keycode_" + keybindIndex, new GetterAndSetter<Integer>(this::getKeybind, this::setKeybind)));
				table.setRow(rowIndex, currentRow);
				table.updateScrollbarRanges();
			}
		}
		
		Hotkey hotkey;
		ScrollableTable table;
		int rowIndex;
		LinkedList<KeyTriggerPair> keyTriggerPairs = new LinkedList<>();
		
		public AddKeybind(Hotkey hotkey, ScrollableTable table, int rowIndex, boolean addToManager) {
			this.hotkey = hotkey;
			this.table = table;
			this.rowIndex = rowIndex;
			if(addToManager) {
				Main.getInstance().hotkeyManager.addHotkey(hotkey);
			}
		}
		
		public KeyTriggerPair addTriggerPair(int keybindIndex) {
			KeyTriggerPair keyTriggerPair = new KeyTriggerPair(this, keybindIndex);
			this.keyTriggerPairs.add(keyTriggerPair);
			return keyTriggerPair;
		}
		
		public void onAddPressed(boolean isPressed) {
			if(isPressed) {
				keyTriggerPairs.add(new KeyTriggerPair(this));
			}
		}
		
		public void onDelPressed(boolean isPressed) {
			if(isPressed) {
				if(table != null) {
					table.deleteRow(rowIndex);
					table.updateScrollbarRanges();
				}
				table = null;
				if(hotkey != null) {
					hotkey.keybindCount--;
				}
				hotkey = null;
				for(AddKeybind addKeybind : addKeybinds) {
					if(addKeybind.rowIndex > rowIndex) {
						addKeybind.rowIndex--;
					}
				}
			}
		}
	}
	
	ArrayList<AddKeybind> addKeybinds = new ArrayList<>();
	public void addCommandButtonPressed(boolean isPressed) {
		if(isPressed) {
			System.out.println("adding row!");
			Hotkey hotkey = new Hotkey();
			AddKeybind addKeybind = new AddKeybind(hotkey, hotkeyTable, hotkeyTable.getRows().size(), true);
			addKeybinds.add(addKeybind);
			hotkeyTable.addRow(
					new ButtonFrame(this, "X", null, hotkeyTable.colors, addKeybind::onDelPressed),
					new ValueFrame(this, null, hotkeyTable.colors, "cmd", new GetterAndSetter<String>(hotkey::getCommand, hotkey::setCommand), String.class),
					new ButtonFrame(this, "+", null, hotkeyTable.colors, addKeybind::onAddPressed)
					);
			hotkeyTable.updateScrollbarRanges();
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
				new FormatData(24, 1)
				);
		hotkeyTable.setUniformRowFormat(false, 4, 1);
		hotkeyTable.addRow(
				new BasicTextFrame(this, "del", null, colors),
				new BasicTextFrame(this, "command", null, colors),
				new BasicTextFrame(this, "triggerState", null, colors),
				new BasicTextFrame(this, "keybind", null, colors)
				);
		generateHotkeys(hotkeyTable, Main.getInstance().hotkeyManager.getHotkeys());
		hotkeyTable.generateScrollbars(true, 3, true, hotkeyTable.matchWidthToHeight(3));
		hotkeyTable.updateScrollbarRanges();
		guiComponents.add(hotkeyTable);
	}
	
	public void generateHotkeys(ScrollableTable table, ArrayList<Hotkey> hotkeys) {
		for(int i = 0; i < hotkeys.size(); i++) {
			System.out.println("i: "+i+" | hotkeys.size: "+hotkeys.size());
			Hotkey hotkey = hotkeys.get(i);
			IRenderableFrame[] frames = new IRenderableFrame[(hotkey.keybinds.size() * 2) + 3];
			AddKeybind addKeybind = new AddKeybind(hotkey, table, i + 1, false);
			frames[0] = (new ButtonFrame(this, "X", null, table.colors, addKeybind::onDelPressed));
			frames[1] = (new ValueFrame(this, null, table.colors, "cmd", new GetterAndSetter<String>(hotkey::getCommand, hotkey::setCommand), String.class));
			for(int keybindIndex = 0; keybindIndex < hotkey.keybinds.size(); keybindIndex++) {
				KeyTriggerPair keyTriggerPair = addKeybind.addTriggerPair(keybindIndex);
				frames[(keybindIndex * 2) + 2] = (new ToggleValueFrame(this, null, table.colors, "trigger_" + keybindIndex, new GetterAndSetter<Boolean>(keyTriggerPair::getTriggerState, keyTriggerPair::setTriggerState)));
				frames[(keybindIndex * 2) + 3] = (new KeybindFrame(this, null, table.colors, "keycode_" + keybindIndex, new GetterAndSetter<Integer>(keyTriggerPair::getKeybind, keyTriggerPair::setKeybind)));
				/*new ToggleValueFrame(gui, null, table.colors, Integer.toString(keybindIndex), new GetterAndSetter<Boolean>(this::getTriggerState, this::setTriggerState)));
				new KeybindFrame(gui, null, table.colors, Integer.toString(keybindIndex), new GetterAndSetter<Integer>(this::getKeybind, this::setKeybind)));*/
			}
			frames[(hotkey.keybinds.size() * 2) + 2] = (new ButtonFrame(this, "+", null, table.colors, addKeybind::onAddPressed));
			table.addRow(frames);
		}
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
			}else if(renderable instanceof IKeyEventRepeaterFrame) {
				((IKeyEventRepeaterFrame)renderable).keyEvent(event);
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
