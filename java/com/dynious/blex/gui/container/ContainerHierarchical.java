package com.dynious.blex.gui.container;

import net.minecraft.inventory.Container;

public abstract class ContainerHierarchical extends Container {

    private Container parentContainer;

    public ContainerHierarchical() 
    {
        parentContainer = null;
    }
    
    public ContainerHierarchical(Container parent)
    {
        parentContainer = parent;
    }
    
    public Container getTopMostContainer()
    {
        return parentContainer == null ? this : (parentContainer instanceof ContainerHierarchical ? ((ContainerHierarchical)parentContainer).getTopMostContainer() : parentContainer);
    }
    
    public Container getParent()
    {
        return parentContainer;
    }
    
    public void setParent( Container parent )
    {
        this.parentContainer = parent;
    }
    
}
