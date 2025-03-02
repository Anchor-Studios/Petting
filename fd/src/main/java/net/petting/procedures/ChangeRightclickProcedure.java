package net.petting.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ChangeRightclickProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getTarget(), event.getEntity());
	}

	public static void execute(Entity entity, Entity sourceentity) {
		execute(null, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (sourceentity.getPersistentData().getBoolean("tdwait") || sourceentity.getPersistentData().getBoolean("adwait") || sourceentity.getPersistentData().getBoolean("fdwait")) {
			if (!((sourceentity.getStringUUID()).equals(entity instanceof LivingEntity _teamEnt && _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()) != null
					? _teamEnt.level().getScoreboard().getPlayersTeam(_teamEnt instanceof Player _pl ? _pl.getGameProfile().getName() : _teamEnt.getStringUUID()).getName()
					: ""))) {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("\u00A74You can't modify other's pets. Modify request cancelled."), false);
				if (sourceentity.getPersistentData().getBoolean("tdwait")) {
					sourceentity.getPersistentData().putBoolean("tdwait", false);
				}
				if (sourceentity.getPersistentData().getBoolean("fdwait")) {
					sourceentity.getPersistentData().putBoolean("fdwait", false);
				}
				if (sourceentity.getPersistentData().getBoolean("adwait")) {
					sourceentity.getPersistentData().putBoolean("adwait", false);
				}
			} else {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("\u00A72Pet settings modified."), false);
				if (sourceentity.getPersistentData().getBoolean("tdwait")) {
					sourceentity.getPersistentData().putBoolean("tdwait", false);
					entity.getPersistentData().putDouble("tpdistance", (sourceentity.getPersistentData().getDouble("tdupdate")));
				}
				if (sourceentity.getPersistentData().getBoolean("fdwait")) {
					sourceentity.getPersistentData().putBoolean("fdwait", false);
					entity.getPersistentData().putDouble("followdistance", (sourceentity.getPersistentData().getDouble("fdupdate")));
				}
				if (sourceentity.getPersistentData().getBoolean("adwait")) {
					sourceentity.getPersistentData().putBoolean("adwait", false);
					entity.getPersistentData().putDouble("attackcalldistance", (sourceentity.getPersistentData().getDouble("adupdate")));
				}
			}
		}
	}
}
