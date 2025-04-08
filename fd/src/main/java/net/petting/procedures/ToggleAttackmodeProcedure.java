package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class ToggleAttackmodeProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!(null == entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world))) {
			if (("none").equals(entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world).getPersistentData().getString("attackmode"))) {
				entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world).getPersistentData().putString("attackmode", "nonplayer");
			} else if (("nonplayer").equals(entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world).getPersistentData().getString("attackmode"))) {
				entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world).getPersistentData().putString("attackmode", "everyone");
			} else {
				entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world).getPersistentData().putString("attackmode", "none");
			}
		}
	}

	public static Entity entityFromStringUUID(String uuid, Level world) {
		Entity _uuidentity = null;
		if (world instanceof ServerLevel _server) {
			try {
				_uuidentity = _server.getEntity(UUID.fromString(uuid));
			} catch (Exception e) {
			}
		}
		return _uuidentity;
	}
}
