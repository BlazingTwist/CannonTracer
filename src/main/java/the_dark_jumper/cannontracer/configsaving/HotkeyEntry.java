package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class HotkeyEntry extends KeybindEntry {
	@JsonProperty("command")
	private final GetterAndSetter<String> command = new GetterAndSetter<>("");

	public HotkeyEntry() {
	}

	public HotkeyEntry(String command, ArrayList<GetterAndSetter<Integer>> trigger, ArrayList<GetterAndSetter<Integer>> exclude) {
		this.command.set(command);
		this.trigger = trigger;
		this.exclude = exclude;
	}

	public GetterAndSetter<String> getCommandGNS() {
		return command;
	}

	public String getCommand() {
		return command.get();
	}

	public HotkeyEntry setCommand(String command){
		this.command.set(command);
		return this;
	}
}
