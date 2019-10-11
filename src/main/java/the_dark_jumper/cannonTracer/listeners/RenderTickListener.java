package the_dark_jumper.cannonTracer.listeners;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_dark_jumper.cannonTracer.Main;

public class RenderTickListener {
	public final Main main;
	
	public RenderTickListener(Main main) {
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {
		if(Minecraft.getInstance().currentScreen != null) {
			return;
		}
		main.guiManager.renderGUIs();
	}
}