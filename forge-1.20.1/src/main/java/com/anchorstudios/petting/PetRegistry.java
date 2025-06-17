package com.anchorstudios.petting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class PetRegistry extends SavedData {

    private final Map<UUID, List<UUID>> playerPets = new HashMap<>();

    public static final String FILE_ID = "petting_pet_registry";

    public static PetRegistry load(CompoundTag tag) {
        return new PetRegistry(tag);
    }

    public static PetRegistry get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(PetRegistry::load, PetRegistry::new, FILE_ID);
    }

    public PetRegistry() {}

    public PetRegistry(CompoundTag nbt) {
        loadFromNBT(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag players = new ListTag();
        for (Map.Entry<UUID, List<UUID>> entry : playerPets.entrySet()) {
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
        tag.put("Registry", players);
        return tag;
    }

    private void loadFromNBT(CompoundTag tag) {
        playerPets.clear();
        ListTag players = tag.getList("Registry", Tag.TAG_COMPOUND);
        for (Tag t : players) {
            CompoundTag playerTag = (CompoundTag) t;
            UUID owner = playerTag.getUUID("Owner");
            List<UUID> pets = new ArrayList<>();
            ListTag petList = playerTag.getList("Pets", Tag.TAG_COMPOUND);
            for (Tag pt : petList) {
                pets.add(((CompoundTag) pt).getUUID("Pet"));
            }
            playerPets.put(owner, pets);
        }
    }

    public void cleanup(ServerLevel level) {
        Iterator<Map.Entry<UUID, List<UUID>>> iterator = playerPets.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, List<UUID>> entry = iterator.next();
            List<UUID> pets = entry.getValue();
            pets.removeIf(petUUID -> level.getEntity(petUUID) == null);
            if (pets.isEmpty()) iterator.remove();
        }
        setDirty();
    }

    public void addPet(UUID player, UUID pet) {
        playerPets.computeIfAbsent(player, k -> new ArrayList<>()).add(pet);
        setDirty();
    }

    public void removePet(UUID player, UUID pet) {
        List<UUID> pets = playerPets.get(player);
        if (pets != null && pets.remove(pet)) {
            if (pets.isEmpty()) playerPets.remove(player);
            setDirty();
        }
    }

    public int getPetCount(UUID player) {
        return playerPets.getOrDefault(player, List.of()).size();
    }

    public boolean isRegisteredPet(UUID petUUID) {
        return playerPets.values().stream().anyMatch(list -> list.contains(petUUID));
    }
}
