package net.petting.procedures;

import net.petting.init.PettingModGameRules;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PetHurtPreventerProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent.Post event) {
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
		if (((sourceentity.getPersistentData().getString("owneruuid")).equals(entity.getPersistentData().getString("owneruuid")) && !world.getLevelData().getGameRules().getBoolean(PettingModGameRules.PET_FRIENDLY_FIRE)
				|| (sourceentity.getPersistentData().getString("owneruuid")).equals(entity.getStringUUID())) && sourceentity.getPersistentData().getBoolean("isTamed")) {
			if (event instanceof LivingIncomingDamageEvent _hurt) {
				_hurt.setAmount(0);
			}
		}
	}
}
