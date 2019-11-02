package the_dark_jumper.cannontracer.hotkey;

import java.util.ArrayList;
import java.util.LinkedList;

import the_dark_jumper.cannontracer.gui.HotkeyGUI;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.KeybindFrame;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.ToggleValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.ValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class HotkeyTableEntry {
	private HotkeyGUI parent;
	public Hotkey hotkey;
	public ScrollableTable table;
	public int rowIndex;
	public LinkedList<KeybindHotkeyEntry> keybinds = new LinkedList<>();
	
	public HotkeyTableEntry(HotkeyGUI parent, Hotkey hotkey, ScrollableTable table, int rowIndex) {
		this.parent = parent;
		this.hotkey = hotkey;
		this.table = table;
		this.rowIndex = rowIndex;
	}
	
	public HotkeyTableEntry setKeybinds(LinkedList<KeybindData> keybinds2) {
		keybinds.clear();
		for(KeybindData keybindData : keybinds2) {
			keybinds.add(new KeybindHotkeyEntry(this, keybindData));
		}
		return this;
	}
	
	public void onAddKeybindPressed(boolean isPressed) {
		if(isPressed) {
			KeybindHotkeyEntry keybindHotkeyEntry = new KeybindHotkeyEntry(this);
			keybinds.add(keybindHotkeyEntry);
			table.setRow(rowIndex, generateRow());
			table.updateScrollbarRanges();
		}
	}
	
	public ArrayList<IRenderableFrame> generateRow(){
		ArrayList<IRenderableFrame> row = new ArrayList<>();
		row.add(new ButtonFrame(parent, "X", null, table.colors, this::onDelPressed));
		row.add(new ValueFrame(parent, null, table.colors, "cmd", new GetterAndSetter<String>(hotkey::getCommand, hotkey::setCommand), String.class));
		
		for(int i = 0; i < keybinds.size(); i++) {
			KeybindHotkeyEntry keybind = keybinds.get(i);
			row.add(new ToggleValueFrame(parent, null, table.colors, "trigger_"+i, keybind.triggerGNS));
			row.add(new KeybindFrame(parent, null, table.colors, "keycode_"+i, keybind.keycodeGNS));
		}
		
		row.add(new ButtonFrame(parent, "+", null, table.colors, this::onAddKeybindPressed));
		return row;
	}
	
	public void onDelPressed(boolean isPressed) {
		if(isPressed) {
			parent.removeHotkey(rowIndex);
		}
	}
}
