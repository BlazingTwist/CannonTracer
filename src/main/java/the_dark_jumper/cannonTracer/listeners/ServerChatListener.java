package the_dark_jumper.cannonTracer.listeners;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.modules.ModuleManager;

public class ServerChatListener {
	public final Main main;
	public boolean isRegistered = false;
	
	public ServerChatListener(Main main) {
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void chatEvent(ClientChatReceivedEvent event) {
		if(main.moduleManager.state != ModuleManager.State.MULTIPLAYER) {
			return;
		}
		String text = event.getMessage().getFormattedText();
		if(text.length() < 21) {
			return;
		}else if(text.substring(0, 20).equals("[JumperCannonTracer]")) {
			String message = text.substring(20);
			if(message.equals("[SettingsRequest]")) {
				event.setCanceled(true);
				isRegistered = true;
				sendConfigMessage();
				return;
			}
		}
	}
	
	public void sendConfigMessage() {
		if(!isRegistered) {
			return;
		}
		//target message: [JumperCannonTracer][Config]"key"="value"|"key"="value;value2;value3"|...
		String message = "[JumperCannonTracer][Config]logIDs=";
		message += Boolean.toString(main.multiPlayerSettings.bLogGNS.getter.get());
		for(String key : main.entityTracker.observedEntityIDMP.keySet()) {
			message += ("|[entity]=" + key + ";" + Float.toString(main.entityTracker.observedEntityIDMP.get(key).timeGNS.getter.get()));
		}
		Minecraft.getInstance().player.sendChatMessage(message);
	}
	
	public void requestTracingData() {
		if(!isRegistered) {
			return;
		}
		Minecraft.getInstance().player.sendChatMessage("[JumperCannonTracer][PullDataRequest]");
	}
}
