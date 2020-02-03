package deserializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.goots.jackson.deserializer.JacksonPostHookDeserializer


class SchemaUtils {
    /**
     * Converts JSON to proper object of the provided class.
     */
    static <T> T dataToObject(Class<T> clazz, String data) {
        ObjectMapper mapper = new ObjectMapper()
        mapper.registerModule( JacksonPostHookDeserializer.getSimpleModule() );
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE)
        return mapper.readValue(data, clazz)
    }

    /**
     * Serializes provided object to JSON using Jackson.
     */
    static String objectToString(Object data) {
        ObjectMapper mapper = new ObjectMapper()
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE)
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)
    }
}
