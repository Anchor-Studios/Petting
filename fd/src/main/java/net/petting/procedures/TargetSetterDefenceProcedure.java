package net.petting.procedures;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

import java.util.regex.Pattern;
import java.util.UUID;

@EventBusSubscriber
public class TargetSetterDefenceProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getEntity(), event.getSource().getEntity());
		}
	}

	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		execute(null, world, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (1 <= ReturnTamedCountProcedure.execute(entity)) {
			{
				String[] _array = (entity.getPersistentData().getString("petlist")).split(Pattern.quote(","));
				if (_array.length != 0) {
					for (String stringiterator : _array) {
						if (!(entityFromStringUUID(stringiterator, (Level) world) == null)
								&& (entityFromStringUUID(stringiterator, (Level) world) != null ? sourceentity.distanceTo(entityFromStringUUID(stringiterator, (Level) world)) : -1) <= entityFromStringUUID(stringiterator, (Level) world)
										.getPersistentData().getDouble("attackdistance")
								&& ShouldEntityAttackmodeAttackSourceProcedure.execute(world, entityFromStringUUID(stringiterator, (Level) world), sourceentity)) {
							entityFromStringUUID(stringiterator, (Level) world).getPersistentData().putString("target", (sourceentity.getStringUUID()));
						}
					}
				} else {
					String stringiterator = (entity.getPersistentData().getString("petlist"));
					for (int _yourmother = 0; _yourmother < 1; _yourmother++) {
						if (!(entityFromStringUUID(stringiterator, (Level) world) == null)
								&& (entityFromStringUUID(stringiterator, (Level) world) != null ? sourceentity.distanceTo(entityFromStringUUID(stringiterator, (Level) world)) : -1) <= entityFromStringUUID(stringiterator, (Level) world)
										.getPersistentData().getDouble("attackdistance")
								&& ShouldEntityAttackmodeAttackSourceProcedure.execute(world, entityFromStringUUID(stringiterator, (Level) world), sourceentity)) {
							entityFromStringUUID(stringiterator, (Level) world).getPersistentData().putString("target", (sourceentity.getStringUUID()));
						}
					}
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
