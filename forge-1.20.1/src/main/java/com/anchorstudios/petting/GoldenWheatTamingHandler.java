package com.anchorstudios.petting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Petting.MODID)
public class GoldenWheatTamingHandler {

    private static final String TAMED_TAG = "PettingTamed";

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Level level = player.level();
        InteractionHand hand = event.getHand();
        ItemStack heldItem = player.getItemInHand(hand);
        Entity target = event.getTarget();

        // Check if holding Golden Wheat
        if (!heldItem.is(Petting.GOLDEN_WHEAT.get())) return;

        // Check if target is a LivingEntity
        if (!(target instanceof LivingEntity entity)) return;

        // Already tamed?
        CompoundTag tag = entity.getPersistentData();
        if (tag.getBoolean(TAMED_TAG)) return;

        // Mark as tamed
        tag.putBoolean(TAMED_TAG, true);

        // Play heart particles
        if (!level.isClientSide) {
            ((ServerLevel) level).sendParticles(ParticleTypes.HEART,
                    entity.getX(), entity.getY() + 1.0D, entity.getZ(),
                    5, 0.5D, 0.5D, 0.5D, 0.1D);
        }

        // Remove item if not in Creative
        if (!player.isCreative()) {
            heldItem.shrink(1);
        }

        // Cancel event to avoid further interaction (e.g., opening inventory)
        event.setCanceled(true);
    }
}