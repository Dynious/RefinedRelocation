package com.dynious.blex.tileentity;

import com.dynious.blex.block.BlockFilteringChest;
import com.dynious.blex.config.Filter;
import com.dynious.blex.helper.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TileFilteringChest extends TileEntity implements IFilteringInventory
{
    private ItemStack[] chestContents = new ItemStack[36];

    /**
     * The current angle of the lid (between 0 and 1)
     */
    public float lidAngle;

    /**
     * The angle of the lid last tick
     */
    public float prevLidAngle;

    /**
     * The number of players currently using this chest
     */
    public int numUsingPlayers;

    /**
     * Server sync counter (once per 20 ticks)
     */
    private int ticksSinceSync;
    private String customName;

    private Filter filter = new Filter();
    private boolean blacklist = true;

    private boolean isFirstRun = true;

    private IFilteringMember leader;
    private ArrayList<IFilteringMember> childs;
    private boolean canJoinGroup = true;

    protected List<EntityPlayer> crafters = new ArrayList<EntityPlayer>();

    public void onTileAdded()
    {
        searchForLeader();
    }

    public void onTileDestroyed()
    {
        canJoinGroup = false;
        getLeader().removeChild(this);
        getLeader().resetChilds();
    }

    public void searchForLeader()
    {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tile = worldObj.getBlockTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (tile != null && tile instanceof IFilteringMember)
            {
                IFilteringMember filteringMember = (IFilteringMember)tile;
                if (filteringMember.canJoinGroup() && filteringMember.getLeader() != this)
                {
                    if (leader == null && childs == null)
                    {
                        setLeader(filteringMember.getLeader());
                    }
                    else if (filteringMember.getLeader() != getLeader())
                    {
                        filteringMember.getLeader().demoteToChild(getLeader());
                    }
                }
            }
        }
    }

    public IFilteringMember getLeader()
    {
        if (leader == null)
        {
            return this;
        }
        else
        {
            return leader;
        }
    }

    public void setLeader(IFilteringMember newLeader)
    {
        if (newLeader == this)
        {
            System.out.println("I set myself :(" + this.xCoord);
        }
        this.leader = newLeader;

        if (leader != null)
        {
            leader.addChild(this);
        }
    }

    public void addChild(IFilteringMember child)
    {
        if (childs == null)
        {
            childs = new ArrayList<IFilteringMember>();
        }
        if (!childs.contains(child))
        {
            childs.add(child);
        }
    }

    public void removeChild(IFilteringMember child)
    {
        if (childs != null)
        {
            childs.remove(child);
        }
    }

    public void resetChilds()
    {
        if (childs != null && !childs.isEmpty())
        {
            ArrayList<IFilteringMember> tempChilds = new ArrayList<IFilteringMember>(childs);
            childs = null;
            for (IFilteringMember child : tempChilds)
            {
                child.setLeader(null);
            }
            for (IFilteringMember child : tempChilds)
            {
                child.searchForLeader();
            }
        }
    }

    public void demoteToChild(IFilteringMember newLeader)
    {
        setLeader(newLeader);
        if (childs != null && !childs.isEmpty())
        {
            for (IFilteringMember child : childs)
            {
                child.setLeader(getLeader());
            }
        }
        childs = null;
    }

    public boolean canJoinGroup()
    {
        return canJoinGroup;
    }

    public ItemStack filterStackToGroup(ItemStack itemStack)
    {
        if (childs != null && !childs.isEmpty())
        {
            for (IFilteringMember filteringMember : childs)
            {
                if (filteringMember instanceof IFilteringInventory)
                {
                    IFilteringInventory filteringInventory = (IFilteringInventory)filteringMember;
                    if (filteringInventory.getBlackList()? !filteringInventory.getFilter().passesFilter(itemStack) : filteringInventory.getFilter().passesFilter(itemStack))
                    {
                        itemStack = filteringInventory.putInInventory(itemStack);
                        if (itemStack == null || itemStack.stackSize == 0)
                        {
                            return null;
                        }
                    }
                }
            }
        }

        if (getBlackList() ? !getFilter().passesFilter(itemStack) : getFilter().passesFilter(itemStack))
        {
            itemStack = putInInventory(itemStack);
            if (itemStack == null || itemStack.stackSize == 0)
            {
                return null;
            }
        }
        return itemStack;
    }

    public ItemStack putInInventory(ItemStack itemStack)
    {
        for (int slot = 0; slot < getSizeInventory() && itemStack != null && itemStack.stackSize > 0; ++slot)
        {
            if (isItemValidForSlot(slot, itemStack))
            {
                ItemStack itemstack1 = getStackInSlot(slot);

                if (itemstack1 == null)
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max >= itemStack.stackSize)
                    {
                        this.chestContents[slot] = itemStack;

                        this.onInventoryChanged();
                        itemStack = null;
                    }
                    else
                    {
                        this.chestContents[slot] =  itemStack.splitStack(max);
                    }
                }
                else if (ItemStackHelper.areItemStacksEqual(itemstack1, itemStack))
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max > itemstack1.stackSize)
                    {
                        int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                        itemStack.stackSize -= l;
                        itemstack1.stackSize += l;
                    }
                }
            }
        }
        return itemStack;
    }

    public void putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        if (slotIndex >= 0 && slotIndex < chestContents.length)
        this.chestContents[slotIndex] = itemStack;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 27;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.chestContents[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.chestContents[par1].stackSize <= par2)
            {
                itemstack = this.chestContents[par1];
                this.chestContents[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[par1].splitStack(par2);

                if (this.chestContents[par1].stackSize == 0)
                {
                    this.chestContents[par1] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack = this.chestContents[par1];
            this.chestContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        if (par2ItemStack == null || getBlackList() ? !getFilter().passesFilter(par2ItemStack) : getFilter().passesFilter(par2ItemStack))
        {
            this.chestContents[par1] = par2ItemStack;
            this.onInventoryChanged();
        }
        else
        {
            ItemStack filteredStack = getLeader().filterStackToGroup(par2ItemStack);
            if (filteredStack != null)
            {
                putInInventory(filteredStack);
            }
        }
        syncInventory();
    }

    public void syncInventory()
    {
        for (EntityPlayer player : crafters)
        {
            player.openContainer.detectAndSendChanges();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return this.isInvNameLocalized() ? this.customName : "container.chest";
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public boolean isInvNameLocalized()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    /**
     * Sets the custom display name to use when opening a GUI for this specific TileEntityChest.
     */
    public void setChestGuiName(String par1Str)
    {
        this.customName = par1Str;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.chestContents = new ItemStack[this.getSizeInventory()];

        if (par1NBTTagCompound.hasKey("CustomName"))
        {
            this.customName = par1NBTTagCompound.getString("CustomName");
        }

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.chestContents.length)
            {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        filter.readFromNBT(par1NBTTagCompound);
        blacklist = par1NBTTagCompound.getBoolean("blacklist");
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.chestContents.length; ++i)
        {
            if (this.chestContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);

        if (this.isInvNameLocalized())
        {
            par1NBTTagCompound.setString("CustomName", this.customName);
        }
        filter.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("blacklist", blacklist);
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
     * ticks and creates a new spawn inside its implementation.
     */
    public void updateEntity()
    {
        super.updateEntity();

        if (isFirstRun)
        {
            searchForLeader();
            isFirstRun = false;
        }

        ++this.ticksSinceSync;
        float f;

        if (!this.worldObj.isRemote && this.numUsingPlayers != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
        {
            this.numUsingPlayers = 0;
            f = 5.0F;
            List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB((double) ((float) this.xCoord - f), (double) ((float) this.yCoord - f), (double) ((float) this.zCoord - f), (double) ((float) (this.xCoord + 1) + f), (double) ((float) (this.yCoord + 1) + f), (double) ((float) (this.zCoord + 1) + f)));

            for (Object aList : list)
            {
                EntityPlayer entityplayer = (EntityPlayer) aList;

                if (entityplayer.openContainer instanceof ContainerChest)
                {
                    IInventory iinventory = ((ContainerChest) entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest) iinventory).isPartOfLargeChest(this))
                    {
                        ++this.numUsingPlayers;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        f = 0.1F;
        double d0;

        if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F)
        {
            double d1 = (double) this.xCoord + 0.5D;
            d0 = (double) this.zCoord + 0.5D;

            this.worldObj.playSoundEffect(d1, (double) this.yCoord + 0.5D, d0, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;

            if (this.numUsingPlayers > 0)
            {
                this.lidAngle += f;
            }
            else
            {
                this.lidAngle -= f;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;

            if (this.lidAngle < f2 && f1 >= f2)
            {
                d0 = (double) this.xCoord + 0.5D;
                double d2 = (double) this.zCoord + 0.5D;

                this.worldObj.playSoundEffect(d0, (double) this.yCoord + 0.5D, d2, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    /**
     * Called when a client event is received with the event number and argument, see World.sendClientEvent
     */
    public boolean receiveClientEvent(int par1, int par2)
    {
        if (par1 == 1)
        {
            this.numUsingPlayers = par2;
            return true;
        }
        else
        {
            return super.receiveClientEvent(par1, par2);
        }
    }

    public void openChest()
    {
        if (this.numUsingPlayers < 0)
        {
            this.numUsingPlayers = 0;
        }

        ++this.numUsingPlayers;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, this.numUsingPlayers);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType().blockID);
    }

    public void closeChest()
    {
        if (this.getBlockType() != null && this.getBlockType() instanceof BlockFilteringChest)
        {
            --this.numUsingPlayers;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, 1, this.numUsingPlayers);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType().blockID);
        }
    }

    public void addCrafter(EntityPlayer player)
    {
        crafters.add(player);
    }

    public void removeCrafter(EntityPlayer player)
    {
        crafters.remove(player);
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    @Override
    public boolean getBlackList()
    {
        return blacklist;
    }

    @Override
    public void setBlackList(boolean value)
    {
        blacklist = value;
    }
}
