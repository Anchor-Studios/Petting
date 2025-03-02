package net.petting.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

public class SourceTameEntityProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		entity.getPersistentData().putBoolean("isTamed", true);
		entity.getPersistentData().putString("owneruuid", (sourceentity.getStringUUID()));
		entity.getPersistentData().putString("attacktarget", "");
		entity.getPersistentData().putDouble("followdistance", 8);
		entity.getPersistentData().putDouble("tpdistance", 15);
		entity.getPersistentData().putDouble("attackcalldistance", 15);
		entity.getPersistentData().putBoolean("petfollow", true);
		entity.getPersistentData().putBoolean("pettp", true);
		entity.getPersistentData().putBoolean("petattack", true);
		entity.getPersistentData().putBoolean("playerattack", true);
		entity.setCustomName(Component.literal((sourceentity.getDisplayName().getString() + "'s " + entity.getDisplayName().getString())));
	}
}
