package com.dynious.refinedrelocation.block;

import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.RelocatorData;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockRelocator extends BlockContainer
{
    private RayTracer rayTracer = new RayTracer();

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
        if (world.isRemote)
            return;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            ((TileRelocator) tile).onBlocksChanged();
        }
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            return ((TileRelocator) tile).shouldConnectToRedstone();
        }
        return false;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            return ((TileRelocator) tile).isProvidingStrongPower(side);
        }
        return 0;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return isProvidingStrongPower(world, x, y, z, side);
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileRelocator)
        {
            ((TileRelocator) tile).onAdded(entity, stack);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            for (ItemStack stack : ((TileRelocator) tile).getDrops())
            {
                IOHelper.spawnItemInWorld(world, stack, x, y, z);
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            MovingObjectPosition hit = RayTracer.retraceBlock(world, player, x, y, z);
            if (hit != null)
            {
                return ((TileRelocator) tile).onActivated(player, hit, player.getHeldItem());
            }
        }
        return false;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            MovingObjectPosition hit = RayTracer.retraceBlock(world, player, x, y, z);
            if (hit != null)
            {
                boolean breakBlock = ((TileRelocator) tile).leftClick(player, hit, player.getHeldItem());
                if (breakBlock)
                {
                    super.onBlockClicked(world, x, y, z, player);
                }
            }
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            MovingObjectPosition hit = RayTracer.retraceBlock(world, player, x, y, z);
            if (hit != null)
            {
                if (!((TileRelocator) tile).leftClick(player, hit, player.getHeldItem()))
                { // If tile.leftClick returns false, return false, otherwise return super.
                    return false;
                }
            }
        }
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List collisionList, Entity entity)
    {
        RelocatorData.middleCuboid.setBlockBounds(this);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, collisionList, entity);

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileRelocator)
        {
            TileRelocator relocator = (TileRelocator) tile;
            for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
            {
                if (relocator.connectsToSide(i))
                {
                    RelocatorData.sideCuboids[i].setBlockBounds(this);
                }
            }
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            List<IndexedCuboid6> cuboids = new ArrayList<IndexedCuboid6>();
            for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
            {
                if (((TileRelocator) tile).connectsToSide(i))
                {
                    cuboids.add(new IndexedCuboid6(i, RelocatorData.sideCuboids[i].copy().add(Vector3.fromTileEntity(tile))));
                }
            }
            cuboids.add(new IndexedCuboid6(6, RelocatorData.middleCuboid.copy().add(Vector3.fromTileEntity(tile))));
            return rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
        }
        return null;
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        blockIcon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorCenter0");
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list)
    {
        for (int i = 0; i < RelocatorData.oreTypes.length; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
