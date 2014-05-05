package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRelocator extends BlockContainer
{
    protected BlockRelocator(int par1)
    {
        super(par1, Material.rock);
        setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setUnlocalizedName(Names.relocator);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileRelocator();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
    {
        super.onNeighborBlockChange(world, x, y, z, par5);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            ((TileRelocator) tile).blocksChanged = true;
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

}
