package the_dark_jumper.cannontracer.modules;

import java.util.ArrayList;
import java.util.LinkedList;

import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.BasicTextFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonDefaultValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.KeybindFrame;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.ToggleValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.utils.FormatData;
import the_dark_jumper.cannontracer.util.KeybindData;

public class ModuleTableEntry {
	private IJumperGUI parent;
	public String moduleName;
	public LinkedList<KeybindData> keybinds;
	public ScrollableTable table;
	public int moduleIndex;
	
	public ModuleTableEntry(IJumperGUI parent, String moduleName, LinkedList<KeybindData> keybinds, ScrollableTable table, int moduleIndex) {
		this.parent = parent;
		this.moduleName = moduleName;
		this.keybinds = keybinds;
		this.table = table;
		this.moduleIndex = moduleIndex;
	}
	
	public void onAddKeybindPressed(boolean isPressed) {
		if(isPressed) {
			KeybindData keybindData = new KeybindData();
			keybinds.add(keybindData);
			table.setRow(moduleIndex + 1, generateRow());
			table.updateScrollbarRanges();
		}
	}
	
	public void onDelKeybindPressed(Integer index) {
		if(index >= keybinds.size()) {
			return;
		}
		keybinds.remove(index.intValue());
		table.setRow(moduleIndex + 1, generateRow());
		table.updateScrollbarRanges();
	}
	
	public ArrayList<IRenderableFrame> generateRow(){
		ArrayList<IRenderableFrame> row = new ArrayList<>();
		row.add(new ButtonFrame(parent, "+", null, table.colors, this::onAddKeybindPressed));
		row.add(new BasicTextFrame(parent, moduleName, null, table.colors));
		for(int i = 0; i < keybinds.size(); i++) {
			KeybindData keybind = keybinds.get(i);
			row.add(new ToggleValueFrame(parent, null, table.colors, "s_"+i, keybind.triggerGNS));
			row.add(new KeybindFrame(parent, null, table.colors, "key_"+i, keybind.keycodeGNS));
			row.add(new ButtonDefaultValueFrame<Integer>(parent, "x", null, table.colors, i, this::onDelKeybindPressed));
			int colIndex = (1 + (3 * (i + 1)));
			table.setColFormat(colIndex, false, new FormatData(2, 2));
		}
		return row;
	}
}
