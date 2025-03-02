
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.petting.init;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.GameRules;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class PettingModGameRules {
	public static GameRules.Key<GameRules.BooleanValue> PET_FRIENDLY_FIRE;
	public static GameRules.Key<GameRules.IntegerValue> PET_TAMEABLE_HEALTH_PERCENTAGE;

	@SubscribeEvent
	public static void registerGameRules(FMLCommonSetupEvent event) {
		PET_FRIENDLY_FIRE = GameRules.register("petFriendlyFire", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
		PET_TAMEABLE_HEALTH_PERCENTAGE = GameRules.register("petTameableHealthPercentage", GameRules.Category.MISC, GameRules.IntegerValue.create(100));
	}
}
