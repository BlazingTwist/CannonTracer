package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import jumpercommons.GetterAndSetter;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.util.ChatUtils;

public class DataManager {
	public final Main main;

	public GetterAndSetter<String> configPathGNS = new GetterAndSetter<>("");
	public GetterAndSetter<String> updatePathGNS = new GetterAndSetter<>("");
	private TracerConfig tracerConfig = new TracerConfig();
	private HashMap<String, TrackingDataEntry> trackingDataSP = null;
	private HashMap<String, TrackingDataEntry> trackingDataMP = null;

	private final static String traceSaveDirectoryName = "traces";

	private ObjectMapper objectMapper = null;

	public DataManager(Main main) {
		this.main = main;
		Preferences prefs = Preferences.userNodeForPackage(DataManager.class);
		configPathGNS.set(prefs.get("TracerConfigPath", ""));
		updatePathGNS.set(prefs.get("TracerUpdatePath", ""));
	}

	private ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper()
					.enable(SerializationFeature.INDENT_OUTPUT)
					.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES,
							MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS,
							MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
					.disable(MapperFeature.AUTO_DETECT_CREATORS,
							MapperFeature.AUTO_DETECT_FIELDS,
							MapperFeature.AUTO_DETECT_GETTERS,
							MapperFeature.AUTO_DETECT_IS_GETTERS);
		}
		return objectMapper;
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

	private String getTraceSaveDirectory() {
		if (configPathGNS.get().equals("")) {
			return null;
		}

		return new File(configPathGNS.get()).getParentFile().getAbsolutePath() + File.separator + traceSaveDirectoryName;
	}

	public List<String> getTraceNames() {
		String basePath = getTraceSaveDirectory();

		if (basePath == null) {
			ChatUtils.messagePlayer("", "Can't list trace, configPath must be set!", false);
			return null;
		}

		File[] files = new File(basePath).listFiles();
		if (files == null) {
			return null;
		}
		return Arrays.stream(files)
				.filter(File::isFile)
				.map(File::getName)
				.collect(Collectors.toList());
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
			ObjectMapper mapper = getObjectMapper();
			String jsonString = mapper.writeValueAsString(tracerConfig);

			out.write(jsonString);
			out.close();
		} catch (Exception e) {
			System.out.println("thrown error while saving");
			e.printStackTrace();
		}
	}

	public void saveTrace(String traceName) {
		String traceDir = getTraceSaveDirectory();

		if (traceDir == null) {
			ChatUtils.messagePlayer("", "Can't save trace, configPath must be set!", false);
			return;
		}

		new File(traceDir).mkdirs();
		String fileName = traceDir + File.separator + traceName;
		try {
			ObjectMapper mapper = getObjectMapper();
			SavedTrace trace = new SavedTrace(main.entityTracker.tracingHistory);
			String jsonString = mapper.writeValueAsString(trace);

			FileWriter out = new FileWriter(fileName);
			out.write(jsonString);
			out.close();

			ChatUtils.messagePlayer("", "Trace saved at " + fileName, true);
		} catch (IOException e) {
			System.out.println("thrown error while saving trace");
			ChatUtils.messagePlayer("", "Unable to save trace - unknown error!", false);
			e.printStackTrace();
		}
	}

	public void deleteTrace(String traceName) {
		String traceDirectory = getTraceSaveDirectory();
		if (traceDirectory == null) {
			ChatUtils.messagePlayer("", "Can't delete trace, save directory not found.", false);
			return;
		}

		String fileName = traceDirectory + File.separator + traceName;
		File traceFile = new File(fileName);
		if (!traceFile.exists()) {
			ChatUtils.messagePlayer("", "File doesn't exist at path: " + fileName, false);
		} else {
			boolean success = traceFile.delete();
			if (success) {
				ChatUtils.messagePlayer("", "Successfully deleted trace", true);
			} else {
				ChatUtils.messagePlayer("", "Unable to delete trace, unknown error", false);
			}
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
			ObjectMapper mapper = getObjectMapper();
			this.tracerConfig = mapper.readValue(json, TracerConfig.class);
		} catch (Exception e) {
			System.err.println("Unable to parse config, resetting to default");
			e.printStackTrace();

			backupConfigFile();
			resetConfig();
			save();
		}
		reloadConfig();
	}

	public void loadTrace(List<String> traceNames) {
		if (configPathGNS.get().equals("")) {
			ChatUtils.messagePlayer("", "Can't open trace, configPath must be set!", false);
			return;
		}

		main.entityTracker.tracingHistory.clear();

		ObjectMapper mapper = getObjectMapper();

		String basePath = getTraceSaveDirectory() + File.separator;
		try {
			for (String traceName : traceNames) {
				String fileName = basePath + traceName;
				ChatUtils.messagePlayer("loading trace from " + fileName, "", true);

				List<String> lines = Files.readAllLines(Paths.get(fileName));
				StringBuilder jsonBuilder = new StringBuilder();
				lines.forEach(line -> jsonBuilder.append(line).append("\n"));
				String json = jsonBuilder.toString();

				SavedTrace trace = mapper.readValue(json, SavedTrace.class);
				main.entityTracker.tracingHistory.addAll(trace.getTraces());
			}

			ChatUtils.messagePlayer("", "traces finished loading.", true);
		} catch (IOException e) {
			System.out.println("thrown error while loading trace");
			ChatUtils.messagePlayer("", "Unable to open trace - unknown error!", false);
			e.printStackTrace();
		}
	}

	private void backupConfigFile() {
		try {
			File configFile = new File(configPathGNS.get());
			if (configFile.exists()) {
				File configFileBackup = new File(configPathGNS.get() + ".bac");
				Files.copy(configFile.toPath(), configFileBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			System.err.println("unable to backup config! Will be overwritten.");
		}
	}

	public void resetConfig() {
		this.tracerConfig = new TracerConfig();
		HashMap<String, TrackingDataEntry> trackingDataSP = getTrackingDataSP();
		HashMap<String, TrackingDataEntry> trackingDataMP = getTrackingDataMP();

		trackingDataSP.put(
				"TNTEntity",
				new TrackingDataEntry(true, 10, 3,
						new Color(255, 0, 0, 255)));
		trackingDataSP.put(
				"FallingBlockEntity",
				new TrackingDataEntry(true, 10, 3,
						new Color(0, 255, 0, 255)));

		trackingDataMP.put(
				"CraftTNTPrimed",
				new TrackingDataEntry(true, 10, 3,
						new Color(255, 0, 0, 255)));
		trackingDataMP.put(
				"CraftFallingBlock",
				new TrackingDataEntry(true, 10, 3,
						new Color(0, 255, 0, 255)));
	}

	public void reloadConfig() {
		main.moduleManager.reloadConfig();
		main.hotkeyManager.reloadConfig();
	}
}