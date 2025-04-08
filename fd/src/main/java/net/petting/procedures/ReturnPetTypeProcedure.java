package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class ReturnPetTypeProcedure {
	public static String execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return "";
		if (null == entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world)) {
			return "Null";
		}
		return "Type: " + entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world).getPersistentData().getString("pettype");
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
