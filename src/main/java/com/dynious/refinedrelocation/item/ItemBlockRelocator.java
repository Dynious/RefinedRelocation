package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBlockRelocator extends ItemBlock
{
    public ItemBlockRelocator(Block block)
    {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
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
