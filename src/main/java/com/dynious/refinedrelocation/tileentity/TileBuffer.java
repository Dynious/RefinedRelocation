package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.transport.IPipeTile;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.helper.LoopHelper;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.IC2Helper;
import com.dynious.refinedrelocation.tileentity.energy.TileUniversalElectricity;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Optional.Interface(iface = "buildcraft.api.power.IPowerEmitter", modid = Mods.BC_API_POWER_ID)
public class TileBuffer extends TileUniversalElectricity implements ISidedInventory, IFluidHandler, IPowerEmitter, ILoopable
{
    protected TileEntity[] tiles = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    protected boolean firstRun = true;

    protected int bufferedSide = -1;
    public ItemStack bufferedItemStack = null;

    public boolean containsItemStack = false;

    private PowerHandler powerHandler;

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
            if (firstRun)
            {
                onBlocksChanged();
                firstRun = false;
                worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, bufferedItemStack == null ? 0 : 1);
                if (Mods.IS_IC2_LOADED)
                {
                    IC2Helper.addToEnergyNet(this);
                }
            }
            if (bufferedItemStack != null)
            {
                bufferedItemStack = outputItemStack(bufferedItemStack, bufferedSide);
                if (bufferedItemStack == null)
                {
                    worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 0);
                }
            }
        }
    }

    public void onBlocksChanged()
    {
        tiles = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tile =  DirectionHelper.getTileAtSide(this, direction);
            if (!LoopHelper.isLooping(this, tile))
            {
                tiles[direction.ordinal()] = tile;
            }
        }
    }

    public ItemStack setBufferedItemStack(ItemStack itemStack, int side)
    {
        if (bufferedItemStack == null)
        {
            bufferedItemStack = itemStack;
            bufferedSide = side;
            worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
            return null;
        }
        else
        {
            return itemStack;
        }
    }
    
    public List<ForgeDirection> getOutputSidesForInsertDirection(ForgeDirection insertDirection)
    {
        List<ForgeDirection> outputSides = new ArrayList<ForgeDirection>();

        // the opposite side of the insert direction has the highest priority by default
        ForgeDirection oppositeDirection = insertDirection.getOpposite();
        if (oppositeDirection != ForgeDirection.UNKNOWN)
            outputSides.add(oppositeDirection);

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        {
            if (dir != insertDirection && dir != oppositeDirection)
                outputSides.add(dir);
        }
        return outputSides;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return new int[]{var1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side)
    {
        if (bufferedItemStack != null)
        {
            return false;
        }
        ItemStack addingItemStack = itemstack.copy();
        for (ForgeDirection outputSide : getOutputSidesForInsertDirection(ForgeDirection.getOrientation(side)))
        {
            TileEntity tile = tiles[outputSide.ordinal()];
            if (tile != null && IOHelper.insert(tile, addingItemStack, outputSide.getOpposite(), true) == null)
            {
                return true;
            }
        }
        return false;
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
        for (ForgeDirection outputSide : getOutputSidesForInsertDirection(ForgeDirection.getOrientation(inputSide)))
        {
            itemstack = insertItemStack(itemstack, outputSide.ordinal());
            if (itemstack == null || itemstack.stackSize == 0)
                return null;
        }
        return itemstack;
    }

    public ItemStack insertItemStack(ItemStack itemstack, int side)
    {
        return IOHelper.insert(tiles[side], itemstack, ForgeDirection.getOrientation(side).getOpposite(), false);
    }

    @Override
    public String getInventoryName()
    {
        return "buffer";
    }

    @Override
    public boolean hasCustomInventoryName()
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
    public void openInventory() {}

    @Override
    public void closeInventory() {}

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
    public List<TileEntity> getConnectedTiles()
    {
        return Arrays.asList(tiles);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("bufferedSide"))
        {
            bufferedSide = compound.getByte("bufferedSide");
            NBTTagList tagList = compound.getTagList("Items", 10);
            bufferedItemStack = ItemStack.loadItemStackFromNBT(tagList.getCompoundTagAt(0));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (bufferedItemStack != null)
        {
            compound.setByte("bufferedSide", (byte) bufferedSide);
            NBTTagList nbttaglist = new NBTTagList();
            NBTTagCompound tag = new NBTTagCompound();
            this.bufferedItemStack.writeToNBT(tag);
            nbttaglist.appendTag(tag);
            compound.setTag("Items", nbttaglist);
        }
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        int inputAmount = resource.amount;
        for (ForgeDirection outputSide : getOutputSidesForInsertDirection(from))
        {
            resource = insertFluidStack(resource, outputSide.ordinal());
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

    @Optional.Method(modid = "IC2")
    @Override
    public double demandedEnergyUnits()
    {
        return Double.MAX_VALUE;
    }

    @Optional.Method(modid = Mods.IC2_ID)
    @Override
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
    {
        double inputAmount = amount;
        for (ForgeDirection outputSide : getOutputSidesForInsertDirection(directionFrom))
        {
            amount = insertEnergyUnits(amount, outputSide.ordinal());
            if (amount == 0)
            {
                return inputAmount;
            }
        }
        return inputAmount - amount;
    }

    @Optional.Method(modid = Mods.IC2_ID)
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

    @Optional.Method(modid = Mods.IC2_ID)
    @Override
    public int getMaxSafeInput()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public void invalidate()
    {
        if (!worldObj.isRemote && Mods.IS_IC2_LOADED)
        {
            IC2Helper.removeFromEnergyNet(this);
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        if (!worldObj.isRemote && Mods.IS_IC2_LOADED)
        {
            IC2Helper.removeFromEnergyNet(this);
        }
        super.onChunkUnload();
    }

    @Optional.Method(modid = Mods.IC2_ID)
    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
    {
        return true;
    }

    @Optional.Method(modid = Mods.BC_API_POWER_ID)
    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection forgeDirection)
    {
        return getPowerReceiver();
    }

    @Optional.Method(modid = Mods.BC_API_POWER_ID)
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

    @Optional.Method(modid = Mods.BC_API_POWER_ID)
    @Override
    public void doWork(PowerHandler powerHandler)
    {
        for (ForgeDirection outputSide : getOutputSidesForInsertDirection(ForgeDirection.UNKNOWN))
        {
            double usedEnergy = powerHandler.getEnergyStored() - insertMinecraftJoules(powerHandler.getEnergyStored(), outputSide.ordinal());
            powerHandler.useEnergy(usedEnergy, usedEnergy, true);
            if (powerHandler.getEnergyStored() == 0)
            {
                return;
            }
        }
    }

    @Optional.Method(modid = Mods.BC_API_POWER_ID)
    public double insertMinecraftJoules(double amount, int side)
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

    @Optional.Method(modid = Mods.BC_API_POWER_ID)
    @Override
    public World getWorld()
    {
        return this.getWorldObj();
    }

    @Optional.Method(modid = Mods.BC_API_POWER_ID)
    @Override
    public boolean canEmitPowerFrom(ForgeDirection direction)
    {
        return true;
    }

    /*

        @Optional.Method(modid = Mods.COFH_CORE_ID)
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        int inputAmount = i;
        for (ForgeDirection outputSide : getOutputSidesForInsertDirection(forgeDirection))
        {
            i = insertRedstoneFlux(i, outputSide.ordinal(), b);
            if (i == 0)
            {
                return inputAmount;
            }
        }
        return inputAmount - i;
    }

    @Optional.Method(modid = Mods.COFH_CORE_ID)
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

    @Optional.Method(modid = Mods.COFH_CORE_ID)
    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        return 0;
    }

    @Optional.Method(modid = Mods.COFH_CORE_ID)
    @Override
    public boolean canInterface(ForgeDirection forgeDirection)
    {
        return true;
    }

    @Optional.Method(modid = Mods.COFH_CORE_ID)
    @Override
    public int getEnergyStored(ForgeDirection forgeDirection)
    {
        return 0;
    }

    @Optional.Method(modid = Mods.UE_ID)
    @Override
    public long onReceiveEnergy(ForgeDirection direction, long l, boolean b)
    {
        long inputAmount = l;
        for (ForgeDirection outputSide : getOutputSidesForInsertDirection(direction))
        {
            l = insertUEEnergy(l, outputSide.ordinal(), b);
            if (l == 0)
            {
                return inputAmount;
            }
        }
        return inputAmount - l;
    }

    @Optional.Method(modid = Mods.UE_ID)
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

    @Optional.Method(modid = Mods.UE_ID)
    @Override
    public long onExtractEnergy(ForgeDirection direction, long l, boolean b)
    {
        return 0;
    }

    @Optional.Method(modid = Mods.UE_ID)
    @Override
    public boolean canConnect(ForgeDirection direction, Object o)
    {
        return false;
    }
    */
}
