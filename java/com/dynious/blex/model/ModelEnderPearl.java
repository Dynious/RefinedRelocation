package com.dynious.blex.model;

import com.dynious.blex.lib.Resources;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelEnderPearl
{
    private IModelCustom modelCustom;

    public ModelEnderPearl()
    {
        this.modelCustom = AdvancedModelLoader.loadModel(Resources.MODEL_BLOCK_EXTENDER);
    }

    public void render()
    {
        modelCustom.renderAll();
    }
}
