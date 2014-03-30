package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileRelocationController;
import com.dynious.refinedrelocation.tileentity.TileRelocationPortal;
import com.dynious.refinedrelocation.until.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class ItemPlayerRelocator extends Item
{
    public ItemPlayerRelocator(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.playerRelocator);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
            return false;

        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationController)
        {
            if (((TileRelocationController)tile).isFormed(false))
            {
                if (!stack.hasTagCompound())
                {
                    stack.setTagCompound(new NBTTagCompound());
                }
                stack.getTagCompound().setInteger("x", x);
                stack.getTagCompound().setInteger("y", y);
                stack.getTagCompound().setInteger("z", z);
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("x"))
        {
            TileEntity connectedTile = world.getBlockTileEntity(stack.getTagCompound().getInteger("x"), stack.getTagCompound().getInteger("y"), stack.getTagCompound().getInteger("z"));
            if (connectedTile != null && connectedTile instanceof TileRelocationController && ((TileRelocationController)connectedTile).isFormed(true))
            {
                int xPos = MathHelper.floor_double(player.posX);
                int yPos = MathHelper.floor_double(player.posY);
                int zPos = MathHelper.floor_double(player.posZ);

                if (checkBlocks(world, xPos, yPos, zPos))
                {
                    player.setPositionAndUpdate(xPos + 0.5D, yPos, zPos + 0.5D);

                    setBlockToPortal(world, xPos, yPos - 1, zPos, null);
                    setBlockToPortal(world, xPos, yPos - 2, zPos, null);
                    setBlockToPortal(world, xPos, yPos - 3, zPos, new Vector3(connectedTile.xCoord, connectedTile.yCoord, connectedTile.zCoord));
                }
            }
        }
        return super.onItemRightClick(stack, world, player);
    }

    private boolean checkBlocks(World world, int posX, int posY, int posZ)
    {
        int blockID = world.getBlockId(posX, posY - 1, posZ);
        int blockID2 = world.getBlockId(posX, posY - 2, posZ);
        int blockID3 = world.getBlockId(posX, posY - 3, posZ);

        return (world.getBlockTileEntity(posX, posY - 1, posZ) == null) && (blockID != Block.bedrock.blockID)
                && (blockID != ModBlocks.relocationPortal.blockID) && (Block.blocksList[blockID] != null) && (!Block.blocksList[blockID].canPlaceBlockAt(world, posX, posY - 1, posZ))
                && (Block.blocksList[blockID].getBlockHardness(world, posX, posY - 1, posZ) != -1.0F)
                && (world.getBlockTileEntity(posX, posY - 2, posZ) == null) && (blockID2 != Block.bedrock.blockID)
                && (blockID2 != ModBlocks.relocationPortal.blockID)
                && (world.getBlockTileEntity(posX, posY - 3, posZ) == null) && (blockID3 != Block.bedrock.blockID)
                && (blockID3 != ModBlocks.relocationPortal.blockID);
    }

    private void setBlockToPortal(World world, int x, int y, int z, Vector3 linkedPos)
    {
        int blockID = world.getBlockId(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, ModBlocks.relocationPortal.blockID);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationPortal)
        {
            ((TileRelocationPortal)tile).init(blockID, blockMeta, linkedPos);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
    {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("x"))
        {
            list.add("Linked to Relocation Controller at: " + itemStack.getTagCompound().getInteger("x") + ":" + itemStack.getTagCompound().getInteger("y") + ":" +  itemStack.getTagCompound().getInteger("z"));
        }
    }
}
