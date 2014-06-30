package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileSortingConnector;
import com.dynious.refinedrelocation.tileentity.TileSortingImporter;
import com.dynious.refinedrelocation.tileentity.TileSortingInterface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockSortingConnector extends BlockContainer
{
    private IIcon[] icons = new IIcon[3];
    private IIcon[] interfaceTextures = new IIcon[2];

    public BlockSortingConnector()
    {
        super(Material.rock);
        this.setBlockName(Names.sortingConnector);
        this.setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        switch (metadata)
        {
            case 0:
                return new TileSortingConnector();
            case 1:
                return new TileSortingInterface();
            case 2:
                return new TileSortingImporter();
        }
        return null;
    }

    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileSortingInterface)
            {
                APIUtils.openFilteringGUI(player, world, x, y, z);
                return true;
            }
            else if (tile instanceof TileSortingImporter)
            {
                ((TileSortingImporter)tile).onRightClick(player);
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs,
                             List par3List)
    {
        for (int metadata = 0; metadata < 3; ++metadata)
        {
            par3List.add(new ItemStack(par1, 1, metadata));
        }
    }
    
    @Override
    public void breakBlock(World world, int par2, int par3, int par4, Block par5, int par6)
    {
        IOHelper.dropInventory(world, par2, par3, par4);
        super.breakBlock(world, par2, par3, par4, par5, par6);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileSortingConnector)
        {
            TileSortingConnector tile = (TileSortingConnector) tileEntity;
            Block blockDisguisedAs = tile.getDisguise();
            int disguisedMeta = tile.blockDisguisedMetadata;
            if (blockDisguisedAs != null)
            {
                return blockDisguisedAs.getIcon(side, disguisedMeta);
            }
            else if (tile instanceof TileSortingInterface)
            {
                TileSortingInterface sortingInterface = (TileSortingInterface) tile;
                if (sortingInterface.getConnectedSide().ordinal() == side)
                {
                    return interfaceTextures[0];
                }
                else if (sortingInterface.isStuffed())
                {
                    return interfaceTextures[1];
                }
            }
        }
        return super.getIcon(world, x, y, z, side);
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileSortingConnector)
        {
            TileSortingConnector tile = (TileSortingConnector) tileEntity;
            Block blockDisguisedAs = tile.getDisguise();
            if (blockDisguisedAs != null)
                return blockDisguisedAs.colorMultiplier(world, x, y, z);
        }
        return super.colorMultiplier(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        icons = new IIcon[3];
        for (int i = 0; i < icons.length; i++)
        {
            icons[i] = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.sortingConnector + i);
        }
        interfaceTextures[0] = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.sortingConnector + 1 + "ConSide");
        interfaceTextures[1] = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.sortingConnector + 1 + "Stuffed");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metaData)
    {
        if (metaData >= 0 && metaData < icons.length)
            return icons[metaData];
        return null;
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileSortingInterface)
        {
            return ((TileSortingInterface) tile).rotateBlock();
        }
        return false;
    }
}
