package com.mrzero784.kaleidoscope_japanese_food.client;

import com.mrzero784.kaleidoscope_japanese_food.KaleidoscopeJapaneseFood;
import com.mrzero784.kaleidoscope_japanese_food.client.render.WoodenPlateRenderer;
import com.mrzero784.kaleidoscope_japanese_food.init.ModBlockEntities;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ClientModEvents {
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        KaleidoscopeJapaneseFood.LOGGER.info("Registering WoodenPlateRenderer for WOODEN_PLATE BlockEntity...");
        event.registerBlockEntityRenderer(ModBlockEntities.WOODEN_PLATE.get(), WoodenPlateRenderer::new);
        KaleidoscopeJapaneseFood.LOGGER.info("Registered WoodenPlateRenderer successfully!");

        KaleidoscopeJapaneseFood.LOGGER.info("Registering SushiDiscRenderer for SUSHI_DISC BlockEntity...");
        event.registerBlockEntityRenderer(ModBlockEntities.SUSHI_DISC.get(), com.mrzero784.kaleidoscope_japanese_food.client.render.SushiDiscRenderer::new);
        KaleidoscopeJapaneseFood.LOGGER.info("Registered SushiDiscRenderer successfully!");
    }
}
