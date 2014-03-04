package com.dynious.refinedrelocation.model;

import com.dynious.refinedrelocation.lib.Resources;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelEnderPearl
{
    private IModelCustom modelCustom;

    public ModelEnderPearl()
    {
        this.modelCustom = AdvancedModelLoader.loadModel(Resources.MODEL_ENDERPEARL);
    }

    public void render()
    {
        modelCustom.renderAll();
    }
}
