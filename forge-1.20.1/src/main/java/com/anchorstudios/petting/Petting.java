package com.anchorstudios.petting;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.UUID;

@Mod(Petting.MODID)
public class Petting {
    public static final String MODID = "petting";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Items
    public static final RegistryObject<Item> GOLDEN_WHEAT = ITEMS.register("golden_wheat",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PETTING_TAB_ICON_ITEM = ITEMS.register("petting_tab",
            () -> new Item(new Item.Properties()));

    // Creative Tab
    public static final RegistryObject<CreativeModeTab> PETTING_TAB = CREATIVE_MODE_TABS.register("petting_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.petting_tab"))
                    .icon(() -> new ItemStack(PETTING_TAB_ICON_ITEM.get()))
                    .displayItems((params, output) -> {
                        output.accept(GOLDEN_WHEAT.get());
                    })
                    .build());

    public Petting(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        context.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new GoldenWheatTamingHandler());
        MinecraftForge.EVENT_BUS.register(new ServerTickHandler());
        MinecraftForge.EVENT_BUS.register(new PetPersistenceHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Petting mod initialized!");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {


    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Petting mod server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Petting mod client setup");
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;

        PetRegistry registry = new PetRegistry(serverLevel);
        registry.cleanup();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;

        Player player = (Player) event.getEntity();
        ServerLevel serverLevel = (ServerLevel) player.level();
        PetRegistry registry = new PetRegistry(serverLevel);

        registry.cleanupPlayerPets(player.getUUID());
        LOGGER.debug("Cleaned up pets for player {}", player.getUUID());
    }

}