package com.dynious.refinedrelocation.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class Vector3
{
    private int x, y, z;

    public Vector3(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 createFromNBT(NBTTagCompound compound)
    {
        return new Vector3(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
    }

    public static Vector3 getFromTile(TileEntity tileEntity)
    {
        return new Vector3(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("x", x);
        compound.setInteger("y", y);
        compound.setInteger("z", z);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        x = compound.getInteger("x");
        y = compound.getInteger("y");
        z = compound.getInteger("z");
    }
}
