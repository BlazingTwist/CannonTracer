package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class DataManager {
	public final Main main;

	public GetterAndSetter<String> configPathGNS = new GetterAndSetter<>("");
	public GetterAndSetter<String> updatePathGNS = new GetterAndSetter<>("");
	private TracerConfig tracerConfig = new TracerConfig();
	private HashMap<String, TrackingDataEntry> trackingDataSP = null;
	private HashMap<String, TrackingDataEntry> trackingDataMP = null;

	public DataManager(Main main) {
		this.main = main;
		Preferences prefs = Preferences.userNodeForPackage(DataManager.class);
		configPathGNS.set(prefs.get("TracerConfigPath", ""));
		updatePathGNS.set(prefs.get("TracerUpdatePath", ""));
	}

	public TracerConfig getTracerConfig() {
		return tracerConfig;
	}

	public DataManager setTracerConfig(TracerConfig tracerConfig) {
		this.tracerConfig = tracerConfig;
		return this;
	}

	public HashMap<String, TrackingDataEntry> getTrackingDataSP() {
		if (trackingDataSP == null) {
			try {
				trackingDataSP = main.dataManager.getTracerConfig().getSinglePlayerConfig().getTrackingData();
			} catch (NullPointerException e) {
				trackingDataSP = null;
			}
		}
		return trackingDataSP;
	}

	public HashMap<String, TrackingDataEntry> getTrackingDataMP() {
		if (trackingDataMP == null) {
			try {
				trackingDataMP = main.dataManager.getTracerConfig().getMultiPlayerConfig().getTrackingData();
			} catch (NullPointerException e) {
				trackingDataMP = null;
			}
		}
		return trackingDataMP;
	}

	//C:\Users\Admin\Documents\The_Dark_Jumper_Cannon_Tracer
	public void save() {
		if (configPathGNS.get().equals("")) {
			return;
		}

		Preferences prefs = Preferences.userNodeForPackage(DataManager.class);
		prefs.put("TracerConfigPath", configPathGNS.get());
		prefs.put("TracerUpdatePath", updatePathGNS.get());

		try {
			FileWriter out = new FileWriter(configPathGNS.get());
			ObjectMapper mapper = new ObjectMapper()
					.enable(SerializationFeature.INDENT_OUTPUT)
					.disable(MapperFeature.AUTO_DETECT_CREATORS,
							MapperFeature.AUTO_DETECT_FIELDS,
							MapperFeature.AUTO_DETECT_GETTERS,
							MapperFeature.AUTO_DETECT_IS_GETTERS);
			String jsonString = mapper.writeValueAsString(tracerConfig);

			out.write(jsonString);
			out.close();
		} catch (Exception e) {
			System.out.println("thrown error while saving");
			e.printStackTrace();
		}
	}

	public void load() {
		trackingDataSP = null;
		trackingDataMP = null;

		Preferences prefs = Preferences.userNodeForPackage(DataManager.class);
		configPathGNS.set(prefs.get("TracerConfigPath", ""));
		updatePathGNS.set(prefs.get("TracerUpdatePath", ""));

		try {
			List<String> lines = Files.readAllLines(Paths.get(configPathGNS.get()));
			StringBuilder jsonBuilder = new StringBuilder();
			lines.forEach(line -> jsonBuilder.append(line).append("\n"));
			String json = jsonBuilder.toString();
			ObjectMapper mapper = new ObjectMapper().enable(
					MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES,
					MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS,
					MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
			this.tracerConfig = mapper.readValue(json, TracerConfig.class);

			main.moduleManager.reloadConfig();
			main.hotkeyManager.reloadConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}