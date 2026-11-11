package com.mrzero784.kaleidoscope_japanese_food.block;

import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.mojang.serialization.MapCodec;
import com.mrzero784.kaleidoscope_japanese_food.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class CookingMatBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<CookingMatBlock> CODEC = simpleCodec(CookingMatBlock::new);
    public static final EnumProperty<CookingMatStage> STAGE = EnumProperty.create("stage", CookingMatStage.class);

    public static final VoxelShape MAT_SHAPE = Block.box(0.95, 0.0, 0.9, 15.0, 0.5, 15.1);
    public static final VoxelShape WITH_FOOD_SHAPE = Block.box(0.95, 0.0, 0.9, 15.0, 4.0, 15.1);

    public CookingMatBlock() {
        this(BlockBehaviour.Properties.of()
                .forceSolidOn()
                .instabreak()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.BAMBOO)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion());
    }

    public CookingMatBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH)
                .setValue(STAGE, CookingMatStage.EMPTY));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(STAGE) == CookingMatStage.EMPTY ? MAT_SHAPE : WITH_FOOD_SHAPE;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public static boolean isCookedRice(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(TagCommon.COOKED_RICE)) return true;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("cooked_rice");
    }

    public static ItemStack getCookedRiceItem() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("kaleidoscope_cookery", "cooked_rice");
        if (BuiltInRegistries.ITEM.containsKey(id)) {
            return new ItemStack(BuiltInRegistries.ITEM.get(id));
        }
        return new ItemStack(Items.WHEAT);
    }

    public static boolean isKnife(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(TagCommon.KNIVES) || stack.is(TagCommon.FD_KNIVES)) return true;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("knife") || id.getPath().contains("kitchen_knife");
    }

    public static boolean isSalmonSlice(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ModItems.SALMON_SUSHI_SLICE.get())) return true;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("salmon_sushi_slice") || id.getPath().contains("salmon_slice");
    }

    public static boolean isCodSlice(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ModItems.COD_SUSHI_SLICE.get())) return true;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("cod_sushi_slice") || id.getPath().contains("cod_slice");
    }

    public static boolean isFuguSlice(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ModItems.FUGU_SUSHI_SLICE.get())) return true;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("fugu_sushi_slice") || id.getPath().contains("fugu_slice");
    }

    public static boolean isTamagoSlice(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ModItems.TAMAGO_SUSHI_SLICE.get())) return true;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("tamago_sushi_slice") || id.getPath().contains("tamagoyaki_sushi_slice") || id.getPath().contains("tamagoyaki_slice");
    }

    public static boolean isDriedKelp(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(Items.DRIED_KELP)) return true;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("dried_kelp") || id.getPath().contains("nori") || id.getPath().contains("seaweed");
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemInHand, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        CookingMatStage stage = state.getValue(STAGE);

        // =========================================================
        // CANCEL / UNDO (Shift + Right Click with empty hand)
        // =========================================================
        if (player.isShiftKeyDown() && itemInHand.isEmpty()) {
            if (!level.isClientSide) {
                switch (stage) {
                    case EMPTY:
                        level.destroyBlock(pos, true, player);
                        break;

                    case COOKED_RICE:
                        ItemHandlerHelper.giveItemToPlayer(player, getCookedRiceItem());
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case SUSHI_RICE:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SUSHI_RICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case RAW_SALMON:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.SALMON));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case SALMON_SLICES:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SALMON_SUSHI_SLICE.get(), 4));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case SALMON_SLICE:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SALMON_SUSHI_SLICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case SALMON_PREP:
                    case SALMON_ON_RICE:
                    case SALMON_SUSHI:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SALMON_SUSHI_SLICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SUSHI_RICE));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case RAW_COD:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.COD));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case COD_SLICES:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.COD_SUSHI_SLICE.get(), 4));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case COD_SLICE:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.COD_SUSHI_SLICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case COD_SUSHI:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.COD_SUSHI_SLICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SUSHI_RICE));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case RAW_FUGU:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.PUFFERFISH));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case FUGU_SLICES:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.FUGU_SUSHI_SLICE.get(), 4));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case FUGU_SLICE:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.FUGU_SUSHI_SLICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case FUGU_SUSHI:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.FUGU_SUSHI_SLICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SUSHI_RICE));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case TAMAGO_SLICE:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.TAMAGO_SUSHI_SLICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case TAMAGO_PREP:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.TAMAGO_SUSHI_SLICE.get()));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SUSHI_RICE));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case TAMAGO_ON_RICE:
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.TAMAGO_PREP));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case NORI_PREP:
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.DRIED_KELP));
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.TAMAGO_ON_RICE));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;

                    case TAMAGO_SUSHI:
                        level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.NORI_PREP));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        break;
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        // =========================================================
        // NORMAL WORKFLOW: Prep, Cutting, and Sushi Assembly
        // =========================================================

        // 1. EMPTY: Place cooked rice, sushi rice, whole salmon, whole cod, or pufferfish
        if (stage == CookingMatStage.EMPTY) {
            if (isCookedRice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.COOKED_RICE));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (itemInHand.is(ModItems.SUSHI_RICE.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SUSHI_RICE));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (itemInHand.is(Items.SALMON)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.RAW_SALMON));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (itemInHand.is(Items.COD)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.RAW_COD));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (itemInHand.is(Items.PUFFERFISH)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.RAW_FUGU));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            // Fish First Workflow: Place single sushi slice onto empty mat first, then add rice later!
            if (isSalmonSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SALMON_SLICE));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isCodSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.COD_SLICE));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isFuguSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.FUGU_SLICE));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isTamagoSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.TAMAGO_SLICE));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            // Place Finished Sushi onto empty mat for display / plating
            if (itemInHand.is(ModItems.SALMON_SUSHI.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SALMON_SUSHI));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (itemInHand.is(ModItems.COD_SUSHI.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.COD_SUSHI));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (itemInHand.is(ModItems.FUGU_SUSHI.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.FUGU_SUSHI));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (itemInHand.is(ModItems.TAMAGO_SUSHI.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.TAMAGO_SUSHI));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 2A (Salmon Cutting). RAW_SALMON: Use kitchen knife to slice into 4 slices
        else if (stage == CookingMatStage.RAW_SALMON) {
            if (isKnife(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        itemInHand.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SALMON_SLICES));
                    level.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 3A (Salmon Slices). SALMON_SLICES: Sliced 4 pieces - MUST collect with empty hand first before making sushi!
        else if (stage == CookingMatStage.SALMON_SLICES) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SALMON_SUSHI_SLICE.get(), 4));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 2B (Cod Cutting). RAW_COD: Use kitchen knife to slice into 4 slices
        else if (stage == CookingMatStage.RAW_COD) {
            if (isKnife(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        itemInHand.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.COD_SLICES));
                    level.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 3B (Cod Slices). COD_SLICES: Sliced 4 pieces - MUST collect with empty hand first before making sushi!
        else if (stage == CookingMatStage.COD_SLICES) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.COD_SUSHI_SLICE.get(), 4));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 2C (Pufferfish Cutting). RAW_FUGU: Use kitchen knife to slice into 4 delicate fugu slices on mat
        else if (stage == CookingMatStage.RAW_FUGU) {
            if (isKnife(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        itemInHand.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.FUGU_SLICES));
                    level.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 3C (Fugu Slices). FUGU_SLICES: Sliced 4 pieces - MUST collect with empty hand first before making sushi!
        else if (stage == CookingMatStage.FUGU_SLICES) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.FUGU_SUSHI_SLICE.get(), 4));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 5A (Fish First Workflow: Salmon Slice on Mat). Add rice to form Salmon Sushi, or empty hand to pick up slice
        else if (stage == CookingMatStage.SALMON_SLICE) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SALMON_SUSHI_SLICE.get()));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isCookedRice(itemInHand) || itemInHand.is(ModItems.SUSHI_RICE.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SALMON_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 5B (Fish First Workflow: Cod Slice on Mat). Add rice to form Cod Sushi, or empty hand to pick up slice
        else if (stage == CookingMatStage.COD_SLICE) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.COD_SUSHI_SLICE.get()));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isCookedRice(itemInHand) || itemInHand.is(ModItems.SUSHI_RICE.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.COD_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 5C (Fish First Workflow: Fugu Slice on Mat). Add rice to form Fugu Nigiri, or empty hand to pick up slice
        else if (stage == CookingMatStage.FUGU_SLICE) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.FUGU_SUSHI_SLICE.get()));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isCookedRice(itemInHand) || itemInHand.is(ModItems.SUSHI_RICE.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.FUGU_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 5D (Fish First Workflow: Tamago Slice on Mat). Add rice to form Tamago Prep, or empty hand to pick up slice
        else if (stage == CookingMatStage.TAMAGO_SLICE) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.TAMAGO_SUSHI_SLICE.get()));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isCookedRice(itemInHand) || itemInHand.is(ModItems.SUSHI_RICE.get())) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.TAMAGO_PREP));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 4B (Cod Sushi). COD_SUSHI: Right-click to collect cod sushi item
        else if (stage == CookingMatStage.COD_SUSHI) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.COD_SUSHI.get()));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 4C (Fugu Sushi). FUGU_SUSHI: Right-click to collect fugu nigiri item
        else if (stage == CookingMatStage.FUGU_SUSHI) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.FUGU_SUSHI.get()));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 2. COOKED_RICE: Mold with empty hand into sushi rice, or direct place slices
        else if (stage == CookingMatStage.COOKED_RICE) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SUSHI_RICE));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            // Quick craft: Place salmon, cod, or fugu slice directly onto cooked rice to form sushi immediately
            if (isSalmonSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SALMON_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isCodSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.COD_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isFuguSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.FUGU_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 3. SUSHI_RICE: Place salmon, cod, fugu, or tamago slice
        else if (stage == CookingMatStage.SUSHI_RICE) {
            if (isSalmonSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SALMON_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isCodSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.COD_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isFuguSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.FUGU_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }

            if (isTamagoSlice(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.TAMAGO_PREP));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 4A (Salmon). Fallback for intermediate stages: Right-click to advance to finished salmon sushi
        else if (stage == CookingMatStage.SALMON_PREP || stage == CookingMatStage.SALMON_ON_RICE) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.SALMON_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 5A (Salmon). SALMON_SUSHI: Right-click to collect salmon sushi item
        else if (stage == CookingMatStage.SALMON_SUSHI) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SALMON_SUSHI.get()));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 4B (Tamago). TAMAGO_PREP (side-by-side): Right-click to combine tamago slice onto rice
        else if (stage == CookingMatStage.TAMAGO_PREP) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.TAMAGO_ON_RICE));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 5B (Tamago). TAMAGO_ON_RICE: Place dried kelp (nori strip) side-by-side
        else if (stage == CookingMatStage.TAMAGO_ON_RICE) {
            if (isDriedKelp(itemInHand)) {
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) itemInHand.shrink(1);
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.NORI_PREP));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 6B (Tamago). NORI_PREP (side-by-side nori): Right-click to wrap nori around tamago sushi
        else if (stage == CookingMatStage.NORI_PREP) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.TAMAGO_SUSHI));
                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 7B (Tamago). TAMAGO_SUSHI: Right-click to collect tamago sushi item
        else if (stage == CookingMatStage.TAMAGO_SUSHI) {
            if (itemInHand.isEmpty()) {
                if (!level.isClientSide) {
                    ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.TAMAGO_SUSHI.get()));
                    level.setBlockAndUpdate(pos, state.setValue(STAGE, CookingMatStage.EMPTY));
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = new ArrayList<>(super.getDrops(state, params));
        CookingMatStage stage = state.getValue(STAGE);
        switch (stage) {
            case COOKED_RICE:
                drops.add(getCookedRiceItem());
                break;
            case SUSHI_RICE:
                drops.add(new ItemStack(ModItems.SUSHI_RICE.get()));
                break;
            case RAW_SALMON:
                drops.add(new ItemStack(Items.SALMON));
                break;
            case SALMON_SLICES:
                drops.add(new ItemStack(ModItems.SALMON_SUSHI_SLICE.get(), 4));
                break;
            case SALMON_SLICE:
                drops.add(new ItemStack(ModItems.SALMON_SUSHI_SLICE.get()));
                break;
            case SALMON_PREP:
            case SALMON_ON_RICE:
                drops.add(new ItemStack(ModItems.SUSHI_RICE.get()));
                drops.add(new ItemStack(ModItems.SALMON_SUSHI_SLICE.get()));
                break;
            case SALMON_SUSHI:
                drops.add(new ItemStack(ModItems.SALMON_SUSHI.get()));
                break;
            case RAW_COD:
                drops.add(new ItemStack(Items.COD));
                break;
            case COD_SLICES:
                drops.add(new ItemStack(ModItems.COD_SUSHI_SLICE.get(), 4));
                break;
            case COD_SLICE:
                drops.add(new ItemStack(ModItems.COD_SUSHI_SLICE.get()));
                break;
            case COD_SUSHI:
                drops.add(new ItemStack(ModItems.COD_SUSHI.get()));
                break;
            case RAW_FUGU:
                drops.add(new ItemStack(Items.PUFFERFISH));
                break;
            case FUGU_SLICES:
                drops.add(new ItemStack(ModItems.FUGU_SUSHI_SLICE.get(), 4));
                break;
            case FUGU_SLICE:
                drops.add(new ItemStack(ModItems.FUGU_SUSHI_SLICE.get()));
                break;
            case FUGU_SUSHI:
                drops.add(new ItemStack(ModItems.FUGU_SUSHI.get()));
                break;
            case TAMAGO_SLICE:
                drops.add(new ItemStack(ModItems.TAMAGO_SUSHI_SLICE.get()));
                break;
            case TAMAGO_PREP:
            case TAMAGO_ON_RICE:
                drops.add(new ItemStack(ModItems.SUSHI_RICE.get()));
                drops.add(new ItemStack(ModItems.TAMAGO_SUSHI_SLICE.get()));
                break;
            case NORI_PREP:
                drops.add(new ItemStack(ModItems.SUSHI_RICE.get()));
                drops.add(new ItemStack(ModItems.TAMAGO_SUSHI_SLICE.get()));
                drops.add(new ItemStack(Items.DRIED_KELP));
                break;
            case TAMAGO_SUSHI:
                drops.add(new ItemStack(ModItems.TAMAGO_SUSHI.get()));
                break;
            case EMPTY:
            default:
                break;
        }
        return drops;
    }

    public enum CookingMatStage implements StringRepresentable {
        EMPTY("empty"),
        COOKED_RICE("cooked_rice"),
        SUSHI_RICE("sushi_rice"),
        RAW_SALMON("raw_salmon"),
        SALMON_SLICES("salmon_slices"),
        SALMON_SLICE("salmon_slice"),
        SALMON_PREP("salmon_prep"),
        SALMON_ON_RICE("salmon_on_rice"),
        SALMON_SUSHI("salmon_sushi"),
        RAW_COD("raw_cod"),
        COD_SLICES("cod_slices"),
        COD_SLICE("cod_slice"),
        COD_SUSHI("cod_sushi"),
        RAW_FUGU("raw_fugu"),
        FUGU_SLICES("fugu_slices"),
        FUGU_SLICE("fugu_slice"),
        FUGU_SUSHI("fugu_sushi"),
        TAMAGO_SLICE("tamago_slice"),
        TAMAGO_PREP("tamago_prep"),
        TAMAGO_ON_RICE("tamago_on_rice"),
        NORI_PREP("nori_prep"),
        TAMAGO_SUSHI("tamago_sushi");

        private final String name;

        CookingMatStage(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
