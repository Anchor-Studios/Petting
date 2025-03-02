
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.petting.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PettingModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> PET_FRIENDLY_FIRE = GameRules.register("petFriendlyFire", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.IntegerValue> PET_TAMEABLE_HEALTH_PERCENTAGE = GameRules.register("petTameableHealthPercentage", GameRules.Category.MISC, GameRules.IntegerValue.create(100));
}
