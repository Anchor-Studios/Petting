package net.petting.procedures;

import net.petting.network.PettingModVariables;
import net.petting.init.PettingModGameRules;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

@EventBusSubscriber
public class GameruleDefaultAllowNewPlayersProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!entity.getPersistentData().getBoolean("firsttimepetting")) {
			entity.getPersistentData().putBoolean("firsttimepetting", true);
			{
				PettingModVariables.PlayerVariables _vars = entity.getData(PettingModVariables.PLAYER_VARIABLES);
				_vars.allowpeting = world instanceof ServerLevel _serverLevelGR2 && _serverLevelGR2.getGameRules().getBoolean(PettingModGameRules.ALLOW_PETTING_BY_DEFAULT);
				_vars.syncPlayerVariables(entity);
			}
		}
	}
}
