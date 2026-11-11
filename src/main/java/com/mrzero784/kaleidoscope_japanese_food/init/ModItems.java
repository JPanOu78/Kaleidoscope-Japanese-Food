package com.mrzero784.kaleidoscope_japanese_food.init;

import com.mrzero784.kaleidoscope_japanese_food.KaleidoscopeJapaneseFood;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeJapaneseFood.MOD_ID);

    // ==================== อาหารจานเดียว & ซาชิมิ (Dishes & Sashimi without rice) ====================
    // ทามาโกะยากิทั้งท่อน (Tamagoyaki / ไข่ม้วนญี่ปุ่น)
    public static final DeferredItem<Item> TAMAGOYAKI = ITEMS.registerItem("tamagoyaki",
            properties -> new BlockItem(ModBlocks.TAMAGOYAKI_BLOCK.get(), properties),
            new Item.Properties().food(ModFoods.TAMAGOYAKI));

    // ชิ้นทามาโกะยากิ (Tamagoyaki Slice)
    public static final DeferredItem<Item> TAMAGOYAKI_SLICE = ITEMS.registerItem("tamagoyaki_slice",
            Item::new,
            new Item.Properties().food(ModFoods.TAMAGOYAKI_SLICE));

    // ถาดไม้เปล่า (Wooden Plate - Dynamic Serving for All Sushi & Dishes)
    public static final DeferredItem<Item> WOODEN_PLATE = ITEMS.registerItem("wooden_plate",
            properties -> new BlockItem(ModBlocks.WOODEN_PLATE_BLOCK.get(), properties),
            new Item.Properties().stacksTo(64));

    // จานซูชิ (Sushi Disc - Stacks up to 10 layers, holds 2 sushi pieces)
    public static final DeferredItem<Item> SUSHI_DISC = ITEMS.registerItem("sushi_disc",
            properties -> new BlockItem(ModBlocks.SUSHI_DISC_BLOCK.get(), properties),
            new Item.Properties().stacksTo(64));

    // ==================== หมวดหมู่ซูชิ (Sushi Category) ====================
    // เสื่อทำอาหาร (Cooking Mat)
    public static final DeferredItem<Item> COOKING_MAT = ITEMS.registerItem("cooking_mat",
            properties -> new BlockItem(ModBlocks.COOKING_MAT_BLOCK.get(), properties),
            new Item.Properties().stacksTo(64));

    // เสื่อปั้นซูชิ (Sushi Mat - alias)
    public static final DeferredItem<Item> SUSHI_MAT = ITEMS.registerItem("sushi_mat",
            properties -> new BlockItem(ModBlocks.SUSHI_MAT_BLOCK.get(), properties),
            new Item.Properties().stacksTo(64));

    // ก้อนข้าวซูชิ (Sushi Rice)
    public static final DeferredItem<Item> SUSHI_RICE = ITEMS.registerItem("sushi_rice",
            Item::new,
            new Item.Properties().food(ModFoods.SUSHI_RICE));

    // ชิ้นแซลมอนซูชิ (Salmon Sushi Slice)
    public static final DeferredItem<Item> SALMON_SUSHI_SLICE = ITEMS.registerItem("salmon_sushi_slice",
            Item::new,
            new Item.Properties().food(ModFoods.SALMON_SUSHI_SLICE));

    // ซูชิแซลมอน (Salmon Sushi)
    public static final DeferredItem<Item> SALMON_SUSHI = ITEMS.registerItem("salmon_sushi",
            Item::new,
            new Item.Properties().food(ModFoods.SALMON_SUSHI));

    // ชิ้นปลาค็อดซูชิ (Cod Sushi Slice)
    public static final DeferredItem<Item> COD_SUSHI_SLICE = ITEMS.registerItem("cod_sushi_slice",
            Item::new,
            new Item.Properties().food(ModFoods.COD_SUSHI_SLICE));

    // ซูชิปลาค็อด (Cod Sushi)
    public static final DeferredItem<Item> COD_SUSHI = ITEMS.registerItem("cod_sushi",
            Item::new,
            new Item.Properties().food(ModFoods.COD_SUSHI));

    // ชิ้นปลาปักเป้าแล่บาง (Fugu Sushi Slice)
    public static final DeferredItem<Item> FUGU_SUSHI_SLICE = ITEMS.registerItem("fugu_sushi_slice",
            Item::new,
            new Item.Properties().food(ModFoods.FUGU_SUSHI_SLICE));

    // ซูชิปลาปักเป้าล้างพิษ (Fugu Nigiri)
    public static final DeferredItem<Item> FUGU_SUSHI = ITEMS.registerItem("fugu_sushi",
            Item::new,
            new Item.Properties().food(ModFoods.FUGU_SUSHI));

    // ชิ้นไข่หวานสำหรับซูชิ (Tamago Sushi Slice)
    public static final DeferredItem<Item> TAMAGO_SUSHI_SLICE = ITEMS.registerItem("tamago_sushi_slice",
            Item::new,
            new Item.Properties().food(ModFoods.TAMAGO_SUSHI_SLICE));

    // ซูชิไข่หวาน (Tamago Sushi)
    public static final DeferredItem<Item> TAMAGO_SUSHI = ITEMS.registerItem("tamago_sushi",
            Item::new,
            new Item.Properties().food(ModFoods.TAMAGO_SUSHI));
}
