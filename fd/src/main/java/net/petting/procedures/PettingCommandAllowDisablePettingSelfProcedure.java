package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class PettingCommandAllowDisablePettingSelfProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		if (!BoolArgumentType.getBool(arguments, "log")) {
			{
				PettingModVariables.PlayerVariables _vars = entity.getData(PettingModVariables.PLAYER_VARIABLES);
				_vars.allowpeting = false;
				_vars.syncPlayerVariables(entity);
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("Disabled petting for yourself."), false);
		} else {
			{
				PettingModVariables.PlayerVariables _vars = entity.getData(PettingModVariables.PLAYER_VARIABLES);
				_vars.allowpeting = true;
				_vars.syncPlayerVariables(entity);
			}
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("Enabled petting for yourself."), false);
		}
	}
}
