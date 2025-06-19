package com.anchorstudios.petting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PetRegistry {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private static final String FILE_NAME = "pets.json";
    private final Path savePath;
    private final ServerLevel level;

    private final Map<UUID, OwnerData> registry = new ConcurrentHashMap<>();

    public PetRegistry(ServerLevel level) {
        this.level = level;
        this.savePath = level.getServer().getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT)
                .resolve("petting").resolve(FILE_NAME);
        load();
    }

    private static class OwnerData {
        String ownerName;
        Map<UUID, PetData> pets = new HashMap<>();

        OwnerData(String ownerName) {
            this.ownerName = ownerName;
        }
    }

    private static class PetData {
        String petName;
        String petType;

        PetData(String petName, String petType) {
            this.petName = petName;
            this.petType = petType;
        }
    }

    private void load() {
        try {
            if (!Files.exists(savePath)) {
                Files.createDirectories(savePath.getParent());
                return;
            }

            String json = Files.readString(savePath);
            Type type = new com.google.gson.reflect.TypeToken<Map<UUID, OwnerData>>(){}.getType();
            Map<UUID, OwnerData> loaded = GSON.fromJson(json, type);

            if (loaded != null) {
                registry.clear();
                registry.putAll(loaded);
            }
        } catch (IOException e) {
        }
    }

    private void save() {
        try {
            Files.createDirectories(savePath.getParent());
            Files.writeString(savePath, GSON.toJson(registry));
        } catch (IOException e) {
        }
    }

    public boolean addPet(Player player, Entity pet) {
        UUID playerId = player.getUUID();
        UUID petId = pet.getUUID();

        OwnerData ownerData = registry.computeIfAbsent(playerId,
                k -> new OwnerData(player.getScoreboardName()));

        if (ownerData.pets.containsKey(petId)) {
            return false; // Pet already registered
        }

        ownerData.pets.put(petId, new PetData(
                pet.getDisplayName().getString(),
                pet.getType().getDescription().getString()
        ));

        save();
        return true;
    }

    public boolean removePet(UUID playerId, UUID petId) {
        OwnerData ownerData = registry.get(playerId);
        if (ownerData != null && ownerData.pets.remove(petId) != null) {
            if (ownerData.pets.isEmpty()) {
                registry.remove(playerId);
            }
            save();
            return true;
        }
        return false;
    }

    public void cleanupPlayerPets(UUID playerId) {
        OwnerData ownerData = registry.get(playerId);
        if (ownerData != null) {
            ownerData.pets.keySet().removeIf(petId ->
                    level.getEntity(petId) == null
            );
            if (ownerData.pets.isEmpty()) {
                registry.remove(playerId);
            }
            save();
        }
    }

    public void cleanup() {
        registry.forEach((playerId, ownerData) -> {
            ownerData.pets.keySet().removeIf(petId ->
                    level.getEntity(petId) == null
            );
        });
        registry.entrySet().removeIf(entry -> entry.getValue().pets.isEmpty());
        save();
    }

    public int getPetCount(UUID playerId) {
        OwnerData ownerData = registry.get(playerId);
        return ownerData != null ? ownerData.pets.size() : 0;
    }

    public boolean isRegisteredPet(UUID petId) {
        return registry.values().stream()
                .anyMatch(ownerData -> ownerData.pets.containsKey(petId));
    }

    public String getRegistryJson() {
        return GSON.toJson(registry);
    }
}