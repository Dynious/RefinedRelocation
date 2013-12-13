package com.dynious.blex.block;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.lib.Names;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockExtender extends Block
{
    public BlockExtender(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.blockExtender);
        this.setCreativeTab(BlockExtenders.tabBlEx);
    }
}
