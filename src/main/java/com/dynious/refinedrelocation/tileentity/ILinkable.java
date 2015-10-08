package com.dynious.refinedrelocation.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ILinkable {
    void linkTo(World world, int x, int y, int z, EntityPlayer entityPlayer);
    void clearLink(EntityPlayer entityPlayer);
    int getMaxLinkRange();
    boolean isLinked();
}
