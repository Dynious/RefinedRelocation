package com.dynious.refinedrelocation.block;

import cofh.api.block.IDismantleable;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;

@Optional.InterfaceList(value = {@Optional.Interface(iface = "cofh.api.block.IDismantleable", modid = "CoFHCore")})
public class BlockPowerLimiter extends BlockContainer implements IDismantleable
{
    private Icon[] icons = new Icon[3];

    public BlockPowerLimiter(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.powerLimiter);
        this.setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TilePowerLimiter();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (!world.isRemote)
        {
            TilePowerLimiter tile = (TilePowerLimiter) world.getBlockTileEntity(x, y, z);
            if (player.isSneaking())
            {
                tile.setDisablePower(!tile.getDisablePower());
                return true;
            }
            GuiHelper.openGui(player, tile);
        }
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
    {
        super.onNeighborBlockChange(world, x, y, z, par5);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TilePowerLimiter)
        {
            ((TilePowerLimiter) tile).blocksChanged = true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        for (int i = 0; i < icons.length; i++)
        {
            icons[i] = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.powerLimiter + i);
        }
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        return icons[0];
    }

    @Override
    public Icon getBlockTexture(IBlockAccess worldObj, int x, int y, int z, int side)
    {
        TilePowerLimiter tile = (TilePowerLimiter) worldObj.getBlockTileEntity(x, y, z);
        if (tile.getConnectedDirection().ordinal() == side)
        {
            return icons[2];
        }
        else if (tile.getDisablePower())
        {
            return icons[1];
        }
        else
        {
            return icons[0];
        }
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x,
                                    int y, int z, boolean returnBlock)
    {
        int meta = world.getBlockMetadata(x, y, z);

        ArrayList<ItemStack> items = this.getBlockDropped(world, x, y, z, meta, 0);

        for (ItemStack item : items)
        {
            IOHelper.spawnItemInWorld(world, item, x, y, z);
        }

        world.setBlockToAir(x, y, z);
        return null;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        TilePowerLimiter tile = (TilePowerLimiter) worldObj.getBlockTileEntity(x, y, z);
        return tile.rotateBlock();
    }
}
