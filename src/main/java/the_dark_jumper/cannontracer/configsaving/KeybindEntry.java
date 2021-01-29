package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.stream.Collectors;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class KeybindEntry {
	@JsonProperty("trigger")
	protected ArrayList<GetterAndSetter<Integer>> trigger = new ArrayList<>();

	@JsonProperty("exclude")
	protected ArrayList<GetterAndSetter<Integer>> exclude = new ArrayList<>();

	public KeybindEntry() {
	}

	public ArrayList<GetterAndSetter<Integer>> getTrigger() {
		return trigger;
	}

	public KeybindEntry setTrigger(ArrayList<GetterAndSetter<Integer>> trigger) {
		this.trigger = trigger;
		return this;
	}

	public KeybindEntry addTrigger(int... keys){
		for (int key : keys) {
			this.trigger.add(new GetterAndSetter<>(key));
		}
		return this;
	}

	public ArrayList<GetterAndSetter<Integer>> getExclude() {
		return exclude;
	}

	public KeybindEntry setExclude(ArrayList<GetterAndSetter<Integer>> exclude) {
		this.exclude = exclude;
		return this;
	}

	public KeybindEntry addExclude(int... keys){
		for (int key : keys) {
			this.exclude.add(new GetterAndSetter<>(key));
		}
		return this;
	}

	public boolean isKeyRelevant(int keycode) {
		return trigger.stream().map(GetterAndSetter::get).anyMatch(x -> x == keycode)
				|| exclude.stream().map(GetterAndSetter::get).anyMatch(x -> x == keycode);
	}

	public boolean isSatisfied() {
		for (GetterAndSetter<Integer> keycode : trigger) {
			if (!Main.getInstance().keyPressListener.pressedKeys.contains(keycode.get())) {
				return false;
			}
		}
		for (GetterAndSetter<Integer> keycode : exclude) {
			if (Main.getInstance().keyPressListener.pressedKeys.contains(keycode.get())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "KeybindEntry{" +
				"trigger=" + trigger.stream().map(GetterAndSetter::toString).collect(Collectors.joining(", ")) +
				", exclude=" + exclude.stream().map(GetterAndSetter::toString).collect(Collectors.joining(", ")) +
				'}';
	}
}
