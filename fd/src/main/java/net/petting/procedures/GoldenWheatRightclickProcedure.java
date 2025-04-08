package net.petting.procedures;

import net.petting.network.PettingModVariables;
import net.petting.init.PettingModItems;
import net.petting.init.PettingModGameRules;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;

@EventBusSubscriber
public class GoldenWheatRightclickProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getTarget(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (PettingModItems.GOLDEN_WHEAT.get() == (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() && !sourceentity.isShiftKeyDown()) {
			if ((entity instanceof LivingEntity _livEnt
					? _livEnt.getHealth()
					: -1) <= ((entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * (world instanceof ServerLevel _serverLevelGR5 ? _serverLevelGR5.getGameRules().getInt(PettingModGameRules.MAXIMUM_HEALTH_PERCENTAGE_TO_TAME) : 0))
							/ 100
					&& ((world instanceof ServerLevel _serverLevelGR6 ? _serverLevelGR6.getGameRules().getInt(PettingModGameRules.MAXIMUM_TAME_COUNT) : 0) == -1
							|| ReturnTamedCountProcedure.execute(sourceentity) < (world instanceof ServerLevel _serverLevelGR7 ? _serverLevelGR7.getGameRules().getInt(PettingModGameRules.MAXIMUM_TAME_COUNT) : 0))
					&& sourceentity.getData(PettingModVariables.PLAYER_VARIABLES).allowpeting && !entity.getPersistentData().getBoolean("tamed") && !(entity instanceof Player || entity instanceof ServerPlayer)) {
				if (!(getEntityGameType(sourceentity) == GameType.CREATIVE)) {
					if (sourceentity instanceof LivingEntity _entity) {
						ItemStack _setstack = new ItemStack(PettingModItems.GOLDEN_WHEAT.get()).copy();
						_setstack.setCount((int) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getCount() - 1));
						_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
						if (_entity instanceof Player _player)
							_player.getInventory().setChanged();
					}
				}
				if (sourceentity instanceof LivingEntity _entity)
					_entity.swing(InteractionHand.MAIN_HAND, true);
				if (getEntityGameType(sourceentity) == GameType.CREATIVE || 100 * Math.random() <= (world instanceof ServerLevel _serverLevelGR17 ? _serverLevelGR17.getGameRules().getInt(PettingModGameRules.CHANCE_TO_TAME) : 0)
						&& 0 != (world instanceof ServerLevel _serverLevelGR18 ? _serverLevelGR18.getGameRules().getInt(PettingModGameRules.CHANCE_TO_TAME) : 0)) {
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.HEART, x, y, z, (int) (2 + Math.round(3 * Math.random())), 1, 1, 1, 1);
					TameEntityforSourceProcedure.execute(world, entity, sourceentity);
				} else {
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.ANGRY_VILLAGER, x, y, z, (int) (2 + Math.round(3 * Math.random())), 1, 1, 1, 1);
				}
			}
		}
	}

	private static GameType getEntityGameType(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer();
		} else if (entity instanceof Player player && player.level().isClientSide()) {
			PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId());
			if (playerInfo != null)
				return playerInfo.getGameMode();
		}
		return null;
	}
}
