package com.anchorstudios.petting;

import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

@Mod.EventBusSubscriber(modid = Petting.MODID)
public class ServerTickHandler {

    private static int tickCounter = 0;
    private static boolean hasWarnedAboutPerformance = false;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        int cleanupInterval = Config.COMMON.cleanupIntervalTicks.get();

        // Warn about performance impact if interval is too low (only once)
        if (!hasWarnedAboutPerformance && cleanupInterval < 100) {
            event.getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal("[Petting] WARNING: Pet cleanup interval is set very low (" + cleanupInterval + " ticks)! This may cause server lag."),
                    false
            );
            hasWarnedAboutPerformance = true;
        }

        tickCounter++;
        if (tickCounter < cleanupInterval) return;
        tickCounter = 0;

        event.getServer().getAllLevels().forEach(level -> {
            PetRegistry registry = new PetRegistry((ServerLevel) level);
            registry.periodicCleanup();
        });

        MinecraftServer server = event.getServer();
        for (ServerLevel level : server.getAllLevels()) {
            PetRegistry registry = new PetRegistry(level);
            registry.cleanup();
        }
    }
}