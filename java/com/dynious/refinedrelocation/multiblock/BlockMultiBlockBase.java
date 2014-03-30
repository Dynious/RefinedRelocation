package com.dynious.refinedrelocation.multiblock;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockMultiBlockBase extends BlockContainer
{
    protected BlockMultiBlockBase(int id, Material material)
    {
        super(id, material);
    }

    @Override
    public abstract TileMultiBlockBase createNewTileEntity(World world);

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            return super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile != null && tile instanceof TileMultiBlockBase)
            {
                ((TileMultiBlockBase)tile).forceCheck();
                return true;
            }
        }
        return super.onBlockActivated(world, x, y, z, player, side, par7, par8, par9);
    }
}
