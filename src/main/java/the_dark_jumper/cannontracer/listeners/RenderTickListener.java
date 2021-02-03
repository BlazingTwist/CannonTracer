package the_dark_jumper.cannontracer.listeners;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_dark_jumper.cannontracer.Main;

public class RenderTickListener {
	public final Main main;

	public RenderTickListener(Main main) {
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {
		main.guiManager.renderGUIs();
	}
}