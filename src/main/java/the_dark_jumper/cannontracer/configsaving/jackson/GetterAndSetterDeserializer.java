package the_dark_jumper.cannontracer.configsaving.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.io.IOException;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class GetterAndSetterDeserializer extends JsonDeserializer<GetterAndSetter<?>> implements ContextualDeserializer {
	private JavaType valueType;

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
		JavaType GNSType;
		if(property != null){
			GNSType = property.getType();
		}else{
			GNSType = ctxt.getContextualType();
		}
		if(GNSType == null){
			throw new NullPointerException("GNS type is missing!");
		}
		// sometimes GNSType is Other<GetterAndSetter<Value>> -> unwrap down to GNS type
		while(!GNSType.isTypeOrSubTypeOf(GetterAndSetter.class) && GNSType.containedTypeCount() > 0) {
			GNSType = GNSType.containedType(0);
		}
		GetterAndSetterDeserializer deserializer = new GetterAndSetterDeserializer();
		deserializer.valueType = GNSType.containedType(0);
		return deserializer;
	}

	@Override
	public GetterAndSetter<?> deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		Object o = context.readValue(parser, valueType);
		return new GetterAndSetter<>(o);
	}
}
