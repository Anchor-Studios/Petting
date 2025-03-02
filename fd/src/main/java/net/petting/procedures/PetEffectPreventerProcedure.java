package net.petting.procedures;

import org.checkerframework.checker.units.qual.s;

import net.petting.init.PettingModGameRules;

import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PetEffectPreventerProcedure {
	@SubscribeEvent
	public static void onMobEffectEvent(MobEffectEvent.Added event) {
		if (event != null && event.getEntity() != null && event.getEffectInstance() != null) {
			String effect = event.getEffectInstance().toString();
			int level = new Object() {
				int convert(String s) {
					try {
						return (int) Double.parseDouble(s.trim());
					} catch (Exception e) {
					}
					return 0;
				}
			}.convert(effect.substring(effect.indexOf("x ") + "x ".length(), effect.indexOf(",")));
			level = Math.max(1, level);
			int duration = new Object() {
				int convert(String s) {
					try {
						return (int) Double.parseDouble(s.trim());
					} catch (Exception e) {
					}
					return 0;
				}
			}.convert(effect.substring(effect.indexOf("Duration: ") + 10, effect.length()));
			effect = effect.replace("effect.", "").replace(".", ":").replace(",", "");
			effect = effect.substring(0, effect.indexOf(" "));
			execute(event, event.getEntity().level(), event.getEntity(), event.getEffectSource());
		}
	}

	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		execute(null, world, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (((sourceentity.getPersistentData().getString("owneruuid")).equals(entity.getPersistentData().getString("owneruuid")) && !world.getLevelData().getGameRules().getBoolean(PettingModGameRules.PET_FRIENDLY_FIRE)
				|| (sourceentity.getPersistentData().getString("owneruuid")).equals(entity.getStringUUID())) && sourceentity.getPersistentData().getBoolean("isTamed")) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeAllEffects();
		}
	}
}
