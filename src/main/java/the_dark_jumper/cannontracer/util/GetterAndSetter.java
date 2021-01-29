package the_dark_jumper.cannontracer.util;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.function.Consumer;
import java.util.function.Supplier;
import the_dark_jumper.cannontracer.configsaving.jackson.GetterAndSetterDeserializer;
import the_dark_jumper.cannontracer.configsaving.jackson.GetterAndSetterSerializer;

@JsonDeserialize(using = GetterAndSetterDeserializer.class)
@JsonSerialize(using = GetterAndSetterSerializer.class)
public class GetterAndSetter<T> {
	public Supplier<T> getter;
	public Consumer<T> setter;

	private T storedValue = null; //optional
	private boolean useStoredValue = false;
	
	public GetterAndSetter(Supplier<T> getter, Consumer<T> setter){
		this.getter=getter;
		this.setter=setter;
	}
	
	public GetterAndSetter(T initValue) {
		this.storedValue = initValue;
		this.useStoredValue = true;
	}
	
	public T get() {
		if(useStoredValue) {
			return storedValue;
		}
		return getter.get();
	}
	
	public void set(T t) {
		if(useStoredValue) {
			storedValue = t;
		}else {
			setter.accept(t);
		}
	}

	@Override
	public String toString() {
		if(useStoredValue){
			return "GetterAndSetter{" +
					"storedValue=" + storedValue +
					'}';
		}
		return "GetterAndSetter{" +
				"getterValue=" + getter.get() +
				'}';
	}
}
