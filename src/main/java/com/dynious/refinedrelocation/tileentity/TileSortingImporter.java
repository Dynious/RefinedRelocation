package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TileSortingImporter extends TileSortingConnector implements IInventory
{
    public ItemStack[] bufferInventory = new ItemStack[1];
    protected List<EntityPlayer> crafters = new ArrayList<EntityPlayer>();
    private List<ItemStack> itemList = new ArrayList<ItemStack>();
    private List<Integer> idList = new ArrayList<Integer>();
    private long lastClickTime;
    private ItemStack lastAddedStack;

    @Override
    public boolean onActivated(EntityPlayer player, int side)
    {
        if (worldObj.isRemote)
            return true;

        if (player.isSneaking() || player.getHeldItem() == null)
        {
            GuiHelper.openGui(player, this);
            return true;
        }
        else if (bufferInventory[0] == null)
        {
            //lastAddedStack = player.getHeldItem();
            setInventorySlotContents(0, player.getHeldItem());
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

            /*
            if (worldObj.getWorldTime() - lastClickTime < 10L)
            {
                for (int invSlot = 0; invSlot < player.inventory.getSizeInventory(); ++invSlot)
                {
                    ItemStack stack = player.inventory.getStackInSlot(invSlot);
                    if (ItemStackHelper.areItemStacksEqual(lastAddedStack, stack))
                    {
                        setInventorySlotContents(0, player.getHeldItem());
                        player.inventory.setInventorySlotContents(invSlot, null);
                    }
                    if (bufferInventory[0] != null)
                    {
                        break;
                    }
                }
            }
            lastClickTime = worldObj.getWorldTime();
            */
            return true;
        }
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
        if (i == 0)
        {
            return bufferInventory[0];
        }
        else
        {
            if (i - 1 < itemList.size())
            {
                return itemList.get(i - 1);
            }
            else
            {
                return null;
            }
        }
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.bufferInventory[par1] != null)
        {
            ItemStack itemstack;

            if (this.bufferInventory[par1].stackSize <= par2)
            {
                itemstack = this.bufferInventory[par1];
                this.bufferInventory[par1] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.bufferInventory[par1].splitStack(par2);

                if (this.bufferInventory[par1].stackSize == 0)
                {
                    this.bufferInventory[par1] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.bufferInventory[par1] != null)
        {
            ItemStack itemstack = this.bufferInventory[par1];
            this.bufferInventory[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (i == 0)
        {
            if (worldObj.isRemote)
            {
                return;
            }
            int[] ids = OreDictionary.getOreIDs(itemstack);
            for (int id : ids)
            {
                if (idList.contains(id))
                {
                    ItemStack stack = itemList.get(idList.indexOf(id)).copy();
                    stack.stackSize = itemstack.stackSize;
                    itemstack = stack;
                }
            }

            if (getHandler() != null && getHandler().getGrid() != null)
                bufferInventory[0] = getHandler().getGrid().filterStackToGroup(itemstack, this, i, false);
            else
                bufferInventory[0] = itemstack;

            if (bufferInventory[0] != null)
            {
                syncInventory();
            }
        }
        else
        {
            int index = i - 1;

            if (itemstack == null && index < itemList.size())
            {
                itemList.remove(index);
                idList.remove(index);
            }
            else if (itemstack != null)
            {
                int[] ids = OreDictionary.getOreIDs(itemstack);
                for (int id : ids)
                {
                    if (i - 1 < itemList.size() && (!idList.contains(id) || id == idList.get(index)))
                    {
                        itemList.set(index, itemstack);
                        idList.set(index, id);
                    }
                    else if (index == itemList.size() && !idList.contains(id))
                    {
                        itemList.add(itemstack);
                        idList.add(id);
                    }
                }
            }
        }
    }

    @Override
    public String getInventoryName()
    {
        return Names.sortingImporter;
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
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    public final void addCrafter(EntityPlayer player)
    {
        crafters.add(player);
    }

    public final void removeCrafter(EntityPlayer player)
    {
        crafters.remove(player);
    }

    public void syncInventory()
    {
        for (EntityPlayer crafter : crafters)
        {
            ((ICrafting) crafter).sendSlotContents(crafter.openContainer, 0, bufferInventory[0]);
        }
    }

    public int getItemListSize()
    {
        return itemList.size();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return bufferInventory[0] == null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound tag = nbttaglist.getCompoundTagAt(i);
            itemList.add(ItemStack.loadItemStackFromNBT(tag));
            idList.add(tag.getInteger("oreId"));
        }

        NBTTagList list = compound.getTagList("buffer", 10);
        if (list.tagCount() > 0)
        {
            NBTTagCompound tag = list.getCompoundTagAt(0);
            bufferInventory[0] = ItemStack.loadItemStackFromNBT(tag);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.itemList.size(); ++i)
        {
            NBTTagCompound tag = new NBTTagCompound();
            this.itemList.get(i).writeToNBT(tag);
            tag.setInteger("oreId", idList.get(i));
            nbttaglist.appendTag(tag);
        }
        compound.setTag("Items", nbttaglist);

        NBTTagList list = new NBTTagList();
        NBTTagCompound tag = new NBTTagCompound();
        if (bufferInventory[0] != null)
        {
            bufferInventory[0].writeToNBT(tag);
            list.appendTag(tag);
        }
        compound.setTag("buffer", list);
    }
}
