package the_dark_jumper.cannonTracer.gui.guiElements;

import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannonTracer.Main;

public abstract class DoubleSegmentFrame extends BasicTextFrame implements Clickable{
	public int innerColor2;
	public int valueColor;
	public String value;
	public int colorHover, colorHover2;
	public boolean ignoreInput = false, hovered = false;
	
	public DoubleSegmentFrame(Main main, Screen parent, String text, String value, int x, int y, int xEnd, int yEnd,
			int borderThickness, int innerColor, int innerColor2, int borderColor, int valueColor, int colorHover,
			int colorHover2) {
		super(main, parent, text, x, y, xEnd, yEnd, borderThickness, innerColor, borderColor);
		init(value, innerColor2, valueColor, colorHover, colorHover2);
	}
	
	public void init(String value, int innerColor2, int valueColor, int colorHover, int colorHover2) {
		this.value = value;
		this.innerColor2 = innerColor2;
		this.valueColor = valueColor;
		this.colorHover = colorHover;
		this.colorHover2 = colorHover2;
	}
	
	public void mouseOver(int x, int y, int scaledScreenWidth, int scaledScreenHeight, boolean mouseLeftDown) {
		if(ignoreInput && !mouseLeftDown) {
			ignoreInput = false;
		}
		int x1 = getPercentValue(scaledScreenWidth, this.x);
		int x2 = getPercentValue(scaledScreenWidth, this.xEnd);
		int y1 = getPercentValue(scaledScreenHeight, this.y);
		int y2 = getPercentValue(scaledScreenHeight, this.yEnd);
		if(x > x1 && x < x2 && y > y1 && y < y2) {
			onHovered();
			if(!ignoreInput && mouseLeftDown) {
				onClicked();
			}
		}else if(hovered) {
			onUnHovered();
		}
	}
	
	public void onHovered() {
		hovered = true;
	}
	
	public void onUnHovered() {
		hovered = false;
	}
	
	public void onClicked() {
		ignoreInput = true;
	}
	
	@Override
	public void doFills(int x1, int y1, int x2, int y2, int borderPx) {
		Screen.fill(x1, y1, x1 + borderPx, y2, borderColor); //left edge
		Screen.fill(x1, y1, x2, y1 + borderPx, borderColor); //top edge
		Screen.fill(x2 - borderPx, y1, x2, y2, borderColor); //right edge
		Screen.fill(x1, y2 - borderPx, x2, y2, borderColor); //bottom edge
		int valueEdge = getEstimateValueBorder(x1, x2);
		Screen.fill(valueEdge, y1, valueEdge + borderPx, y2, colorToFullAlpha(borderColor));
		//fill value sections
		Screen.fill(x1 + borderPx, y1 + borderPx, valueEdge, y2 - borderPx, getInnerColor());
		Screen.fill(valueEdge + borderPx, y1 + borderPx, x2 - borderPx, y2 - borderPx, getInnerColor2());
	}
	
	@Override
	public void drawTexts(int x1, int y1, int x2, int y2) {
		int valueEdge = getEstimateValueBorder(x1, x2);
		int height = (y2 + y1 - minecraft.fontRenderer.FONT_HEIGHT) / 2;
		parent.drawCenteredString(minecraft.fontRenderer, text, (x1 + valueEdge) / 2, height, 0xfff1f1f1);
		parent.drawCenteredString(minecraft.fontRenderer, value, (x2 + valueEdge) / 2, height, valueColor);
	}
	
	public int getEstimateValueBorder(int x1, int x2) {
		double bias = ((double)text.length()) / (text.length() + value.length());
		if(bias > 0.9) {
			bias = 0.9;
		}
		return (int)(bias * (x2 - x1) + x1);
	}
	
	public int colorToFullAlpha(int color) {
		return (0xff000000 | color);
	}
	
	public int getInnerColor() {
		return hovered ? colorHover : innerColor;
	}
	
	public int getInnerColor2() {
		return hovered ? colorHover2 : innerColor2;
	}
}
