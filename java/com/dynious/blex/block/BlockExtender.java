package com.dynious.blex.block;

import com.dynious.blex.BlockExtenders;
import com.dynious.blex.gui.GuiAdvancedBlockExtender;
import com.dynious.blex.gui.GuiAdvancedFilteredBlockExtender;
import com.dynious.blex.gui.GuiFilteredBlockExtender;
import com.dynious.blex.item.ItemLinker;
import com.dynious.blex.item.ModItems;
import com.dynious.blex.lib.Names;
import com.dynious.blex.tileentity.*;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
        switch (metadata)
        {
            case 0:
                return new TileBlockExtender();
            case 1:
                return new TileAdvancedBlockExtender();
            case 2:
                return new TileFilteredBlockExtender();
            case 3:
                return new TileAdvancedFilteredBlockExtender();
            case 4:
                return new TileWirelessBlockExtender();
            default:
                return null;
        }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
                             List par3List)
    {
        for (int j = 0; j < 5; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile != null && tile instanceof TileWirelessBlockExtender)
            {
                if (player.getItemInUse().getItem() == ModItems.linker && player.getItemInUse().hasTagCompound())
                {
                    NBTTagCompound tag = player.getItemInUse().getTagCompound();
                    ((TileWirelessBlockExtender) tile).setConnection(tag.getInteger("tileX"), tag.getInteger("tileY"), tag.getInteger("tileZ"));
                    return true;
                }
            }
            return false;
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile != null)
            {
                if (tile instanceof TileAdvancedBlockExtender)
                {
                    FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedBlockExtender((TileAdvancedBlockExtender) tile));
                }
                else if (tile instanceof TileFilteredBlockExtender)
                {
                    FMLCommonHandler.instance().showGuiScreen(new GuiFilteredBlockExtender((TileFilteredBlockExtender) tile));
                }
                else if (tile instanceof TileAdvancedBlockExtender)
                {
                    FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedBlockExtender((TileAdvancedBlockExtender) tile));
                }
                else if (tile instanceof TileAdvancedFilteredBlockExtender)
                {
                    FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedFilteredBlockExtender((TileAdvancedFilteredBlockExtender) tile));
                }
                else if (tile instanceof TileWirelessBlockExtender)
                {
                    if (player.getItemInUse().getItem() == ModItems.linker && player.getItemInUse().hasTagCompound())
                    {
                        NBTTagCompound tag = player.getItemInUse().getTagCompound();
                        ((TileWirelessBlockExtender) tile).setConnection(tag.getInteger("tileX"), tag.getInteger("tileY"), tag.getInteger("tileZ"));
                        return true;
                    }
                    else
                    {
                        FMLCommonHandler.instance().showGuiScreen(new GuiAdvancedFilteredBlockExtender((TileAdvancedFilteredBlockExtender) tile));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
    {
        super.onNeighborBlockChange(world, x, y, z, par5);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileBlockExtender)
        {
            ((TileBlockExtender) tile).blocksChanged = true;
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
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    protected String getTextureName()
    {
        return "obsidian";
    }
}
