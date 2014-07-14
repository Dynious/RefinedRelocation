package com.dynious.refinedrelocation.tileentity;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.helper.LoopHelper;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.mods.IC2Helper;
import com.dynious.refinedrelocation.tileentity.energy.TileUniversalElectricity;
import cpw.mods.fml.common.Loader;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
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

import static cpw.mods.fml.common.Optional.Method;

public class TileBlockExtender extends TileUniversalElectricity implements ISidedInventory, IFluidHandler, IDisguisable, ILoopable
{
    protected ForgeDirection connectedDirection = ForgeDirection.UNKNOWN;
    protected ForgeDirection previousConnectedDirection = ForgeDirection.UNKNOWN;
    protected IInventory inventory;
    protected int[] accessibleSlots;
    protected IFluidHandler fluidHandler;
    protected IPowerReceptor powerReceptor;
    protected IEnergySink energySink;
    /*
    protected IEnergyHandler energyHandler;
    protected IEnergyInterface energyInterface;
    */
    protected TileEntity[] tiles = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    public boolean blocksChanged = true;
    protected boolean isRedstonePowered = false;
    protected boolean isRedstoneEnabled = true;
    public Block blockDisguisedAs = null;
    public int blockDisguisedMetadata = 0;

    public TileBlockExtender()
    {
        super();
    }

    @Override
    public boolean canDisguise()
    {
        return true;
    }
    
    @Override
    public boolean canDisguiseAs(Block block, int metadata)
    {
        return block.isOpaqueCube();
    }

    @Override
    public Block getDisguise()
    {
        return blockDisguisedAs;
    }

    @Override
    public int getDisguiseMeta()
    {
        return blockDisguisedMetadata;
    }

    @Override
    public void setDisguise(Block block, int metadata)
    {
        blockDisguisedAs = block;
        blockDisguisedMetadata = metadata;
        if (worldObj != null)
        {
            worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);
        }
    }

    @Override
    public void clearDisguise()
    {
        setDisguise(null, 0);
    }

    public void setConnectedSide(int connectedSide)
    {
        this.connectedDirection = ForgeDirection.getOrientation(connectedSide);
        this.blocksChanged = true;
        if (worldObj != null)
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
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

    @Method(modid = Mods.BC_API_POWER_ID)
    public void setPowerReceptor(IPowerReceptor powerReceptor)
    {
        this.powerReceptor = powerReceptor;
    }

    @Method(modid = Mods.IC2_ID)
    public void setEnergySink(IEnergySink energySink)
    {
        if (this.energySink == null && energySink != null)
        {
            this.energySink = energySink;
            if (!worldObj.isRemote)
            {
                IC2Helper.addToEnergyNet(this);
            }
        }
        else if (this.energySink != null)
        {
            if (energySink == null && !worldObj.isRemote)
            {
                IC2Helper.removeFromEnergyNet(this);
            }
            this.energySink = energySink;
        }
    }

    /*
    
    @Method(modid = Mods.COFH_CORE_ID)
    public void setEnergyHandler(IEnergyHandler energyHandler)
    {
        this.energyHandler = energyHandler;
    }

    @Method(modid = Mods.UE_ID)
    public void setEnergyInterface(IEnergyInterface energyInterface)
    {
        this.energyInterface = energyInterface;
    }

    */

    public IInventory getInventory()
    {
        return inventory;
    }

    public IFluidHandler getFluidHandler()
    {
        return fluidHandler;
    }

    @Method(modid = Mods.BC_API_POWER_ID)
    public IPowerReceptor getPowerReceptor()
    {
        return powerReceptor;
    }

    @Method(modid = Mods.IC2_ID)
    public IEnergySink getEnergySink()
    {
        return energySink;
    }

    /*
    @Method(modid = Mods.COFH_CORE_ID)
    public IEnergyHandler getEnergyHandler()
    {
        return energyHandler;
    }

    @Method(modid = Mods.UE_ID)
    public IEnergyInterface getEnergyInterface()
    {
        return energyInterface;
    }

    */

    public TileEntity[] getTiles()
    {
        return tiles;
    }

    @Override
    public void invalidate()
    {
        if (Loader.isModLoaded(Mods.IC2_ID) && this.getEnergySink() != null)
        {
            IC2Helper.removeFromEnergyNet(this);
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        if (Loader.isModLoaded(Mods.IC2_ID) && this.getEnergySink() != null)
        {
            IC2Helper.removeFromEnergyNet(this);
        }
        super.onChunkUnload();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (canConnect())
        {
            TileEntity tile = null;

            if (connectedDirection != previousConnectedDirection)
            {
                //Look up the tile we are connected to
                tile = getConnectedTile();

                resetConnections();
                checkConnectedDirection(tile);
                previousConnectedDirection = connectedDirection;
                worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }

            if (blocksChanged)
            {
                //If we haven't looked up the tile we are connected to, do that
                if (tile == null)
                {
                    tile = getConnectedTile();
                }

                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                {
                    if (direction != connectedDirection)
                    {
                        tiles[direction.ordinal()] = DirectionHelper.getTileAtSide(this, direction);
                    }
                }
                this.checkRedstonePower();

                if (tile == null)
                {
                    resetConnections();
                    worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
                    worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                }
                else
                {
                    checkConnectedDirection(tile);
                }

                blocksChanged = false;
            }
        }
    }

    protected void checkConnectedDirection(TileEntity tile)
    {
        if (tile != null && !LoopHelper.isLooping(this, tile))
        {
            boolean updated = false;
            if (tile instanceof IInventory)
            {
                if (getInventory() == null)
                {
                    updated = true;
                }
                setInventory((IInventory) tile);
            }
            if (tile instanceof IFluidHandler)
            {
                if (getFluidHandler() == null)
                {
                    updated = true;
                }
                setFluidHandler((IFluidHandler) tile);
            }
            if (Mods.IS_BC_ENERGY_LOADED && tile instanceof IPowerReceptor)
            {
                if (getPowerReceptor() == null)
                {
                    updated = true;
                }
                setPowerReceptor((IPowerReceptor) tile);
            }
            if (Mods.IS_IC2_LOADED && tile instanceof IEnergySink)
            {
                if (getEnergySink() == null)
                {
                    updated = true;
                }
                setEnergySink((IEnergySink) tile);
            }

            /*

            if (Mods.IS_COFH_CORE_LOADED && tile instanceof IEnergyHandler)
            {
                if (getEnergyHandler() == null)
                {
                    updated = true;
                }
                setEnergyHandler((IEnergyHandler) tile);
            }
            if (Mods.IS_UE_LOADED && tile instanceof IEnergyInterface)
            {
                if (getEnergyInterface() == null)
                {
                    updated = true;
                }
                setEnergyInterface((IEnergyInterface) tile);
            }

            */
            
            if (updated)
            {
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
                worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    protected void resetConnections()
    {
        setInventory(null);
        setFluidHandler(null);

        if (Mods.IS_BC_ENERGY_LOADED)
            setPowerReceptor(null);
        if (Mods.IS_IC2_LOADED)
            setEnergySink(null);
        /*
        if (Mods.IS_COFH_CORE_LOADED)
            setEnergyHandler(null);
        if (Mods.IS_UE_LOADED)
            setEnergyInterface(null);
        */
    }

    public boolean hasConnection()
    {
        if (getInventory() != null || getFluidHandler() != null)
        {
            return true;
        }
        if (Mods.IS_BC_ENERGY_LOADED && getPowerReceptor() != null)
        {
            return true;
        }
        if (Mods.IS_IC2_LOADED && getEnergySink() != null)
        {
            return true;
        }
/*        if (Mods.IS_COFH_CORE_LOADED && getEnergyHandler() != null)
        {
            return true;
        }
        if (Mods.IS_UE_LOADED && getEnergyInterface() != null)
        {
            return true;
        }*/
        return false;
    }

    public List<String> getConnectionTypes()
    {
        List<String> connections = new ArrayList<String>();

        if (getInventory() != null)
            connections.add("Inventory");
        if (getFluidHandler() != null)
            connections.add("Fluid Transmission");
        if (Mods.IS_BC_ENERGY_LOADED && getPowerReceptor() != null)
            connections.add("Buildcraft Energy");
        if (Mods.IS_IC2_LOADED && getEnergySink() != null)
            connections.add("IC2 Energy");
/*        if (Mods.IS_COFH_CORE_LOADED && getEnergyHandler() != null)
            connections.add("Thermal Expansion Energy");
        if (Mods.IS_UE_LOADED && getEnergyInterface() != null)
            connections.add("Universal Electricity Energy");*/

        return connections;
    }

    public ForgeDirection getInputSide(ForgeDirection side)
    {
        return connectedDirection.getOpposite();
    }

    public boolean canConnect()
    {
        return connectedDirection != ForgeDirection.UNKNOWN;
    }

    public TileEntity getConnectedTile()
    {
        return DirectionHelper.getTileAtSide(this, connectedDirection);
    }

    public List<TileEntity> getConnectedTiles()
    {
        return Arrays.asList(DirectionHelper.getTileAtSide(this, connectedDirection));
    }

    public void checkRedstonePower()
    {
        boolean wasRedstonePowered = isRedstoneTransmissionActive();

        setRedstoneTransmissionActive(false);
        if (isRedstoneTransmissionEnabled())
        {
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                // facing direction is output only
                if (direction == connectedDirection)
                    continue;

                int indirectPowerLevelFromDirection = worldObj.getIndirectPowerLevelTo(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ, direction.ordinal());
                if (indirectPowerLevelFromDirection > 0)
                {
                    setRedstoneTransmissionActive(true);
                    break;
                }
            }
        }

        if (isRedstoneTransmissionActive() != wasRedstonePowered)
        {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            worldObj.notifyBlockOfNeighborChange(xCoord + connectedDirection.offsetX, yCoord + connectedDirection.offsetY, zCoord + connectedDirection.offsetZ, worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord));
        }
    }

    public int isPoweringTo(int side)
    {
        ForgeDirection realDir = ForgeDirection.getOrientation(side).getOpposite();

        if (isRedstoneTransmissionActive() && connectedDirection == realDir)
            return 15;

        return 0;
    }

    /*
    * Side:
    *  -1: UP
    *   0: NORTH
    *   1: EAST
    *   2: SOUTH
    *   3: WEST
    *   */
    public boolean canConnectRedstone(int side)
    {
        if (!this.isRedstoneTransmissionEnabled())
            return false;

        ForgeDirection realDirection = ForgeDirection.UNKNOWN;

        switch (side)
        {
            case -1:
                realDirection = ForgeDirection.UP;
                break;
            case 0:
                realDirection = ForgeDirection.NORTH;
                break;
            case 1:
                realDirection = ForgeDirection.EAST;
                break;
            case 2:
                realDirection = ForgeDirection.SOUTH;
                break;
            case 3:
                realDirection = ForgeDirection.WEST;
                break;
        }

        return realDirection != ForgeDirection.UNKNOWN && realDirection == connectedDirection;
    }

    /*
    Item/Fluid/Power interaction
     */

    @Override
    public int[] getAccessibleSlotsFromSide(int i)
    {
        if (getInventory() != null)
        {
            if (getInventory() instanceof ISidedInventory)
            {
                return ((ISidedInventory) getInventory()).getAccessibleSlotsFromSide(getInputSide(ForgeDirection.getOrientation(i)).ordinal());
            }
            return accessibleSlots;
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        if (getInventory() != null)
        {
            if (getInventory() instanceof ISidedInventory)
            {
                return ((ISidedInventory) getInventory()).canInsertItem(i, itemStack, getInputSide(ForgeDirection.getOrientation(i2)).ordinal());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int i2)
    {
        if (getInventory() != null)
        {
            if (getInventory() instanceof ISidedInventory)
            {
                return ((ISidedInventory) getInventory()).canExtractItem(i, itemStack, getInputSide(ForgeDirection.getOrientation(i2)).ordinal());
            }
            return true;
        }
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        if (getInventory() != null)
        {
            return getInventory().getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        if (getInventory() != null)
        {
            return getInventory().getStackInSlot(i);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int i2)
    {
        if (getInventory() != null)
        {
            return getInventory().decrStackSize(i, i2);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (getInventory() != null)
        {
            return getInventory().getStackInSlotOnClosing(i);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack)
    {
        if (getInventory() != null)
        {
            getInventory().setInventorySlotContents(i, itemStack);
        }
    }

    @Override
    public String getInventoryName()
    {
        if (getInventory() != null)
        {
            return getInventory().getInventoryName();
        }
        return null;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return getInventory() != null && getInventory().hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit()
    {
        if (getInventory() != null)
        {
            return getInventory().getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        return getInventory() != null && getInventory().isUseableByPlayer(entityPlayer);
    }

    @Override
    public void openInventory()
    {
        if (getInventory() != null)
        {
            getInventory().openInventory();
        }
    }

    @Override
    public void closeInventory()
    {
        if (getInventory() != null)
        {
            getInventory().closeInventory();
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack)
    {
        return getInventory() != null && getInventory().isItemValidForSlot(i, itemStack);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (getFluidHandler() != null)
        {
            return getFluidHandler().fill(getInputSide(from), resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (getFluidHandler() != null)
        {
            return getFluidHandler().drain(getInputSide(from), resource, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        if (getFluidHandler() != null)
        {
            return getFluidHandler().drain(getInputSide(from), maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return getFluidHandler() != null && getFluidHandler().canFill(getInputSide(from), fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return getFluidHandler() != null && getFluidHandler().canDrain(getInputSide(from), fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        if (getFluidHandler() != null)
        {
            return getFluidHandler().getTankInfo(getInputSide(from));
        }
        return new FluidTankInfo[0];
    }

    @Method(modid = Mods.BC_API_POWER_ID)
    @Override
    public PowerHandler.PowerReceiver getPowerReceiver(ForgeDirection forgeDirection)
    {
        if (getPowerReceptor() != null)
        {
            return getPowerReceptor().getPowerReceiver(getInputSide(forgeDirection));
        }
        return null;
    }

    @Method(modid = Mods.BC_API_POWER_ID)
    @Override
    public void doWork(PowerHandler powerHandler)
    {
        if (getPowerReceptor() != null)
        {
            getPowerReceptor().doWork(powerHandler);
        }
    }

    @Method(modid = Mods.BC_API_POWER_ID)
    @Override
    public World getWorld()
    {
        if (getPowerReceptor() != null)
        {
            return getPowerReceptor().getWorld();
        }
        return null;
    }

    @Method(modid = Mods.IC2_ID)
    @Override
    public double getDemandedEnergy()
    {
        if (getEnergySink() != null)
        {
            return getEnergySink().getDemandedEnergy();
        }
        return 0;
    }

    @Method(modid = Mods.IC2_ID)
    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double v, double v1)
    {
        if (getEnergySink() != null)
        {
            return getEnergySink().injectEnergy(getInputSide(forgeDirection), v, v1);
        }
        return 0;
    }

    @Method(modid = Mods.IC2_ID)
    @Override
    public int getSinkTier()
    {
        if (getEnergySink() != null)
        {
            return getEnergySink().getSinkTier();
        }
        return 0;
    }

    @Method(modid = Mods.IC2_ID)
    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection)
    {
        return getEnergySink() != null && getEnergySink().acceptsEnergyFrom(tileEntity, getInputSide(forgeDirection));
    }

    /*

    @Method(modid = Mods.COFH_CORE_ID)
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().receiveEnergy(getInputSide(forgeDirection), i, b);
        }
        return 0;
    }

    @Method(modid = Mods.COFH_CORE_ID)
    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().extractEnergy(getInputSide(forgeDirection), i, b);
        }
        return 0;
    }

    @Method(modid = Mods.COFH_CORE_ID)
    @Override
    public boolean canInterface(ForgeDirection forgeDirection)
    {
        return getEnergyHandler() != null && getEnergyHandler().canInterface(getInputSide(forgeDirection));
    }

    @Method(modid = Mods.COFH_CORE_ID)
    @Override
    public int getEnergyStored(ForgeDirection forgeDirection)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().getEnergyStored(getInputSide(forgeDirection));
        }
        return 0;
    }

    @Method(modid = Mods.COFH_CORE_ID)
    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection)
    {
        if (getEnergyHandler() != null)
        {
            return getEnergyHandler().getMaxEnergyStored(getInputSide(forgeDirection));
        }
        return 0;
    }

    @Method(modid = Mods.UE_ID)
    @Override
    public long onReceiveEnergy(ForgeDirection direction, long l, boolean b)
    {
        if (getEnergyInterface() != null)
        {
            return getEnergyInterface().onReceiveEnergy(getInputSide(direction), l, b);
        }
        return 0;
    }

    @Method(modid = Mods.UE_ID)
    @Override
    public long onExtractEnergy(ForgeDirection direction, long l, boolean b)
    {
        if (getEnergyInterface() != null)
        {
            return getEnergyInterface().onExtractEnergy(getInputSide(direction), l, b);
        }
        return 0;
    }

    @Method(modid = Mods.UE_ID)
    @Override
    public boolean canConnect(ForgeDirection direction, Object o)
    {
        if (getEnergyInterface() != null)
        {
            return getEnergyInterface().canConnect(getInputSide(direction), o);
        }
        return false;
    }

    */

    /*
    NBT stuffs
     */

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        setConnectedSide(compound.getByte("side"));
        setRedstoneTransmissionEnabled(compound.getBoolean("redstoneEnabled"));
        int disguiseBlockId = compound.getInteger("disguisedId");
        if (disguiseBlockId != 0)
        {
            int disguisedMeta = compound.getInteger("disguisedMeta");
            setDisguise(Block.getBlockById(disguiseBlockId), disguisedMeta);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("side", (byte) connectedDirection.ordinal());
        compound.setBoolean("redstoneEnabled", this.isRedstoneTransmissionEnabled());
        if (blockDisguisedAs != null)
        {
            compound.setInteger("disguisedId", Block.getIdFromBlock(blockDisguisedAs));
            compound.setInteger("disguisedMeta", blockDisguisedMetadata);
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        setConnectedSide(pkt.func_148857_g().getByte("side"));
        setRedstoneTransmissionActive(pkt.func_148857_g().getBoolean("redstone"));
        setRedstoneTransmissionEnabled(pkt.func_148857_g().getBoolean("redstoneEnabled"));
        int disguiseBlockId = pkt.func_148857_g().getInteger("disguisedId");
        if (disguiseBlockId != 0)
        {
            int disguisedMeta = pkt.func_148857_g().getInteger("disguisedMeta");
            setDisguise(Block.getBlockById(disguiseBlockId), disguisedMeta);
        }
    }



    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setByte("side", (byte) connectedDirection.ordinal());
        compound.setBoolean("redstone", this.isRedstoneTransmissionActive());
        compound.setBoolean("redstoneEnabled", this.isRedstoneTransmissionEnabled());
        if (blockDisguisedAs != null)
        {
            compound.setInteger("disguisedId", Block.getIdFromBlock(blockDisguisedAs));
            compound.setInteger("disguisedMeta", blockDisguisedMetadata);
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, compound);
    }

    public boolean rotateBlock()
    {
        setConnectedSide((getConnectedDirection().ordinal() + 1) % ForgeDirection.VALID_DIRECTIONS.length);
        return true;
    }

    public boolean isRedstoneTransmissionEnabled()
    {
        return isRedstoneEnabled;
    }

    public void setRedstoneTransmissionEnabled(boolean state)
    {
        boolean wasRedstoneEnabled = isRedstoneTransmissionEnabled();
        isRedstoneEnabled = state;

        if (worldObj != null && isRedstoneEnabled != wasRedstoneEnabled)
        {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            this.checkRedstonePower();
        }
    }

    public boolean isRedstoneTransmissionActive()
    {
        return isRedstonePowered;
    }

    public void setRedstoneTransmissionActive(boolean state)
    {
        isRedstonePowered = state;
    }
}
