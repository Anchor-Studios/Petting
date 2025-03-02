package net.petting.procedures;

import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class DeathMessageToggleProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!((entity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()) != null
				? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()).getName()
				: "").equals(entity.getStringUUID()))) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A74Tame a mob before using the command."), false);
		} else if (world instanceof Level _level && _level.getScoreboard().getPlayerTeam((entity.getStringUUID())) != null
				? _level.getScoreboard().getPlayerTeam((entity.getStringUUID())).getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS
				: false) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A7cDeath message visibility set to False."), false);
			if (world instanceof Level _level) {
				PlayerTeam _pt = _level.getScoreboard().getPlayerTeam((entity.getStringUUID()));
				if (_pt != null)
					_pt.setDeathMessageVisibility(Team.Visibility.NEVER);
			}
		} else {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A72Death message visibility set to True."), false);
			if (world instanceof Level _level) {
				PlayerTeam _pt = _level.getScoreboard().getPlayerTeam((entity.getStringUUID()));
				if (_pt != null)
					_pt.setDeathMessageVisibility(Team.Visibility.HIDE_FOR_OTHER_TEAMS);
			}
		}
	}
}
