package com.dynious.blex.gui.widget;

import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import org.lwjgl.opengl.GL11;
import com.dynious.blex.gui.IGuiParent;
import com.dynious.blex.helper.BlockHelper;
import com.dynious.blex.lib.Resources;
import com.dynious.blex.network.PacketTypeHandler;
import com.dynious.blex.network.packet.PacketInsertDirection;
import com.dynious.blex.tileentity.IAdvancedTile;
import com.dynious.blex.tileentity.TileBlockExtender;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiInsertDirection extends GuiBlExWidgetBase
{
    public ForgeDirection side;
    public ForgeDirection insertDirection;
    public ForgeDirection rotation;
    public ForgeDirection relativeSide;
    public static final int defaultSizeW = 16;
    public static final int defaultSizeH = 16;
    public boolean isConnected = false;
    protected IAdvancedTile tile;

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

        if (isMouseInsideBounds(mouseX, mouseY))
        {
            TileEntity tile = (TileEntity) this.tile;
            tooltip.add(BlockHelper.getBlockDisplayName(tile.worldObj, tile.xCoord + side.offsetX, tile.yCoord + side.offsetY, tile.zCoord + side.offsetZ));

            if (tile instanceof TileBlockExtender)
            {
                TileBlockExtender blockExtender = ((TileBlockExtender) tile);
                isConnected = blockExtender.getConnectedDirection() == side;
                if (isConnected)
                {
                    String colorCode = "\u00A7";
                    String grayColor = colorCode+"7";
                    String yellowColor = colorCode+"e";
                    if (blockExtender.hasConnection())
                    {
                        tooltip.add(grayColor+"Connected");
                        List<String> connections = blockExtender.getConnectionTypes();
                        for (int i=0; i<connections.size(); i++)
                            connections.set(i, yellowColor+connections.get(i));
                        
                        tooltip.addAll(connections);
                    }
                    else
                    {
                        tooltip.add(grayColor+"Not connected");
                    }
                }
            }
        }

        return tooltip;
    }

    public void drawBackground(int mouseX, int mouseY)
    {
        mc.getTextureManager().bindTexture(Resources.GUI_SHARED);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        FontRenderer fontRenderer = mc.fontRenderer;

        boolean hasTile = true;
        boolean isHovered = isMouseInsideBounds(mouseX, mouseY);
        int textureOffset = (relativeSide != null ? relativeSide.ordinal() : side.ordinal()) + 1;
        if (tile instanceof TileBlockExtender)
        {
            TileBlockExtender blockExtender = ((TileBlockExtender) tile);
            isConnected = blockExtender.getConnectedDirection() == side;
            hasTile = blockExtender.getTiles()[side.ordinal()] != null;
        }
        this.drawTexturedModalRect(x, y, isConnected ? 0 : textureOffset * w, 80 + (isConnected ? h*2 : (isHovered ? h : (hasTile ? 0 : h*2))), w, h);

        if (!isConnected)
        {
            this.insertDirection = ForgeDirection.getOrientation(tile.getInsertDirection()[side.ordinal()]);
            char letter = insertDirection.toString().charAt(0);
            fontRenderer.drawString(Character.toString(letter), x + w / 2 - fontRenderer.getCharWidth(letter) / 2, y + h / 2 - fontRenderer.FONT_HEIGHT / 2, hasTile || isHovered ? 0xFFFFFF : 0xAAAAAA, true);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if ((type == 0 || type == 1) && !isConnected && isMouseInsideBounds(mouseX, mouseY))
        {
            byte step = (byte) (type == 0 ? 1 : -1);
            tile.setInsertDirection(side.ordinal(), tile.getInsertDirection()[side.ordinal()] + step);
            PacketDispatcher.sendPacketToServer(PacketTypeHandler.populatePacket(new PacketInsertDirection((byte) side.ordinal(), (byte) tile.getInsertDirection()[side.ordinal()])));
        }
    }
}
