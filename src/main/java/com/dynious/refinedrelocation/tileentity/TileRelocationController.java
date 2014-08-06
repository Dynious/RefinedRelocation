package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.item.ItemPlayerRelocator;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class TileRelocationController extends TileMultiBlockBase
{
    private String linkedUUID;
    public boolean isLocked = false;

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

    public boolean isIntraLinker()
    {
        return type == 1;
    }

    public boolean isLinked()
    {
        return linkedUUID != null;
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
        isLocked = compound.getBoolean("isLocked");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (linkedUUID != null && !linkedUUID.isEmpty())
        {
            compound.setString("UUID", linkedUUID);
        }
        compound.setBoolean("isLocked", isLocked);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
        if (getWorldObj() != null)
        {
            getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return super.getRenderBoundingBox().expand(1D, 3D, 1D);
    }

    public boolean onActivated(World world, EntityPlayer player, int side)
    {
        if (!isFormed(true))
            return false;
        if (world.isRemote)
        {
            world.markBlockForUpdate(xCoord, yCoord, zCoord);
            return true;
        }

        if (player.isSneaking())
        {
            if (this.canPlayerToggleLock(player))
            {
                isLocked = !isLocked;
            }
            else
            {
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(Strings.TOGGLE_LOCK)));
            }
        }

        markDirty(); // save to NBT
        world.markBlockForUpdate(xCoord, yCoord, zCoord); // render update
        return true;
    }



    public boolean canPlayerToggleLock(EntityPlayer player)
    {
        if (!this.isLinked())
            return false;

        for (ItemStack itemStack : player.inventory.mainInventory)
        {
            if (itemStack != null && itemStack.getItem() instanceof ItemPlayerRelocator)
            {
                if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(ItemPlayerRelocator.UUID_TAG)) // Found player relocator is linked
                {
                    if (this.getLinkedUUID().equals(itemStack.getTagCompound().getString(ItemPlayerRelocator.UUID_TAG)))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
