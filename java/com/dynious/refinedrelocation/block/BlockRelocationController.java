package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.multiblock.BlockMultiBlockBase;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import com.dynious.refinedrelocation.tileentity.TileRelocationController;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockRelocationController extends BlockMultiBlockBase
{
    protected BlockRelocationController(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.relocationController);
        this.setHardness(3.0F);
    }

    @Override
    public TileMultiBlockBase createNewTileEntity(World world)
    {
        return new TileRelocationController();
    }
}
