package com.dynious.refinedrelocation.part;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.*;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.RelocatorData;
import com.dynious.refinedrelocation.mods.FMPHelper;
import com.dynious.refinedrelocation.renderer.RendererRelocator;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class PartRelocator extends JCuboidPart implements IRelocator, ISidedInventory, JNormalOcclusion, TSlottedPart, IRedstonePart
{
    private TileRelocator relocator;

    public PartRelocator(TileRelocator tile)
    {
        relocator = tile;
    }

    public PartRelocator()
    {
        this(new TileRelocator());
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
        return "tile." + Names.relocator;
    }

    @Override
    public void onNeighborChanged()
    {
        super.onNeighborChanged();
        relocator.onBlocksChanged();
    }

    @Override
    public void onPartChanged(TMultiPart part)
    {
        relocator.onBlocksChanged();
    }

    @Override
    public Iterable<ItemStack> getDrops()
    {
        List<ItemStack> items = relocator.getDrops();
        items.add(new ItemStack(FMPHelper.partRelocator));
        return items;
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit)
    {
        return new ItemStack(FMPHelper.partRelocator);
    }

    @Override
    public void update()
    {
        super.update();
        relocator.updateEntity();
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
        if (pass == 0)
            RendererRelocator.instance.renderTileEntityAt(relocator, pos.x, pos.y, pos.z, frame);
    }

    @Override
    public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack item)
    {
        return relocator.onActivated(player, hit, item);
    }

    @Override
    public void click(EntityPlayer player, MovingObjectPosition hit, ItemStack item)
    {
        if (hit != null)
        {
            relocator.leftClick(player, hit, player.getHeldItem());
        }
        super.click(player, hit, item);
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes()
    {
        List<Cuboid6> list = new ArrayList<Cuboid6>();
        list.add(RelocatorData.middleCuboid);
        return list;
    }

    @Override
    public boolean occlusionTest(TMultiPart part)
    {
        return NormalOcclusionTest.apply(this, part);
    }

    @Override
    public int getSlotMask()
    {
        return PartMap.CENTER.mask;
    }

    @Override
    public int strongPowerLevel(int i)
    {
        return 0;
    }

    @Override
    public int weakPowerLevel(int i)
    {
        return 0;
    }

    @Override
    public boolean canConnectRedstone(int i)
    {
        return relocator.shouldConnectToRedstone();
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
        packet.writeNBTTagCompound(((Packet132TileEntityData) relocator.getDescriptionPacket()).data);
    }

    @Override
    public void readDesc(MCDataInput packet)
    {
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
    public boolean canConnectOnSide(int side)
    {
        return tile().canAddPart(new NormallyOccludedPart(RelocatorData.sideCuboids[side]));
    }

    @Override
    public boolean connectsToSide(int side)
    {
        return relocator.connectsToSide(side);
    }

    @Override
    public boolean isStuffedOnSide(int side)
    {
        return relocator.isStuffedOnSide(side);
    }

    @Override
    public IRelocatorModule getRelocatorModule(int side)
    {
        return relocator.getRelocatorModule(side);
    }

    @Override
    public boolean getRedstoneState()
    {
        return relocator.getRedstoneState();
    }

    @Override
    public boolean passesFilter(ItemStack itemStack, int side, boolean input, boolean simulate)
    {
        return relocator.passesFilter(itemStack, side, input, simulate);
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

    @Override
    public TileEntity getTileEntity()
    {
        return relocator;
    }

    @Override
    public GuiScreen getGUI(int side, EntityPlayer player)
    {
        return relocator.getGUI(side, player);
    }

    @Override
    public Container getContainer(int side, EntityPlayer player)
    {
        return relocator.getContainer(side, player);
    }

    @Override
    public byte getRenderType()
    {
        return relocator.getRenderType();
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
