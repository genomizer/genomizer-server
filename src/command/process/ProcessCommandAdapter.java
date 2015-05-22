package command.process;
/**
 * File:        ProcessCommandAdapter.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ProcessCommandAdapter implements JsonDeserializer<ProcessCommand> {

    private static final String TYPE = "type";
    private static final Map<String, Class<?>> CLASS_MAP = new HashMap<>();

    static {
        CLASS_MAP.put("bowtie", BowtieProcessCommand.class);
        CLASS_MAP.put("ratio", RatioProcessCommand.class);
    }

    @Override
    public ProcessCommand deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonObject.get(TYPE);
        String className = jsonPrimitive.getAsString();

        Class<?> commandClass = CLASS_MAP.get(className);
        if (commandClass == null) {
            throw new JsonParseException("Unrecognized class: " + className);
        }
        return jsonDeserializationContext.deserialize(jsonElement, commandClass);
    }

    public static Gson getProcessCommandGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ProcessCommand.class, new ProcessCommandAdapter());
        return builder.create();
    }
}
