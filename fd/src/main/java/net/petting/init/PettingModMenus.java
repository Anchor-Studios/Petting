
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.petting.init;

import net.petting.world.inventory.PetSettingsGUIMenu;
import net.petting.world.inventory.FollowSettingsGUIMenu;
import net.petting.world.inventory.AttackSettingsGUIMenu;
import net.petting.PettingMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;

public class PettingModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, PettingMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<PetSettingsGUIMenu>> PET_SETTINGS_GUI = REGISTRY.register("pet_settings_gui", () -> IMenuTypeExtension.create(PetSettingsGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<FollowSettingsGUIMenu>> FOLLOW_SETTINGS_GUI = REGISTRY.register("follow_settings_gui", () -> IMenuTypeExtension.create(FollowSettingsGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<AttackSettingsGUIMenu>> ATTACK_SETTINGS_GUI = REGISTRY.register("attack_settings_gui", () -> IMenuTypeExtension.create(AttackSettingsGUIMenu::new));
}
