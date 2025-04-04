package net.petting.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class PettingCommandReturnPetCountSelfProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(Component.literal(("You currently have " + Math.round(ReturnTamedCountProcedure.execute(entity)) + " tamed mobs.")), false);
	}
}
