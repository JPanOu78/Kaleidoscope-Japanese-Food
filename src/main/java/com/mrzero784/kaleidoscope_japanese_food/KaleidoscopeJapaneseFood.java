package com.mrzero784.kaleidoscope_japanese_food;

import com.mojang.logging.LogUtils;
import com.mrzero784.kaleidoscope_japanese_food.client.ClientModEvents;
import com.mrzero784.kaleidoscope_japanese_food.init.ModBlockEntities;
import com.mrzero784.kaleidoscope_japanese_food.init.ModBlocks;
import com.mrzero784.kaleidoscope_japanese_food.init.ModCreativeTabs;
import com.mrzero784.kaleidoscope_japanese_food.init.ModItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(KaleidoscopeJapaneseFood.MOD_ID)
public class KaleidoscopeJapaneseFood {
    public static final String MOD_ID = "kaleidoscope_japanese_food";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeJapaneseFood(IEventBus modEventBus) {
        LOGGER.info("Kaleidoscope Japanese Food initializing for NeoForge 1.21.1...");

        // Register blocks, items, block entities & creative mode tab
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ClientModEvents::registerRenderers);
        }

        LOGGER.info("Kaleidoscope Japanese Food initialized successfully!");
    }
}
