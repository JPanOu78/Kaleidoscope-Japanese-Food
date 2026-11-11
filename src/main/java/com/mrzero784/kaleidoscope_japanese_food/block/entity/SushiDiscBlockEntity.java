package com.mrzero784.kaleidoscope_japanese_food.block.entity;

import com.mrzero784.kaleidoscope_japanese_food.init.ModBlockEntities;
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

public class SushiDiscBlockEntity extends BlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    public SushiDiscBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SUSHI_DISC.get(), pos, blockState);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
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
        int count = 0;
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public boolean canAddItem() {
        return getItemCount() < 2;
    }

    public boolean canAddItem(ItemStack stack) {
        return getItemCount() < 2;
    }

    public boolean addItem(ItemStack stack) {
        if (!canAddItem(stack)) {
            return false;
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

