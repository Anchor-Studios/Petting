package com.anchorstudios.petting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Petting.MODID)
public class PetPersistenceHandler {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide) return;

        CompoundTag tag = entity.getPersistentData();
        if (tag.getBoolean("PettingTamed") && entity instanceof Mob mob) {
            mob.setPersistenceRequired();
        }
    }
}