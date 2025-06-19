package com.anchorstudios.petting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEventHandler {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = (Player) event.getEntity();
        if (player.level().isClientSide) return;

        ServerLevel serverLevel = (ServerLevel) player.level();
        PetRegistry registry = new PetRegistry(serverLevel);

        registry.cleanupPlayerPets(player.getUUID());
    }
}