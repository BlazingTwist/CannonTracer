package the_dark_jumper.cannonTracer.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GetterAndSetter_INT {
	public Consumer<Integer> setter;
	public Supplier<Integer> getter;
	
	public GetterAndSetter_INT(Supplier<Integer> getter, Consumer<Integer> setter){
		this.setter=setter;
		this.getter=getter;
	}
}
