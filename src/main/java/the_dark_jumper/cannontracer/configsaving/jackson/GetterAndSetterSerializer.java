package the_dark_jumper.cannontracer.configsaving.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class GetterAndSetterSerializer extends JsonSerializer<GetterAndSetter<?>> {

	@Override
	public void serialize(GetterAndSetter<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (value == null) {
			gen.writeNumber(0);
		} else {
			gen.writeObject(value.get());
		}
	}
}
