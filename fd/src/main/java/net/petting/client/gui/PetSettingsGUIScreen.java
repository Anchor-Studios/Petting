package net.petting.client.gui;

import net.petting.world.inventory.PetSettingsGUIMenu;
import net.petting.procedures.ReturnPetTypeProcedure;
import net.petting.procedures.ReturnPetNickProcedure;
import net.petting.procedures.ReturnPetHealthProcedure;
import net.petting.network.PetSettingsGUIButtonMessage;

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

public class PetSettingsGUIScreen extends AbstractContainerScreen<PetSettingsGUIMenu> {
	private final static HashMap<String, Object> guistate = PetSettingsGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_followsectionclose;
	ImageButton imagebutton_attacksectionclose;

	public PetSettingsGUIScreen(PetSettingsGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 54;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("petting:textures/screens/pet_settings_gui.png");

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

		guiGraphics.blit(RenderType::guiTextured, ResourceLocation.parse("petting:textures/screens/pethealth.png"), this.leftPos + 3, this.topPos + 16, 0, 0, 18, 18, 18, 18);

		guiGraphics.blit(RenderType::guiTextured, ResourceLocation.parse("petting:textures/screens/pettype.png"), this.leftPos + 5, this.topPos + 34, 0, 0, 15, 15, 15, 15);

		guiGraphics.blit(RenderType::guiTextured, ResourceLocation.parse("petting:textures/screens/infosectionopen.png"), this.leftPos + 0, this.topPos + -28, 0, 0, 26, 32, 26, 32);

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
		guiGraphics.drawString(this.font,

				ReturnPetHealthProcedure.execute(world, entity), 23, 20, -12829636, false);
		guiGraphics.drawString(this.font,

				ReturnPetTypeProcedure.execute(world, entity), 23, 37, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_followsectionclose = new ImageButton(this.leftPos + 27, this.topPos + -29, 26, 32,
				new WidgetSprites(ResourceLocation.parse("petting:textures/screens/followsectionclose.png"), ResourceLocation.parse("petting:textures/screens/followsectionclose.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new PetSettingsGUIButtonMessage(0, x, y, z));
						PetSettingsGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_followsectionclose", imagebutton_followsectionclose);
		this.addRenderableWidget(imagebutton_followsectionclose);
		imagebutton_attacksectionclose = new ImageButton(this.leftPos + 54, this.topPos + -29, 26, 32,
				new WidgetSprites(ResourceLocation.parse("petting:textures/screens/attacksectionclose.png"), ResourceLocation.parse("petting:textures/screens/attacksectionclose.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new PetSettingsGUIButtonMessage(1, x, y, z));
						PetSettingsGUIButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(RenderType::guiTextured, sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_attacksectionclose", imagebutton_attacksectionclose);
		this.addRenderableWidget(imagebutton_attacksectionclose);
	}
}
