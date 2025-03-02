package net.petting.procedures;

import net.petting.init.PettingModGameRules;

import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PetTameableHealthPercentageCoreEuProcedure {
	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (100 < (world.getLevelData().getGameRules().getInt(PettingModGameRules.PET_TAMEABLE_HEALTH_PERCENTAGE))) {
			world.getLevelData().getGameRules().getRule(PettingModGameRules.PET_TAMEABLE_HEALTH_PERCENTAGE).set(100, world.getServer());
		} else if (0 > (world.getLevelData().getGameRules().getInt(PettingModGameRules.PET_TAMEABLE_HEALTH_PERCENTAGE))) {
			world.getLevelData().getGameRules().getRule(PettingModGameRules.PET_TAMEABLE_HEALTH_PERCENTAGE).set(0, world.getServer());
		}
	}
}
