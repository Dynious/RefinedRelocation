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
}
