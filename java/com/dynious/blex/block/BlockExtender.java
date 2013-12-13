package com.dynious.blex.block;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.lib.Names;
import com.dynious.blex.tileentity.TileBlockExtender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockExtender extends BlockContainer
{
    public BlockExtender(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.blockExtender);
        this.setCreativeTab(BlockExtenders.tabBlEx);
    }


    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileBlockExtender();
    }
}
