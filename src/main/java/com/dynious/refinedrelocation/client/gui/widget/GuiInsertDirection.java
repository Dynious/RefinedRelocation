package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.GuiRefinedRelocationContainer;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageInsertDirection;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.TileAdvancedBuffer;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiInsertDirection extends GuiWidgetBase
{
    public static final int defaultSizeW = 16;
    public static final int defaultSizeH = 16;
    public ForgeDirection side;
    public ForgeDirection insertDirection;
    public ForgeDirection rotation;
    public ForgeDirection relativeSide;
    public boolean isFrontSide = false;
    protected IAdvancedTile tile;
    private boolean adventureModeRestriction;

    public GuiInsertDirection(IGuiParent parent, int x, int y, IAdvancedTile tile, ForgeDirection side, ForgeDirection relativeSide)
    {
        super(parent, x, y, defaultSizeW, defaultSizeH);

        this.relativeSide = relativeSide;
        this.side = side; //.getRotation(ForgeDirection.UP);
        this.tile = tile;
        this.insertDirection = ForgeDirection.getOrientation(tile.getInsertDirection()[side.ordinal()]);
    }

    @Override
    public List<String> getTooltip(int mouseX, int mouseY)
    {
        List<String> tooltip = super.getTooltip(mouseX, mouseY);

        if (isInsideBounds(mouseX, mouseY))
        {
            TileEntity tile = (TileEntity) this.tile;

            if (tile instanceof TileAdvancedBuffer)
            {
                tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.PRIORITY_ORDER) + ": \u00a7r" + (((TileAdvancedBuffer) tile).getInsertDirection()[side.ordinal()] + 1));
            }
            tooltip.add("\u00a7a" + StatCollector.translateToLocal(Strings.DIRECTION + side.ordinal()) + " \u00a7r(" + BlockHelper.getBlockDisplayName(tile.getWorldObj(), tile.xCoord + side.offsetX, tile.yCoord + side.offsetY, tile.zCoord + side.offsetZ, side) + ")");

            if (tile instanceof TileBlockExtender)
            {
                TileBlockExtender blockExtender = ((TileBlockExtender) tile);
                isFrontSide = blockExtender.getConnectedDirection() == side;
                if (isFrontSide)
                {
                    String colorCode = "\u00A7";
                    String grayColor = colorCode + "7";
                    String yellowColor = colorCode + "e";
                    if (blockExtender.hasConnection())
                    {
                        tooltip.add(grayColor + StatCollector.translateToLocal(Strings.CONNECTED));
                        List<String> connections = blockExtender.getConnectionTypes();
                        for (int i = 0; i < connections.size(); i++)
                            connections.set(i, yellowColor + "* " + connections.get(i));

                        tooltip.addAll(connections);
                    } else
                    {
                        tooltip.add(grayColor + StatCollector.translateToLocal(Strings.NOT_CONNECTED));
                    }
                } else
                {
                    tooltip.add("\u00a7a" + StatCollector.translateToLocal(Strings.INSERT_EXTRACT) + ": \u00a7r" + StatCollector.translateToLocal(Strings.DIRECTION + insertDirection.ordinal()));
                    tooltip.add("\u00a7e" + StatCollector.translateToLocal(Strings.CLICK_TO_TOGGLE));
                }
            }
        }

        return tooltip;
    }

    public void drawBackground(int mouseX, int mouseY)
    {
        mc.getTextureManager().bindTexture(Resources.GUI_SHARED);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        FontRenderer fontRendererObj = mc.fontRenderer;

        boolean isHovered = isInsideBounds(mouseX, mouseY);
        int textureOffset = (relativeSide != null ? relativeSide.ordinal() : side.ordinal()) + 1;

        if (tile instanceof TileBlockExtender)
        {
            TileBlockExtender blockExtender = ((TileBlockExtender) tile);
            isFrontSide = blockExtender.getConnectedDirection() == side;
            boolean hasTile = blockExtender.getTiles()[side.ordinal()] != null;

            this.drawTexturedModalRect(x, y, isFrontSide ? 0 : textureOffset * w, 80 + (isFrontSide ? h * 2 : (isHovered ? h : (hasTile ? 0 : h * 2))), w, h);

            if (!isFrontSide)
            {
                this.insertDirection = ForgeDirection.getOrientation(tile.getInsertDirection()[side.ordinal()]);
                char letter = insertDirection.toString().charAt(0);
                fontRendererObj.drawString(Character.toString(letter), x + w / 2 - fontRendererObj.getCharWidth(letter) / 2, y + h / 2 - fontRendererObj.FONT_HEIGHT / 2, hasTile || isHovered ? 0xFFFFFF : 0xAAAAAA, true);
            }
        } else if (tile instanceof TileAdvancedBuffer)
        {
            this.drawTexturedModalRect(x, y, isFrontSide ? 0 : textureOffset * w, 80 + (isFrontSide ? h * 2 : (isHovered ? h : 0)), w, h);

            TileAdvancedBuffer buffer = (TileAdvancedBuffer) tile;
            byte p = buffer.getPriority(side.ordinal());
            String priority = p == TileAdvancedBuffer.NULL_PRIORITY ? "--" : Byte.toString((byte) (p + 1));
            fontRendererObj.drawString(priority, x + w / 2 - fontRendererObj.getStringWidth(priority) / 2, y + h / 2 - fontRendererObj.FONT_HEIGHT / 2, isHovered ? 0xFFFFFF : 0xAAAAAA, true);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if ((type == 0 || type == 1) && !isFrontSide && isInsideBounds(mouseX, mouseY))
        {
            if(!isAdventureModeRestriction() || !GuiRefinedRelocationContainer.isRestrictedAccessWithError())
            {
                if (tile instanceof TileBlockExtender)
                {
                    byte step = (byte) (type == 0 ? 1 : -1);
                    tile.setInsertDirection(side.ordinal(), tile.getInsertDirection()[side.ordinal()] + step);
                    NetworkHandler.INSTANCE.sendToServer(new MessageInsertDirection((byte) side.ordinal(), tile.getInsertDirection()[side.ordinal()]));
                }
                if (tile instanceof TileAdvancedBuffer)
                {
                    byte step = (byte) (type == 0 ? -1 : 1);
                    if (isShiftKeyDown) step = (byte) (step * 6);

                    tile.setInsertDirection(side.ordinal(), tile.getInsertDirection()[side.ordinal()] + step);
                    NetworkHandler.INSTANCE.sendToServer(new MessageInsertDirection((byte) side.ordinal(), tile.getInsertDirection()[side.ordinal()]));
                }
            }
        }
    }

    public final boolean isAdventureModeRestriction() {
        return adventureModeRestriction;
    }

    public void setAdventureModeRestriction(boolean adventureModeRestriction) {
        this.adventureModeRestriction = adventureModeRestriction;
    }

}
