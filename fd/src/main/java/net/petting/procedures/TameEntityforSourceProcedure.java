package net.petting.procedures;

import net.petting.init.PettingModGameRules;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementHolder;

public class TameEntityforSourceProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (!(sourceentity instanceof ServerPlayer _plr0 && _plr0.level() instanceof ServerLevel
				&& _plr0.getAdvancements().getOrStartProgress(_plr0.server.getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/tame_an_animal"))).isDone())) {
			if (sourceentity instanceof ServerPlayer _player) {
				AdvancementHolder _adv = _player.server.getAdvancements().get(ResourceLocation.parse("minecraft:husbandry/tame_an_animal"));
				if (_adv != null) {
					AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
					if (!_ap.isDone()) {
						for (String criteria : _ap.getRemainingCriteria())
							_player.getAdvancements().award(_adv, criteria);
					}
				}
			}
		}
		if (entity instanceof TamableAnimal) {
			if (entity instanceof TamableAnimal _toTame && sourceentity instanceof Player _owner)
				_toTame.tame(_owner);
		} else {
			entity.setCustomName(Component.literal((sourceentity.getDisplayName().getString() + "'s " + entity.getDisplayName().getString())));
			entity.getPersistentData().putString("owneruuid", (sourceentity.getStringUUID()));
			entity.getPersistentData().putBoolean("tamed", true);
			entity.getPersistentData().putDouble("followdistance", (world instanceof ServerLevel _serverLevelGR10 ? _serverLevelGR10.getGameRules().getInt(PettingModGameRules.DEFAULT_FOLLOW_DISTANCE) : 0));
			entity.getPersistentData().putDouble("teleportdistance", (world instanceof ServerLevel _serverLevelGR12 ? _serverLevelGR12.getGameRules().getInt(PettingModGameRules.DEFAULT_TELEPORT_DISTANCE) : 0));
			entity.getPersistentData().putDouble("attackdistance", (world instanceof ServerLevel _serverLevelGR14 ? _serverLevelGR14.getGameRules().getInt(PettingModGameRules.DEFAULT_ATTACK_DISTANCE) : 0));
			if ((world instanceof ServerLevel _serverLevelGR16 ? _serverLevelGR16.getGameRules().getInt(PettingModGameRules.DEFAULT_TELEPORT_DISTANCE) : 0) == -1) {
				entity.getPersistentData().putBoolean("teleport", false);
			} else {
				entity.getPersistentData().putBoolean("teleport", true);
			}
			if ((world instanceof ServerLevel _serverLevelGR19 ? _serverLevelGR19.getGameRules().getInt(PettingModGameRules.DEFAULT_ATTACK_DISTANCE) : 0) == -1) {
				entity.getPersistentData().putString("attackmode", "none");
			} else {
				entity.getPersistentData().putString("attackmode", "everyone");
			}
			if ((sourceentity.getPersistentData().getString("petlist")).equals("") || (sourceentity.getPersistentData().getString("petlist")).equals(",")) {
				sourceentity.getPersistentData().putString("petlist", (entity.getStringUUID()));
			} else {
				sourceentity.getPersistentData().putString("petlist", ("," + entity.getStringUUID()));
			}
			if (entity instanceof Mob _entity) {
				_entity.setTarget(null);
			}
			if (entity instanceof Mob _entity)
				_entity.getNavigation().stop();
			if (world instanceof ServerLevel _serverLevelGR30 && _serverLevelGR30.getGameRules().getBoolean(PettingModGameRules.HEAL_MOB_WHEN_TAMED)) {
				if (entity instanceof LivingEntity _entity)
					_entity.setHealth(entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
			}
		}
	}
}
