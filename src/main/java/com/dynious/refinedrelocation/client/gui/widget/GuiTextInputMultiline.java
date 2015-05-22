package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiTextInputMultiline extends GuiWidgetBase {

	private final FontRenderer fontRenderer;

	protected boolean isMultiLine;
	private boolean isEnabled = true;
	private boolean hasFocus;

	private int cursorPosition;
	private int cursorCounter;
	private int maxLength;
	private int maxLengthPerLine;

	private String text;
	private String[] renderLines;
	private int lineScrollOffset;

	public GuiTextInputMultiline(IGuiParent parent, int x, int y, int w, int h) {
		super(parent, x, y, w, h);
		fontRenderer = mc.fontRenderer;
	}

	@Override
	public void update() {
		super.update();

		cursorCounter++;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY) {
		super.drawBackground(mouseX, mouseY);

		drawRect(x - 1, y - 1, x + w + 1, y + h + 1, -6250336);
		drawRect(x, y, x + w, y + h, -16777216);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		super.drawForeground(mouseX, mouseY);

		if (visible)
		{
			if(renderLines == null) {
				renderLines = text.split("\n");
			}
			for(int i = 0; i < renderLines.length; i++) {
				fontRenderer.drawString(renderLines[i], x, y + i * fontRenderer.FONT_HEIGHT, Integer.MAX_VALUE);
			}
			int cursorLine = 0;
			int lastLineIdx = 0;
			for(int i = 0; i < cursorPosition; i++) {
				if(text.charAt(i) == '\n') {
					cursorLine++;
					lastLineIdx = i + 1;
				}
			}
			drawCursorVertical(x + fontRenderer.getStringWidth(text.substring(lastLineIdx, cursorPosition)), y + cursorLine * fontRenderer.FONT_HEIGHT);
		}
	}

	private void drawCursorVertical(int x, int y)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);
		tessellator.startDrawingQuads();
		tessellator.addVertex((double) x, (double) y + fontRenderer.FONT_HEIGHT, 0.0D);
		tessellator.addVertex((double) x + 1, (double) y + fontRenderer.FONT_HEIGHT, 0.0D);
		tessellator.addVertex((double) x + 1, (double) y, 0.0D);
		tessellator.addVertex((double) x, (double) y, 0.0D);
		tessellator.draw();
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public boolean keyTyped(char unicode, int keyCode) {
		if(!hasFocus) {
			return false;
		}
		if(!isEnabled) {
			return false;
		}
		switch(keyCode) {
			case Keyboard.KEY_RETURN:
				insertText("\n");
				markDirty();
				return true;
			case Keyboard.KEY_END:
				setCursorPosition(getEndOfLine(cursorPosition, 1));
				return true;
			case Keyboard.KEY_HOME:
				setCursorPosition(getStartOfLine(cursorPosition, 1));
				return true;
			case Keyboard.KEY_LEFT:
				setCursorPosition(cursorPosition - 1);
				return true;
			case Keyboard.KEY_RIGHT:
				setCursorPosition(cursorPosition + 1);
				return true;
			case Keyboard.KEY_UP:
				int upLine = getStartOfLine(cursorPosition, 2);
				setCursorPosition(upLine + Math.min(getLineLength(upLine), (cursorPosition - getStartOfLine(cursorPosition, 1))));
				return true;
			case Keyboard.KEY_DOWN:
				int downLine = getEndOfLine(cursorPosition, 2);
				setCursorPosition(getStartOfLine(downLine, 1) + Math.min(getLineLength(downLine), (cursorPosition - getStartOfLine(cursorPosition, 1))));
				return true;
			case Keyboard.KEY_DELETE:
				deleteFront(false);
				return true;
			case Keyboard.KEY_BACK:
				deleteBack(false);
				return true;
			default:
				if (ChatAllowedCharacters.isAllowedCharacter(unicode)) {
					insertText(Character.toString(unicode));
					return true;
				}
		}
		return super.keyTyped(unicode, keyCode);
	}

	private int getLineLength(int position) {
		return getEndOfLine(position, 1) - getStartOfLine(position, 1);
	}

	private int getStartOfLine(int position, int iterations) {
		int startOfLine = position;
		for(int i = 0; i < iterations; i++) {
			startOfLine = text.lastIndexOf('\n', startOfLine - 1);
		}
		return startOfLine != -1 ? startOfLine + 1 : 0;
	}

	private int getEndOfLine(int position, int iteration) {
		int endOfLine = position - 1;
		for(int i = 0; i < iteration; i++) {
			endOfLine = text.indexOf('\n', endOfLine + 1);
		}
		return endOfLine != -1 ? endOfLine : text.length();
	}

	private void markDirty() {
		renderLines = null;
	}

	private void deleteBack(boolean wholeWord) {
		if(cursorPosition > 0) {
			text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
			setCursorPosition(cursorPosition - 1);
			markDirty();
			onTextChangedByUser(text);
		}
	}

	private void deleteFront(boolean wholeWord) {
		if(cursorPosition < text.length()) {
			text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
			markDirty();
			onTextChangedByUser(text);
		}
	}

	private void insertText(String s) {
		text = (cursorPosition > 0 ? text.substring(0, cursorPosition) : "") + s + (text.length() > cursorPosition ? text.substring(cursorPosition) : "");
		setCursorPosition(cursorPosition + s.length());
		markDirty();
		onTextChangedByUser(text);
	}

	public void setFocused(boolean hasFocus) {
		if(hasFocus && !this.hasFocus) {
			cursorCounter = 0;
		}
		this.hasFocus = hasFocus;
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = MathHelper.clamp_int(cursorPosition, 0, text.length());
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown) {
		super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);

		boolean isInside = isInsideBounds(mouseX, mouseY);
		setFocused(isInside);

		if (isInside && type == 0)
		{
			int relX = mouseX - x - 4;
			String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), w);
			setCursorPosition(fontRenderer.trimStringToWidth(s, relX).length() + lineScrollOffset);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		markDirty();
	}

	protected void onTextChangedByUser(String newText) {}
}
