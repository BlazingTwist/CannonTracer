package the_dark_jumper.cannonTracer.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KeybindAccessors{
	public GetterAndSetter accessors[] = new GetterAndSetter[2];
	
	public KeybindAccessors(GetterAndSetter accessor1, GetterAndSetter accessor2) {
		this.accessors[0] = accessor1;
		this.accessors[1] = accessor2;
	}
	
	public KeybindAccessors(Supplier<Integer> getter1, Consumer<Integer> setter1, Supplier<Integer> getter2, Consumer<Integer> setter2){
		this.accessors[0] = new GetterAndSetter(getter1, setter1);
		this.accessors[1] = new GetterAndSetter(getter2, setter2);
	}
}
