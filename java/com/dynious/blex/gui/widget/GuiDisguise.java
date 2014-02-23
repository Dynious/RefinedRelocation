package com.dynious.blex.gui.widget;

import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.tileentity.TileBlockExtender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiDisguise extends GuiBlExWidgetBase
{
    protected GuiBlExButton button;
    protected TileBlockExtender tile;

    public GuiDisguise(IGuiParent parent, int x, int y, int w, int h, TileBlockExtender tile)
    {
        super(parent, x, y, w, h);
        this.tile = tile;
        button = new GuiBlExButton(this, x, y, w, h, 144, 112, null);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);
        if (isMouseInsideBounds(mouseX, mouseY))
        {
            if (tile.getDisguise() != null)
            {
                Block disguisedAs = tile.getDisguise();
                int meta = tile.blockDisguisedMetadata;
                ItemStack item = new ItemStack(disguisedAs, 0, meta);
                tooltip.add("Disguised as " + item.getDisplayName());
                tooltip.add("\u00A77Use an unlinked linker");
                tooltip.add("\u00A77to undisguise this block");
            }
            else
            {
                tooltip.add("Undisguised");
                tooltip.add("\u00A77Use a linker to disguise this block");
            }
        }
        return tooltip;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        if (tile.getDisguise() != null)
        {
            Block disguisedAs = tile.getDisguise();
            int meta = tile.blockDisguisedMetadata;

            Icon icon = disguisedAs.getIcon(tile.getConnectedDirection().ordinal(), meta);

            this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            this.drawTexturedModelRectFromIcon(x, y, icon, w, h);
        }
        if (tile.getDisguise() != null && isMouseInsideBounds(mouseX, mouseY))
            return;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        super.drawBackground(mouseX, mouseY);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void update()
    {
        super.update();
    }
}
