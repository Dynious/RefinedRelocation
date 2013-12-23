package com.dynious.blex.block;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.lib.Names;
import com.dynious.blex.tileentity.TileBuffer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBuffer extends BlockContainer
{
    protected BlockBuffer(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.buffer);
        this.setCreativeTab(BlockExtenders.tabBlEx);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        switch(metadata)
        {
            case 0:
                return new TileBuffer();
        }
        return null;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int side)
    {
        super.onNeighborBlockChange(world, x, y, z, side);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileBuffer)
        {
            ((TileBuffer)tile).onBlocksChanged();
        }
    }
}
