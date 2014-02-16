package com.dynious.blex.tileentity;

import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.Comparator;

public class TileIronFilteringChest extends TileFilteringChest
{
    private IronChestType type = IronChestType.IRON;
    private ItemStack[] topStacks;
    private boolean hadStuff;
    private boolean inventoryTouched;

    public TileIronFilteringChest()
    {
        this.topStacks = new ItemStack[8];
    }

    @Override
    public void onTileAdded()
    {
        type = IronChestType.values()[worldObj.getBlockMetadata(xCoord, yCoord, zCoord) - 1];
        inventory = new ItemStack[getSizeInventory()];
        super.onTileAdded();
    }

    public IronChestType getType()
    {
        return type;
    }

    public boolean applyUpgradeItem(ItemChestChanger itemChestChanger)
    {
        if (numUsingPlayers > 0)
        {
            return false;
        }
        if (!itemChestChanger.getType().canUpgrade(this.getType()))
        {
            return false;
        }
        type = IronChestType.values()[itemChestChanger.getTargetChestOrdinal(getType().ordinal())];
        worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, type.ordinal() + 1, 3);
        ItemStack[] tempStacks = inventory;
        inventory = new ItemStack[getSizeInventory()];
        System.arraycopy(tempStacks, 0, inventory, 0, Math.min(tempStacks.length, inventory.length));
        return true;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        if (type == null)
        {
            return IronChestType.IRON.getRowCount() * IronChestType.IRON.getRowLength();
        }
        return type.getRowCount() * type.getRowLength();
    }

    protected void sortTopStacks()
    {
        if (!type.isTransparent() || (worldObj != null && worldObj.isRemote))
        {
            return;
        }
        ItemStack[] tempCopy = new ItemStack[getSizeInventory()];
        boolean hasStuff = false;
        int compressedIdx = 0;
        mainLoop: for (int i = 0; i < getSizeInventory(); i++)
        {
            if (inventory[i] != null)
            {
                for (int j = 0; j < compressedIdx; j++)
                {
                    if (tempCopy[j].isItemEqual(inventory[i]))
                    {
                        tempCopy[j].stackSize += inventory[i].stackSize;
                        continue mainLoop;
                    }
                }
                tempCopy[compressedIdx++] = inventory[i].copy();
                hasStuff = true;
            }
        }
        if (!hasStuff && hadStuff)
        {
            hadStuff = false;
            for (int i = 0; i < topStacks.length; i++)
            {
                topStacks[i] = null;
            }
            if (worldObj != null)
            {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            return;
        }
        hadStuff = true;
        Arrays.sort(tempCopy, new Comparator<ItemStack>()
        {
            @Override
            public int compare(ItemStack o1, ItemStack o2)
            {
                if (o1 == null)
                {
                    return 1;
                }
                else if (o2 == null)
                {
                    return -1;
                }
                else
                {
                    return o2.stackSize - o1.stackSize;
                }
            }
        });
        int p = 0;
        for (ItemStack aTempCopy : tempCopy)
        {
            if (aTempCopy != null && aTempCopy.stackSize > 0)
            {
                topStacks[p++] = aTempCopy;
                if (p == topStacks.length)
                {
                    break;
                }
            }
        }
        for (int i = p; i < topStacks.length; i++)
        {
            topStacks[i] = null;
        }
        if (worldObj != null)
        {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!worldObj.isRemote && inventoryTouched)
        {
            inventoryTouched = false;
            sortTopStacks();
        }
    }

    public ItemStack[] getTopItemStacks()
    {
        return topStacks;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        inventoryTouched = true;
        return super.getStackInSlot(par1);
    }

    @Override
    public void onInventoryChanged()
    {
        super.onInventoryChanged();

        sortTopStacks();
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        sortTopStacks();
    }
}
