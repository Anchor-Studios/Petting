package net.petting.procedures;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class EntityFollowSourceProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		entity.getPersistentData().putString("target", "");
		if (entity instanceof Mob _entity)
			_entity.getNavigation().moveTo((sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), 1.2);
		entity.getPersistentData().putBoolean("following", true);
	}
}
