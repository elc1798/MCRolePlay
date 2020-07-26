package me.kunai.mcroleplay.npcs;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BaseNPCDeserializer implements JsonDeserializer<BaseNPC> {

    private static final String[] REQUIRED_FIELDS = new String[]{
            "name",
            "spawnLocation",
            "voiceLines"
    };
    private static final String DEFAULT_WORLD = "world";

    @Override
    public BaseNPC deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject o = jsonElement.getAsJsonObject();

        // Validate required fields
        for (String s : REQUIRED_FIELDS) {
            if (!o.has(s)) {
                throw new JsonParseException(String.format("BaseNPC deserialization failed: Missing '%s'", s));
            }
        }

        // NPC Name
        String name = o.get("name").getAsString();

        // NPC spawnLocation
        JsonArray locArr = o.get("spawnLocation").getAsJsonArray();
        if (locArr.size() != 3) {
            throw new JsonParseException(String.format("BaseNPC deserialization failed: Got %d elements, expected 3", locArr.size()));
        }
        double[] spawnLoc = new double[]{
                locArr.get(0).getAsDouble(),
                locArr.get(1).getAsDouble(),
                locArr.get(2).getAsDouble()
        };

        // Voice line loading
        HashMap<String, String> voiceLines = new HashMap<>();
        JsonObject voiceJson = o.get("voiceLines").getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : voiceJson.entrySet()) {
            voiceLines.put(entry.getKey(), entry.getValue().getAsString());
        }

        // Check if world is set. Otherwise use default.
        String worldID = DEFAULT_WORLD;
        if (o.has("world")) {
            worldID = o.get("world").getAsString();
        }

        return new BaseNPC(name, spawnLoc, voiceLines, worldID);
    }
}
