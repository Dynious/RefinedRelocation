package com.dynious.refinedrelocation.client.gui;

import com.dynious.refinedrelocation.client.gui.widget.GuiWidgetBase;
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
    protected IGuiWidgetBase rootNode;

    public GuiRefinedRelocationContainer(ContainerRefinedRelocation container)
    {
        super(container);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        rootNode = new GuiWidgetBase(0, 0, width, height) {};

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
        this.rootNode.addChild(child);
    }

    @Override
    public void addChildren(List<IGuiWidgetBase> children)
    {
        this.rootNode.addChildren(children);
    }

    @Override
    public void clearChildren()
    {
        this.rootNode.clearChildren();
    }

    @Override
    public boolean removeChild(IGuiWidgetBase child)
    {
        return this.rootNode.removeChild(child);
    }

    @Override
    public void removeChildren(List<IGuiWidgetBase> children)
    {
        this.rootNode.removeChildren(children);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        GL11.glPushMatrix();

        GL11.glColor4f(1, 1, 1, 1);
        rootNode.drawBackground(mouseX, mouseY);

        GL11.glPopMatrix();
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) -guiLeft, (float) -guiTop, 0.0F);

        GL11.glColor4f(1, 1, 1, 1);
        rootNode.drawForeground(mouseX, mouseY);

        GL11.glPopMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f)
    {
        super.drawScreen(mouseX, mouseY, f);

        // TODO optimize by only querying the mouseover widget for tooltip
        List<String> tooltip = new ArrayList<String>();
        tooltip.addAll(rootNode.getTooltip(mouseX, mouseY));
        if (!tooltip.isEmpty())
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        rootNode.update();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int type)
    {
        IGuiWidgetBase child = rootNode.getChildAt(mouseX, mouseY);
        if (child != null)
        {
            child.mouseClicked(mouseX, mouseY, type, isShiftKeyDown());
        }
        super.mouseClicked(mouseX, mouseY, type);
    }

    @Override
    public void handleMouseInput()
    {
        rootNode.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void keyTyped(char c, int i)
    {
        if(!rootNode.keyTyped(c, i))
        {
            super.keyTyped(c, i);
        }
    }

    @Override
    public void mouseMovedOrUp(int par1, int par2, int par3)
    {
        rootNode.mouseMovedOrUp(par1, par2, par3);
        super.mouseMovedOrUp(par1, par2, par3);
    }

    @Override
    public ContainerRefinedRelocation getContainer()
    {
        return (ContainerRefinedRelocation) inventorySlots;
    }
}
