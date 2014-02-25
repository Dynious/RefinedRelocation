package com.dynious.blex.tileentity;

import cpw.mods.fml.common.Optional;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import org.apache.commons.lang3.ArrayUtils;

public class TileWirelessBlockExtender extends TileAdvancedFilteredBlockExtender
{
    public int xConnected = Integer.MAX_VALUE;
    public int yConnected = Integer.MAX_VALUE;
    public int zConnected = Integer.MAX_VALUE;

    public void setConnection(int x, int y, int z)
    {
        this.xConnected = x;
        this.yConnected = y;
        this.zConnected = z;
    }

    @Override
    public void setConnectedSide(int connectedSide)
    {
    }

    @Override
    public ForgeDirection getConnectedDirection()
    {
        return ForgeDirection.UNKNOWN;
    }

    @Override
    public boolean canConnect()
    {
        return xConnected != Integer.MAX_VALUE;
    }
    @Override
    public boolean canDisguise()
    {
        return false;
    }


    @Override
    public TileEntity getConnectedTile()
    {
        return worldObj.getBlockTileEntity(xConnected, yConnected, zConnected);
    }

    @Override
    public boolean isRedstoneTransmissionActive()
    {
        // never render as if redstone is active
        return false;
    }

    @Override
    public boolean isRedstoneTransmissionEnabled()
    {
        // always render as if redstone is enabled
        return true;
    }

    /*
    ComputerCraft interaction
    */

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public String getType()
    {
        return "wireless_block_extender";
    }

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public String[] getMethodNames()
    {
        return ArrayUtils.addAll(super.getMethodNames(), "getConnectedPosition", "setConnectedPosition");
    }

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        switch (method)
        {
            case 0:
                return null;
            case 1:
                return new Boolean[]{false};
            case 8:
                if (xConnected == Integer.MAX_VALUE)
                {
                    return null;
                }
                return new Integer[]{xConnected, yConnected, zConnected};
            case 9:
                if (arguments.length > 0 && arguments[0] instanceof Double && arguments[1] instanceof Double && arguments[2] instanceof Double)
                {
                    double x = (Double) arguments[0];
                    double y = (Double) arguments[1];
                    double z = (Double) arguments[2];
                    setConnection((int) x, (int) y, (int) z);
                }
        }
        Object[] superArr = super.callMethod(computer, context, method, arguments);
        if (superArr != null)
        {
            return superArr;
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        xConnected = compound.getInteger("xConnected");
        yConnected = compound.getInteger("yConnected");
        zConnected = compound.getInteger("zConnected");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("xConnected", xConnected);
        compound.setInteger("yConnected", yConnected);
        compound.setInteger("zConnected", zConnected);
    }
}
