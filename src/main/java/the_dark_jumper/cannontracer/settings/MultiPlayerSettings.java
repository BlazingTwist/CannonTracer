package the_dark_jumper.cannontracer.settings;

import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.client.Minecraft;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.tracking.SingleTickMoveData;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class MultiPlayerSettings {
	public final Main main;
	
	public GetterAndSetter<Integer> renderTickGNS = new GetterAndSetter<>(0);
	public GetterAndSetter<Boolean> xRayTraceGNS = new GetterAndSetter<>(false);
	private boolean renderMenu = false;
	public GetterAndSetter<Boolean> renderMenuGNS = new GetterAndSetter<>(this::getRenderMenu, this::setRenderMenu);
	public GetterAndSetter<Boolean> bLogGNS = new GetterAndSetter<>(false);
	
	public MultiPlayerSettings(Main main) {
		this.main = main;
	}
	
	private boolean getRenderMenu() {
		return this.renderMenu;
	}
	private void setRenderMenu(boolean renderMenu) {
		if(renderMenu) {
			main.guiManager.configGUI.generateMultiplayerScreenComponents();
			Minecraft.getInstance().displayGuiScreen(main.guiManager.configGUI);
		}else{
			main.guiManager.configGUI.onClose();
			main.dataManager.save();
			main.serverChatListener.sendConfigMessage();
		}
		this.renderMenu = renderMenu;
	}
	
	public void pullData(boolean b) {
		if(!b) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
		main.serverChatListener.requestTracingData();
	}
	
	public void clearData(boolean b) {
		if(!b) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
	}

	public void findNextDespawnTick(boolean b){
		if(!b){
			return;
		}

		HashSet<Integer> despawnTicks = new HashSet<>();
		for(SingleTickMoveData moveData : main.entityTracker.tracingHistory){
			for(HashMap<Integer, Boolean> tickData : moveData.tickData.values()){
				for(int key : tickData.keySet()){
					if(tickData.get(key)){
						despawnTicks.add(key);
					}
				}
			}
		}

		int startTick = renderTickGNS.get();
		int max = main.moduleManager.getMaxDisplayTickMP();
		int currentTick = startTick;
		while(((currentTick = (currentTick + 1) % max)) != startTick){
			if(despawnTicks.contains(currentTick)){
				renderTickGNS.set(currentTick);
				return;
			}
		}

		//no next tick found
	}
}
