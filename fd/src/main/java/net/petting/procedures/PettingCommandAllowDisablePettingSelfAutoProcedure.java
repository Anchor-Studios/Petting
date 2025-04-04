package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class PettingCommandAllowDisablePettingSelfAutoProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			PettingModVariables.PlayerVariables _vars = entity.getData(PettingModVariables.PLAYER_VARIABLES);
			_vars.allowpeting = true;
			_vars.syncPlayerVariables(entity);
		}
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal("Enabled petting for yourself."), false);
	}
}
