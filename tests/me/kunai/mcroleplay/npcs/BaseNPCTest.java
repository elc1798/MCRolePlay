package me.kunai.mcroleplay.npcs;

import com.google.gson.JsonDeserializer;
import me.kunai.mcroleplay.utils.JSONReader;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BaseNPCTest {

    @Test
    public void checkDeserializeBaseNPC() throws Exception {
        String npcJSON = "{ \"name\": \"Bannon\", " +
                "\"spawnLocation\": [69, 420, 1337], " +
                "\"voiceLines\": {" +
                "\"damnation\": \"What in damnation have you done?!\"," +
                "\"arse\": \"By Innocence's golden muscular arse...\"}}";

        HashMap<Type, JsonDeserializer> customDeserializers = new HashMap<>();
        customDeserializers.put(BaseNPC.class, new BaseNPCDeserializer());
        BaseNPC bannon = JSONReader.fromString(npcJSON, BaseNPC.class, customDeserializers);

        assertTrue(bannon.getName().equals("Bannon"));
        assertArrayEquals(new double[]{69, 420, 1337}, bannon.getSpawnLocation());
        assertEquals(2, bannon.getVoiceLines().size());
        assertTrue(bannon.getVoiceLines().get("damnation").equals("What in damnation have you done?!"));
        assertTrue(bannon.getVoiceLines().get("arse").equals("By Innocence's golden muscular arse..."));
    }
}
