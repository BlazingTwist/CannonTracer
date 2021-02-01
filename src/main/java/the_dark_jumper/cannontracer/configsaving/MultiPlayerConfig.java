package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class MultiPlayerConfig {
	@JsonProperty("trackingData")
	private HashMap<String, TrackingDataEntry> trackingData = new HashMap<>();

	@JsonProperty("maxRange")
	private GetterAndSetter<Integer> maxRange = new GetterAndSetter<>(500);

	@JsonProperty("keybinds")
	private MultiPlayerKeybinds keybinds = new MultiPlayerKeybinds();

	public MultiPlayerConfig() {
	}

	public HashMap<String, TrackingDataEntry> getTrackingData() {
		return trackingData;
	}

	public MultiPlayerConfig setTrackingData(HashMap<String, TrackingDataEntry> trackingData) {
		this.trackingData = trackingData;
		return this;
	}

	public GetterAndSetter<Integer> getMaxRangeGNS() {
		return maxRange;
	}

	public int getMaxRange() {
		return maxRange.get();
	}

	public MultiPlayerConfig setMaxRange(int maxRange) {
		this.maxRange.set(maxRange);
		return this;
	}

	public MultiPlayerKeybinds getKeybinds() {
		return keybinds;
	}

	public MultiPlayerConfig setKeybinds(MultiPlayerKeybinds keybinds) {
		this.keybinds = keybinds;
		return this;
	}
}
