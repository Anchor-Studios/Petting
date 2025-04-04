package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class PettingCommandAllowDisablePettingProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		if (!BoolArgumentType.getBool(arguments, "log")) {
			try {
				for (Entity entityiterator : EntityArgument.getEntities(arguments, "name")) {
					{
						PettingModVariables.PlayerVariables _vars = entityiterator.getData(PettingModVariables.PLAYER_VARIABLES);
						_vars.allowpeting = false;
						_vars.syncPlayerVariables(entityiterator);
					}
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal(("Disabled petting for " + entityiterator.getDisplayName().getString() + ".")), false);
				}
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}
		} else {
			try {
				for (Entity entityiterator : EntityArgument.getEntities(arguments, "name")) {
					{
						PettingModVariables.PlayerVariables _vars = entityiterator.getData(PettingModVariables.PLAYER_VARIABLES);
						_vars.allowpeting = true;
						_vars.syncPlayerVariables(entityiterator);
					}
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal(("Enabled petting for " + entityiterator.getDisplayName().getString() + ".")), false);
				}
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}
		}
	}
}
