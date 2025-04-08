package net.petting.client.gui;

import net.petting.world.inventory.FollowSettingsGUIMenu;
import net.petting.procedures.ReturnTeleportOwnerProcedure;
import net.petting.procedures.ReturnPetTeleportDistanceProcedure;
import net.petting.procedures.ReturnPetNickProcedure;
import net.petting.procedures.ReturnPetFollowDistanceProcedure;
import net.petting.procedures.ReturnNotTeleportOwnerProcedure;
import net.petting.procedures.ReturnNotFollowOwnerProcedure;
import net.petting.procedures.ReturnFollowOwnerProcedure;
import net.petting.network.FollowSettingsGUIButtonMessage;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class FollowSettingsGUIScreen extends AbstractContainerScreen<FollowSettingsGUIMenu> {
	private final static HashMap<String, Object> guistate = FollowSettingsGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_attacksectionclose;
	ImageButton imagebutton_infosectionclose;
	ImageButton imagebutton_plus_ico;
	ImageButton imagebutton_plus_ico1;
	ImageButton imagebutton_minus_ico;
	ImageButton imagebutton_cross_ico1;
	ImageButton imagebutton_minus_ico1;
	ImageButton imagebutton_cross_ico;
	ImageButton imagebutton_tick_ico1;
	ImageButton imagebutton_tick_ico;

	public FollowSettingsGUIScreen(FollowSettingsGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 94;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("petting:textures/screens/follow_settings_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(RenderType::guiTextured, texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		guiGraphics.blit(RenderType::guiTextured, ResourceLocation.parse("petting:textures/screens/followsectionopen.png"), this.leftPos + 27, this.topPos + -28, 0, 0, 26, 32, 26, 32);

		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font,

				ReturnPetNickProcedure.execute(world, entity), 5, 5, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.petting.follow_settings_gui.label_follow_owner"), 22, 23, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.petting.follow_settings_gui.label_teleport_owner"), 22, 58, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.petting.follow_settings_gui.label_follow_distance"), 6, 39, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.petting.follow_settings_gui.label_teleport_distance"), 6, 75, -12829636, false);
		guiGraphics.drawString(this.font,

				ReturnPetFollowDistanceProcedure.execute(world, entity), 118, 39, -12829636, false);
		guiGraphics.drawString(this.font,

				ReturnPetTeleportDistanceProcedure.execute(world, entity), 128, 75, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_attacksectionclose = new ImageButton(this.leftPos + 54, this.topPos + -29, 26, 32,
				new WidgetSprites(ResourceLocation.parse("petting:textures/screens/attacksectionclose.png"), ResourceLocation.parse("petting:textures/screens/attacksectionclose.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(0, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_attacksectionclose", imagebutton_attacksectionclose);
		this.addRenderableWidget(imagebutton_attacksectionclose);
		imagebutton_infosectionclose = new ImageButton(this.leftPos + 0, this.topPos + -29, 26, 32,
				new WidgetSprites(ResourceLocation.parse("petting:textures/screens/infosectionclose.png"), ResourceLocation.parse("petting:textures/screens/infosectionclose.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(1, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_infosectionclose", imagebutton_infosectionclose);
		this.addRenderableWidget(imagebutton_infosectionclose);
		imagebutton_plus_ico = new ImageButton(this.leftPos + 89, this.topPos + 38, 12, 12, new WidgetSprites(ResourceLocation.parse("petting:textures/screens/plus_ico.png"), ResourceLocation.parse("petting:textures/screens/plus_ico_hold.png")),
				e -> {
					if (true) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(2, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_plus_ico", imagebutton_plus_ico);
		this.addRenderableWidget(imagebutton_plus_ico);
		imagebutton_plus_ico1 = new ImageButton(this.leftPos + 99, this.topPos + 74, 12, 12, new WidgetSprites(ResourceLocation.parse("petting:textures/screens/plus_ico.png"), ResourceLocation.parse("petting:textures/screens/plus_ico_hold.png")),
				e -> {
					if (true) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(3, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 3, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_plus_ico1", imagebutton_plus_ico1);
		this.addRenderableWidget(imagebutton_plus_ico1);
		imagebutton_minus_ico = new ImageButton(this.leftPos + 102, this.topPos + 38, 12, 12, new WidgetSprites(ResourceLocation.parse("petting:textures/screens/minus_ico.png"), ResourceLocation.parse("petting:textures/screens/minus_ico_hold.png")),
				e -> {
					if (true) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(4, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 4, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_minus_ico", imagebutton_minus_ico);
		this.addRenderableWidget(imagebutton_minus_ico);
		imagebutton_cross_ico1 = new ImageButton(this.leftPos + 6, this.topPos + 20, 12, 16, new WidgetSprites(ResourceLocation.parse("petting:textures/screens/cross_ico.png"), ResourceLocation.parse("petting:textures/screens/cross_ico_hold.png")),
				e -> {
					if (ReturnNotFollowOwnerProcedure.execute(world, entity)) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(5, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 5, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				if (ReturnNotFollowOwnerProcedure.execute(world, entity))
					guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_cross_ico1", imagebutton_cross_ico1);
		this.addRenderableWidget(imagebutton_cross_ico1);
		imagebutton_minus_ico1 = new ImageButton(this.leftPos + 112, this.topPos + 74, 12, 12, new WidgetSprites(ResourceLocation.parse("petting:textures/screens/minus_ico.png"), ResourceLocation.parse("petting:textures/screens/minus_ico_hold.png")),
				e -> {
					if (true) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(6, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 6, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_minus_ico1", imagebutton_minus_ico1);
		this.addRenderableWidget(imagebutton_minus_ico1);
		imagebutton_cross_ico = new ImageButton(this.leftPos + 6, this.topPos + 55, 12, 16, new WidgetSprites(ResourceLocation.parse("petting:textures/screens/cross_ico.png"), ResourceLocation.parse("petting:textures/screens/cross_ico_hold.png")),
				e -> {
					if (ReturnNotTeleportOwnerProcedure.execute(world, entity)) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(7, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 7, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				if (ReturnNotTeleportOwnerProcedure.execute(world, entity))
					guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_cross_ico", imagebutton_cross_ico);
		this.addRenderableWidget(imagebutton_cross_ico);
		imagebutton_tick_ico1 = new ImageButton(this.leftPos + 6, this.topPos + 55, 12, 16, new WidgetSprites(ResourceLocation.parse("petting:textures/screens/tick_ico.png"), ResourceLocation.parse("petting:textures/screens/tick_ico_hold.png")),
				e -> {
					if (ReturnTeleportOwnerProcedure.execute(world, entity)) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(8, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 8, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				if (ReturnTeleportOwnerProcedure.execute(world, entity))
					guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_tick_ico1", imagebutton_tick_ico1);
		this.addRenderableWidget(imagebutton_tick_ico1);
		imagebutton_tick_ico = new ImageButton(this.leftPos + 6, this.topPos + 20, 12, 16, new WidgetSprites(ResourceLocation.parse("petting:textures/screens/tick_ico.png"), ResourceLocation.parse("petting:textures/screens/tick_ico_hold.png")),
				e -> {
					if (ReturnFollowOwnerProcedure.execute(world, entity)) {
						PacketDistributor.sendToServer(new FollowSettingsGUIButtonMessage(9, x, y, z));
						FollowSettingsGUIButtonMessage.handleButtonAction(entity, 9, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				if (ReturnFollowOwnerProcedure.execute(world, entity))
					guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_tick_ico", imagebutton_tick_ico);
		this.addRenderableWidget(imagebutton_tick_ico);
	}
}
