package net.petting.procedures;

import net.petting.PettingMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public class WaitFDRightclickProcedure {
	public static void execute(LevelAccessor world, CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		if (!((entity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()) != null
				? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()).getName()
				: "").equals(entity.getStringUUID()))) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A74Tame a mob before using the command."), false);
		} else {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal(("\u00A7eRightclick to a pet to set it's follow distance to " + DoubleArgumentType.getDouble(arguments, "name") + ".")), false);
			entity.getPersistentData().putDouble("fdupdate", (DoubleArgumentType.getDouble(arguments, "name")));
			entity.getPersistentData().putBoolean("fdwait", true);
			PettingMod.queueServerWork(350, () -> {
				if (entity.getPersistentData().getBoolean("fdwait")) {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("\u00A74Follow distance changing request cancelled, waited too long."), false);
					entity.getPersistentData().putBoolean("fdwait", false);
				}
			});
		}
	}
}
