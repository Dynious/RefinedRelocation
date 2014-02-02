package com.dynious.blex.item;

import com.dynious.blex.block.ModBlocks;
import com.dynious.blex.tileentity.TileBlockExtender;
import com.dynious.blex.util.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class ItemBlockExtender extends ItemBlock
{
    public ItemBlockExtender(int id)
    {
        super(id);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (isLooping(world, x, y, z, side) || !super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
        {
            return false;
        }
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileBlockExtender)
        {
            ((TileBlockExtender) tile).setConnectedSide(ForgeDirection.OPPOSITES[side]);
        }
        return true;
    }

    private boolean isLooping(World world, int x, int y, int z, int side)
    {
        ForgeDirection direction = ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[side]);
        TileEntity tile = world.getBlockTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
        if (tile != null && tile instanceof TileBlockExtender)
        {
            Vector3 firstConnectedPos = checkTile((TileBlockExtender)tile);
            if (firstConnectedPos != null && firstConnectedPos.x == x && firstConnectedPos.y == y && firstConnectedPos.z == z)
            {
                return true;
            }
        }
        return false;
    }

    private Vector3 checkTile(TileBlockExtender blockExtender)
    {
        Vector3 firstConnectedPos;
        TileEntity tile = blockExtender.getConnectedTile();
        if (tile != null && tile instanceof TileBlockExtender)
        {
            firstConnectedPos = checkTile((TileBlockExtender)tile);
        }
        else
        {
            ForgeDirection direction = blockExtender.getConnectedDirection();
            return new Vector3(blockExtender.xCoord + direction.offsetX, blockExtender.yCoord + direction.offsetY, blockExtender.zCoord + direction.offsetZ);
        }
        return firstConnectedPos;
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack i)
    {
        return ModBlocks.blockExtender.getUnlocalizedName() + i.getItemDamage();
    }
}
