package com.dynious.refinedrelocation.gui;

import com.dynious.refinedrelocation.api.gui.IGuiParent;
import com.dynious.refinedrelocation.api.gui.IGuiWidgetBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiRefinedRelocationContainer extends GuiContainer implements IGuiParent
{
    protected List<IGuiWidgetBase> children = new ArrayList<IGuiWidgetBase>();

    public GuiRefinedRelocationContainer(Container container)
    {
        super(container);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.clearChildren();
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
        this.children.add(child);
    }

    @Override
    public void addChildren(List<IGuiWidgetBase> children)
    {
        this.children.addAll(children);
    }

    @Override
    public void clearChildren()
    {
        this.children.clear();
    }

    @Override
    public boolean removeChild(IGuiWidgetBase child)
    {
        return this.children.remove(child);
    }

    @Override
    public void removeChildren(List<IGuiWidgetBase> children)
    {
        this.children.removeAll(children);
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
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

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
}
