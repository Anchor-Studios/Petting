package net.petting.procedures;

import net.petting.network.PettingModVariables;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import java.util.regex.Pattern;

@EventBusSubscriber
public class GlobalPetRemoverProcedure {
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
		String owner = "";
		String pet = "";
		String newiteritor = "";
		String olditeritor = "";
		owner = "";
		{
			String[] _array = PettingModVariables.MapVariables.get(world).petdelete.split(Pattern.quote(","));
			if (_array.length != 0) {
				for (String stringiterator : _array) {
					if (stringiterator.contains(entity.getStringUUID())) {
						newiteritor = stringiterator;
					}
				}
			} else {
				String stringiterator = PettingModVariables.MapVariables.get(world).petdelete;
				for (int _yourmother = 0; _yourmother < 1; _yourmother++) {
					if (stringiterator.contains(entity.getStringUUID())) {
						newiteritor = stringiterator;
					}
				}
			}
		}
		{
			String[] _array = newiteritor.split(Pattern.quote("--"));
			if (_array.length != 0) {
				for (String stringiterator : _array) {
					if ((owner).equals("")) {
						owner = stringiterator;
					} else {
						pet = stringiterator;
					}
				}
			} else {
				String stringiterator = newiteritor;
				for (int _yourmother = 0; _yourmother < 1; _yourmother++) {
					if ((owner).equals("")) {
						owner = stringiterator;
					} else {
						pet = stringiterator;
					}
				}
			}
		}
		if ((entity.getStringUUID()).equals(owner)) {
			PettingModVariables.MapVariables.get(world).petdelete = PettingModVariables.MapVariables.get(world).petdelete.replaceAll(owner + "--" + pet, "");
			PettingModVariables.MapVariables.get(world).syncData(world);
			entity.getPersistentData().putString("petlist", ((entity.getPersistentData().getString("petlist")).replaceAll(pet, "")));
		}
	}
}
