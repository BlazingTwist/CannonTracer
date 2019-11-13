package the_dark_jumper.cannontracer.util;

public class KeybindData {
	public GetterAndSetter<Boolean> triggerGNS;
	public GetterAndSetter<Integer> keycodeGNS;
	
	public KeybindData(){
		triggerGNS = new GetterAndSetter<Boolean>(true);
		keycodeGNS = new GetterAndSetter<Integer>(1);
	}
	
	public KeybindData(GetterAndSetter<Boolean> triggerStateGNS, GetterAndSetter<Integer> keycodeGNS) {
		this.triggerGNS = triggerStateGNS;
		this.keycodeGNS = keycodeGNS;
	}
	
	public KeybindData(boolean triggerState, int keycode) {
		triggerGNS = new GetterAndSetter<Boolean>(triggerState);
		keycodeGNS = new GetterAndSetter<Integer>(keycode);
	}
}
