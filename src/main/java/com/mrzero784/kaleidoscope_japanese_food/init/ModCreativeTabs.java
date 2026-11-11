package com.mrzero784.kaleidoscope_japanese_food.init;

import com.mrzero784.kaleidoscope_japanese_food.KaleidoscopeJapaneseFood;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeJapaneseFood.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> JAPANESE_FOOD_TAB =
            CREATIVE_MODE_TABS.register("japanese_food_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.kaleidoscope_japanese_food.japanese_food_tab"))
                    .icon(() -> ModItems.SALMON_SUSHI.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // เครื่องครัวและภาชนะ (Utensils & Plates)
                        output.accept(ModItems.WOODEN_PLATE.get());
                        output.accept(ModItems.SUSHI_DISC.get());
                        output.accept(ModItems.COOKING_MAT.get());

                        // หมวดอาหารจานเดียว & ซาชิมิ (Dishes & Sashimi - ไม่มีข้าว)
                        output.accept(ModItems.TAMAGOYAKI.get());
                        output.accept(ModItems.TAMAGOYAKI_SLICE.get());

                        // หมวดซูชิ (Sushi - มีข้าว)
                        output.accept(ModItems.SUSHI_RICE.get());
                        output.accept(ModItems.SALMON_SUSHI_SLICE.get());
                        output.accept(ModItems.SALMON_SUSHI.get());
                        output.accept(ModItems.COD_SUSHI_SLICE.get());
                        output.accept(ModItems.COD_SUSHI.get());
                        output.accept(ModItems.FUGU_SUSHI_SLICE.get());
                        output.accept(ModItems.FUGU_SUSHI.get());
                        output.accept(ModItems.TAMAGO_SUSHI_SLICE.get());
                        output.accept(ModItems.TAMAGO_SUSHI.get());
                    })
                    .build());

    private ModCreativeTabs() {}
}
