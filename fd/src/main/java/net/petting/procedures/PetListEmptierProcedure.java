package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PetListEmptierProcedure {
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
		entity.getPersistentData().putString("petlist", ((entity.getPersistentData().getString("petlist")).replaceAll(",,", ",")));
		PettingModVariables.MapVariables.get(world).petdelete = PettingModVariables.MapVariables.get(world).petdelete.replaceAll(",,", ",");
		PettingModVariables.MapVariables.get(world).syncData(world);
		if ((",").equals(entity.getPersistentData().getString("petlist"))) {
			entity.getPersistentData().putString("petlist", "");
		}
	}
}
