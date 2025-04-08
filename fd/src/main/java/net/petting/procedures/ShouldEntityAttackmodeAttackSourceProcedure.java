package net.petting.procedures;

import net.petting.init.PettingModGameRules;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

public class ShouldEntityAttackmodeAttackSourceProcedure {
	public static boolean execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return false;
		if ((entity.getPersistentData().getString("attackmode")).equals("none")) {
			return false;
		} else if ((entity.getPersistentData().getString("attackmode")).equals("nonplayer")) {
			return !(sourceentity instanceof Player || sourceentity instanceof ServerPlayer);
		} else if (!(world instanceof ServerLevel _serverLevelGR4 && _serverLevelGR4.getGameRules().getBoolean(PettingModGameRules.PETS_FRIENDLY_FIRE)) && sourceentity.getPersistentData().getBoolean("tamed")
				&& (entity.getPersistentData().getString("owneruuid")).equals(sourceentity.getPersistentData().getString("owneruuid"))) {
			return false;
		}
		return true;
	}
}
