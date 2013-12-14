package com.dynious.blex.block;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.lib.Names;
import com.dynious.blex.tileentity.TileAdvancedBlockExtender;
import com.dynious.blex.tileentity.TileBlockExtender;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

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
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        switch(metadata)
        {
            case 0:
                return new TileBlockExtender();
            case 1:
                System.out.println("ADVANCED!");
                return new TileAdvancedBlockExtender();
            default:
                return null;
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
                             List par3List)
    {
        for (int j = 0; j < 2; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }
}
