package me.kunai.mcroleplay.game;

import com.google.gson.JsonDeserializer;
import me.kunai.mcroleplay.npcs.BaseNPC;
import me.kunai.mcroleplay.npcs.BaseNPCDeserializer;
import me.kunai.mcroleplay.utils.JSONReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    private static final String JSON_STR = "{\n" +
            "        \"name\": \"root\",\n" +
            "        \"npcs\": [],\n" +
            "        \"substates\": [\n" +
            "            {\n" +
            "                \"name\": \"prologue\",\n" +
            "                \"npcs\": [\n" +
            "                    {\n" +
            "                        \"name\": \"Jordan\",\n" +
            "                        \"spawnLocation\": [ 1000, 800, 40 ],\n" +
            "                        \"voiceLines\": {\n" +
            "                            \"meeting\": \"Ah strangers, welcome to the town of Elmwood. Pray tell, where are you coming from?\",\n" +
            "                            \"expositionOkten\": \"There was a traveling mage by the name of Okten. He wasn't a noble, but he was quite skilled. He's pretty famous around these parts. And he suddenly mysteriously vanished about... 3 weeks past? There hasn't been any sign of him in the area since...\"\n" +
            "                        }\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"substates\": []\n" +
            "            }\n" +
            "        ]\n" +
            "    }";

    private GameState rootState;

    @BeforeEach
    void setUp() {
        HashMap<Type, JsonDeserializer> customDeserializers = new HashMap<>();
        customDeserializers.put(BaseNPC.class, new BaseNPCDeserializer());
        try {
            rootState = JSONReader.fromString(JSON_STR, GameState.class, customDeserializers);
        } catch (Exception e) {
            rootState = null;
        }
    }

    @Test
    public void checkDeserializeGameState() throws Exception {
        assertNotNull(rootState);
        assertTrue(rootState.getName().equals("root"));
        assertEquals(0, rootState.getNpcs().length);
        assertEquals(1, rootState.getSubstates().length);
        assertTrue(rootState.getSubstates()[0].getName().equals("prologue"));
    }

    @Test
    public void checkGetSubState() throws Exception {
        assertEquals(rootState, rootState.getSubState(null));

        GameState prologueState = rootState.getSubState("prologue");
        assertNotNull(rootState.getSubState("prologue"));
        assertTrue(prologueState.getName().equals("prologue"));
        assertEquals(1, prologueState.getNpcs().length);
        assertEquals(0, prologueState.getSubstates().length);
    }

    @Test
    public void checkDeserializedNPC() throws Exception {
        GameState prologueState = rootState.getSubState("prologue");
        BaseNPC jordan = prologueState.getNpcs()[0];
        assertTrue(jordan.getName().equals("Jordan"));
        assertArrayEquals(new double[]{1000, 800,40}, jordan.getSpawnLocation());
        assertEquals(2, jordan.getVoiceLines().size());
        assertTrue(jordan.getVoiceLines().get("meeting").equals("Ah strangers, welcome to the town of Elmwood. Pray tell, where are you coming from?"));
        assertTrue(jordan.getVoiceLines().get("expositionOkten").equals("There was a traveling mage by the name of Okten. He wasn't a noble, but he was quite skilled. He's pretty famous around these parts. And he suddenly mysteriously vanished about... 3 weeks past? There hasn't been any sign of him in the area since..."));
    }
}
