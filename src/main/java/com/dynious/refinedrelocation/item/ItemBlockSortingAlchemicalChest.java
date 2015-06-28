package com.dynious.refinedrelocation.item;

import com.pahimar.ee3.item.ItemBlockAlchemicalChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockSortingAlchemicalChest extends ItemBlockAlchemicalChest
{
    public ItemBlockSortingAlchemicalChest(Block block) {
        super(block);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        super.addInformation(itemStack, entityPlayer, list, flag);
        list.add("\u00a7cAlchemical Sorting Chests have been removed from Refined Relocation.");
        list.add("\u00a7cPut this chest in your crafting table to get a normal Alchemical Chest back.");
    }
}
