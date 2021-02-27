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

		/*Minecraft minecraft = Minecraft.getInstance();
		Queue<SingleTickMoveData> tracingHistory = main.entityTracker.tracingHistory;
		if(main.moduleManager.state == ModuleManager.State.MENU || tracingHistory == null || tracingHistory.size() <= 0 || minecraft.player == null){
			return;
		}
		boolean isSinglePlayer = (main.moduleManager.state == ModuleManager.State.SINGLEPLAYER);
		EntityRendererManager rendererManager = minecraft.getRenderManager();
		IRenderTypeBuffer.Impl bufferSource = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		MatrixStack matrixStack = new MatrixStack();

		for (SingleTickMoveData moveData : tracingHistory) {
			for (String key : moveData.tickData.keySet()) {
				TrackingDataEntry trackData = isSinglePlayer ? main.dataManager.getTrackingDataSP().get(key) : main.dataManager.getTrackingDataMP().get(key);
				if(trackData != null && trackData.isRender()){ // TODO check for axis render flag
					moveData.renderAxisText(rendererManager, matrixStack, bufferSource);
				}
			}
		}*/
	}
}