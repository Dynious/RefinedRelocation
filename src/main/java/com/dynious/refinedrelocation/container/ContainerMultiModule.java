package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.grid.relocator.RelocatorMultiModule;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIByte;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ContainerMultiModule extends ContainerHierarchical {
    private final RelocatorMultiModule multiModule;
    private final IItemRelocator relocator;
    private final int side;

    public ContainerMultiModule(RelocatorMultiModule module, IItemRelocator relocator, int side) {
        this.multiModule = module;
        this.relocator = relocator;
        this.side = side;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        multiModule.setCurrentModule(-1);
    }

    @Override
    public void onMessageByte(int messageId, byte value, EntityPlayer entityPlayer, Side side) {
        switch (messageId) {
            case MessageGUI.REMOVE_MODULE:
                IRelocatorModule module = multiModule.removeModule(value);
                multiModule.setCurrentModule(-1);
                if(side == Side.SERVER) {
                    List<ItemStack> itemStacks = module.getDrops(relocator, this.side);
                    for (ItemStack itemStack : itemStacks) {
                        if (!entityPlayer.inventory.addItemStackToInventory(itemStack)) {
                            entityPlayer.dropPlayerItemWithRandomChoice(itemStack, false);
                        } else {
                            entityPlayer.inventoryContainer.detectAndSendChanges();
                        }
                    }
                    sendSyncMessage(new MessageGUIByte(MessageGUI.REMOVE_MODULE, value));
                }
                break;
            case MessageGUI.OPEN_MODULE:
                multiModule.setCurrentModule(value);
                multiModule.getCurrentModule().onActivated(relocator, entityPlayer, this.side, entityPlayer.getCurrentEquippedItem());
                break;
        }
    }
}
