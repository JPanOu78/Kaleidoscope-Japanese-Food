package com.mrzero784.kaleidoscope_japanese_food.block;

import com.mojang.serialization.MapCodec;
import com.mrzero784.kaleidoscope_japanese_food.block.entity.WoodenPlateBlockEntity;
import com.mrzero784.kaleidoscope_japanese_food.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class WoodenPlateBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final MapCodec<WoodenPlateBlock> CODEC = simpleCodec(WoodenPlateBlock::new);

    public static final VoxelShape SHAPE_NS = Block.box(0.5, 0.0, 3.5, 15.5, 3.0, 12.5);
    public static final VoxelShape SHAPE_EW = Block.box(3.5, 0.0, 0.5, 12.5, 3.0, 15.5);

    public WoodenPlateBlock() {
        this(BlockBehaviour.Properties.of()
                .forceSolidOn()
                .instabreak()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion());
    }

    public WoodenPlateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.EAST || facing == Direction.WEST) {
            return SHAPE_EW;
        }
        return SHAPE_NS;
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

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenPlateBlockEntity(pos, state);
    }

    public static boolean isSushi(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ModItems.SALMON_SUSHI.get()) ||
            stack.is(ModItems.TAMAGO_SUSHI.get()) ||
            stack.is(ModItems.SUSHI_RICE.get()) ||
            stack.is(ModItems.TAMAGOYAKI.get()) ||
            stack.is(ModItems.TAMAGOYAKI_SLICE.get()) ||
            stack.is(ModItems.SALMON_SUSHI_SLICE.get()) ||
            stack.is(ModItems.TAMAGO_SUSHI_SLICE.get())) {
            return true;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("sushi") || id.getPath().contains("nigiri") || id.getPath().contains("tamagoyaki");
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemInHand, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof WoodenPlateBlockEntity plateBe)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // 1. Placing food into the plate (Mixed Sushi & Whole Tamagoyaki support!)
        if (!itemInHand.isEmpty() && isSushi(itemInHand)) {
            if (plateBe.canAddItem(itemInHand)) {
                if (!level.isClientSide) {
                    plateBe.addItem(itemInHand);
                    if (!player.getAbilities().instabuild) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 2. Sneak + right click with empty hand on empty plate -> pick up plate
        if (player.isShiftKeyDown() && itemInHand.isEmpty()) {
            if (plateBe.getItemCount() == 0) {
                level.destroyBlock(pos, true, player);
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 3. Taking food from the plate (right click with empty hand)
        if (itemInHand.isEmpty() && plateBe.getItemCount() > 0) {
            if (!level.isClientSide) {
                ItemStack removed = plateBe.removeItem();
                if (!removed.isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(player, removed);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof WoodenPlateBlockEntity plateBe) {
                for (ItemStack stack : plateBe.getItems()) {
                    if (!stack.isEmpty()) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
