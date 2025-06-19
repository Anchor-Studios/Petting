package com.anchorstudios.petting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class PetRegistry extends SavedData {
    private final Map<UUID, Set<UUID>> playerPets = new HashMap<>(); // Changed to Set to prevent duplicates

    public static final String FILE_ID = "petting_pet_registry";

    public static PetRegistry get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                tag -> new PetRegistry(tag),
                PetRegistry::new,
                FILE_ID
        );
    }

    public PetRegistry() {}

    public PetRegistry(CompoundTag nbt) {
        loadFromNBT(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag players = new ListTag();
        synchronized (playerPets) { // Thread-safety
            for (Map.Entry<UUID, Set<UUID>> entry : playerPets.entrySet()) {
                CompoundTag playerTag = new CompoundTag();
                playerTag.putUUID("Owner", entry.getKey());

                ListTag petList = new ListTag();
                for (UUID pet : entry.getValue()) {
                    CompoundTag petTag = new CompoundTag();
                    petTag.putUUID("Pet", pet);
                    petList.add(petTag);
                }

                playerTag.put("Pets", petList);
                players.add(playerTag);
            }
        }
        tag.put("Registry", players);
        return tag;
    }

    private void loadFromNBT(CompoundTag tag) {
        playerPets.clear();
        if (!tag.contains("Registry", Tag.TAG_LIST)) return;

        ListTag players = tag.getList("Registry", Tag.TAG_COMPOUND);
        for (Tag t : players) {
            CompoundTag playerTag = (CompoundTag) t;
            if (!playerTag.hasUUID("Owner")) continue;

            UUID owner = playerTag.getUUID("Owner");
            Set<UUID> pets = new HashSet<>();

            if (playerTag.contains("Pets", Tag.TAG_LIST)) {
                ListTag petList = playerTag.getList("Pets", Tag.TAG_COMPOUND);
                for (Tag pt : petList) {
                    if (((CompoundTag) pt).hasUUID("Pet")) {
                        pets.add(((CompoundTag) pt).getUUID("Pet"));
                    }
                }
            }

            if (!pets.isEmpty()) {
                playerPets.put(owner, pets);
            }
        }
    }

    public synchronized void cleanup(ServerLevel level) {
        playerPets.forEach((owner, pets) -> {
            pets.removeIf(petUUID -> level.getEntity(petUUID) == null);
        });
        playerPets.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        setDirty();
    }

    public synchronized boolean addPet(UUID player, UUID pet) {
        if (player == null || pet == null) return false;
        boolean added = playerPets.computeIfAbsent(player, k -> new HashSet<>()).add(pet);
        if (added) setDirty();
        return added;
    }

    public synchronized boolean removePet(UUID player, UUID pet) {
        if (player == null || pet == null) return false;
        Set<UUID> pets = playerPets.get(player);
        if (pets != null && pets.remove(pet)) {
            if (pets.isEmpty()) playerPets.remove(player);
            setDirty();
            return true;
        }
        return false;
    }

    public synchronized int getPetCount(UUID player) {
        return playerPets.getOrDefault(player, Collections.emptySet()).size();
    }

    public synchronized boolean isRegisteredPet(UUID petUUID) {
        return playerPets.values().stream().anyMatch(set -> set.contains(petUUID));
    }
}