package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class TracerConfig {
	@JsonProperty("SinglePlayer")
	private SinglePlayerConfig singlePlayerConfig = new SinglePlayerConfig();

	@JsonProperty("MultiPlayer")
	private MultiPlayerConfig multiPlayerConfig = new MultiPlayerConfig();

	@JsonProperty("Hotkeys")
	private ArrayList<HotkeyEntry> hotkeys = new ArrayList<>();

	@JsonProperty("GuiConfig")
	private GuiConfig guiConfig = new GuiConfig();

	public TracerConfig() {
	}

	public TracerConfig(SinglePlayerConfig singlePlayerConfig, MultiPlayerConfig multiPlayerConfig, ArrayList<HotkeyEntry> hotkeys, GuiConfig guiConfig) {
		this.singlePlayerConfig = singlePlayerConfig;
		this.multiPlayerConfig = multiPlayerConfig;
		this.hotkeys = hotkeys;
		this.guiConfig = guiConfig;
	}

	public SinglePlayerConfig getSinglePlayerConfig() {
		return singlePlayerConfig;
	}

	public TracerConfig setSinglePlayerConfig(SinglePlayerConfig singlePlayerConfig) {
		this.singlePlayerConfig = singlePlayerConfig;
		return this;
	}

	public MultiPlayerConfig getMultiPlayerConfig() {
		return multiPlayerConfig;
	}

	public TracerConfig setMultiPlayerConfig(MultiPlayerConfig multiPlayerConfig) {
		this.multiPlayerConfig = multiPlayerConfig;
		return this;
	}

	public ArrayList<HotkeyEntry> getHotkeys() {
		return hotkeys;
	}

	public TracerConfig setHotkeys(ArrayList<HotkeyEntry> hotkeys) {
		this.hotkeys = hotkeys;
		return this;
	}

	public GuiConfig getGuiConfig() {
		return guiConfig;
	}

	public TracerConfig setGuiConfig(GuiConfig guiConfig) {
		this.guiConfig = guiConfig;
		return this;
	}
}
