package net.petting.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class WhenPetNeedToFollowOwnerProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		if (entity.getPersistentData().getBoolean("tamed") && !(entityFromStringUUID((entity.getPersistentData().getString("owneruuid")), (Level) world) == null)) {
			if ((entityFromStringUUID((entity.getPersistentData().getString("owneruuid")), (Level) world) != null ? entity.distanceTo(entityFromStringUUID((entity.getPersistentData().getString("owneruuid")), (Level) world)) : -1) < entity
					.getPersistentData().getDouble("teleportdistance")
					&& entity.getPersistentData()
							.getDouble("followdistance") <= (entityFromStringUUID((entity.getPersistentData().getString("owneruuid")), (Level) world) != null
									? entity.distanceTo(entityFromStringUUID((entity.getPersistentData().getString("owneruuid")), (Level) world))
									: -1)) {
				return true;
			}
		}
		return false;
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
