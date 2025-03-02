package net.petting.procedures;

import net.petting.init.PettingModGameRules;

import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class SourceTameEntityProcedure {
	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		entity.getPersistentData().putBoolean("isTamed", true);
		entity.getPersistentData().putString("owneruuid", (sourceentity.getStringUUID()));
		entity.getPersistentData().putString("attacktarget", "");
		entity.getPersistentData().putDouble("followdistance", 8);
		entity.getPersistentData().putDouble("tpdistance", 15);
		entity.getPersistentData().putDouble("attackcalldistance", 15);
		entity.getPersistentData().putBoolean("petfollow", true);
		entity.getPersistentData().putBoolean("pettp", true);
		entity.getPersistentData().putBoolean("petattack", true);
		entity.getPersistentData().putBoolean("playerattack", true);
		entity.setCustomName(Component.literal((sourceentity.getDisplayName().getString() + "'s " + entity.getDisplayName().getString())));
		if (!((sourceentity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()) != null
				? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()).getName()
				: "").equals(sourceentity.getStringUUID()))) {
			if (world instanceof Level _level)
				_level.getScoreboard().addPlayerTeam((sourceentity.getStringUUID()));
			if (world instanceof Level _level) {
				PlayerTeam _pt = _level.getScoreboard().getPlayerTeam((sourceentity.getStringUUID()));
				if (_pt != null)
					_pt.setDeathMessageVisibility(Team.Visibility.HIDE_FOR_OTHER_TEAMS);
			}
			if (world instanceof Level _level) {
				PlayerTeam _pt = _level.getScoreboard().getPlayerTeam((sourceentity.getStringUUID()));
				if (_pt != null)
					_pt.setNameTagVisibility(Team.Visibility.NEVER);
			}
			if (world instanceof Level _level) {
				PlayerTeam _pt = _level.getScoreboard().getPlayerTeam((sourceentity.getStringUUID()));
				if (_pt != null)
					_pt.setAllowFriendlyFire((world.getLevelData().getGameRules().getBoolean(PettingModGameRules.PET_FRIENDLY_FIRE)));
			}
			{
				Entity _entityTeam = entity;
				PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam((sourceentity.getStringUUID()));
				if (_pt != null) {
					if (_entityTeam instanceof Player _player)
						_entityTeam.level().getScoreboard().addPlayerToTeam(_player.getGameProfile().getName(), _pt);
					else
						_entityTeam.level().getScoreboard().addPlayerToTeam(_entityTeam.getStringUUID(), _pt);
				}
			}
		} else {
			{
				Entity _entityTeam = entity;
				PlayerTeam _pt = _entityTeam.level().getScoreboard().getPlayerTeam((sourceentity.getStringUUID()));
				if (_pt != null) {
					if (_entityTeam instanceof Player _player)
						_entityTeam.level().getScoreboard().addPlayerToTeam(_player.getGameProfile().getName(), _pt);
					else
						_entityTeam.level().getScoreboard().addPlayerToTeam(_entityTeam.getStringUUID(), _pt);
				}
			}
		}
	}
}
