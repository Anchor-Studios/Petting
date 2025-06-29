package com.anchorstudios.petting;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.EnumSet;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Petting.MODID)
public class TamedMobBehavior {

    // Configuration variables with default values
    public static double FOLLOW_DISTANCE = 8.0D;
    public static double TELEPORT_DISTANCE = 32.0D;
    public static boolean DEFEND_OWNER = true;
    public static boolean ATTACK_OWNERS_TARGET = true;

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Mob entity) || event.getLevel().isClientSide()) {
            return;
        }

        CompoundTag tag = entity.getPersistentData();
        if (tag.getBoolean(GoldenWheatTamingHandler.TAMED_TAG)) {
            // Reapply owner UUID if it exists (fixes relog issue)
            if (tag.hasUUID("PettingOwnerUUID")) {
                setupTamedBehavior(entity);
            }
        }
    }
    private static boolean isRestrictedMob(Mob mob) {
        return mob instanceof Warden || mob instanceof WitherBoss || mob instanceof EnderDragon;
    }

    public static void setupTamedBehavior(Mob entity) {
        if (!(entity instanceof TamableAnimal) && !entity.getPersistentData().getBoolean(GoldenWheatTamingHandler.TAMED_TAG)) {
            return;
        }

        // Clear existing AI goals that might interfere
        entity.goalSelector.removeAllGoals(goal -> true);
        entity.targetSelector.removeAllGoals(goal -> true);

        UUID ownerUUID = entity.getPersistentData().getUUID("PettingOwnerUUID");
        if (ownerUUID == null) return;

        if (isRestrictedMob(entity)) {
            if (entity instanceof Warden warden) {
                warden.getBrain().removeAllBehaviors();
            }
            // Disable all special AI for these mobs
            entity.goalSelector.addGoal(0, new TamedRestrictedMobGoal(entity));
        }

        // Add standard tamed behaviors
        entity.goalSelector.addGoal(1, new FloatGoal(entity));
        entity.goalSelector.addGoal(2, new TamedSitGoal(entity));
        entity.goalSelector.addGoal(3, new TamedFollowOwnerGoal(entity, FOLLOW_DISTANCE, 1.0D, 5.0F, entity instanceof WaterAnimal));

        if (entity instanceof PathfinderMob pathfinderMob) {
            entity.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(pathfinderMob, 1.0D));

            // Special handling for creepers
            if (entity instanceof Creeper creeper) {
                entity.goalSelector.addGoal(4, new TamedCreeperSwellGoal(creeper));
            }
            // Only add attack goals if the mob has attack damage attribute and isn't restricted
            else if (entity.getAttribute(Attributes.ATTACK_DAMAGE) != null && !isRestrictedMob(entity)) {
                entity.goalSelector.addGoal(4, new MeleeAttackGoal(pathfinderMob, 1.0D, true));
            }
        }

        entity.goalSelector.addGoal(6, new LookAtPlayerGoal(entity, Player.class, 8.0F));
        entity.goalSelector.addGoal(7, new RandomLookAroundGoal(entity));

        // Target selector - only add if mob can attack and isn't restricted
        if (entity.getAttribute(Attributes.ATTACK_DAMAGE) != null && !isRestrictedMob(entity)) {
            entity.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(entity));
            entity.targetSelector.addGoal(2, new OwnerHurtTargetGoal(entity));
            entity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(entity, LivingEntity.class, 10, true, false,
                    target -> shouldAttackTarget(entity, target)));
        }
    }

    static class TamedRestrictedMobGoal extends Goal {
        private final Mob mob;

        public TamedRestrictedMobGoal(Mob mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return mob.getPersistentData().getBoolean(GoldenWheatTamingHandler.TAMED_TAG);
        }

        @Override
        public void tick() {
            // This effectively disables all AI for restricted mobs
            mob.setTarget(null);
            mob.getNavigation().stop();
        }
    }

    static class TamedCreeperSwellGoal extends Goal {
        private final Creeper creeper;
        private LivingEntity target;

        public TamedCreeperSwellGoal(Creeper creeper) {
            this.creeper = creeper;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.creeper.getTarget();
            return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity) < 9.0D;
        }

        @Override
        public void start() {
            this.creeper.getNavigation().stop();
            this.target = this.creeper.getTarget();
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                this.creeper.setSwellDir(-1);
            } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
                this.creeper.setSwellDir(-1);
            } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
                this.creeper.setSwellDir(-1);
            } else {
                this.creeper.setSwellDir(1);
            }
        }
    }

    private static boolean shouldAttackTarget(Mob pet, LivingEntity target) {

        if (pet instanceof IronGolem) {
            UUID ownerUUID = pet.getPersistentData().getUUID("PettingOwnerUUID");
            if (ownerUUID != null) {
                Player owner = pet.level().getPlayerByUUID(ownerUUID);
                if (owner != null) {
                    // Attack if owner is being attacked or if attacking owner's target
                    return owner.getLastHurtByMob() == target ||
                            (ATTACK_OWNERS_TARGET && owner.getLastHurtMob() == target);
                }
            }
            return false;
        }

        if (!DEFEND_OWNER) return false;

        CompoundTag tag = pet.getPersistentData();
        if (!tag.getBoolean(GoldenWheatTamingHandler.TAMED_TAG)) return false;

        UUID ownerUUID = tag.getUUID("PettingOwnerUUID");
        if (ownerUUID == null) return false;

        Level level = pet.level();
        Player owner = level.getPlayerByUUID(ownerUUID);
        if (owner == null) return false;

        // Don't attack owner
        if (target == owner) return false;

        // Don't attack other tamed mobs of same owner
        if (target.getPersistentData().getBoolean(GoldenWheatTamingHandler.TAMED_TAG) &&
                ownerUUID.equals(target.getPersistentData().getUUID("PettingOwnerUUID"))) {
            return false;
        }

        // Attack if owner is being attacked by this target
        if (owner.getLastHurtByMob() == target) return true;

        // Attack if owner is attacking this target
        if (ATTACK_OWNERS_TARGET && owner.getLastHurtMob() == target) return true;

        // Special case for creepers
        return target instanceof Creeper;
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Mob entity) {
            LivingEntity target = event.getNewTarget();

            if (entity.getPersistentData().getBoolean(GoldenWheatTamingHandler.TAMED_TAG) && target != null) {
                UUID ownerUUID = entity.getPersistentData().getUUID("PettingOwnerUUID");
                if (ownerUUID != null && target.getUUID().equals(ownerUUID)) {
                    // Cancel targeting owner
                    entity.setTarget(null);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player attacker) {
            LivingEntity target = event.getEntity();
        }
    }

    // Custom AI Goals
    static class TamedFollowOwnerGoal extends Goal {
        private final Mob entity;
        private final Player owner;
        private final Level level;
        private final double speedModifier;
        private final float stopDistance;
        private final boolean canFly;
        private final boolean isWaterMob;
        private double wantedX;
        private double wantedY;
        private double wantedZ;

        public TamedFollowOwnerGoal(Mob entity, double followDistance, double speedModifier, float stopDistance, boolean isWaterMob) {
            this.entity = entity;
            this.level = entity.level();
            this.speedModifier = speedModifier;
            this.stopDistance = stopDistance;
            this.isWaterMob = isWaterMob;

            UUID ownerUUID = entity.getPersistentData().getUUID("PettingOwnerUUID");
            this.owner = ownerUUID != null ? level.getPlayerByUUID(ownerUUID) : null;
            this.canFly = entity.getType().is(EntityTypeTags.ARROWS) || entity instanceof FlyingAnimal;

            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (owner == null || entity.getPersistentData().getBoolean("PettingSitting")) {
                return false;
            }

            Vec3 ownerPos = owner.position();
            double distance = entity.distanceToSqr(ownerPos.x, ownerPos.y, ownerPos.z);
            return distance > (stopDistance * stopDistance);
        }

        @Override
        public boolean canContinueToUse() {
            if (owner == null || entity.getPersistentData().getBoolean("PettingSitting")) {
                return false;
            }

            if (!entity.getNavigation().isDone()) {
                Vec3 ownerPos = owner.position();
                double distance = entity.distanceToSqr(ownerPos.x, ownerPos.y, ownerPos.z);
                return distance > (stopDistance * stopDistance * 0.75);
            }
            return false;
        }

        @Override
        public void start() {
            wantedX = owner.getX();
            wantedY = owner.getY();
            wantedZ = owner.getZ();

            // Check if we should teleport instead of follow
            double distance = entity.distanceToSqr(wantedX, wantedY, wantedZ);
            if (distance > TELEPORT_DISTANCE * TELEPORT_DISTANCE) {
                tryTeleport();
                return;
            }

            // Regular pathfinding
            entity.getNavigation().moveTo(owner, speedModifier);
        }

        private void tryTeleport() {
            if (!owner.isAlive() || level.isClientSide) return;

            // Find suitable teleport location near owner
            for(int i = 0; i < 10; ++i) {
                double x = owner.getX() + (level.random.nextDouble() - 0.5D) * 5.0D;
                double y = owner.getY() + (double)(level.random.nextInt(3) - 1);
                double z = owner.getZ() + (level.random.nextDouble() - 0.5D) * 5.0D;
                BlockPos pos = new BlockPos((int)x, (int)y, (int)z);

                if (isWaterMob) {
                    // For water mobs, ensure we're teleporting to water
                    if (level.getBlockState(pos).liquid()) {
                        entity.teleportTo(x, y, z);
                        return;
                    }
                } else if (canFly || entity.getNavigation().createPath(x, y, z, 1) != null) {
                    entity.teleportTo(x, y, z);
                    return;
                }
            }

            // If we couldn't find a good spot, just try regular pathfinding
            entity.getNavigation().moveTo(owner, speedModifier);
        }
    }

    static class TamedSitGoal extends Goal {
        private final Mob entity;

        public TamedSitGoal(Mob entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!entity.getPersistentData().getBoolean(GoldenWheatTamingHandler.TAMED_TAG)) {
                return false;
            }
            return entity.getPersistentData().getBoolean("PettingSitting");
        }

        @Override
        public void start() {
            entity.getNavigation().stop();
            entity.setTarget(null);
        }
    }

    static class OwnerHurtByTargetGoal extends TargetGoal {
        private final Mob pet;
        private Player owner;
        private LivingEntity ownerAttacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(Mob pet) {
            super(pet, false);
            this.pet = pet;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (!DEFEND_OWNER || !pet.getPersistentData().getBoolean(GoldenWheatTamingHandler.TAMED_TAG)) {
                return false;
            }

            UUID ownerUUID = pet.getPersistentData().getUUID("PettingOwnerUUID");
            if (ownerUUID == null) return false;

            this.owner = pet.level().getPlayerByUUID(ownerUUID);
            if (owner == null) return false;

            this.ownerAttacker = owner.getLastHurtByMob();
            if (ownerAttacker == null) return false;

            int lastAttackedTime = owner.getLastHurtByMobTimestamp();
            return lastAttackedTime != this.timestamp && this.canAttack(ownerAttacker, TargetingConditions.DEFAULT);
        }

        @Override
        public void start() {
            this.pet.setTarget(this.ownerAttacker);
            this.owner = pet.level().getPlayerByUUID(pet.getPersistentData().getUUID("PettingOwnerUUID"));
            if (this.owner != null) {
                this.timestamp = this.owner.getLastHurtByMobTimestamp();
            }
            super.start();
        }
    }

    static class OwnerHurtTargetGoal extends TargetGoal {
        private final Mob pet;
        private Player owner;
        private LivingEntity ownerTarget;
        private int timestamp;

        public OwnerHurtTargetGoal(Mob pet) {
            super(pet, false);
            this.pet = pet;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (!DEFEND_OWNER || !ATTACK_OWNERS_TARGET || !pet.getPersistentData().getBoolean(GoldenWheatTamingHandler.TAMED_TAG)) {
                return false;
            }

            UUID ownerUUID = pet.getPersistentData().getUUID("PettingOwnerUUID");
            if (ownerUUID == null) return false;

            this.owner = pet.level().getPlayerByUUID(ownerUUID);
            if (owner == null) return false;

            this.ownerTarget = owner.getLastHurtMob();
            if (ownerTarget == null) return false;

            int lastAttackedTime = owner.getLastHurtMobTimestamp();
            return lastAttackedTime != this.timestamp && this.canAttack(ownerTarget, TargetingConditions.DEFAULT);
        }

        @Override
        public void start() {
            this.pet.setTarget(this.ownerTarget);
            this.owner = pet.level().getPlayerByUUID(pet.getPersistentData().getUUID("PettingOwnerUUID"));
            if (this.owner != null) {
                this.timestamp = this.owner.getLastHurtMobTimestamp();
            }
            super.start();
        }
    }
}