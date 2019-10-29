package the_dark_jumper.cannonTracer;

import java.io.File;
import java.io.IOException;

import net.minecraftforge.fml.common.Mod;
import the_dark_jumper.cannonTracer.configSaving.DataManager;
import the_dark_jumper.cannonTracer.gui.GuiManager;
import the_dark_jumper.cannonTracer.listeners.ClientConnectsToServerListener;
import the_dark_jumper.cannonTracer.listeners.KeyPressListener;
import the_dark_jumper.cannonTracer.listeners.RenderTickListener;
import the_dark_jumper.cannonTracer.listeners.ServerChatListener;
import the_dark_jumper.cannonTracer.modules.ModuleManager;
import the_dark_jumper.cannonTracer.settings.KeybindManagerMP;
import the_dark_jumper.cannonTracer.settings.KeybindManagerSP;
import the_dark_jumper.cannonTracer.settings.MultiPlayerSettings;
import the_dark_jumper.cannonTracer.settings.SinglePlayerSettings;
import the_dark_jumper.cannonTracer.util.KeyLibrary;
import the_dark_jumper.cannonTracer.util.TrackingData;

@Mod("cannontracer")
public class Main {
	//some sort of managers
	public final DataManager dataManager;
	public final KeybindManagerSP keybindManagerSP;
	public final KeybindManagerMP keybindManagerMP;
	public final ModuleManager moduleManager;
	public final GuiManager guiManager;
	
	//settings
	public final MultiPlayerSettings multiPlayerSettings;
	public final SinglePlayerSettings singlePlayerSettings;
	
	//listeners
	public final EntityTracker entityTracker;
	public final ClientConnectsToServerListener clientConnectsToServerListener;
	public final KeyPressListener keyPressListener;
	public final RenderTickListener renderTickListener;
	public final ServerChatListener serverChatListener;
	
	//keylib
	public final KeyLibrary keyLibrary;
	
	private static Main instance;
	public static Main getInstance() {return instance;}
	
	public Main() {
		instance = this;
		keybindManagerSP = new KeybindManagerSP(this);
		keybindManagerMP = new KeybindManagerMP(this);
		multiPlayerSettings = new MultiPlayerSettings(this);
		singlePlayerSettings = new SinglePlayerSettings(this);
		moduleManager = new ModuleManager(this);
		dataManager = new DataManager(this);
		guiManager = new GuiManager(this);
		entityTracker = new EntityTracker(this);
		clientConnectsToServerListener = new ClientConnectsToServerListener(this);
		keyPressListener = new KeyPressListener(this);
		renderTickListener = new RenderTickListener(this);
		serverChatListener = new ServerChatListener(this);
		keyLibrary = new KeyLibrary();
		
		checkConfig();
	}
	
	private void checkConfig() {
		File file = new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\The_Dark_Jumper_Cannon_Tracer\\tracer.config");
		if(!file.isFile()) {
			entityTracker.observedEntityIDSP.put("TNTEntity", new TrackingData(5, 1.0f, 255, 0, 0, 255, true));
			entityTracker.observedEntityIDSP.put("FallingBlockEntity", new TrackingData(5, 1.0f, 0, 255, 0, 255, true));
			entityTracker.observedEntityIDMP.put("CraftTNTPrimed", new TrackingData(5, 1.0f, 255, 0, 0, 255, true));
			entityTracker.observedEntityIDMP.put("CraftFallingBlock", new TrackingData(5, 1.0f, 0, 255, 0, 255, true));
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}catch(IOException e) {
				e.printStackTrace();
			}
			dataManager.Save();
		}
		dataManager.Load();
	}
}
