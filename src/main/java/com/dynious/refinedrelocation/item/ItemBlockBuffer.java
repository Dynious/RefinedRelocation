package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemBlockBuffer extends ItemBlock
{
    public ItemBlockBuffer(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack i)
    {
        return ModBlocks.buffer.getUnlocalizedName() + i.getItemDamage();
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean isShiftDown)
    {
        String[] tooltipLines = StatCollector.translateToLocal("tile." + getUnlocalizedName(itemStack) + ".tooltip").split("\\\\n");
        for (String s : tooltipLines)
        {
            list.add("\u00a77" + s);
        }
    }
}
