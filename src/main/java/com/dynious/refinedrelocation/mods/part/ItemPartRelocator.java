package com.dynious.refinedrelocation.mods.part;

import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TMultiPart;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.RelocatorData;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemPartRelocator extends JItemMultiPart
{
    public ItemPartRelocator()
    {
        super();
        this.setUnlocalizedName(Names.relocator);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TMultiPart newPart(ItemStack itemStack, EntityPlayer player, World world, BlockCoord blockCoord, int i, Vector3 vector3)
    {
        TMultiPart part = PartFactory.INSTANCE.createPart("tile." + Names.relocator, false);
        ((IRelocator) part).onAdded(player, itemStack);
        return part;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        for (int i = 0; i < RelocatorData.oreTypes.length; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
