
package net.petting.network;

import net.petting.world.inventory.AttackSettingsGUIMenu;
import net.petting.procedures.ToggleAttackmodeProcedure;
import net.petting.procedures.OpenPetSettingsProcedure;
import net.petting.procedures.OpenFollowSettingsProcedure;
import net.petting.PettingMod;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.BlockPos;

import java.util.HashMap;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record AttackSettingsGUIButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {

	public static final Type<AttackSettingsGUIButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(PettingMod.MODID, "attack_settings_gui_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AttackSettingsGUIButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, AttackSettingsGUIButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new AttackSettingsGUIButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
	@Override
	public Type<AttackSettingsGUIButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final AttackSettingsGUIButtonMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				int buttonID = message.buttonID;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				handleButtonAction(entity, buttonID, x, y, z);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		HashMap guistate = AttackSettingsGUIMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			ToggleAttackmodeProcedure.execute(world, entity);
		}
		if (buttonID == 1) {

			ToggleAttackmodeProcedure.execute(world, entity);
		}
		if (buttonID == 2) {

			ToggleAttackmodeProcedure.execute(world, entity);
		}
		if (buttonID == 3) {

			OpenFollowSettingsProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 4) {

			OpenPetSettingsProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		PettingMod.addNetworkMessage(AttackSettingsGUIButtonMessage.TYPE, AttackSettingsGUIButtonMessage.STREAM_CODEC, AttackSettingsGUIButtonMessage::handleData);
	}
}
