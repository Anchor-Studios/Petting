
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.petting.init;

import net.petting.client.gui.PetSettingsGUIScreen;
import net.petting.client.gui.FollowSettingsGUIScreen;
import net.petting.client.gui.AttackSettingsGUIScreen;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PettingModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(PettingModMenus.PET_SETTINGS_GUI.get(), PetSettingsGUIScreen::new);
		event.register(PettingModMenus.FOLLOW_SETTINGS_GUI.get(), FollowSettingsGUIScreen::new);
		event.register(PettingModMenus.ATTACK_SETTINGS_GUI.get(), AttackSettingsGUIScreen::new);
	}
}
