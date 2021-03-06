package the_dark_jumper.cannontracer.listeners;

import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedOutEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.Update;
import the_dark_jumper.cannontracer.modules.ModuleManager;

//this must be the pinnacle of my quest to create the longest classnames
public class ClientConnectsToServerListener {
	public final Main main;
	private Timer timer;

	public ClientConnectsToServerListener(Main main) {
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void playerConnects(LoggedInEvent event) {
		queueWorldCheck();
	}

	@SubscribeEvent
	public void playerConnects(LoggedOutEvent event) {
		queueWorldCheck();
	}

	private void queueWorldCheck() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				checkWorldStatus();
			}
		}, 1000);
	}

	private void checkWorldStatus() {
		main.serverChatListener.setRegistered(false);
		main.entityTracker.tracingHistory.clear();
		main.entityTracker.lastSecond.clear();
		main.entityTracker.currentTick = 0;
		if (Minecraft.getInstance().world == null) {
			main.moduleManager.state = ModuleManager.State.MENU;
			return;
		}
		if (Minecraft.getInstance().isSingleplayer()) {
			main.moduleManager.state = ModuleManager.State.SINGLEPLAYER;
		} else {
			main.moduleManager.state = ModuleManager.State.MULTIPLAYER;
		}
		Update.sendUpdateMessageIfNeeded();
	}
}
