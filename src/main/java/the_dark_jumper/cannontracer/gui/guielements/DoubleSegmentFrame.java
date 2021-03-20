package the_dark_jumper.cannontracer.gui.guielements;

import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class DoubleSegmentFrame extends BasicTextFrame implements IClickableFrame {
	public int valueColor;

	public String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isClicked = false;

	@Override
	public boolean getIsClicked() {
		return isClicked;
	}

	@Override
	public void setIsClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}

	public boolean hovered = false;

	@Override
	public boolean getHovered() {
		return hovered;
	}

	@Override
	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

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
	public void doFills(float x1, float y1, float x2, float y2, float borderPx) {
		int x1i = Math.round(x1);
		int x2i = Math.round(x2);
		int y1i = Math.round(y1);
		int y2i = Math.round(y2);
		int borderPxi = Math.round(borderPx);

		Screen.fill(x1i, y1i, x1i + borderPxi, y2i, colors.borderColor); //left edge
		Screen.fill(x1i + borderPxi, y1i, x2i - borderPxi, y1i + borderPxi, colors.borderColor); //top edge
		Screen.fill(x2i - borderPxi, y1i, x2i, y2i, colors.borderColor); //right edge
		Screen.fill(x1i + borderPxi, y2i - borderPxi, x2i - borderPxi, y2i, colors.borderColor); //bottom edge
		int valueEdge = Math.round(getEstimateValueBorder(x1i, x2i));
		Screen.fill(valueEdge, y1i, valueEdge + borderPxi, y2i, colorToFullAlpha(colors.borderColor));
		//fill value sections
		Screen.fill(x1i + borderPxi, y1i + borderPxi, valueEdge, y2i - borderPxi, getInnerColor());
		Screen.fill(valueEdge + borderPxi, y1i + borderPxi, x2i - borderPxi, y2i - borderPxi, getInnerColor2());
	}

	@Override
	public void drawTexts(float x1, float y1, float x2, float y2) {
		float valueEdge = getEstimateValueBorder(x1, x2);
		int height = Math.round((y2 + y1) / 2);
		int width1 = Math.round((x1 + valueEdge) / 2);
		int width2 = Math.round((x2 + valueEdge) / 2);
		parent.drawCenteredString(minecraft.fontRenderer, text, width1, height, 0xfff1f1f1);
		parent.drawCenteredString(minecraft.fontRenderer, value, width2, height, valueColor);
	}

	public float getEstimateValueBorder(float x1, float x2) {
		float bias = ((float) text.length()) / (text.length() + value.length());
		if (bias > 0.8f) {
			bias = 0.8f;
		}
		if (bias < 0.2f) {
			bias = 0.2f;
		}
		return (bias * (x2 - x1) + x1);
	}

	public int colorToFullAlpha(int color) {
		return (0xff000000 | color);
	}
}
