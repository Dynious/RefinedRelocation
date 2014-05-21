package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.renderer.RendererRelocator;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRelocator extends BlockContainer
{
    protected BlockRelocator()
    {
        super(Material.rock);
        setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setBlockName(Names.relocator);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileRelocator();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5)
    {
        super.onNeighborBlockChange(world, x, y, z, par5);
        TileEntity tile = world.getTileEntity(x, y, z);
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

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            for (TravellingItem item :((TileRelocator) tile).getItems(true))
            {
                IOHelper.spawnItemInWorld(world, item.getItemStack(), x + item.getX(0), y + item.getY(0), z + item.getZ(0));
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        RendererRelocator.loadIcons(register);
    }
}
