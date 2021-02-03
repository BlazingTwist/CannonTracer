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
		if (renderMenu) {
			main.guiManager.configGUI.generateMultiplayerScreenComponents();
			Minecraft.getInstance().displayGuiScreen(main.guiManager.configGUI);
		} else {
			main.guiManager.configGUI.onClose();
			main.dataManager.save();
			main.serverChatListener.sendConfigMessage();
		}
		this.renderMenu = renderMenu;
	}

	public void pullData(boolean b) {
		if (!b) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
		main.serverChatListener.requestTracingData();
	}

	public void clearData(boolean b) {
		if (!b) {
			return;
		}
		main.entityTracker.tracingHistory.clear();
	}

	public void findNextDespawnTick(boolean b) {
		if (!b) {
			return;
		}

		HashSet<Integer> despawnTicks = getDespawnTicks();

		int startTick = renderTickGNS.get();
		int max = main.moduleManager.getMaxDisplayTickMP();
		int currentTick = startTick;
		while (((currentTick = (currentTick + 1) % max)) != startTick) {
			if (despawnTicks.contains(currentTick)) {
				renderTickGNS.set(currentTick);
				return;
			}
		}

		//no next tick found
	}

	public void findPreviousDespawnTick(boolean b) {
		if (!b) {
			return;
		}

		HashSet<Integer> despawnTicks = getDespawnTicks();

		int startTick = renderTickGNS.get();
		int max = main.moduleManager.getMaxDisplayTickMP();
		int currentTick = startTick;
		while (((currentTick = (currentTick - 1) % max)) != startTick) {
			if (despawnTicks.contains(currentTick)) {
				renderTickGNS.set(currentTick);
				return;
			}
		}

		//no previous tick found
	}

	public HashSet<Integer> getDespawnTicks() {
		HashSet<Integer> result = new HashSet<>();
		for (SingleTickMoveData moveData : main.entityTracker.tracingHistory) {
			for (HashMap<Integer, Boolean> tickData : moveData.tickData.values()) {
				for (int key : tickData.keySet()) {
					if (tickData.get(key)) {
						result.add(key);
					}
				}
			}
		}
		return result;
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
