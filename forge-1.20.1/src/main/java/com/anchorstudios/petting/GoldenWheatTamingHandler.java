package com.anchorstudios.petting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

        if (!heldItem.is(Petting.GOLDEN_WHEAT.get())) return;
        if (!(target instanceof LivingEntity entity)) return;

        CompoundTag tag = entity.getPersistentData();

        if (tag.getBoolean(TAMED_TAG)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }

        if (!level.isClientSide) {
            // Set name and tag
            String playerName = player.getDisplayName().getString();
            String oldName = target.getDisplayName().getString();
            Component newName = Component.literal(playerName + "'s " + oldName);
            target.setCustomName(newName);

            tag.putBoolean(TAMED_TAG, true);

            ((ServerLevel) level).sendParticles(ParticleTypes.HEART,
                    entity.getX(), entity.getY() + 1.0D, entity.getZ(),
                    5, 0.5D, 0.5D, 0.5D, 0.1D);

            if (!player.isCreative()) {
                heldItem.shrink(1);
            }

            player.getCooldowns().addCooldown(Petting.GOLDEN_WHEAT.get(), 6);

            // Mark event success on server
            event.setCancellationResult(InteractionResult.SUCCESS);
        } else {
            event.setCancellationResult(InteractionResult.PASS);
        }

        event.setCanceled(true);
    }
}