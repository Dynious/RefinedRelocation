package com.dynious.blex.gui.container;

import com.dynious.blex.tileentity.TileFilteringChest;
import cpw.mods.ironchest.ContainerIronChestBase;
import cpw.mods.ironchest.IronChestType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerIronFilteringChest extends ContainerIronChestBase
{
    public IInventory chest;

    public ContainerIronFilteringChest(EntityPlayer player, IInventory chestInventory, IronChestType type, int xSize, int ySize)
    {
        super(player.inventory, chestInventory, type, xSize, ySize);
        chest = chestInventory;
        ((TileFilteringChest)chest).addCrafter(player);
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack)
    {
        ((TileFilteringChest)chest).putStackInSlot(par2ItemStack, par1);
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        ((TileFilteringChest)chest).removeCrafter(par1EntityPlayer);
        super.onContainerClosed(par1EntityPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
