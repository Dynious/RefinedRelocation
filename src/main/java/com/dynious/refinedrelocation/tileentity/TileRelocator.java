package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.filter.IRelocatorModule;
import com.dynious.refinedrelocation.api.item.IItemRelocatorModule;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.grid.relocator.RelocatorGridLogic;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.helper.*;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.network.packet.MessageItemList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.util.MovingObjectPosition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileRelocator extends TileEntity implements IRelocator, ISidedInventory
{
    public boolean blocksChanged = true;

    private TileEntity[] inventories = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
    private IRelocator[] relocators = new IRelocator[ForgeDirection.VALID_DIRECTIONS.length];
    private IRelocatorModule[] modules = new IRelocatorModule[ForgeDirection.VALID_DIRECTIONS.length];
    private List<ItemStack>[] stuffedItems;

    private boolean[] isConnected = new boolean[6];
    private byte renderType = 0;

    private TravellingItem cachedTravellingItem;
    private int maxStackSize = 64;

    private List<TravellingItem> items = new ArrayList<TravellingItem>();
    private List<TravellingItem> itemsToAdd = new ArrayList<TravellingItem>();

    private byte ticker = 0;

    @SuppressWarnings("unchecked")
    public TileRelocator()
    {
        stuffedItems = (List<ItemStack>[]) new ArrayList[ForgeDirection.VALID_DIRECTIONS.length];
        for (int i = 0; i < stuffedItems.length; i++)
        {
            stuffedItems[i] = new ArrayList<ItemStack>();
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        for (int i = 0; i < modules.length; i++)
        {
            if (modules[i] != null)
            {
                modules[i].onUpdate(this, i);
            }
        }

        if (worldObj.isRemote)
        {
            clientSideUpdate();
        }
        else
        {
            serverSideUpdate();
        }
    }

    private void serverSideUpdate()
    {
        if (blocksChanged)
        {
            inventories = new TileEntity[ForgeDirection.VALID_DIRECTIONS.length];
            relocators = new IRelocator[ForgeDirection.VALID_DIRECTIONS.length];

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            {
                if (!((IRelocator)worldObj.getTileEntity(xCoord, yCoord, zCoord)).canConnectOnSide(direction.ordinal()))
                    continue;

                TileEntity tile = DirectionHelper.getTileAtSide(this, direction);
                if (tile != null)
                {
                    if (tile instanceof IRelocator)
                    {
                        if (((IRelocator)tile).canConnectOnSide(direction.getOpposite().ordinal()))
                        {
                            relocators[direction.ordinal()] = (IRelocator) tile;
                        }
                    }
                    else if (IOHelper.canInterfaceWith(tile, direction.getOpposite()))
                    {
                        inventories[direction.ordinal()] = tile;
                    }
                }

                if (relocators[direction.ordinal()] == null && inventories[direction.ordinal()] == null)
                {
                    emptySide(direction.ordinal());
                }
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            blocksChanged = false;
        }

        if (!itemsToAdd.isEmpty())
        {
            items.addAll(itemsToAdd);
            //TODO: More efficient client syncing
            NetworkHandler.INSTANCE.sendToAllAround(new MessageItemList(this, itemsToAdd), new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 64));
            //NetworkHelper.sendToAllAround(new PacketItemList(this, itemsToAdd), new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 64));
            itemsToAdd.clear();
        }

        for (Iterator<TravellingItem> iterator = items.iterator(); iterator.hasNext(); )
        {
            TravellingItem item = iterator.next();
            item.counter++;
            if (item.counter > TravellingItem.timePerRelocator)
            {
                iterator.remove();
                outputToSide(item, item.onOutput());
            }
            else if (item.counter == TravellingItem.timePerRelocator / 2)
            {
                if (!connectsToSide(item.getOutputSide()))
                {
                    iterator.remove();
                    retryOutput(item, -1);
                }
            }
        }

        ticker++;
        if (ticker >= Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION)
        {
            ticker = 0;
            for (byte side = 0; side < stuffedItems.length; side++)
            {
                ArrayList<ItemStack> stacksUnableToAdd = new ArrayList<ItemStack>();
                for (Iterator<ItemStack> iterator = stuffedItems[side].iterator(); iterator.hasNext();)
                {
                    ItemStack stack = iterator.next();
                    if (!stacksUnableToAdd.contains(stack))
                    {
                        stacksUnableToAdd.add(stack.copy());
                        stack = IOHelper.insert(getConnectedInventories()[side], stack.copy(), ForgeDirection.getOrientation(side).getOpposite(), false);
                        if (stack == null)
                        {
                            stacksUnableToAdd.remove(stacksUnableToAdd.size() - 1);
                            iterator.remove();
                        }
                    }
                }
            }
        }

        cachedTravellingItem = null;
        maxStackSize = 64;
    }

    private void clientSideUpdate()
    {
        for (Iterator<TravellingItem> iterator = items.iterator(); iterator.hasNext(); )
        {
            TravellingItem item = iterator.next();
            item.counter++;
            if (item.counter > TravellingItem.timePerRelocator)
            {
                iterator.remove();
            }
            else if (item.counter == TravellingItem.timePerRelocator / 2)
            {
                if (!connectsToSide(item.getOutputSide()))
                {
                    iterator.remove();
                }
            }
        }
    }

    public boolean onActivated(EntityPlayer player, MovingObjectPosition hit, ItemStack stack)
    {
        if (hit.subHit < 6)
        {
            return sideHit(player, hit.subHit, stack);
        }
        else
        {
            //Middle was hit
        }
        return false;
    }

    public boolean sideHit(EntityPlayer player, int side, ItemStack stack)
    {
        if (stack != null && stack.getItem() instanceof IItemRelocatorModule && modules[side] == null)
        {
            IRelocatorModule filter = ((IItemRelocatorModule) stack.getItem()).getRelocatorModule(stack);
            if (filter != null)
            {
                modules[side] = filter;
                stack.stackSize--;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return true;
            }
        }
        else if (modules[side] != null)
        {
            if (player.isSneaking())
            {
                List<ItemStack> list = modules[side].getDrops(this, side);
                if (list != null)
                {
                    for (ItemStack stack1 : list)
                    {
                        IOHelper.spawnItemInWorld(worldObj, stack1, xCoord, yCoord, zCoord);
                    }
                }
                modules[side] = null;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return true;
            }
            else
            {
                return modules[side].onActivated(this, player, side, stack);
            }
        }
        return false;
    }

    public void emptySide(int side)
    {
        if (modules[side] != null)
        {
            for (ItemStack stack : modules[side].getDrops(this, side))
            {
                IOHelper.spawnItemInWorld(worldObj, stack, xCoord, yCoord, zCoord);
            }
            modules[side] = null;
        }
        if (stuffedItems[side] != null)
        {
            for (ItemStack stack : stuffedItems[side])
            {
                IOHelper.spawnItemInWorld(worldObj, stack, xCoord, yCoord, zCoord);
            }
            stuffedItems[side].clear();
        }
    }

    public List<ItemStack> getDrops()
    {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (TravellingItem travellingItem : getItems(true))
        {
            items.add(travellingItem.getItemStack());
        }
        for (int i = 0; i < modules.length; i++)
        {
            IRelocatorModule module = modules[i];
            if (module != null)
            {
                items.addAll(module.getDrops(this, i));
            }
        }
        for (List<ItemStack> stuffedItem : stuffedItems)
        {
            items.addAll(stuffedItem);
        }
        return items;
    }

    @Override
    public GuiScreen getGUI(int side)
    {
        return modules[side].getGUI(this);
    }

    @Override
    public Container getContainer(int side)
    {
        return modules[side].getContainer(this);
    }

    @Override
    public boolean canConnectOnSide(int side)
    {
        return true;
    }

    @Override
    public IRelocator[] getConnectedRelocators()
    {
        return relocators;
    }

    @Override
    public boolean passesFilter(ItemStack itemStack, int side, boolean input)
    {
        return stuffedItems[side].isEmpty() && (modules[side] == null || modules[side].passesFilter(itemStack, input));
    }

    @Override
    public TileEntity[] getConnectedInventories()
    {
        return inventories;
    }

    public List<TravellingItem> getItems(boolean includeItemsToAdd)
    {
        if (includeItemsToAdd)
        {
            List<TravellingItem> list = new ArrayList<TravellingItem>(items);
            list.addAll(itemsToAdd);
            return list;
        }
        return items;
    }

    @Override
    public TileEntity getTileEntity()
    {
        return this;
    }

    @Override
    public void receiveTravellingItem(TravellingItem item)
    {
        if (worldObj.isRemote)
        {
            if (!item.getPath().isEmpty())
                items.add(item);
        }
        else
        {
            if (item.getPath().isEmpty())
            {
                retryOutput(item, item.input);
            }
            else
            {
                itemsToAdd.add(item);
            }
        }
    }

    public void outputToSide(TravellingItem item, byte side)
    {
        if (getConnectedRelocators()[side] != null)
        {
            getConnectedRelocators()[side].receiveTravellingItem(item);
        }
        else if (getConnectedInventories()[side] != null)
        {
            ForgeDirection output = ForgeDirection.getOrientation(side).getOpposite();
            if (getRelocatorModule(side) != null)
            {
                output = ForgeDirection.getOrientation(getRelocatorModule(side).getOutputSide(this, side));
            }
            ItemStack stack = IOHelper.insert(getConnectedInventories()[side], item.getItemStack().copy(), output, false);
            if (stack != null)
            {
                item.getItemStack().stackSize = stack.stackSize;
                retryOutput(item, side);
            }
        }
        else
        {
            retryOutput(item, side);
        }
    }

    public void retryOutput(TravellingItem item, int side)
    {
        ItemStack stack = item.getItemStack();
        TravellingItem travellingItem = RelocatorGridLogic.findOutput(item.getItemStack(), this, side);
        if (travellingItem != null)
        {
            travellingItem.setStartingPoint(item.getStartingPoint());
            receiveTravellingItem(travellingItem);
            stack.stackSize -= item.getStackSize();
            if (stack.stackSize <= 0)
            {
                stack = null;
            }
        }
        if (stack != null)
        {
            if (side != -1)
            {
                for (ItemStack stack1 : stuffedItems[side])
                {
                    if (stack1.stackSize != 64 && ItemStackHelper.areItemStacksEqual(stack1, stack))
                    {
                        int amount = Math.min(stack.stackSize, 64 - stack1.stackSize);
                        stack1.stackSize += amount;
                        stack.stackSize -= amount;
                        if (stack.stackSize == 0)
                        {
                            stack = null;
                            break;
                        }
                    }
                }
                if (stack != null)
                    stuffedItems[side].add(stack);
            }
            else
            {
                IOHelper.spawnItemInWorld(worldObj, stack, xCoord, yCoord, zCoord);
            }
        }
    }

    @Override
    public ItemStack insert(ItemStack itemStack, int side, boolean simulate)
    {
        if (passesFilter(itemStack, side, true))
        {
            TravellingItem item = RelocatorGridLogic.findOutput(itemStack.copy(), this, side);
            if (item != null)
            {
                if (!simulate)
                {
                    receiveTravellingItem(item);
                }
                itemStack.stackSize -= item.getStackSize();
                if (itemStack.stackSize <= 0)
                {
                    return null;
                }
            }
        }
        return itemStack;
    }

    @Override
    public boolean connectsToSide(int side)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            return relocators[side] != null || inventories[side] != null;
        else
            return isConnected[side];
    }

    @Override
    public IRelocatorModule getRelocatorModule(int side)
    {
        return modules[side];
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("Items"))
        {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
                items.add(TravellingItem.createFromNBT(nbttagcompound1));
            }
        }
        if (compound.hasKey("ItemsToAdd"))
        {
            NBTTagList nbttaglist = compound.getTagList("ItemsToAdd", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
                itemsToAdd.add(TravellingItem.createFromNBT(nbttagcompound1));
            }
        }

        readFilters(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (!items.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();
            for (TravellingItem item : this.items)
            {
                if (item != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    item.writeToNBT(nbttagcompound1);
                    nbttaglist.appendTag(nbttagcompound1);
                }
            }
            compound.setTag("Items", nbttaglist);
        }

        if (!itemsToAdd.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();
            for (TravellingItem item : this.itemsToAdd)
            {
                if (item != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    item.writeToNBT(nbttagcompound1);
                    nbttaglist.appendTag(nbttagcompound1);
                }
            }
            compound.setTag("ItemsToAdd", nbttaglist);
        }

        saveFilters(compound);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
        {
            if (connectsToSide(i))
                tag.setBoolean("c" + i, true);
        }
        saveFilters(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.func_148857_g();
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
        {
            isConnected[i] = tag.hasKey("c" + i);
        }
        calculateRenderType();
        modules = new IRelocatorModule[ForgeDirection.VALID_DIRECTIONS.length];
        readFilters(pkt.func_148857_g());
    }

    public void saveFilters(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < modules.length; i++)
        {
            if (modules[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setString("clazzIdentifier", RelocatorModuleRegistry.getIdentifier(modules[i].getClass()));
                nbttagcompound1.setByte("place", (byte) i);
                modules[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        compound.setTag("modules", nbttaglist);
    }

    public void readFilters(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = compound.getTagList("modules", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
            byte place = nbttagcompound1.getByte("place");
            IRelocatorModule filter = RelocatorModuleRegistry.getFilter(nbttagcompound1.getString("clazzIdentifier"));
            if (filter != null)
            {
                modules[place] = filter;
                modules[place].readFromNBT(nbttagcompound1);
            }
        }
    }

    public void calculateRenderType()
    {
        if (isConnected[0] && isConnected[1] && !isConnected[2] && !isConnected[3] && !isConnected[4] && !isConnected[5])
            renderType = 1;
        else if (!isConnected[0] && !isConnected[1] && isConnected[2] && isConnected[3] && !isConnected[4] && !isConnected[5])
            renderType = 2;
        else if (!isConnected[0] && !isConnected[1] && !isConnected[2] && !isConnected[3] && isConnected[4] && isConnected[5])
            renderType = 3;
        else
            renderType = 0;
    }

    public byte getRenderType()
    {
        return renderType;
    }

    /*
    ISidedInventory implementation
     */

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { side };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side)
    {
        if (connectsToSide(side) && passesFilter(itemStack, side, true))
        {
            cachedTravellingItem = RelocatorGridLogic.findOutput(itemStack, this, side);
            if (cachedTravellingItem != null)
            {
                maxStackSize = cachedTravellingItem.getStackSize();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return 6;
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
    public void setInventorySlotContents(int side, ItemStack itemstack)
    {
        if (side == cachedTravellingItem.input && cachedTravellingItem.isItemSameAs(itemstack))
        {
            receiveTravellingItem(cachedTravellingItem);
        }
        else
        {
            TravellingItem travellingItem = RelocatorGridLogic.findOutput(itemstack, this, side);
            if (travellingItem != null)
            {
                itemstack.stackSize -= travellingItem.getStackSize();
                receiveTravellingItem(travellingItem);
            }
            if (itemstack != null && itemstack.stackSize > 0)
            {
                TileEntity tile = DirectionHelper.getTileAtSide(this, ForgeDirection.getOrientation(side).getOpposite());
                LogHelper.warning(String.format("%s at %s:%s:%s inserted ItemStack wrongly into Relocator!!", BlockHelper.getTileEntityDisplayName(tile), tile.xCoord, tile.yCoord, tile.zCoord));
                IOHelper.spawnItemInWorld(worldObj, itemstack, xCoord, yCoord, zCoord);
            }
        }
    }

    @Override
    public String getInventoryName()
    {
        return "tile.relocator.name";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return maxStackSize;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return true;
    }
}
