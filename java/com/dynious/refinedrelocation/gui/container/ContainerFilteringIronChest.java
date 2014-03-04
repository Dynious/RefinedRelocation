package com.dynious.refinedrelocation.gui.container;

import com.dynious.refinedrelocation.tileentity.TileFilteringIronChest;
import cpw.mods.ironchest.ContainerIronChestBase;
import cpw.mods.ironchest.IronChestType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerFilteringIronChest extends ContainerIronChestBase
{
    public IInventory chest;

    public ContainerFilteringIronChest(EntityPlayer player, IInventory chestInventory, IronChestType type, int xSize, int ySize)
    {
        super(player.inventory, chestInventory, type, xSize, ySize);
        chest = chestInventory;
        ((TileFilteringIronChest)chest).getFilteringInventoryHandler().addCrafter(player);
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack)
    {
        ((TileFilteringIronChest)chest).getFilteringInventoryHandler().putStackInSlot(par2ItemStack, par1);
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        ((TileFilteringIronChest)chest).getFilteringInventoryHandler().removeCrafter(par1EntityPlayer);
        super.onContainerClosed(par1EntityPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
