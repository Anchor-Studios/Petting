package net.petting.procedures;

import net.petting.init.PettingModItems;
import net.petting.init.PettingModGameRules;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

@EventBusSubscriber
public class NPSProjectileProjectileHitsLivingEntityProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getLevel(), event.getTarget(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		execute(null, world, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		ItemStack sphere = ItemStack.EMPTY;
		double maxtamehealth = 0;
		sphere = new ItemStack(PettingModItems.GOLDEN_WHEAT.get()).copy();
		if ((sphere.getItem() == (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem()
				|| sphere.getItem() == (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()) && !(entity instanceof Player || entity instanceof ServerPlayer)
				&& !((entity instanceof TamableAnimal _tamEnt ? _tamEnt.isTame() : false) || entity.getPersistentData().getBoolean("isTamed"))
				&& ((entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * (world.getLevelData().getGameRules().getInt(PettingModGameRules.PET_TAMEABLE_HEALTH_PERCENTAGE)))
						/ 100 >= (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1)) {
			if (entity instanceof TamableAnimal) {
				if (entity instanceof TamableAnimal _toTame && sourceentity instanceof Player _owner)
					_toTame.tame(_owner);
			} else {
				SourceTameEntityProcedure.execute(entity, sourceentity);
			}
			if (sphere.getItem() == (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()) {
				if (sourceentity instanceof LivingEntity _entity) {
					ItemStack _setstack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
					_setstack.setCount((int) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getCount() - 1));
					_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
					if (_entity instanceof Player _player)
						_player.getInventory().setChanged();
				}
				if (sourceentity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.MAIN_HAND, true);
			} else if (sphere.getItem() == (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem()) {
				if (sourceentity instanceof LivingEntity _entity) {
					ItemStack _setstack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).copy();
					_setstack.setCount((int) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getCount() - 1));
					_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
					if (_entity instanceof Player _player)
						_player.getInventory().setChanged();
				}
				if (sourceentity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.OFF_HAND, true);
			} else {
				if (sourceentity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.MAIN_HAND, true);
				if (sourceentity instanceof Player _player) {
					ItemStack _stktoremove = sphere;
					_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
				}
			}
		}
	}
}
