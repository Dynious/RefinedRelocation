package com.dynious.refinedrelocation.part;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultipartGenerator;
import codechicken.multipart.TMultiPart;
import com.dynious.refinedrelocation.api.tileentity.IRelocator;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.world.World;

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
    public boolean canConvert(int id)
    {
        return id == BlockIds.RELOCATOR;
    }

    @Override
    public TMultiPart convert(World world, BlockCoord pos)
    {
        int id = world.getBlockId(pos.x, pos.y, pos.z);
        if (id == BlockIds.RELOCATOR)
        {
            return new PartRelocator((TileRelocator) world.getBlockTileEntity(pos.x, pos.y, pos.z));
        }

        return null;
    }
}
