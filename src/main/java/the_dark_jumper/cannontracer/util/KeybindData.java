package the_dark_jumper.cannontracer.util;

import jumpercommons.GetterAndSetter;

public class KeybindData {
	public GetterAndSetter<Boolean> triggerGNS;
	public GetterAndSetter<Integer> keycodeGNS;
	
	public KeybindData(){
		triggerGNS = new GetterAndSetter<>(true);
		keycodeGNS = new GetterAndSetter<>(1);
	}

	public KeybindData(boolean triggerState, int keycode) {
		triggerGNS = new GetterAndSetter<>(triggerState);
		keycodeGNS = new GetterAndSetter<>(keycode);
	}
}
