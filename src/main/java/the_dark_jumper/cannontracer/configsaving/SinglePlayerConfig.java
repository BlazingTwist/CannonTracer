package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;

public class SinglePlayerConfig {
	@JsonProperty("trackingData")
	private HashMap<String, TrackingDataEntry> trackingData = new HashMap<>();

	@JsonProperty("keybinds")
	private SinglePlayerKeybinds keybinds = new SinglePlayerKeybinds();

	public SinglePlayerConfig() {
	}

	public HashMap<String, TrackingDataEntry> getTrackingData() {
		return trackingData;
	}

	public SinglePlayerConfig setTrackingData(HashMap<String, TrackingDataEntry> trackingData) {
		this.trackingData = trackingData;
		return this;
	}

	public SinglePlayerKeybinds getKeybinds() {
		return keybinds;
	}

	public SinglePlayerConfig setKeybinds(SinglePlayerKeybinds keybinds) {
		this.keybinds = keybinds;
		return this;
	}
}
