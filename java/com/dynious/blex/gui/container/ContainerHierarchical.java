package com.dynious.blex.gui.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import java.util.ArrayList;
import java.util.List;

public abstract class ContainerHierarchical extends Container
{

    private ContainerHierarchical parentContainer;
    private List<ContainerHierarchical> childContainers = new ArrayList<ContainerHierarchical>();

    public ContainerHierarchical()
    {
        parentContainer = null;
    }

    public ContainerHierarchical(ContainerHierarchical parent)
    {
        parentContainer = parent;
        parent.addChild(this);
    }

    public ContainerHierarchical getTopMostContainer()
    {
        return parentContainer == null ? this : parentContainer.getTopMostContainer();
    }

    public ContainerHierarchical getParent()
    {
        return parentContainer;
    }

    public void setParent(ContainerHierarchical parent)
    {
        this.parentContainer = parent;
    }

    public void addChild(ContainerHierarchical child)
    {
        childContainers.add(child);
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        for (Container child : childContainers)
        {
            child.addCraftingToCrafters(par1ICrafting);
        }
        super.addCraftingToCrafters(par1ICrafting);
    }

    @Override
    public void removeCraftingFromCrafters(ICrafting par1ICrafting)
    {
        for (Container child : childContainers)
        {
            child.removeCraftingFromCrafters(par1ICrafting);
        }
        super.removeCraftingFromCrafters(par1ICrafting);
    }

}
