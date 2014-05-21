package com.dynious.refinedrelocation.part;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultipartGenerator;
import codechicken.multipart.TMultiPart;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PartFactory implements MultiPartRegistry.IPartFactory, MultiPartRegistry.IPartConverter
{
    public static final PartFactory INSTANCE = new PartFactory();

    private PartFactory()
    {
    }

    public static void init()
    {
        MultiPartRegistry.registerParts(INSTANCE, new String[]{ Names.relocator });

        MultipartGenerator.registerPassThroughInterface(IRelocator.class.getCanonicalName());
        MultipartGenerator.registerPassThroughInterface(ISidedInventory.class.getCanonicalName());
    }

    @Override
    public TMultiPart createPart(String name, boolean b)
    {
        if (name.equalsIgnoreCase(Names.relocator))
            return new PartRelocator();

        return null;
    }

    @Override
    public Iterable<Block> blockTypes()
    {
        List<Block> list = new ArrayList<Block>();
        list.add(ModBlocks.relocator);
        return list;
    }

    @Override
    public TMultiPart convert(World world, BlockCoord pos)
    {
        Block block = world.getBlock(pos.x, pos.y, pos.z);
        if (block == ModBlocks.relocator)
        {
            return new PartRelocator((TileRelocator) world.getTileEntity(pos.x, pos.y, pos.z));
        }

        return null;
    }
}
