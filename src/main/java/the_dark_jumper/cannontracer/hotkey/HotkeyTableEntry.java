package the_dark_jumper.cannontracer.hotkey;

import java.util.ArrayList;
import jumpercommons.GetterAndSetter;
import the_dark_jumper.cannontracer.gui.HotkeyGUI;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.ToggleKeybindFrame;
import the_dark_jumper.cannontracer.gui.guielements.ValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;

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
			// hotkeyIndex + 1 to account for header-row (pretty hacky junk, I know)
			int keyCount = hotkey.getHotkey().getTrigger().size() + hotkey.getHotkey().getExclude().size() + 1;
			table.setRow(hotkeyIndex + 1, generateRow(keyCount));
			table.updateScrollbarRanges();
		}
	}

	public ArrayList<IRenderableFrame> generateRow(){
		int keyCount = hotkey.getHotkey().getTrigger().size() + hotkey.getHotkey().getExclude().size();
		return generateRow(keyCount);
	}

	public ArrayList<IRenderableFrame> generateRow(int keyCount){
		ArrayList<IRenderableFrame> row = new ArrayList<>();
		row.add(new ButtonFrame(parent, "X", null, table.colors, this::onDelPressed));
		row.add(new ButtonFrame(parent, "+", null, table.colors, this::onAddKeybindPressed));
		row.add(new ValueFrame<>(parent, null, table.colors, "cmd", hotkey.getHotkey().getCommandGNS(), String.class, true));

		int index = 0;
		for (GetterAndSetter<Integer> trigger : hotkey.getHotkey().getTrigger()) {
			row.add(new ToggleKeybindFrame(parent, null, table.colors, "key_ " + index, trigger, new KeybindToggleHandler(true, hotkey, trigger)));
			index++;
		}
		for(GetterAndSetter<Integer> exclude : hotkey.getHotkey().getExclude()){
			row.add(new ToggleKeybindFrame(parent, null, table.colors, "key_" + index, exclude, new KeybindToggleHandler(false, hotkey, exclude)));
			index++;
		}
		for(; index < keyCount; index++){
			GetterAndSetter<Integer> keycodeGNS = new GetterAndSetter<>(1);
			hotkey.getHotkey().getTrigger().add(keycodeGNS);
			row.add(new ToggleKeybindFrame(parent, null, table.colors, "key_" + index, keycodeGNS, new KeybindToggleHandler(true, hotkey, keycodeGNS)));
		}
		return row;
	}
	
	public void onDelPressed(boolean isPressed) {
		if(isPressed) {
			parent.removeHotkey(hotkeyIndex);
		}
	}

	private static class KeybindToggleHandler extends GetterAndSetter<Boolean>{
		private final Hotkey hotkey;
		private final GetterAndSetter<Integer> keycode;

		public KeybindToggleHandler(Boolean initialValue, Hotkey hotkey, GetterAndSetter<Integer> keycode) {
			super(initialValue);
			this.hotkey = hotkey;
			this.keycode = keycode;
		}

		@Override
		public void set(Boolean value) {
			if(get() != value){
				if(value){
					hotkey.getHotkey().getExclude().remove(keycode);
					hotkey.getHotkey().getTrigger().add(keycode);
				}else{
					hotkey.getHotkey().getTrigger().remove(keycode);
					hotkey.getHotkey().getExclude().add(keycode);
				}
			}
			super.set(value);
		}
	}
}
