package com.dynious.refinedrelocation.part;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JCuboidPart;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.RelocatorData;
import com.dynious.refinedrelocation.renderer.RendererRelocator;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class PartRelocator extends JCuboidPart implements IRelocator, ISidedInventory
{
    private TileRelocator relocator;

    public PartRelocator(TileRelocator tile)
    {
        relocator = tile;
    }

    public PartRelocator()
    {
        relocator = new TileRelocator();
    }

    @Override
    public void onWorldJoin()
    {
        super.onWorldJoin();
        initRelocator();
    }

    @Override
    public Cuboid6 getBounds()
    {
        return RelocatorData.middleCuboid;
    }

    public Iterable<IndexedCuboid6> getSubParts()
    {
        List<IndexedCuboid6> list = new ArrayList<IndexedCuboid6>();
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
        {
            if (relocator.connectsToSide(i))
            {
                list.add(new IndexedCuboid6(i, RelocatorData.sideCuboids[i]));
            }
        }
        list.add(new IndexedCuboid6(6, RelocatorData.middleCuboid));
        return list;
    }

    @Override
    public String getType()
    {
        return Names.relocator;
    }

    @Override
    public void onNeighborChanged()
    {
        super.onNeighborChanged();
        relocator.blocksChanged = true;
    }

    @Override
    public Iterable<ItemStack> getDrops()
    {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (TravellingItem travellingItem : relocator.getItems(true))
        {
            items.add(travellingItem.getItemStack());
        }
        return items;
    }

    @Override
    public void update()
    {
        super.update();
        if (relocator.blocksChanged)
        {
            relocator.updateEntity();
            if (!world().isRemote)
                sendDescUpdate();
        }
        else
        {
            relocator.updateEntity();
        }
    }

    public void initRelocator()
    {
        if (relocator.worldObj == null)
        {
            relocator.setWorldObj(world());
            relocator.xCoord = x();
            relocator.yCoord = y();
            relocator.zCoord = z();
            relocator.validate();
        }
    }

    @Override
    public void renderDynamic(Vector3 pos, float frame, int pass)
    {
        RendererRelocator.instance.renderTileEntityAt(relocator, pos.x, pos.y, pos.z, frame);
    }



    /*
    NBT Handling
     */

    @Override
    public void save(NBTTagCompound tag)
    {
        super.save(tag);
        NBTTagCompound compound = new NBTTagCompound();
        relocator.writeToNBT(compound);
        tag.setCompoundTag("relocator", compound);
    }

    @Override
    public void load(NBTTagCompound tag)
    {
        super.load(tag);
        relocator.readFromNBT(tag.getCompoundTag("relocator"));
    }

    @Override
    public void writeDesc(MCDataOutput packet)
    {
        System.out.println("write");
        packet.writeNBTTagCompound(((Packet132TileEntityData) relocator.getDescriptionPacket()).data);
    }

    @Override
    public void readDesc(MCDataInput packet)
    {
        System.out.println("read");
        relocator.onDataPacket(null, new Packet132TileEntityData(0, 0, 0, 0, packet.readNBTTagCompound()));
    }

    /*
    Relocator implementation
     */

    @Override
    public TileEntity[] getConnectedInventories()
    {
        return relocator.getConnectedInventories();
    }

    @Override
    public IRelocator[] getConnectedRelocators()
    {
        return relocator.getConnectedRelocators();
    }

    @Override
    public boolean passesFilter(ItemStack itemStack, int side)
    {
        return relocator.passesFilter(itemStack, side);
    }

    @Override
    public ItemStack insert(ItemStack itemStack, int side, boolean simulate)
    {
        return relocator.insert(itemStack, side, simulate);
    }

    @Override
    public void receiveTravellingItem(TravellingItem item)
    {
        relocator.receiveTravellingItem(item);
    }

    @Override
    public List<TravellingItem> getItems(boolean includeItemsToAdd)
    {
        return relocator.getItems(includeItemsToAdd);
    }

    /*
    ISidedInventory implementation
     */

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return relocator.getAccessibleSlotsFromSide(var1);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j)
    {
        return relocator.canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j)
    {
        return relocator.canExtractItem(i, itemstack, j);
    }

    @Override
    public int getSizeInventory()
    {
        return relocator.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return relocator.getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        return relocator.decrStackSize(i, j);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return relocator.getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        relocator.setInventorySlotContents(i, itemstack);
    }

    @Override
    public String getInvName()
    {
        return relocator.getInvName();
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return relocator.isInvNameLocalized();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return relocator.getInventoryStackLimit();
    }

    @Override
    public void onInventoryChanged()
    {
        relocator.onInventoryChanged();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return relocator.isUseableByPlayer(entityplayer);
    }

    @Override
    public void openChest()
    {
        relocator.openChest();
    }

    @Override
    public void closeChest()
    {
        relocator.closeChest();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return relocator.isItemValidForSlot(i, itemstack);
    }
}
