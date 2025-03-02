package net.petting.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ModfyRequestNullerProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getBoolean("tdwait")) {
			entity.getPersistentData().putBoolean("tdwait", false);
		}
		if (entity.getPersistentData().getBoolean("fdwait")) {
			entity.getPersistentData().putBoolean("fdwait", false);
		}
		if (entity.getPersistentData().getBoolean("adwait")) {
			entity.getPersistentData().putBoolean("adwait", false);
		}
	}
}
