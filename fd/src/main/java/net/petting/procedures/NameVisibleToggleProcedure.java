package net.petting.procedures;

import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class NameVisibleToggleProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!((entity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()) != null
				? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()).getName()
				: "").equals(entity.getStringUUID()))) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A74Tame a mob before using the command."), false);
		} else if (world instanceof Level _level && _level.getScoreboard().getPlayerTeam((entity.getStringUUID())) != null
				? _level.getScoreboard().getPlayerTeam((entity.getStringUUID())).getNameTagVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS
				: false) {
			if (world instanceof Level _level) {
				PlayerTeam _pt = _level.getScoreboard().getPlayerTeam((entity.getStringUUID()));
				if (_pt != null)
					_pt.setNameTagVisibility(Team.Visibility.NEVER);
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A7cName tag visibility set to False."), false);
		} else {
			if (world instanceof Level _level) {
				PlayerTeam _pt = _level.getScoreboard().getPlayerTeam((entity.getStringUUID()));
				if (_pt != null)
					_pt.setNameTagVisibility(Team.Visibility.HIDE_FOR_OTHER_TEAMS);
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A72Name tag visibility set to True."), false);
		}
	}
}
