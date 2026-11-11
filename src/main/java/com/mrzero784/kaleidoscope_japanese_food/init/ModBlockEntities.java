package com.mrzero784.kaleidoscope_japanese_food.init;

import com.mrzero784.kaleidoscope_japanese_food.KaleidoscopeJapaneseFood;
import com.mrzero784.kaleidoscope_japanese_food.block.entity.WoodenPlateBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, KaleidoscopeJapaneseFood.MOD_ID);

    public static final Supplier<BlockEntityType<WoodenPlateBlockEntity>> WOODEN_PLATE =
            BLOCK_ENTITIES.register("wooden_plate",
                    () -> BlockEntityType.Builder.of(WoodenPlateBlockEntity::new, ModBlocks.WOODEN_PLATE_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<com.mrzero784.kaleidoscope_japanese_food.block.entity.SushiDiscBlockEntity>> SUSHI_DISC =
            BLOCK_ENTITIES.register("sushi_disc",
                    () -> BlockEntityType.Builder.of(com.mrzero784.kaleidoscope_japanese_food.block.entity.SushiDiscBlockEntity::new, ModBlocks.SUSHI_DISC_BLOCK.get()).build(null));
}

