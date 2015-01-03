package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.container.ContainerRefinedRelocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiRefinedRelocationContainer extends GuiContainer implements IGuiParent
{
    private List<IGuiWidgetBase> children = new ArrayList<IGuiWidgetBase>();
    private List<IGuiWidgetBase> toAdd = new ArrayList<IGuiWidgetBase>();
    private List<IGuiWidgetBase> toRemove = new ArrayList<IGuiWidgetBase>();
    private boolean clear = false;

    public GuiRefinedRelocationContainer(ContainerRefinedRelocation container)
    {
        super(container);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.clearChildren();
    }

    public List<IGuiWidgetBase> getChildren()
    {
        if (toAdd.isEmpty())
            return children;
        else
        {
            List<IGuiWidgetBase> list = new ArrayList<IGuiWidgetBase>(children);
            list.addAll(toAdd);
            list.removeAll(toRemove);
            return list;
        }
    }

    public int getLeft()
    {
        return guiLeft;
    }

    public int getTop()
    {
        return guiTop;
    }

    @Override
    public void addChild(IGuiWidgetBase child)
    {
        this.toAdd.add(child);
    }

    @Override
    public void addChildren(List<IGuiWidgetBase> children)
    {
        this.toAdd.addAll(children);
    }

    @Override
    public void clearChildren()
    {
        clear = true;
    }

    @Override
    public boolean removeChild(IGuiWidgetBase child)
    {
        return this.toRemove.add(child);
    }

    @Override
    public void removeChildren(List<IGuiWidgetBase> children)
    {
        this.toRemove.addAll(children);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        GL11.glPushMatrix();

        for (IGuiWidgetBase child : this.children)
        {
            GL11.glColor4f(1, 1, 1, 1);
            child.drawBackground(mouseX, mouseY);
        }

        GL11.glPopMatrix();
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) -guiLeft, (float) -guiTop, 0.0F);

        for (IGuiWidgetBase child : this.children)
        {
            GL11.glColor4f(1, 1, 1, 1);
            child.drawForeground(mouseX, mouseY);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f)
    {
        super.drawScreen(mouseX, mouseY, f);

        List<String> tooltip = new ArrayList<String>();
        for (IGuiWidgetBase child : this.children)
        {
            tooltip.addAll(child.getTooltip(mouseX, mouseY));
        }
        if (!tooltip.isEmpty())
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (clear)
        {
            children.clear();
            clear = false;
        }
        if(!toAdd.isEmpty())
        {
            children.addAll(toAdd);
            toAdd.clear();
        }
        if(!toRemove.isEmpty())
        {
            children.removeAll(toRemove);
            toRemove.clear();
        }

        for (IGuiWidgetBase child : this.children)
        {
            child.update();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int type)
    {
        for (IGuiWidgetBase child : this.children)
            child.mouseClicked(mouseX, mouseY, type, isShiftKeyDown());
        super.mouseClicked(mouseX, mouseY, type);
    }

    @Override
    public void handleMouseInput()
    {
        for (IGuiWidgetBase child : this.children)
            child.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        for (IGuiWidgetBase child : this.children)
        {
            if (child.keyTyped(c, i))
            {
                return;
            }
        }
        super.keyTyped(c, i);
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        for (IGuiWidgetBase child : this.children)
            child.mouseMovedOrUp(par1, par2, par3);
        super.mouseMovedOrUp(par1, par2, par3);
    }

    @Override
    public ContainerRefinedRelocation getContainer()
    {
        return (ContainerRefinedRelocation) inventorySlots;
    }
}
