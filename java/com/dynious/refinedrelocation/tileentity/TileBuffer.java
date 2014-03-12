package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.transport.IPipeTile;
import cofh.api.energy.IEnergyHandler;
import cofh.api.transport.IItemConduit;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.api.energy.IEnergyInterface;

@Optional.InterfaceList(value = {
        @Optional.Interface(iface = "buildcraft.api.power.IPowerReceptor", modid = "BuildCraft|Energy"),
        @Optional.Interface(iface = "buildcraft.api.power.IPowerEmitter", modid = "BuildCraft|Energy"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
        @Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore"),
        @Optional.Interface(iface = "universalelectricity.api.energy.IEnergyInterface", modid = "UniversalElectricity")})
public class TileBuffer extends TileEntity implements ISidedInventory, IFluidHandler, IPowerEmitter, IPowerReceptor, IEnergySink, IEnergyHandler, IEnergyInterface
{
    protected TileEntity[] tiles = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    protected boolean firstRun = true;

    protected int bufferedSide = -1;
    protected ItemStack bufferedItemStack = null;

    public boolean containsItemStack = false;

    private PowerHandler powerHandler;

    @Override
    public void updateEntity()
    {
        if (firstRun)
        {
            onBlocksChanged();
            firstRun = false;
            worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, bufferedItemStack == null ? 0 : 1);
            if (!worldObj.isRemote && Loader.isModLoaded("IC2"))
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            }
        }
        if (!worldObj.isRemote)
        {
            if (bufferedItemStack != null)
            {
                bufferedItemStack = outputItemStack(bufferedItemStack, bufferedSide);
                if (bufferedItemStack == null)
                {
                    worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, 0);
                }
            }
        }
    }

    public void onBlocksChanged()
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            tiles[direction.ordinal()] = DirectionHelper.getTileAtSide(this, direction);
        }
    }

    public ItemStack setBufferedItemStack(ItemStack itemStack, int side)
    {
        if (bufferedItemStack == null)
        {
            bufferedItemStack = itemStack;
            bufferedSide = side;
            worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, 1);
            return null;
        }
        else
        {
            return itemStack;
        }
    }

    public int getNextInsertSide(int currentInsertSide)
    {
        if (currentInsertSide + 1 < tiles.length)
        {
            return currentInsertSide + 1;
        }
        else
        {
            return -1;
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return new int[]{var1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side)
    {
        return bufferedItemStack == null;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
        itemstack = outputItemStack(itemstack, slot);

        if (itemstack != null && setBufferedItemStack(itemstack, slot) != null)
        {
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, itemstack));
        }
    }

    public ItemStack outputItemStack(ItemStack itemstack, int inputSide)
    {
        int currentInsetSide = -1;
        while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
        {
            if (currentInsetSide == inputSide)
                continue;
            itemstack = insertItemStack(itemstack, currentInsetSide);
            if (itemstack == null || itemstack.stackSize == 0)
            {
                return null;
            }
        }
        return itemstack;
    }

    public ItemStack insertItemStack(ItemStack itemstack, int side)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (Loader.isModLoaded("CoFHCore") && tile instanceof IItemConduit)
            {
                return ((IItemConduit) tile).insertItem(ForgeDirection.getOrientation(side).getOpposite(), itemstack);
            }
            else if (Loader.isModLoaded("BuildCraft|Transport") && tile instanceof IPipeTile)
            {
                IPipeTile pipe = (IPipeTile) tile;
                if (pipe.isPipeConnected(ForgeDirection.getOrientation(side).getOpposite()))
                {
                    int size = pipe.injectItem(itemstack, true, ForgeDirection.getOrientation(side).getOpposite());
                    itemstack.stackSize -= size;
                    return itemstack;
                }
            }
            else if (tile instanceof IInventory)
            {
                return TileEntityHopper.insertStack((IInventory) tile, itemstack, ForgeDirection.OPPOSITES[side]);
            }
        }
        return itemstack;
    }

    @Override
    public String getInvName()
    {
        return "buffer";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {

    }

    @Override
    public void closeChest()
    {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }

    public boolean rotateBlock()
    {
        return false;
    }

    @Override
    public boolean receiveClientEvent(int eventId, int eventData)
    {
        switch (eventId)
        {
            case 1:
                containsItemStack = eventData == 1;
                return true;
        }
        return super.receiveClientEvent(eventId, eventData);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("bufferedSide"))
        {
            bufferedSide = par1NBTTagCompound.getByte("bufferedSide");
            this.bufferedItemStack = ItemStack.loadItemStackFromNBT(par1NBTTagCompound);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        if (bufferedItemStack != null)
        {
            par1NBTTagCompound.setByte("bufferedSide", (byte) bufferedSide);
            this.bufferedItemStack.writeToNBT(par1NBTTagCompound);
        }
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int inputAmount = resource.amount;
        int currentInsetSide = -1;
        while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
        {
            if (currentInsetSide == from.ordinal() || DirectionHelper.getTileAtSide(this, ForgeDirection.getOrientation(currentInsetSide)) instanceof TileBlockExtender)
                continue;
            resource = insertFluidStack(resource, currentInsetSide);
            if (resource == null || resource.amount == 0)
            {
                return inputAmount;
            }
        }

        return inputAmount - resource.amount;
    }

    public FluidStack insertFluidStack(FluidStack fluidStack, int side)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (tile instanceof IFluidHandler)
            {
                fluidStack.amount -= ((IFluidHandler)tile).fill(ForgeDirection.getOrientation(side).getOpposite(), fluidStack, true);
            }
        }
        if (fluidStack.amount == 0)
        {
            return null;
        }
        return fluidStack;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[]{new FluidTankInfo(null, Integer.MAX_VALUE)};
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        int inputAmount = i;
        int currentInsetSide = -1;
        while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
        {
            if (currentInsetSide == forgeDirection.ordinal() || DirectionHelper.getTileAtSide(this, ForgeDirection.getOrientation(currentInsetSide)) instanceof TileBlockExtender)
                continue;
            i = insertRedstoneFlux(i, currentInsetSide, b);
            if (i == 0)
            {
                return inputAmount;
            }
        }

        return inputAmount - i;
    }

    public int insertRedstoneFlux(int amount, int side, boolean simulate)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (tile instanceof IEnergyHandler)
            {
                amount -= ((IEnergyHandler)tile).receiveEnergy(ForgeDirection.getOrientation(side).getOpposite(), amount, simulate);
            }
        }
        return amount;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        return 0;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public boolean canInterface(ForgeDirection forgeDirection)
    {
        return true;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getEnergyStored(ForgeDirection forgeDirection)
    {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection)
    {
        return Integer.MAX_VALUE;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double demandedEnergyUnits()
    {
        return Double.MAX_VALUE;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
    {
        double inputAmount = amount;
        int currentInsetSide = -1;
        while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
        {
            if (currentInsetSide == directionFrom.ordinal() || DirectionHelper.getTileAtSide(this, ForgeDirection.getOrientation(currentInsetSide)) instanceof TileBlockExtender)
                continue;
            amount = insertEnergyUnits(amount, currentInsetSide);
            if (amount == 0)
            {
                return inputAmount;
            }
        }

        return inputAmount - amount;
    }

    public double insertEnergyUnits(double amount, int side)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (tile instanceof IEnergySink)
            {
                amount -= ((IEnergySink)tile).injectEnergyUnits(ForgeDirection.getOrientation(side).getOpposite(), Math.min(amount, ((IEnergySink)tile).getMaxSafeInput()));
            }
        }
        return amount;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getMaxSafeInput()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public void invalidate()
    {
        if (!worldObj.isRemote && Loader.isModLoaded("IC2"))
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        if (!worldObj.isRemote && Loader.isModLoaded("IC2"))
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.onChunkUnload();
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
    {
        return true;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection forgeDirection)
    {
        return getPowerReceiver();
    }

    public PowerHandler.PowerReceiver getPowerReceiver()
    {
        if (powerHandler == null)
        {
            powerHandler = new PowerHandler(this, PowerHandler.Type.MACHINE);
            powerHandler.configure(0.1F, 500F, 0.01F, 500F);
            powerHandler.configurePowerPerdition(0, 0);
        }
        return powerHandler.getPowerReceiver();
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    @Override
    public void doWork(PowerHandler powerHandler)
    {
        int currentInsetSide = -1;
        while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
        {
            if (DirectionHelper.getTileAtSide(this, ForgeDirection.getOrientation(currentInsetSide)) instanceof TileBlockExtender)
                continue;
            float usedEnergy = powerHandler.getEnergyStored() - insertMinecraftJoules(powerHandler.getEnergyStored(), currentInsetSide);
            powerHandler.useEnergy(usedEnergy, usedEnergy, true);
            if (powerHandler.getEnergyStored() == 0)
            {
                return;
            }
        }
    }

    public float insertMinecraftJoules(float amount, int side)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (tile instanceof IPowerReceptor)
            {
                PowerHandler.PowerReceiver powerReceiver = ((IPowerReceptor)tile).getPowerReceiver(ForgeDirection.getOrientation(side).getOpposite());
                if (powerReceiver != null)
                {
                    amount -= powerReceiver.receiveEnergy(PowerHandler.Type.PIPE, amount, ForgeDirection.getOrientation(side).getOpposite());
                }
            }
        }
        return amount;
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    @Override
    public World getWorld()
    {
        return this.getWorldObj();
    }

    @Optional.Method(modid = "BuildCraft|Energy")
    @Override
    public boolean canEmitPowerFrom(ForgeDirection direction)
    {
        return true;
    }

    @Optional.Method(modid = "UniversalElectricity")
    @Override
    public long onReceiveEnergy(ForgeDirection direction, long l, boolean b)
    {
        long inputAmount = l;
        int currentInsetSide = -1;
        while ((currentInsetSide = getNextInsertSide(currentInsetSide)) != -1)
        {
            if (currentInsetSide == direction.ordinal() || DirectionHelper.getTileAtSide(this, ForgeDirection.getOrientation(currentInsetSide)) instanceof TileBlockExtender)
                continue;
            l = insertUEEnergy(l, currentInsetSide, b);
            if (l == 0)
            {
                return inputAmount;
            }
        }

        return inputAmount - l;
    }

    public long insertUEEnergy(long amount, int side, boolean simulate)
    {
        TileEntity tile = tiles[side];
        if (tile != null)
        {
            if (tile instanceof IEnergyInterface)
            {
                amount -= ((IEnergyInterface)tile).onReceiveEnergy(ForgeDirection.getOrientation(side).getOpposite(), amount, simulate);
            }
        }
        return amount;
    }

    @Optional.Method(modid = "UniversalElectricity")
    @Override
    public long onExtractEnergy(ForgeDirection direction, long l, boolean b)
    {
        return 0;
    }

    @Optional.Method(modid = "UniversalElectricity")
    @Override
    public boolean canConnect(ForgeDirection direction, Object o)
    {
        return false;
    }
}
