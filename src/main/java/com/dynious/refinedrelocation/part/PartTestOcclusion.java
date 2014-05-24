package com.dynious.refinedrelocation.part;

import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.JCuboidPart;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;

import java.util.LinkedList;
import java.util.List;

public class PartTestOcclusion extends JCuboidPart implements JNormalOcclusion
{
    Cuboid6 bounds;
    List<Cuboid6> occlusions = new LinkedList();

    public PartTestOcclusion(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
    {
        this.bounds = new Cuboid6(minX, minY, minZ, maxX, maxY, maxZ);
        this.occlusions.add(this.bounds);
    }

    public PartTestOcclusion(Cuboid6 cuboid)
    {
        bounds = cuboid;
        occlusions.add(cuboid);
    }

    @Override
    public String getType()
    {
        return "test";
    }

    @Override
    public Cuboid6 getBounds()
    {
        return this.bounds;
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes()
    {
        return this.occlusions;
    }

    public boolean occlusionTest(TMultiPart part)
    {
        return NormalOcclusionTest.apply(this, part);
    }
}
