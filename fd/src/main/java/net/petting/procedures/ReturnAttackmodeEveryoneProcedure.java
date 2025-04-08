package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class ReturnAttackmodeEveryoneProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		if (null == entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world)) {
			return false;
		}
		return ("everyone").equals(entityFromStringUUID(entity.getData(PettingModVariables.PLAYER_VARIABLES).petsettingsuuid, (Level) world).getPersistentData().getString("attackmode"));
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
