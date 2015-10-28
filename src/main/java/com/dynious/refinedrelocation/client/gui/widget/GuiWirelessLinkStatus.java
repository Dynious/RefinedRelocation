package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.graphics.TextureRegion;
import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.client.gui.SharedAtlas;
import com.dynious.refinedrelocation.client.gui.widget.button.GuiButton;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileWirelessBlockExtender;
import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;

import java.util.List;

public class GuiWirelessLinkStatus extends GuiButton {

    private final TextureRegion[] activeTexture = new TextureRegion[2];
    private final TextureRegion[] inactiveTexture = new TextureRegion[2];
    protected TileWirelessBlockExtender tile;
    protected boolean linked;
    protected boolean lastLinked;

    public GuiWirelessLinkStatus(IGuiParent parent, int x, int y, TileWirelessBlockExtender tile) {
        super(parent, x, y, 16, 16, null, null);
        this.tile = tile;

        inactiveTexture[0] = SharedAtlas.findRegion("icon_link_inactive");
        inactiveTexture[1] = SharedAtlas.findRegion("icon_link_inactive_hover");
        activeTexture[0] = SharedAtlas.findRegion("icon_link_active");
        activeTexture[1] = SharedAtlas.findRegion("icon_link_active_hover");
        setButtonTextures(inactiveTexture);

        update();
    }

    public void setLinked(boolean state) {
        this.linked = state;

        if(state) {
            setButtonTextures(activeTexture);
        } else {
            setButtonTextures(inactiveTexture);
        }
    }

    @Override
    public void getTooltip(List<String> tooltip, int mouseX, int mouseY) {
        super.getTooltip(tooltip, mouseX, mouseY);
        if (isInsideBounds(mouseX, mouseY)) {
            String colorCode = "\u00A7";
            String grayColor = colorCode + "7";
            String yellowColor = colorCode + "e";
            tooltip.add(StatCollector.translateToLocal(Strings.WIRELESS_LINK));
            if (linked) {
                Block linkedBlock = tile.getWorldObj().getBlock(tile.xConnected, tile.yConnected, tile.zConnected);
                int linkedMetadata = tile.getWorldObj().getBlockMetadata(tile.xConnected, tile.yConnected, tile.zConnected);
                tooltip.add(grayColor + StatCollector.translateToLocalFormatted(Strings.LINKED_TO_AT, BlockHelper.getBlockDisplayName(linkedBlock, linkedMetadata), tile.xConnected, tile.yConnected, tile.zConnected));

                if (tile.hasConnection()) {
                    tooltip.add(grayColor + StatCollector.translateToLocal(Strings.CONNECTIONS) + ":");
                    List<String> connections = tile.getConnectionTypes();
                    for (int i = 0; i < connections.size(); i++)
                        connections.set(i, yellowColor + connections.get(i));

                    tooltip.addAll(connections);
                }
            } else {
                tooltip.add(grayColor + StatCollector.translateToLocal(Strings.UNLINKED));
                tooltip.add(yellowColor + StatCollector.translateToLocal(Strings.USE_LINKER_TO_LINK));
            }
        }

    }

    @Override
    public void update() {
        super.update();

        boolean isLinked = tile.xConnected != Integer.MAX_VALUE;
        if (lastLinked != isLinked)
            setLinked(isLinked);
    }
}
