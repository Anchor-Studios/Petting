package net.petting.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

import java.util.UUID;

@EventBusSubscriber
public class PetGetAttackTargetProcedure {
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
		if (!(null == new Object() {
			Entity getEntity(String uuid) {
				Entity _uuidentity = null;
				if (world instanceof ServerLevel _server) {
					try {
						_uuidentity = _server.getEntity(UUID.fromString(uuid));
					} catch (IllegalArgumentException e) {
					}
				}
				return _uuidentity;
			}
		}.getEntity((entity.getPersistentData().getString("owneruuid"))))) {
			if (entity.getPersistentData().getDouble("attackcalldistance") >= (new Object() {
				Entity getEntity(String uuid) {
					Entity _uuidentity = null;
					if (world instanceof ServerLevel _server) {
						try {
							_uuidentity = _server.getEntity(UUID.fromString(uuid));
						} catch (IllegalArgumentException e) {
						}
					}
					return _uuidentity;
				}
			}.getEntity((entity.getPersistentData().getString("owneruuid"))) != null ? entity.distanceTo(new Object() {
				Entity getEntity(String uuid) {
					Entity _uuidentity = null;
					if (world instanceof ServerLevel _server) {
						try {
							_uuidentity = _server.getEntity(UUID.fromString(uuid));
						} catch (IllegalArgumentException e) {
						}
					}
					return _uuidentity;
				}
			}.getEntity((entity.getPersistentData().getString("owneruuid")))) : -1) && null == (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) && entity.getPersistentData().getBoolean("isTamed")
					&& entity.getPersistentData().getBoolean("petattack")) {
				if (!(null == new Object() {
					Entity getEntity(String uuid) {
						Entity _uuidentity = null;
						if (world instanceof ServerLevel _server) {
							try {
								_uuidentity = _server.getEntity(UUID.fromString(uuid));
							} catch (IllegalArgumentException e) {
							}
						}
						return _uuidentity;
					}
				}.getEntity((entity.getPersistentData().getString("attacktarget"))))) {
					if ((entity instanceof ServerPlayer || entity instanceof Player) && !entity.getPersistentData().getBoolean("playerattack")) {
						entity.getPersistentData().putString("petattack", (entity.getPersistentData().getString("attacktarget")));
						if (entity instanceof Mob _entity && new Object() {
							Entity getEntity(String uuid) {
								Entity _uuidentity = null;
								if (world instanceof ServerLevel _server) {
									try {
										_uuidentity = _server.getEntity(UUID.fromString(uuid));
									} catch (IllegalArgumentException e) {
									}
								}
								return _uuidentity;
							}
						}.getEntity((entity.getPersistentData().getString("attacktarget"))) instanceof LivingEntity _ent)
							_entity.setTarget(_ent);
					}
				}
			}
		}
	}
}
