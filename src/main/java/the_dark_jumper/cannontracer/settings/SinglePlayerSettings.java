package the_dark_jumper.cannontracer.settings;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.tracking.SingleTickMoveData;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class SinglePlayerSettings {
	public final Main main;
	
	private int mode = 0;
	public GetterAndSetter<Integer> modeGNS = new GetterAndSetter<>(this::getMode, this::setMode);
	public GetterAndSetter<Integer> renderTickGNS = new GetterAndSetter<>(0);
	public GetterAndSetter<Boolean> xRayTraceGNS = new GetterAndSetter<>(false);
	public GetterAndSetter<Boolean> renderBoxesGNS = new GetterAndSetter<>(false);
	private boolean renderMenu = false;
	public GetterAndSetter<Boolean> renderMenuGNS = new GetterAndSetter<>(this::getRenderMenu, this::setRenderMenu);
	public GetterAndSetter<Boolean> bLogGNS = new GetterAndSetter<>(false);
	
	public SinglePlayerSettings(Main main) {
		this.main = main;
	}
	
	private int getMode() {
		return this.mode;
	}
	private void setMode(int mode) {
		this.mode = mode;
		if(mode != 1) {
			main.entityTracker.tracingHistory.clear();
			main.entityTracker.lastSecond.clear();
		}
	}
	
	private boolean getRenderMenu() {
		return this.renderMenu;
	}
	private void setRenderMenu(boolean renderMenu) {
		if(renderMenu) {
			main.guiManager.configGUI.generateSingleplayerScreenComponents();
			Minecraft.getInstance().displayGuiScreen(main.guiManager.configGUI);
		}else{
			main.guiManager.configGUI.onClose();
			main.dataManager.save();
		}
		this.renderMenu = renderMenu;
	}
	
	public void loadLastSeconds(boolean b) {
		if(!b) {
			return;
		}
		if(mode != 2) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
		for(SingleTickMoveData moveData : main.entityTracker.lastSecond) {
			SingleTickMoveData copiedMoveData = moveData.copy();
			copiedMoveData.tickOffset = main.entityTracker.currentTick - main.moduleManager.getMaxDisplayTickSP();
			main.entityTracker.tracingHistory.add(copiedMoveData);
		}
	}
	
	public void clearHistory(boolean b) {
		if(!b) {
			return;
		}
		if(mode != 1 && mode != 2) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
	}

	public void showFirstTick(boolean b) {
		int first = Integer.MAX_VALUE;
		for (SingleTickMoveData singleTickMoveData : main.entityTracker.tracingHistory) {
			for (HashMap<Integer, Boolean> tickData : singleTickMoveData.tickData.values()) {
				int minTick = tickData.keySet().stream().min(Integer::compareTo).orElse(Integer.MAX_VALUE);
				if(minTick < first){
					first = minTick;
				}
			}
		}
		if(first < main.moduleManager.getMaxDisplayTickMP()){
			renderTickGNS.set(first);
		}
	}

	public void showLastTick(boolean b){
		int last = Integer.MIN_VALUE;
		for (SingleTickMoveData singleTickMoveData : main.entityTracker.tracingHistory) {
			for (HashMap<Integer, Boolean> tickData : singleTickMoveData.tickData.values()) {
				int maxTick = tickData.keySet().stream().max(Integer::compareTo).orElse(Integer.MIN_VALUE);
				if(maxTick > last){
					last = maxTick;
				}
			}
		}
		if(last >= 0){
			renderTickGNS.set(last);
		}
	}
}
