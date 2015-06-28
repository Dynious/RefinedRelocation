package com.dynious.refinedrelocation.tileentity;

import com.pahimar.ee3.tileentity.TileEntityAlchemicalChest;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Deprecated
public class TileSortingAlchemicalChest extends TileEntityAlchemicalChest
{
    public TileSortingAlchemicalChest()
    {
        super(0);
    }

    public TileSortingAlchemicalChest(int metaData)
    {
        super(metaData);
    }

    private void fixState(byte state)
    {
        ItemStack[] inventory = new ItemStack[48];
        if (state == 1)
        {
            inventory = new ItemStack[84];
        }
        else if (state == 2)
        {
            inventory = new ItemStack[117];
        }
        ReflectionHelper.setPrivateValue(TileEntityAlchemicalChest.class, this, inventory, "inventory");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 0 || pass == 1;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        fixState(nbtTagCompound.getByte("teState"));
        super.readFromNBT(nbtTagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
    }
}
