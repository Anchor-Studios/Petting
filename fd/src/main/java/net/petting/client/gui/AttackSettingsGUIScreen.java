package net.petting.client.gui;

import net.petting.world.inventory.AttackSettingsGUIMenu;
import net.petting.procedures.ReturnPetNickProcedure;
import net.petting.procedures.ReturnAttackmodeNonplayerProcedure;
import net.petting.procedures.ReturnAttackmodeNoneProcedure;
import net.petting.procedures.ReturnAttackmodeEveryoneProcedure;
import net.petting.network.AttackSettingsGUIButtonMessage;

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
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class AttackSettingsGUIScreen extends AbstractContainerScreen<AttackSettingsGUIMenu> {
	private final static HashMap<String, Object> guistate = AttackSettingsGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_attack_everyone;
	Button button_attack_everyone1;
	Button button_attack_everyone2;
	ImageButton imagebutton_followsectionclose;
	ImageButton imagebutton_infosectionclose;

	public AttackSettingsGUIScreen(AttackSettingsGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 46;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("petting:textures/screens/attack_settings_gui.png");

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

		guiGraphics.blit(RenderType::guiTextured, ResourceLocation.parse("petting:textures/screens/attacksectionopen.png"), this.leftPos + 54, this.topPos + -28, 0, 0, 26, 32, 26, 32);

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
	}

	@Override
	public void init() {
		super.init();
		button_attack_everyone = Button.builder(Component.translatable("gui.petting.attack_settings_gui.button_attack_everyone"), e -> {
			if (ReturnAttackmodeEveryoneProcedure.execute(world, entity)) {
				PacketDistributor.sendToServer(new AttackSettingsGUIButtonMessage(0, x, y, z));
				AttackSettingsGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 35, this.topPos + 19, 103, 20).build(builder -> new Button(builder) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
				this.visible = ReturnAttackmodeEveryoneProcedure.execute(world, entity);
				super.renderWidget(guiGraphics, gx, gy, ticks);
			}
		});
		guistate.put("button:button_attack_everyone", button_attack_everyone);
		this.addRenderableWidget(button_attack_everyone);
		button_attack_everyone1 = Button.builder(Component.translatable("gui.petting.attack_settings_gui.button_attack_everyone1"), e -> {
			if (ReturnAttackmodeNonplayerProcedure.execute(world, entity)) {
				PacketDistributor.sendToServer(new AttackSettingsGUIButtonMessage(1, x, y, z));
				AttackSettingsGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}).bounds(this.leftPos + 35, this.topPos + 19, 103, 20).build(builder -> new Button(builder) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
				this.visible = ReturnAttackmodeNonplayerProcedure.execute(world, entity);
				super.renderWidget(guiGraphics, gx, gy, ticks);
			}
		});
		guistate.put("button:button_attack_everyone1", button_attack_everyone1);
		this.addRenderableWidget(button_attack_everyone1);
		button_attack_everyone2 = Button.builder(Component.translatable("gui.petting.attack_settings_gui.button_attack_everyone2"), e -> {
			if (ReturnAttackmodeNoneProcedure.execute(world, entity)) {
				PacketDistributor.sendToServer(new AttackSettingsGUIButtonMessage(2, x, y, z));
				AttackSettingsGUIButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}).bounds(this.leftPos + 35, this.topPos + 19, 103, 20).build(builder -> new Button(builder) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int gx, int gy, float ticks) {
				this.visible = ReturnAttackmodeNoneProcedure.execute(world, entity);
				super.renderWidget(guiGraphics, gx, gy, ticks);
			}
		});
		guistate.put("button:button_attack_everyone2", button_attack_everyone2);
		this.addRenderableWidget(button_attack_everyone2);
		imagebutton_followsectionclose = new ImageButton(this.leftPos + 27, this.topPos + -29, 26, 32,
				new WidgetSprites(ResourceLocation.parse("petting:textures/screens/followsectionclose.png"), ResourceLocation.parse("petting:textures/screens/followsectionclose.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new AttackSettingsGUIButtonMessage(3, x, y, z));
						AttackSettingsGUIButtonMessage.handleButtonAction(entity, 3, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_followsectionclose", imagebutton_followsectionclose);
		this.addRenderableWidget(imagebutton_followsectionclose);
		imagebutton_infosectionclose = new ImageButton(this.leftPos + 0, this.topPos + -29, 26, 32,
				new WidgetSprites(ResourceLocation.parse("petting:textures/screens/infosectionclose.png"), ResourceLocation.parse("petting:textures/screens/infosectionclose.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new AttackSettingsGUIButtonMessage(4, x, y, z));
						AttackSettingsGUIButtonMessage.handleButtonAction(entity, 4, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_infosectionclose", imagebutton_infosectionclose);
		this.addRenderableWidget(imagebutton_infosectionclose);
	}
}
