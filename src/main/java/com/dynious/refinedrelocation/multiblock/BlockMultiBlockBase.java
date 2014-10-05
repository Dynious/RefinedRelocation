package com.dynious.refinedrelocation.multiblock;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockMultiBlockBase extends BlockContainer
{
    protected BlockMultiBlockBase(Material material)
    {
        super(material);
    }

    @Override
    public abstract TileMultiBlockBase createNewTileEntity(World world, int meta);

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile != null && tile instanceof TileMultiBlockBase)
            {
                ((TileMultiBlockBase) tile).forceCheck();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
}
