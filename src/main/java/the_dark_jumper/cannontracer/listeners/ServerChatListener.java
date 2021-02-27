package the_dark_jumper.cannontracer.listeners;

import java.util.HashMap;
import jumpercommons.SimpleLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.modules.ModuleManager;
import the_dark_jumper.cannontracer.tracking.SingleTickMoveData;
import the_dark_jumper.cannontracer.util.ChatUtils;

public class ServerChatListener {
	public final Main main;
	public boolean isRegistered = false;
	public boolean debugPrint = false;

	public ServerChatListener(Main main) {
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void playerChatEvent(ClientChatEvent event) {
		if(handleChatMessageSent(event.getMessage())){
			event.setCanceled(true);
		}
	}

	/**
	 * @return true when the event should be cancelled
	 */
	public static boolean handleChatMessageSent(String message){
		boolean shouldCancel = false;
		for (ChatCommands command : ChatCommands.values()) {
			if (command.isCommand(message)) {
				shouldCancel |= command.handle(message);
			}
		}
		return shouldCancel;
	}

	@SubscribeEvent
	public void chatEvent(ClientChatReceivedEvent event) {
		String text = event.getMessage().getFormattedText();

		if (main.moduleManager.state == ModuleManager.State.MULTIPLAYER) {
			if (text.startsWith("[JumperCannonTracer]")) {
				if (!debugPrint) {
					event.setCanceled(true);
				}
				String message = text.substring(20);
				if (message.equals("[SettingsRequest]")) {
					isRegistered = true;
					sendConfigMessage();
				} else {
					String[] data = message.split("\\|");
					if (main.dataManager.getTrackingDataMP().containsKey(data[0])) {
						handleTracingData(data);
					}
				}
			} else if(text.startsWith("[TestCannonData]")) {
				String data = text.substring("[TestCannonData]".length());
				main.guiManager.testCannonGUI.open(data);
				if(!debugPrint){
					event.setCanceled(true);
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
			if (moveData.isSameData(pos1, pos2)) {
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
		StringBuilder message = new StringBuilder("[JumperCannonTracer][Config]logIDs=");
		message.append(main.multiPlayerSettings.bLogGNS.get());
		message.append("|range=");
		message.append(main.dataManager.getTracerConfig().getMultiPlayerConfig().getMaxRange());
		for (String key : main.dataManager.getTrackingDataMP().keySet()) {
			if (!main.dataManager.getTrackingDataMP().get(key).isRender()) {
				continue;
			}
			message.append("|[entity]=").append(key).append(";").append(main.dataManager.getTrackingDataMP().get(key).getTime());
		}
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player != null) {
			player.sendChatMessage(message.toString());
		}
	}

	public void requestTracingData() {
		if (!isRegistered) {
			ChatUtils.messagePlayer("", "can't pull data, not registered on this server. Try /tracer register", false);
			return;
		}
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player != null) {
			player.sendChatMessage("[JumperCannonTracer][PullDataRequest]");
		}
	}
}
