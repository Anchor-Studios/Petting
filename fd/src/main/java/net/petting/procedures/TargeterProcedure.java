package net.petting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

import java.util.UUID;

@EventBusSubscriber
public class TargeterProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getBoolean("tamed")) {
			if (!(null == entityFromStringUUID((entity.getPersistentData().getString("target")), (Level) world))
					&& ShouldEntityAttackmodeAttackSourceProcedure.execute(world, entity, entityFromStringUUID((entity.getPersistentData().getString("target")), (Level) world))) {
				if (entity instanceof Mob _entity && entityFromStringUUID((entity.getPersistentData().getString("target")), (Level) world) instanceof LivingEntity _ent)
					_entity.setTarget(_ent);
			} else if (!(null == (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null))) {
				if (entity instanceof Mob _entity && null instanceof LivingEntity _ent)
					_entity.setTarget(_ent);
				if (entity instanceof Mob) {
					try {
						((Mob) entity).setTarget(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (entity instanceof Mob _entity) {
					_entity.setTarget(null);
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
