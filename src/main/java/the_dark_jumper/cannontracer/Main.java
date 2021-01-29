package the_dark_jumper.cannontracer;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import net.minecraftforge.fml.common.Mod;
import the_dark_jumper.cannontracer.configsaving.DataManager;
import the_dark_jumper.cannontracer.gui.GuiManager;
import the_dark_jumper.cannontracer.hotkey.HotkeyManager;
import the_dark_jumper.cannontracer.listeners.ClientConnectsToServerListener;
import the_dark_jumper.cannontracer.listeners.KeyPressListener;
import the_dark_jumper.cannontracer.listeners.RenderTickListener;
import the_dark_jumper.cannontracer.listeners.ServerChatListener;
import the_dark_jumper.cannontracer.modules.ModuleManager;
import the_dark_jumper.cannontracer.settings.MultiPlayerSettings;
import the_dark_jumper.cannontracer.settings.SinglePlayerSettings;
import the_dark_jumper.cannontracer.tracking.EntityTracker;

@Mod("cannontracer")
public class Main {
	//some sort of managers
	public final DataManager dataManager;
	public final ModuleManager moduleManager;
	public final GuiManager guiManager;
	public final HotkeyManager hotkeyManager;

	//settings
	public final MultiPlayerSettings multiPlayerSettings;
	public final SinglePlayerSettings singlePlayerSettings;

	//listeners
	public final EntityTracker entityTracker;
	public final ClientConnectsToServerListener clientConnectsToServerListener;
	public final KeyPressListener keyPressListener;
	public final RenderTickListener renderTickListener;
	public final ServerChatListener serverChatListener;

	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	public Main() {
		instance = this;

		dataManager = new DataManager(this);
		guiManager = new GuiManager(this);
		hotkeyManager = new HotkeyManager(this);
		entityTracker = new EntityTracker(this);
		clientConnectsToServerListener = new ClientConnectsToServerListener(this);
		keyPressListener = new KeyPressListener(this);
		renderTickListener = new RenderTickListener(this);
		serverChatListener = new ServerChatListener(this);

		multiPlayerSettings = new MultiPlayerSettings(this);
		singlePlayerSettings = new SinglePlayerSettings(this);
		moduleManager = new ModuleManager(this);

		checkConfig();
	}

	private void checkConfig() {
		Preferences prefs = Preferences.userNodeForPackage(DataManager.class);
		String path = prefs.get("TracerConfigPath", null);
		if (path == null) {
			//no path stored, try default path?
			String defaultPath = "C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\The_Dark_Jumper_Cannon_Tracer\\tracer.config";
			dataManager.configPathGNS.set(defaultPath);
			path = defaultPath;
			String jarDefaultPath = "C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\.minecraft\\mods\\cannontracer.jar";
			dataManager.updatePathGNS.set(jarDefaultPath);
		}

		File file = new File(path);
		if (!file.isFile()) {
			System.out.println("WARNING: config file not found at path: " + path);
			dataManager.resetConfig();
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Unable to create config file at path: " + path);
				e.printStackTrace();
				//path probably failed because of wrong OS
				return;
			}
			dataManager.save();
		}
		dataManager.load();
	}
}
