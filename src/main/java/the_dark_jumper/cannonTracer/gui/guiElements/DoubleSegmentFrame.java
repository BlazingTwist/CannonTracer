package the_dark_jumper.cannontracer.gui.guielements;

import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class DoubleSegmentFrame extends BasicTextFrame implements IClickableFrame{
	public int valueColor;
	
	public String value;
	public String getValue() {return value;}
	public void setValue(String value) {this.value = value;}
	
	public boolean isClicked = false;
	@Override public boolean getIsClicked() {return isClicked;}
	@Override public void setIsClicked(boolean isClicked) {this.isClicked = isClicked;}
	
	public boolean hovered = false;
	@Override public boolean getHovered() {return hovered;}
	@Override public void setHovered(boolean hovered) {this.hovered = hovered;}
	
	public DoubleSegmentFrame(IJumperGUI parent, String text, String value, int valueColor, FrameConfig config, FrameColors colors) {
		super(parent, text, config, colors);
		init(value, valueColor);
	}
	
	public DoubleSegmentFrame(IJumperGUI parent, String text, String value, FrameConfig config, FrameColors colors) {
		super(parent, text, config, colors);
		init(value, colors.defaultValueColor);
	}
	
	public void init(String value, int valueColor) {
		this.value = value;
		this.valueColor = valueColor;
	}
	
	@Override
	public void doFills(int x1, int y1, int x2, int y2, int borderPx) {
		Screen.fill(x1, y1, x1 + borderPx, y2, colors.borderColor); //left edge
		Screen.fill(x1 + borderPx, y1, x2 - borderPx, y1 + borderPx, colors.borderColor); //top edge
		Screen.fill(x2 - borderPx, y1, x2, y2, colors.borderColor); //right edge
		Screen.fill(x1 + borderPx, y2 - borderPx, x2 - borderPx, y2, colors.borderColor); //bottom edge
		int valueEdge = getEstimateValueBorder(x1, x2);
		Screen.fill(valueEdge, y1, valueEdge + borderPx, y2, colorToFullAlpha(colors.borderColor));
		//fill value sections
		Screen.fill(x1 + borderPx, y1 + borderPx, valueEdge, y2 - borderPx, getInnerColor());
		Screen.fill(valueEdge + borderPx, y1 + borderPx, x2 - borderPx, y2 - borderPx, getInnerColor2());
	}
	
	@Override
	public void drawTexts(int x1, int y1, int x2, int y2) {
		int valueEdge = getEstimateValueBorder(x1, x2);
		int height = (y2 + y1) / 2;
		parent.drawCenteredString(minecraft.fontRenderer, text, (x1 + valueEdge) / 2, height, 0xfff1f1f1);
		parent.drawCenteredString(minecraft.fontRenderer, value, (x2 + valueEdge) / 2, height, valueColor);
	}
	
	public int getEstimateValueBorder(int x1, int x2) {
		double bias = ((double)text.length()) / (text.length() + value.length());
		if(bias > 0.8) {
			bias = 0.8;
		}
		return (int)(bias * (x2 - x1) + x1);
	}
	
	public int colorToFullAlpha(int color) {
		return (0xff000000 | color);
	}
}
