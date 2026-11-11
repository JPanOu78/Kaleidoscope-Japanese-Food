package com.mrzero784.kaleidoscope_japanese_food.init;

import com.mrzero784.kaleidoscope_japanese_food.KaleidoscopeJapaneseFood;
import com.mrzero784.kaleidoscope_japanese_food.block.*;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeJapaneseFood.MOD_ID);

    // ทามาโกะยากิวางเดี่ยว (Single Tamagoyaki 3D Block)
    public static final DeferredBlock<TamagoyakiBlock> TAMAGOYAKI_BLOCK = BLOCKS.register("tamagoyaki", () -> new TamagoyakiBlock());

    // ถาดไม้เปล่า (Empty Wooden Plate 3D Block - Dynamic Serving for All Sushi & Dishes)
    public static final DeferredBlock<WoodenPlateBlock> WOODEN_PLATE_BLOCK = BLOCKS.register("wooden_plate", () -> new WoodenPlateBlock());

    // จานซูชิ (Sushi Disc 3D Block - Holds 2 sushi pieces, stacks up to 10 layers!)
    public static final DeferredBlock<SushiDiscBlock> SUSHI_DISC_BLOCK = BLOCKS.register("sushi_disc", () -> new SushiDiscBlock());

    // เสื่อทำอาหาร (Cooking Mat 3D Block)
    public static final DeferredBlock<CookingMatBlock> COOKING_MAT_BLOCK = BLOCKS.register("cooking_mat", () -> new CookingMatBlock());

    // เสื่อปั้นซูชิ (Sushi Mat - alias to CookingMatBlock)
    public static final DeferredBlock<SushiMatBlock> SUSHI_MAT_BLOCK = BLOCKS.register("sushi_mat", () -> new SushiMatBlock());

    private ModBlocks() {}
}
