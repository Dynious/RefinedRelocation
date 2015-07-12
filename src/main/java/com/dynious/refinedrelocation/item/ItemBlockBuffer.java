package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            String[] tooltipLines = StatCollector.translateToLocal(getUnlocalizedName(itemStack) + ".tooltip").split("\\\\n");
            for (String s : tooltipLines)
            {
                list.add("\u00a73" + s);
            }
        } else {
            list.add("\u00a76" + StatCollector.translateToLocal(Strings.TOOLTIP_SHIFT));
        }
    }
}
