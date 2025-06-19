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

import java.util.UUID;


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
        if (!(level instanceof ServerLevel serverLevel)) {
            event.setCancellationResult(InteractionResult.PASS);
            return;
        }

        CompoundTag tag = entity.getPersistentData();
        PetRegistry registry = PetRegistry.get(serverLevel);
        if (registry == null) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }

        // Check pet limit
        int petLimit = Config.COMMON.petLimitPerPlayer.get();
        if (petLimit != -1 && registry.getPetCount(player.getUUID()) >= petLimit) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }

        // Check if already tamed
        if (tag.getBoolean(TAMED_TAG)) {
            if (!Config.COMMON.allowRetaming.get() ||
                    (tag.hasUUID("PettingOwnerUUID") &&
                            player.getUUID().equals(tag.getUUID("PettingOwnerUUID")))) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }
        }

        // Taming logic
        String playerName = player.getDisplayName().getString();
        String oldName = target.getDisplayName().getString();
        Component newName = Component.literal(playerName + "'s " + oldName);

        tag.putBoolean(TAMED_TAG, true);
        tag.putUUID("PettingOwnerUUID", player.getUUID());
        tag.putString("PettingOwnerName", playerName);

        registry.addPet(player.getUUID(), target.getUUID());

        if (Config.COMMON.showPetOwnershipName.get()) {
            target.setCustomName(newName);
            tag.putString("PettingCustomName", newName.getString());
        }

        serverLevel.sendParticles(ParticleTypes.HEART,
                entity.getX(), entity.getY() + 1.0D, entity.getZ(),
                5, 0.5D, 0.5D, 0.5D, 0.1D);

        if (!player.isCreative()) {
            heldItem.shrink(1);
        }

        player.getCooldowns().addCooldown(Petting.GOLDEN_WHEAT.get(), 6);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}