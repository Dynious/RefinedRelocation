package com.dynious.refinedrelocation.block;

import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.grid.relocator.TravellingItem;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.RelocatorData;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockRelocator extends BlockContainer
{
    private RayTracer rayTracer = new RayTracer();

    protected BlockRelocator(int par1)
    {
        super(par1, Material.rock);
        setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setUnlocalizedName(Names.relocator);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileRelocator();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
    {
        super.onNeighborBlockChange(world, x, y, z, par5);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
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
    public void breakBlock(World world, int x, int y, int z, int id, int meta)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            for (TravellingItem item :((TileRelocator) tile).getItems(true))
            {
                IOHelper.spawnItemInWorld(world, item.getItemStack(), x + item.getX(0), y + item.getY(0), z + item.getZ(0));
            }
        }
        super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            MovingObjectPosition hit = RayTracer.retraceBlock(world, player, x, y, z);
            if (hit != null)
            {
                return ((TileRelocator)tile).onActivated(player, hit, player.getHeldItem());
            }
        }
        return false;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocator)
        {
            List<IndexedCuboid6> cuboids = new ArrayList<IndexedCuboid6>();
            for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
            {
                if (((TileRelocator)tile).connectsToSide(i))
                {
                    cuboids.add(new IndexedCuboid6(i, RelocatorData.sideCuboids[i].copy().add(Vector3.fromTileEntity(tile))));
                }
            }
            cuboids.add(new IndexedCuboid6(6, RelocatorData.middleCuboid.copy().add(Vector3.fromTileEntity(tile))));
            return rayTracer.rayTraceCuboids(new Vector3(start), new Vector3(end), cuboids, new BlockCoord(x, y, z), this);
        }
        return null;
    }
}
