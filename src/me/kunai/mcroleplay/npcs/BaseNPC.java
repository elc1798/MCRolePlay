package me.kunai.mcroleplay.npcs;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class BaseNPC {

    private static final String ENTITY_NAME_FORMAT = "[NPC] %s";

    // Name of the NPC. Must be unique for that game state
    private String name;
    // (x,y,z) coordinates to spawn the NPC
    private double[] spawnLocation;
    // Map(VoiceLineID -> VoiceLine)
    private HashMap<String, String> voiceLines;
    // Name of the world this NPC spawns in
    private String worldID;

    private EntityType entityType;
    private LivingEntity spawnedEntity;
    private UUID entityID;

    public BaseNPC(String name, double[] spawnLocation, HashMap<String, String> voiceLines, String worldID) {
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.voiceLines = voiceLines;
        this.worldID = worldID;

        this.spawnedEntity = null;
        this.entityType = EntityType.VILLAGER;
    }

    public String getEntityName() {
        return String.format(ENTITY_NAME_FORMAT, this.getName());
    }

    public String getEntityUUID() {
        return entityID.toString();
    }

    public void spawn(Plugin plugin) {
        // Assert that spawnLocation is valid
        if (spawnLocation == null || spawnLocation.length != 3) {
            return;
        }

        World world = plugin.getServer().getWorld(worldID);
        if (world == null) {
            return;
        }

        // If already spawned, despawn first
        despawn();

        Location spawnLocation = new Location(world, getSpawnLocation()[0], getSpawnLocation()[1], getSpawnLocation()[2]);

        entityID = UUID.randomUUID();
        spawnedEntity = (LivingEntity) world.spawnEntity(spawnLocation, entityType);
        spawnedEntity.setInvulnerable(true);
        spawnedEntity.setCustomName(getEntityName());
        spawnedEntity.setCustomNameVisible(true);
        spawnedEntity.setAI(false);
        spawnedEntity.setCanPickupItems(false);
        spawnedEntity.setCollidable(false);
    }

    public void despawn() {
        if (spawnedEntity != null && spawnedEntity.isValid()) {
            spawnedEntity.remove();
            spawnedEntity = null;
        }
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getName() {
        return name;
    }

    public double[] getSpawnLocation() {
        return spawnLocation;
    }

    public HashMap<String, String> getVoiceLines() {
        return voiceLines;
    }
}
