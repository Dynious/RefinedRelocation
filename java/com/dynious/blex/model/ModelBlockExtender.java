package com.dynious.blex.model;

import com.dynious.blex.lib.Resources;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelBlockExtender
{
    private IModelCustom modelCustom;

    public ModelBlockExtender()
    {
        this.modelCustom = AdvancedModelLoader.loadModel(Resources.MODEL_BLOCK_EXTENDER);
    }

    public void render()
    {
        modelCustom.renderAll();
    }

    public void renderBase()
    {
        modelCustom.renderPart("Base");
    }

    public void renderPilars()
    {
        modelCustom.renderOnly("PilarA", "PilarB", "PilarC", "PilarD", "PilarE", "PilarF", "PilarG", "PilarH");
    }
    public void renderSides()
    {
        modelCustom.renderOnly("SideNorth", "SideSouth", "SideWest", "SideEast", "SideUp");
    }
}
