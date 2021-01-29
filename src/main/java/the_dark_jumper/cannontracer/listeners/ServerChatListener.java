package the_dark_jumper.cannontracer.listeners;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.modules.ModuleManager;
import the_dark_jumper.cannontracer.tracking.SingleTickMoveData;
import the_dark_jumper.cannontracer.util.SimpleLocation;

public class ServerChatListener {
	public final Main main;
	public boolean isRegistered = false;

	public ServerChatListener(Main main) {
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void chatEvent(ClientChatReceivedEvent event) {
		if (main.moduleManager.state != ModuleManager.State.MULTIPLAYER) {
			return;
		}
		String text = event.getMessage().getFormattedText();
		if (text.length() >= 21) {
			if (text.startsWith("[JumperCannonTracer]")) {
				event.setCanceled(true);
				String message = text.substring(20);
				if (message.equals("[SettingsRequest]")) {
					isRegistered = true;
					sendConfigMessage();
					return;
				}
				String[] data = message.split("\\|");
				if (main.dataManager.getTrackingDataMP().containsKey(data[0])) {
					handleTracingData(data);
				}
			}
		}
	}

	public void handleTracingData(String[] data) {
		String entityName = data[0];
		int creationTick = Integer.parseInt(data[1]);
		SimpleLocation previous = null;
		SimpleLocation current;
		for (int i = 2; i < data.length; i++) {
			String[] coords = data[i].split(",");
			if (coords.length != 3) {
				System.out.println("huh, that's weird");
				continue;
			}
			current = new SimpleLocation(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));
			if (previous != null) {
				putDataInHistory(entityName, creationTick + i - 2, previous.copy(), current.copy(), i >= (data.length - 1));
			}
			previous = current;
		}

	}

	public void putDataInHistory(String entityName, int tick, SimpleLocation pos1, SimpleLocation pos2, boolean isLastTick) {
		for (SingleTickMoveData moveData : main.entityTracker.tracingHistory) {
			if (!moveData.isNewData(pos1, pos2)) {
				//is old
				if (!moveData.tickData.containsKey(entityName)) {
					moveData.tickData.put(entityName, new HashMap<>());
				}
				moveData.tickData.get(entityName).put(tick, isLastTick);
				return;
			}
		}
		//is new
		SingleTickMoveData moveData = new SingleTickMoveData(main.entityTracker, pos1, pos2);
		moveData.tickData.put(entityName, new HashMap<>());
		moveData.tickData.get(entityName).put(tick, isLastTick);
		main.entityTracker.tracingHistory.add(moveData);
	}

	public void sendConfigMessage() {
		if (!isRegistered) {
			return;
		}
		//target message: [JumperCannonTracer][Config]"key"="value"|"key"="value;value2;value3"|...
		String message = "[JumperCannonTracer][Config]logIDs=";
		message += Boolean.toString(main.multiPlayerSettings.bLogGNS.get());
		for (String key : main.dataManager.getTrackingDataMP().keySet()) {
			if (!main.dataManager.getTrackingDataMP().get(key).isRender()) {
				continue;
			}
			message += ("|[entity]=" + key + ";" + main.dataManager.getTrackingDataMP().get(key).getTime());
		}
		Minecraft.getInstance().player.sendChatMessage(message);
	}

	public void requestTracingData() {
		if (!isRegistered) {
			return;
		}
		Minecraft.getInstance().player.sendChatMessage("[JumperCannonTracer][PullDataRequest]");
	}
}
