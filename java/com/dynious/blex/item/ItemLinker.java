package com.dynious.blex.item;


import com.dynious.blex.BlockExtenders;
import com.dynious.blex.lib.Names;
import com.dynious.blex.tileentity.TileWirelessBlockExtender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class ItemLinker extends Item
{
    public ItemLinker(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.linker);
        this.setCreativeTab(BlockExtenders.tabBlEx);
        this.setMaxStackSize(1);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer,
                               List list, boolean par4)
    {
        if (stack.hasTagCompound())
        {
            int x = stack.getTagCompound().getInteger("tileX");
            int y = stack.getTagCompound().getInteger("tileY");
            int z = stack.getTagCompound().getInteger("tileZ");
            list.add("Linked Wireless Block Extender position: " + x + ", " + y + ", " + z);
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && !(tile instanceof TileWirelessBlockExtender))
        {
            linkTileAtPosition(itemStack, x, y, z);
            return true;
        }
        return false;
    }

    private static void linkTileAtPosition(ItemStack stack, int x, int y, int z)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("tileX", x);
        stack.getTagCompound().setInteger("tileY", y);
        stack.getTagCompound().setInteger("tileZ", z);
    }
}
