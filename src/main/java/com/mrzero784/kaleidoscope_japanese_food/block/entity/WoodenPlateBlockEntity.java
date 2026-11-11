package com.mrzero784.kaleidoscope_japanese_food.block.entity;

import com.mrzero784.kaleidoscope_japanese_food.init.ModBlockEntities;
import com.mrzero784.kaleidoscope_japanese_food.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;

public class WoodenPlateBlockEntity extends BlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public WoodenPlateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.WOODEN_PLATE.get(), pos, blockState);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public boolean isWholeTamagoyaki() {
        return !this.items.get(0).isEmpty() && this.items.get(0).is(ModItems.TAMAGOYAKI.get());
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int getItemCount() {
        if (isWholeTamagoyaki()) {
            return 4;
        }
        int count = 0;
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public boolean canAddItem() {
        return !isWholeTamagoyaki() && getItemCount() < 4;
    }

    public boolean canAddItem(ItemStack stack) {
        if (isWholeTamagoyaki()) {
            return false;
        }
        if (stack.is(ModItems.TAMAGOYAKI.get())) {
            return isEmpty();
        }
        return getItemCount() < 4;
    }

    public boolean addItem(ItemStack stack) {
        if (!canAddItem(stack)) {
            return false;
        }
        if (stack.is(ModItems.TAMAGOYAKI.get())) {
            this.items.set(0, stack.copyWithCount(1));
            markUpdated();
            return true;
        }
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).isEmpty()) {
                this.items.set(i, stack.copyWithCount(1));
                markUpdated();
                return true;
            }
        }
        return false;
    }

    public ItemStack removeItem() {
        if (isWholeTamagoyaki()) {
            ItemStack stack = this.items.get(0);
            this.items.set(0, ItemStack.EMPTY);
            markUpdated();
            return stack;
        }
        for (int i = this.items.size() - 1; i >= 0; i--) {
            ItemStack stack = this.items.get(i);
            if (!stack.isEmpty()) {
                this.items.set(i, ItemStack.EMPTY);
                markUpdated();
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private void markUpdated() {
        setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        Collections.fill(this.items, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        CompoundTag tag = pkt.getTag();
        if (tag != null) {
            Collections.fill(this.items, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, this.items, lookupProvider);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        Collections.fill(this.items, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
    }
}
