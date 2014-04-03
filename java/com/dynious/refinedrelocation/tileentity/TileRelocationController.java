package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import net.minecraft.nbt.NBTTagCompound;

public class TileRelocationController extends TileMultiBlockBase
{
    private String linkedUUID;

    @Override
    public String getMultiBlockIdentifier()
    {
        return Names.playerRelocator;
    }

    @Override
    public boolean shouldAutoCheckFormation()
    {
        return false;
    }

    @Override
    protected void onFormationChange()
    {
    }

    public boolean isIntraLinker()
    {
        return type == 1;
    }

    public String getLinkedUUID()
    {
        return linkedUUID;
    }

    public void setLinkedUUID(String linkedUUID)
    {
        this.linkedUUID = linkedUUID;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        linkedUUID = compound.getString("UUID");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("UUID", linkedUUID);
    }
}
