package com.dynious.refinedrelocation.gui.widget;

import com.dynious.refinedrelocation.gui.IGuiParent;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiDisguise extends GuiRefinedRelocationWidgetBase
{
    protected GuiRefinedRelocationButton button;
    protected TileBlockExtender tile;

    public GuiDisguise(IGuiParent parent, int x, int y, int w, int h, TileBlockExtender tile)
    {
        super(parent, x, y, w, h);
        this.tile = tile;
        button = new GuiRefinedRelocationButton(this, x, y, w, h, 144, 112, null);
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
                tooltip.add(StatCollector.translateToLocalFormatted(Strings.DISGUISED,  item.getDisplayName()));
                for (String s : StatCollector.translateToLocal(Strings.DISGUISED_INFO).split("\\\\n"))
                {
                    tooltip.add("\u00A77" + s);
                }
            }
            else
            {
                tooltip.add(StatCollector.translateToLocal(Strings.UNDISGUISED));
                for (String s : StatCollector.translateToLocal(Strings.UNDISGUISED_INFO).split("\\\\n"))
                {
                    tooltip.add("\u00A77" + s);
                }
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
            if (icon != null)
            {
                this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                this.drawTexturedModelRectFromIcon(x, y, icon, w, h);
            }
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
