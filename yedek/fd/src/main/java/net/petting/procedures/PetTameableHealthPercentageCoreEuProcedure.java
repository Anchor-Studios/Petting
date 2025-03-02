package net.petting.procedures;

import net.petting.init.PettingModGameRules;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PetTameableHealthPercentageCoreEuProcedure {
	@SubscribeEvent
	public static void onWorldTick(TickEvent.LevelTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.level);
		}
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
