
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
	public static GameRules.Key<GameRules.IntegerValue> MAXIMUM_HEALTH_PERCENTAGE_TO_TAME;
	public static GameRules.Key<GameRules.IntegerValue> CHANCE_TO_TAME;
	public static GameRules.Key<GameRules.BooleanValue> HEAL_MOB_WHEN_TAMED;
	public static GameRules.Key<GameRules.IntegerValue> MAXIMUM_TAME_COUNT;
	public static GameRules.Key<GameRules.BooleanValue> ALLOW_PETTING_BY_DEFAULT;

	@SubscribeEvent
	public static void registerGameRules(FMLCommonSetupEvent event) {
		MAXIMUM_HEALTH_PERCENTAGE_TO_TAME = GameRules.register("maximumHealthPercentageToTame", GameRules.Category.MOBS, GameRules.IntegerValue.create(100));
		CHANCE_TO_TAME = GameRules.register("chanceToTame", GameRules.Category.PLAYER, GameRules.IntegerValue.create(100));
		HEAL_MOB_WHEN_TAMED = GameRules.register("healMobWhenTamed", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
		MAXIMUM_TAME_COUNT = GameRules.register("maximumTameCount", GameRules.Category.PLAYER, GameRules.IntegerValue.create(-1));
		ALLOW_PETTING_BY_DEFAULT = GameRules.register("allowPettingByDefault", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
	}
}
