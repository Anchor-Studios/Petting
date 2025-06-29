package com.anchorstudios.petting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Petting.MODID)
public class GoldenWheatTamingHandler {
    public static final String TAMED_TAG = "PettingTamed";
    public static final String OWNER_UUID_TAG = "PettingOwnerUUID";
    public static final String OWNER_NAME_TAG = "PettingOwnerName";
    public static final String SITTING_TAG = "PettingSitting";

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
        PetRegistry registry = new PetRegistry(serverLevel);
        if (registry == null) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }

        // Check pet limit
        int petLimit = Config.COMMON.petLimitPerPlayer.get();
        if (petLimit != -1 && registry.getPetCount(player.getUUID()) >= petLimit) {
            player.sendSystemMessage(Component.literal("You've reached your pet limit!"));
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }

        // Check if already tamed
        if (tag.getBoolean(TAMED_TAG)) {
            if (!Config.COMMON.allowRetaming.get() ||
                    (tag.hasUUID(OWNER_UUID_TAG) &&
                            player.getUUID().equals(tag.getUUID(OWNER_UUID_TAG)))) {
                // Toggle sitting if already owned by this player
                if (player.isShiftKeyDown()) {
                    boolean sitting = !tag.getBoolean(SITTING_TAG);
                    tag.putBoolean(SITTING_TAG, sitting);
                    if (entity instanceof Mob mob) {
                        mob.setTarget(null);
                    }
                    if (sitting) {
                        player.sendSystemMessage(Component.literal(entity.getDisplayName().getString() + " is now sitting"));
                    } else {
                        player.sendSystemMessage(Component.literal(entity.getDisplayName().getString() + " is now standing"));
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                return;
            }
        }

        // Taming logic
        String playerName = player.getDisplayName().getString();
        String oldName = target.getDisplayName().getString();
        Component newName = Component.literal(playerName + "'s " + oldName);

        tag.putBoolean(TAMED_TAG, true);
        tag.putUUID(OWNER_UUID_TAG, player.getUUID());
        tag.putString(OWNER_NAME_TAG, playerName);
        tag.putBoolean(SITTING_TAG, false); // Default to not sitting

        registry.addPet(player, target);

        if (target instanceof Warden || target instanceof WitherBoss || target instanceof EnderDragon) {
            // Clear any special AI goals
            if (target instanceof Warden warden) {
                warden.getBrain().removeAllBehaviors();
            }
        }

        if (Config.COMMON.showPetOwnershipName.get()) {
            target.setCustomName(newName);
            target.setCustomNameVisible(true);
        }

        // Set up AI behaviors
        if (entity instanceof Mob) {
            TamedMobBehavior.setupTamedBehavior((Mob) entity);
        }

        serverLevel.sendParticles(ParticleTypes.HEART,
                entity.getX(), entity.getY() + 1.0D, entity.getZ(),
                5, 0.5D, 0.5D, 0.5D, 0.1D);

        if (!player.isCreative()) {
            heldItem.shrink(1);
        }

        player.getCooldowns().addCooldown(Petting.GOLDEN_WHEAT.get(), 6);
        player.sendSystemMessage(Component.literal("Successfully tamed " + oldName + "!"));
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}