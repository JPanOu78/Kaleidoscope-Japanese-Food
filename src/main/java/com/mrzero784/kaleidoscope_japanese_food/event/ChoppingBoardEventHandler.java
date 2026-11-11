package com.mrzero784.kaleidoscope_japanese_food.event;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
import com.mrzero784.kaleidoscope_japanese_food.KaleidoscopeJapaneseFood;
import com.mrzero784.kaleidoscope_japanese_food.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.lang.reflect.Method;

@EventBusSubscriber(modid = KaleidoscopeJapaneseFood.MOD_ID)
public class ChoppingBoardEventHandler {
    private static Method resetBoardDataMethod = null;

    static {
        try {
            resetBoardDataMethod = ChoppingBoardBlockEntity.class.getDeclaredMethod("resetBoardData");
            resetBoardDataMethod.setAccessible(true);
        } catch (Exception e) {
            KaleidoscopeJapaneseFood.LOGGER.error("Failed to find resetBoardData in ChoppingBoardBlockEntity", e);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        ItemStack itemInHand = event.getItemStack();
        // Support Wooden Plate to scoop up finished sliced food
        if (!itemInHand.is(ModItems.WOODEN_PLATE.get())) {
            return;
        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockEntity be = level.getBlockEntity(pos);

        if (be instanceof ChoppingBoardBlockEntity choppingBoard) {
            ItemStack cutStack = choppingBoard.getCurrentCutStack();
            int currentCut = choppingBoard.getCurrentCutCount();
            int maxCut = choppingBoard.getMaxCutCount();

            // When Tamagoyaki has been sliced on the board (4 slices ready)
            if (cutStack.is(ModItems.TAMAGOYAKI.get()) && currentCut >= maxCut && maxCut > 0) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);

                Player player = event.getEntity();
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        itemInHand.shrink(1);
                    }

                    // Reset the board data
                    if (resetBoardDataMethod != null) {
                        try {
                            resetBoardDataMethod.invoke(choppingBoard);
                        } catch (Exception e) {
                            KaleidoscopeJapaneseFood.LOGGER.error("Failed to invoke resetBoardData", e);
                        }
                    }

                    // Create Wooden Plate loaded with 4 Tamagoyaki Slices
                    ItemStack plateStack = new ItemStack(ModItems.WOODEN_PLATE.get());
                    NonNullList<ItemStack> slices = NonNullList.withSize(4, ItemStack.EMPTY);
                    for (int i = 0; i < 4; i++) {
                        slices.set(i, new ItemStack(ModItems.TAMAGOYAKI_SLICE.get()));
                    }
                    CompoundTag tag = new CompoundTag();
                    ContainerHelper.saveAllItems(tag, slices, level.registryAccess());
                    plateStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));

                    ItemHandlerHelper.giveItemToPlayer(player, plateStack);

                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }
}
