package the_dark_jumper.cannontracer.hotkey;

import java.util.ArrayList;

import the_dark_jumper.cannontracer.gui.HotkeyGUI;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.KeybindFrame;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.ToggleValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.ValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.util.KeybindData;

public class HotkeyTableEntry {
	private HotkeyGUI parent;
	public Hotkey hotkey;
	public ScrollableTable table;
	public int hotkeyIndex;
	
	public HotkeyTableEntry(HotkeyGUI parent, Hotkey hotkey, ScrollableTable table, int hotkeyIndex) {
		this.parent = parent;
		this.hotkey = hotkey;
		this.table = table;
		this.hotkeyIndex = hotkeyIndex;
	}
	
	public void onAddKeybindPressed(boolean isPressed) {
		if(isPressed) {
			KeybindData keybindData = new KeybindData();
			hotkey.keybinds.add(keybindData);
			table.setRow(hotkeyIndex + 1, generateRow());
			table.updateScrollbarRanges();
		}
	}
	
	public ArrayList<IRenderableFrame> generateRow(){
		ArrayList<IRenderableFrame> row = new ArrayList<>();
		row.add(new ButtonFrame(parent, "X", null, table.colors, this::onDelPressed));
		row.add(new ValueFrame(parent, null, table.colors, "cmd", hotkey.commandGNS, String.class));
		
		for(int i = 0; i < hotkey.keybinds.size(); i++) {
			KeybindData keybind = hotkey.keybinds.get(i);
			row.add(new ToggleValueFrame(parent, null, table.colors, "trigger_"+i, keybind.triggerGNS));
			row.add(new KeybindFrame(parent, null, table.colors, "keycode_"+i, keybind.keycodeGNS));
		}
		
		row.add(new ButtonFrame(parent, "+", null, table.colors, this::onAddKeybindPressed));
		return row;
	}
	
	public void onDelPressed(boolean isPressed) {
		if(isPressed) {
			parent.removeHotkey(hotkeyIndex);
		}
	}
}
