package com.mrzero784.kaleidoscope_japanese_food.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    // ทามาโกะยากิทั้งท่อน (Tamagoyaki whole roll)
    public static final FoodProperties TAMAGOYAKI = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.6F)
            .build();

    // ชิ้นทามาโกะยากิ (Tamagoyaki slice)
    public static final FoodProperties TAMAGOYAKI_SLICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.4F)
            .fast()
            .build();

    // ==================== หมวดหมู่ซูชิ (Sushi Foods) ====================
    // ข้าวซูชิเปล่า (Sushi Rice)
    public static final FoodProperties SUSHI_RICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.3F)
            .fast()
            .build();

    // ชิ้นแซลมอนซูชิ (Salmon Sushi Slice)
    public static final FoodProperties SALMON_SUSHI_SLICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.4F)
            .fast()
            .build();

    // ซูชิแซลมอน (Salmon Sushi)
    public static final FoodProperties SALMON_SUSHI = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.8F)
            .build();

    // ชิ้นปลาค็อดซูชิ (Cod Sushi Slice)
    public static final FoodProperties COD_SUSHI_SLICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.4F)
            .fast()
            .build();

    // ซูชิปลาค็อด / ปลาเนื้อขาว (Cod Sushi) - บัฟว่ายน้ำเร็วขึ้น (Dolphin's Grace) 30 วินาที
    public static final FoodProperties COD_SUSHI = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.8F)
            .effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 0), 1.0F)
            .build();

    // ชิ้นปลาปักเป้าแล่บาง (Fugu Sushi Slice)
    public static final FoodProperties FUGU_SUSHI_SLICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.4F)
            .fast()
            .build();

    // ซูชิปลาปักเป้าล้างพิษ (Fugu Nigiri) - บัฟต้านทานความเสียหาย (Resistance II) 45 วินาที
    public static final FoodProperties FUGU_SUSHI = new FoodProperties.Builder()
            .nutrition(7)
            .saturationModifier(0.9F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 900, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 40, 0), 0.3F) // 30% chance 2s slight thrill
            .build();

    // ชิ้นไข่หวานสำหรับซูชิ (Tamago Sushi Slice)
    public static final FoodProperties TAMAGO_SUSHI_SLICE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.4F)
            .fast()
            .build();

    // ซูชิไข่หวาน (Tamago Sushi)
    public static final FoodProperties TAMAGO_SUSHI = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.8F)
            .build();
}
