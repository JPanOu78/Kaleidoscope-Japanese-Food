package com.mrzero784.kaleidoscope_japanese_food.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrzero784.kaleidoscope_japanese_food.block.WoodenPlateBlock;
import com.mrzero784.kaleidoscope_japanese_food.block.entity.WoodenPlateBlockEntity;
import com.mrzero784.kaleidoscope_japanese_food.init.ModItems;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class WoodenPlateRenderer implements BlockEntityRenderer<WoodenPlateBlockEntity> {
    private final ItemRenderer itemRenderer;

    // Slot X-offsets from the plate center (matching Blockbench models exactly)
    // Slot 0: 3.5 - 8.0 = -4.5 px = -0.28125
    // Slot 1: 6.5 - 8.0 = -1.5 px = -0.09375
    // Slot 2: 9.5 - 8.0 = +1.5 px = +0.09375
    // Slot 3: 12.5 - 8.0 = +4.5 px = +0.28125
    private static final float[] SLOT_X_OFFSETS = new float[]{
            -0.28125F, // Slot 0
            -0.09375F, // Slot 1
             0.09375F, // Slot 2
             0.28125F  // Slot 3
    };

    public WoodenPlateRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(WoodenPlateBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        NonNullList<ItemStack> items = blockEntity.getItems();
        boolean hasItems = false;
        for (ItemStack s : items) {
            if (!s.isEmpty()) {
                hasItems = true;
                break;
            }
        }
        if (!hasItems) {
            return;
        }

        Direction facing = blockEntity.getBlockState().getValue(WoodenPlateBlock.FACING);

        // Center of the plate top surface: (0.5, 3.0/16.0 = 0.1875, 0.5)
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.1875D, 0.5D);

        // Rotate by block facing to match blockstates/wooden_plate.json
        float yRot = switch (facing) {
            case NORTH -> 0.0F;
            case SOUTH -> 180.0F;
            case EAST -> -90.0F;
            case WEST -> 90.0F;
            default -> 0.0F;
        };
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        int light = packedLight;
        if (blockEntity.getLevel() != null) {
            int blockLight = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above());
            light = Math.max(light, blockLight);
        }

        // Render whole tamagoyaki (occupies whole plate) or individual sushi pieces
        if (blockEntity.isWholeTamagoyaki()) {
            ItemStack stack = items.get(0);
            poseStack.pushPose();
            // Centered on the plate: no slot offset, base at Y=0
            poseStack.translate(0.0D, 0.5D, 0.0D);

            this.itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.NONE,
                    light,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    (int) blockEntity.getBlockPos().asLong()
            );

            poseStack.popPose();
        } else {
            // Render each of the sushi pieces
            for (int i = 0; i < items.size() && i < SLOT_X_OFFSETS.length; i++) {
                ItemStack stack = items.get(i);
                if (stack.isEmpty()) {
                    continue;
                }

                poseStack.pushPose();
                poseStack.translate(SLOT_X_OFFSETS[i], 0.0D, 0.0D);

                // 3D food items (sushi, tamagoyaki slices, etc.): base at Y=0.
                // ItemRenderer shifts by -0.5 on Y, so +0.5 brings base back to plate surface!
                poseStack.translate(0.0D, 0.5D, 0.0D);

                this.itemRenderer.renderStatic(
                        stack,
                        ItemDisplayContext.NONE,
                        light,
                        packedOverlay,
                        poseStack,
                        bufferSource,
                        blockEntity.getLevel(),
                        (int) blockEntity.getBlockPos().asLong() + i
                );

                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(WoodenPlateBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(1.0);
    }
}
