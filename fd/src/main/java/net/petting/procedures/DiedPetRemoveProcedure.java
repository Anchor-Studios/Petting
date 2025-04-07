package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

import java.util.UUID;

@EventBusSubscriber
public class DiedPetRemoveProcedure {
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getBoolean("tamed")) {
			if (!(entityFromStringUUID((entity.getPersistentData().getString("owneruuid")), (Level) world) == null)) {
				entityFromStringUUID((entity.getPersistentData().getString("owneruuid")), (Level) world).getPersistentData().putString("petlist",
						((entityFromStringUUID((entity.getPersistentData().getString("owneruuid")), (Level) world).getPersistentData().getString("petlist")).replaceAll(entity.getStringUUID(), "")));
			} else {
				if ((",").equals(PettingModVariables.MapVariables.get(world).petdelete) || ("\"\"").equals(PettingModVariables.MapVariables.get(world).petdelete) || ("").equals(PettingModVariables.MapVariables.get(world).petdelete)) {
					PettingModVariables.MapVariables.get(world).petdelete = entity.getPersistentData().getString("owneruuid") + "--" + entity.getStringUUID();
					PettingModVariables.MapVariables.get(world).syncData(world);
				} else {
					PettingModVariables.MapVariables.get(world).petdelete = "," + entity.getPersistentData().getString("owneruuid") + "--" + entity.getStringUUID();
					PettingModVariables.MapVariables.get(world).syncData(world);
				}
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
