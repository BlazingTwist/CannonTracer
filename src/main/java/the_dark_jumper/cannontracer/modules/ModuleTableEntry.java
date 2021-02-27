package the_dark_jumper.cannontracer.modules;

import java.util.ArrayList;
import jumpercommons.GetterAndSetter;
import the_dark_jumper.cannontracer.configsaving.KeybindEntry;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.BasicTextFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonDefaultValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.ToggleKeybindFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.utils.FormatData;

public class ModuleTableEntry {
	private IJumperGUI parent;
	public String moduleName;
	public KeybindEntry keybind;
	public ScrollableTable table;
	public int moduleIndex;

	public ModuleTableEntry(IJumperGUI parent, String moduleName, KeybindEntry keybind, ScrollableTable table, int moduleIndex) {
		this.parent = parent;
		this.moduleName = moduleName;
		this.keybind = keybind;
		this.table = table;
		this.moduleIndex = moduleIndex;
	}

	public void onAddKeybindPressed(boolean isPressed) {
		if (isPressed) {
			int keyCount = keybind.getTrigger().size() + keybind.getExclude().size() + 1;
			table.setRow(moduleIndex + 1, generateRow(keyCount));
			table.updateScrollbarRanges();
		}
	}

	public void onDelKeybindPressed(GetterAndSetter<Integer> keyGNS) {
		boolean update = false;
		if (keybind.getTrigger().contains(keyGNS)) {
			keybind.getTrigger().remove(keyGNS);
			update = true;
		}
		if (keybind.getExclude().contains(keyGNS)) {
			keybind.getExclude().remove(keyGNS);
			update = true;
		}

		if (update) {
			table.setRow(moduleIndex + 1, generateRow());
			table.updateScrollbarRanges();
		}
	}

	public ArrayList<IRenderableFrame> generateRow() {
		int keyCount = keybind.getTrigger().size() + keybind.getExclude().size();
		return generateRow(keyCount);
	}

	public ArrayList<IRenderableFrame> generateRow(int keyCount) {
		ArrayList<IRenderableFrame> row = new ArrayList<>();
		row.add(new ButtonFrame(parent, "+", null, table.colors, this::onAddKeybindPressed));
		row.add(new BasicTextFrame(parent, moduleName, null, table.colors));

		int index = 0;
		for (GetterAndSetter<Integer> trigger : keybind.getTrigger()) {
			addButtons(row, index, trigger, true);
			index++;
		}
		for (GetterAndSetter<Integer> exclude : keybind.getExclude()) {
			addButtons(row, index, exclude, false);
			index++;
		}
		for (; index < keyCount; index++) {
			GetterAndSetter<Integer> keycodeGNS = new GetterAndSetter<>(1);
			keybind.getTrigger().add(keycodeGNS);
			addButtons(row, index, keycodeGNS, true);
		}
		return row;
	}

	private void addButtons(ArrayList<IRenderableFrame> row, int index, GetterAndSetter<Integer> keyGNS, boolean triggerMode) {
		row.add(new ToggleKeybindFrame(parent, null, table.colors, "key_" + index, keyGNS, new KeybindToggleHandler(triggerMode, keybind, keyGNS)));
		row.add(new ButtonDefaultValueFrame<>(parent, "x", null, table.colors, keyGNS, this::onDelKeybindPressed));
		int colIndex = (1 + (2 * (index + 1)));
		table.setColFormat(colIndex, false, new FormatData(2, 2));
	}

	private static class KeybindToggleHandler extends GetterAndSetter<Boolean> {
		private final KeybindEntry keybind;
		private final GetterAndSetter<Integer> keycode;

		public KeybindToggleHandler(Boolean initialValue, KeybindEntry keybind, GetterAndSetter<Integer> keycode) {
			super(initialValue);
			this.keybind = keybind;
			this.keycode = keycode;
		}

		@Override
		public void set(Boolean value) {
			if (get() != value) {
				if (value) {
					keybind.getExclude().remove(keycode);
					keybind.getTrigger().add(keycode);
				} else {
					keybind.getTrigger().remove(keycode);
					keybind.getExclude().add(keycode);
				}
			}
			super.set(value);
		}
	}
}
