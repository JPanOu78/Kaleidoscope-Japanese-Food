package com.mrzero784.kaleidoscope_japanese_food.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrzero784.kaleidoscope_japanese_food.block.SushiDiscBlock;
import com.mrzero784.kaleidoscope_japanese_food.block.entity.SushiDiscBlockEntity;
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

public class SushiDiscRenderer implements BlockEntityRenderer<SushiDiscBlockEntity> {
    private final ItemRenderer itemRenderer;

    // Slot X-offsets from the plate center (2 sushi pieces side by side)
    // Slot 0: -1.5 px = -0.09375
    // Slot 1: +1.5 px = +0.09375
    private static final float[] SLOT_X_OFFSETS = new float[]{
            -0.09375F, // Slot 0 (Left)
             0.09375F  // Slot 1 (Right)
    };

    public SushiDiscRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SushiDiscBlockEntity blockEntity, float partialTick, PoseStack poseStack,
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

        Direction facing = blockEntity.getBlockState().getValue(SushiDiscBlock.FACING);
        int discs = blockEntity.getBlockState().getValue(SushiDiscBlock.DISCS);

        // Height of the top plate surface (each plate is 1.25 px high)
        float surfaceY = (discs * 1.25F) / 16.0F;

        poseStack.pushPose();
        // Center of the plate top surface: (0.5, surfaceY, 0.5)
        poseStack.translate(0.5D, surfaceY, 0.5D);

        // Rotate by block facing
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

        // Render each of the 2 sushi pieces
        for (int i = 0; i < items.size() && i < SLOT_X_OFFSETS.length; i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) {
                continue;
            }

            poseStack.pushPose();
            poseStack.translate(SLOT_X_OFFSETS[i], 0.0D, 0.0D);

            // 3D food items: base at Y=0. ItemRenderer shifts by -0.5 on Y, so +0.5 brings base back to plate surface!
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

        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(SushiDiscBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(1.0);
    }
}

