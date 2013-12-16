package com.dynious.blex.tileentity;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
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

@InterfaceList(value = {
        @Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy"),
        @Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore")})
public class TileBlockExtender extends TileEntity implements ISidedInventory, IFluidHandler, IPowerReceptor, IEnergySink, IEnergyHandler

{
    protected ForgeDirection connectedDirection = ForgeDirection.UNKNOWN;
    protected IInventory inventory;
    protected int[] accessibleSlots;
    protected IFluidHandler fluidHandler;
    protected IPowerReceptor powerReceptor;
    protected IEnergySink energySink;
    protected IEnergyHandler energyHandler;
    protected TileEntity[] tiles = new TileEntity[ForgeDirection.values().length];
    public boolean blocksChanged = true;
    private float lightAmount = 0F;

    public TileBlockExtender()
    {
        super();
    }

    public void setConnectedSide(int connectedSide)
    {
        this.connectedDirection = ForgeDirection.getOrientation(connectedSide);
    }

    public ForgeDirection getConnectedDirection()
    {
        return connectedDirection;
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

    public void setEnergyHandler(IEnergyHandler energyHandler)
    {
        this.energyHandler = energyHandler;
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

    public TileEntity[] getTiles()
    {
        return tiles;
    }

    @Override
    public void invalidate()
    {
        if (this.energySink != null)
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        super.invalidate();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (connectedDirection != ForgeDirection.UNKNOWN)
        {
            TileEntity tile = worldObj.getBlockTileEntity(this.xCoord + connectedDirection.offsetX, this.yCoord + connectedDirection.offsetY, this.zCoord + connectedDirection.offsetZ);
            if (!hasConnection())
            {
                if (tile != null && !(tile instanceof TileBlockExtender && ((TileBlockExtender)tile).connectedDirection == this.connectedDirection.getOpposite()))
                {
                    if (tile instanceof IInventory)
                    {
                        setInventory((IInventory) tile);
                    }
                    if (tile instanceof IFluidHandler)
                    {
                        setFluidHandler((IFluidHandler) tile);
                    }
                    if (Loader.isModLoaded("BuildCraft|Energy") && tile instanceof IPowerReceptor)
                    {
                        setPowerReceptor((IPowerReceptor) tile);
                    }
                    if (Loader.isModLoaded("IC2") && tile instanceof IEnergySink)
                    {
                        setEnergySink((IEnergySink) tile);
                    }
                    if (Loader.isModLoaded("CoFHCore") && tile instanceof IEnergyHandler)
                    {
                        setEnergyHandler((IEnergyHandler) tile);
                    }
                }
            }
            else if (tile == null)
            {
                setInventory(null);
                setFluidHandler(null);
                setPowerReceptor(null);
                setEnergySink(null);
                setEnergyHandler(null);
            }
        }
        if (blocksChanged)
        {
            for (ForgeDirection direction : ForgeDirection.values())
            {
                if (direction != connectedDirection)
                {
                    tiles[direction.ordinal()] = worldObj.getBlockTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
                }
            }
            blocksChanged = false;
        }
        if (lightAmount > 0F)
        {
            lightAmount = lightAmount - 0.01F;
        }
    }

    protected boolean hasConnection()
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
        if (Loader.isModLoaded("CoFHCore") && energyHandler != null)
        {
            return true;
        }
        return false;
    }

    public void objectTransported()
    {
        lightAmount = 0.15F;
    }

    public float getLightAmount()
    {
        return lightAmount;
    }

    /*
    Item/Fluid/Power interaction
     */

    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        if (inventory != null)
        {
            if (inventory instanceof ISidedInventory)
            {
                return ((ISidedInventory) inventory).getAccessibleSlotsFromSide(i);
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
                if (((ISidedInventory) inventory).canInsertItem(i, itemStack, i2))
                {
                    objectTransported();
                    return true;
                }
                return false;
            }
            objectTransported();
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
                if (((ISidedInventory) inventory).canExtractItem(i, itemStack, i2))
                {
                    objectTransported();
                    return true;
                }
                return false;
            }
            objectTransported();
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
            int amount =  fluidHandler.fill(from, resource, doFill);
            if (amount > 0 && doFill)
            {
                objectTransported();
            }
            return amount;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (fluidHandler != null)
        {
            FluidStack amount =  fluidHandler.drain(from, resource, doDrain);
            if (amount.amount > 0 && doDrain)
            {
                objectTransported();
            }
            return amount;
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (fluidHandler != null)
        {
            FluidStack amount =  fluidHandler.drain(from, maxDrain, doDrain);
            if (amount.amount > 0 && doDrain)
            {
                objectTransported();
            }
            return amount;
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
            objectTransported();
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
            double amount =  energySink.injectEnergyUnits(forgeDirection, v);
            if (amount > 0)
            {
                objectTransported();
            }
            return amount;
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

    @Method(modid = "CoFHCore")
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (energyHandler != null)
        {
            int amount =  energyHandler.receiveEnergy(forgeDirection, i, b);
            if (amount > 0 && b)
            {
                objectTransported();
            }
            return amount;
        }
        return 0;
    }

    @Method(modid = "CoFHCore")
    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (energyHandler != null)
        {
            int amount =  energyHandler.extractEnergy(forgeDirection, i, b);
            if (amount > 0 && b)
            {
                objectTransported();
            }
            return amount;
        }
        return 0;
    }

    @Method(modid = "CoFHCore")
    @Override
    public boolean canInterface(ForgeDirection forgeDirection)
    {
        if (energyHandler != null)
        {
            return energyHandler.canInterface(forgeDirection);
        }
        return false;
    }

    @Method(modid = "CoFHCore")
    @Override
    public int getEnergyStored(ForgeDirection forgeDirection)
    {
        if (energyHandler != null)
        {
            return energyHandler.getEnergyStored(forgeDirection);
        }
        return 0;
    }

    @Method(modid = "CoFHCore")
    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection)
    {
        if (energyHandler != null)
        {
            return energyHandler.getMaxEnergyStored(forgeDirection);
        }
        return 0;
    }

    /*
    NBT stuffs
     */

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        setConnectedSide(compound.getByte("side"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("side", (byte) connectedDirection.ordinal());
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        setConnectedSide(pkt.data.getByte("side"));
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("side", (byte) connectedDirection.ordinal());
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, compound);
    }
}
