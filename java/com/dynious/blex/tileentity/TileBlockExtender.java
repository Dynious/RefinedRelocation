package com.dynious.blex.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.*;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

@InterfaceList(value = {@Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"), @Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy")})
public class TileBlockExtender extends TileEntity implements ISidedInventory, IFluidHandler, IPowerReceptor, IEnergySink

{
    private ForgeDirection connectedDirection = ForgeDirection.UNKNOWN;
    private IInventory inventory;
    private int[] accessibleSlots;
    private IFluidHandler fluidHandler;
    private IPowerReceptor powerReceptor;
    private IEnergySink energySink;

    public TileBlockExtender()
    {
        super();
    }

    public void setConnectedSide(int connectedSide)
    {
        this.connectedDirection = ForgeDirection.getOrientation(connectedSide);
    }

    public void setInventory(IInventory inventory)
    {
        this.inventory = inventory;
        if (inventory != null)
        {
            accessibleSlots = new int[inventory.getSizeInventory()];
            for (int i = 0; i < inventory.getSizeInventory(); i++)
            {
                accessibleSlots[i] = i;
            }
        }
    }

    public void setFluidHandler(IFluidHandler fluidHandler)
    {
        this.fluidHandler = fluidHandler;
    }

    public void setPowerReceptor(IPowerReceptor powerReceptor)
    {
        this.powerReceptor = powerReceptor;
    }

    public void setEnergySink(IEnergySink energySink)
    {
        if (this.energySink == null && energySink != null)
        {
            this.energySink = energySink;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        }
        else
        {
            if (energySink == null)
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }
            this.energySink = energySink;
        }
    }

    @Override
    public void invalidate()
    {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (connectedDirection != ForgeDirection.UNKNOWN)
        {
            if (!hasConnection())
            {
                TileEntity tile = worldObj.getBlockTileEntity(this.xCoord + connectedDirection.offsetX, this.yCoord + connectedDirection.offsetY, this.zCoord + connectedDirection.offsetZ);
                if (tile != null)
                {
                    if (tile instanceof IInventory)
                    {
                        setInventory((IInventory)tile);
                    }
                    if (tile instanceof IFluidHandler)
                    {
                        setFluidHandler((IFluidHandler)tile);
                    }
                    if (Loader.isModLoaded("BuildCraft|Energy") && tile instanceof IPowerReceptor)
                    {
                        setPowerReceptor((IPowerReceptor)tile);
                    }
                    if (Loader.isModLoaded("IC2") && tile instanceof IEnergySink)
                    {
                        setEnergySink((IEnergySink) tile);
                    }
                }
            }
        }
    }

    private boolean hasConnection()
    {
        if (inventory != null || fluidHandler != null)
        {
            return true;
        }
        if (Loader.isModLoaded("BuildCraft|Energy") && powerReceptor != null)
        {
            return true;
        }
        if (Loader.isModLoaded("IC2") && energySink != null)
        {
            return true;
        }
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory)inventory).getAccessibleSlotsFromSide(i);
            }
            return accessibleSlots;
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory)inventory).canInsertItem(i, itemStack, i2);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i2)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory)inventory).canExtractItem(i, itemStack, i2);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        if (inventory != null)
        {
            return inventory.getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        if (inventory != null)
        {
            return inventory.getStackInSlot(i);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int i2)
    {
        if (inventory != null)
        {
            return inventory.decrStackSize(i, i2);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (inventory != null)
        {
            return inventory.getStackInSlotOnClosing(i);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack)
    {
        if (inventory != null)
        {
            inventory.setInventorySlotContents(i, itemStack);
        }
    }

    @Override
    public String getInvName()
    {
        if (inventory != null)
        {
            return inventory.getInvName();
        }
        return null;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        if (inventory != null)
        {
            return inventory.isInvNameLocalized();
        }
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        if (inventory != null)
        {
            return inventory.getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        if (inventory != null)
        {
            return inventory.isUseableByPlayer(entityPlayer);
        }
        return false;
    }

    @Override
    public void openChest()
    {
        if (inventory != null)
        {
            inventory.openChest();
        }
    }

    @Override
    public void closeChest()
    {
        if (inventory != null)
        {
            inventory.closeChest();
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack)
    {
        if (inventory != null)
        {
            return inventory.isItemValidForSlot(i, itemStack);
        }
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.fill(from, resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.drain(from, resource, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.drain(from, maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.canFill(from, fluid);
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.canDrain(from, fluid);
        }
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        if (fluidHandler != null)
        {
            return fluidHandler.getTankInfo(from);
        }
        return new FluidTankInfo[0];
    }

    @Method(modid = "BuildCraft|Energy")
    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection forgeDirection)
    {
        if (powerReceptor != null)
        {
            return powerReceptor.getPowerReceiver(forgeDirection);
        }
        return null;
    }

    @Method(modid = "BuildCraft|Energy")
    @Override
    public void doWork(PowerHandler powerHandler)
    {
        if (powerReceptor != null)
        {
            powerReceptor.doWork(powerHandler);
        }
    }

    @Method(modid = "BuildCraft|Energy")
    @Override
    public World getWorld()
    {
        if (powerReceptor != null)
        {
            return powerReceptor.getWorld();
        }
        return null;
    }

    @Method(modid = "IC2")
    @Override
    public double demandedEnergyUnits()
    {
        if (energySink != null)
        {
            return energySink.demandedEnergyUnits();
        }
        return 0;
    }

    @Method(modid = "IC2")
    @Override
    public double injectEnergyUnits(ForgeDirection forgeDirection, double v)
    {
        if (energySink != null)
        {
            return energySink.injectEnergyUnits(forgeDirection, v);
        }
        return 0;
    }

    @Method(modid = "IC2")
    @Override
    public int getMaxSafeInput()
    {
        if (energySink != null)
        {
            return energySink.getMaxSafeInput();
        }
        return 0;
    }

    @Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection)
    {
        if (energySink != null)
        {
            return energySink.acceptsEnergyFrom(tileEntity, forgeDirection);
        }
        return false;
    }

    /*
    NBT stuffs
     */

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        connectedDirection = ForgeDirection.getOrientation(compound.getByte("side"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("side", (byte)connectedDirection.ordinal());
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        readFromNBT(pkt.data);
        super.onDataPacket(net, pkt);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, compound);
    }
}
