package net.petting.procedures;

import net.petting.init.PettingModGameRules;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PetHurtPreventerProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		if (event != null && event.getEntity() != null) {
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
			if (event instanceof LivingHurtEvent _hurt) {
				_hurt.setAmount(0);
			}
		}
	}
}
