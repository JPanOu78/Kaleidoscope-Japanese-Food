package com.mrzero784.kaleidoscope_japanese_food.block;

import com.mojang.serialization.MapCodec;
import com.mrzero784.kaleidoscope_japanese_food.block.entity.SushiDiscBlockEntity;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
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
public class SushiDiscBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final MapCodec<SushiDiscBlock> CODEC = simpleCodec(SushiDiscBlock::new);
    public static final IntegerProperty DISCS = IntegerProperty.create("discs", 1, 10);

    // Bounding boxes for each stack height (1..10 discs, each disc is 1.25 px high)
    private static final VoxelShape[] SHAPES = new VoxelShape[10];

    static {
        for (int i = 0; i < 10; i++) {
            double height = (i + 1) * 1.25;
            SHAPES[i] = Block.box(3.5, 0.0, 3.5, 12.5, height, 12.5);
        }
    }

    public SushiDiscBlock() {
        this(BlockBehaviour.Properties.of()
                .forceSolidOn()
                .instabreak()
                .mapColor(MapColor.COLOR_BLACK)
                .sound(SoundType.STONE)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion());
    }

    public SushiDiscBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH)
                .setValue(DISCS, 1));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DISCS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int count = state.getValue(DISCS);
        return SHAPES[Math.max(0, Math.min(9, count - 1))];
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(DISCS, 1);
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
        return new SushiDiscBlockEntity(pos, state);
    }

    public static boolean isSushi(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ModItems.SALMON_SUSHI.get()) ||
            stack.is(ModItems.TAMAGO_SUSHI.get()) ||
            stack.is(ModItems.SUSHI_RICE.get()) ||
            stack.is(ModItems.TAMAGOYAKI_SLICE.get()) ||
            stack.is(ModItems.SALMON_SUSHI_SLICE.get()) ||
            stack.is(ModItems.TAMAGO_SUSHI_SLICE.get())) {
            return true;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id.getPath().contains("sushi") || id.getPath().contains("nigiri") || id.getPath().contains("slice");
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemInHand, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof SushiDiscBlockEntity discBe)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        int discs = state.getValue(DISCS);

        // 1. STACKING: Using another Sushi Disc on this block (up to 10 layers!)
        if (!itemInHand.isEmpty() && itemInHand.is(ModItems.SUSHI_DISC.get())) {
            if (discs < 10 && discBe.isEmpty()) {
                if (!level.isClientSide) {
                    level.setBlockAndUpdate(pos, state.setValue(DISCS, discs + 1));
                    if (!player.getAbilities().instabuild) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.2F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 2. PLACING SUSHI: Placing sushi onto the top disc (up to 2 pieces)
        if (!itemInHand.isEmpty() && isSushi(itemInHand)) {
            if (discBe.canAddItem(itemInHand)) {
                if (!level.isClientSide) {
                    discBe.addItem(itemInHand);
                    if (!player.getAbilities().instabuild) {
                        itemInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        // 3. SNEAK + EMPTY HAND: Pick up the whole stack at once
        if (player.isShiftKeyDown() && itemInHand.isEmpty()) {
            if (!level.isClientSide) {
                // Drop or give all stacked discs
                ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SUSHI_DISC.get(), discs));
                // Give any sushi on top
                for (ItemStack s : discBe.getItems()) {
                    if (!s.isEmpty()) {
                        ItemHandlerHelper.giveItemToPlayer(player, s);
                    }
                }
                level.destroyBlock(pos, false, player);
                level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.SUCCESS;
        }

        // 4. NORMAL RIGHT-CLICK WITH EMPTY HAND:
        if (itemInHand.isEmpty()) {
            // A. If top disc has sushi: take 1 sushi piece
            if (discBe.getItemCount() > 0) {
                if (!level.isClientSide) {
                    ItemStack removed = discBe.removeItem();
                    if (!removed.isEmpty()) {
                        ItemHandlerHelper.giveItemToPlayer(player, removed);
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
                return ItemInteractionResult.SUCCESS;
            }

            // B. If empty plate: take 1 disc off the stack
            if (discBe.isEmpty()) {
                if (!level.isClientSide) {
                    if (discs > 1) {
                        level.setBlockAndUpdate(pos, state.setValue(DISCS, discs - 1));
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SUSHI_DISC.get()));
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                    } else {
                        // Last disc: remove block
                        level.destroyBlock(pos, false, player);
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.SUSHI_DISC.get()));
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = new ArrayList<>();
        int discs = state.getValue(DISCS);
        drops.add(new ItemStack(ModItems.SUSHI_DISC.get(), discs));
        return drops;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SushiDiscBlockEntity discBe) {
                for (ItemStack stack : discBe.getItems()) {
                    if (!stack.isEmpty()) {
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
