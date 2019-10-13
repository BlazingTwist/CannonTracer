package the_dark_jumper.cannonTracer.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GetterAndSetter<T> {
	public Consumer<T> setter;
	public Supplier<T> getter;
	
	public GetterAndSetter(Supplier<T> getter, Consumer<T> setter){
		this.setter=setter;
		this.getter=getter;
	}
}
