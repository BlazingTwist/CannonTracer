package the_dark_jumper.cannonTracer.gui.guiElements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannonTracer.Main;

public class BasicTextFrame implements RenderableFrame{
	public final Main main;
	public final Screen parent;
	public final Minecraft minecraft;
	public String text;
	public int x, y, xEnd, yEnd, borderThickness, borderColor, innerColor;
	
	//all values are percentages of the full screen
	public BasicTextFrame(Main main, Screen parent, String text, int x, int y, int xEnd, int yEnd, int borderThickness,
			int innerColor, int borderColor) {
		this.main = main;
		this.parent = parent;
		this.minecraft = parent.getMinecraft();
		init(text, x, y, xEnd, yEnd, borderThickness, innerColor, borderColor);
	}
	
	public void init(String text, int x, int y, int xEnd, int yEnd, int borderThickness, int innerColor, int borderColor) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		this.borderThickness = borderThickness;
		this.innerColor = innerColor;
		this.borderColor = borderColor;
	}
	
	public void render(int scaledScreenWidth, int scaledScreenHeight, int guiScale) {
		//outer corners
		int x1 = getPercentValue(scaledScreenWidth, this.x);
		int x2 = getPercentValue(scaledScreenWidth, this.xEnd);
		int y1 = getPercentValue(scaledScreenHeight, this.y);
		int y2 = getPercentValue(scaledScreenHeight, this.yEnd);
		doFills(x1, y1, x2, y2, borderThickness / guiScale);
		drawTexts(x1, y1, x2, y2);
	}
	
	public void doFills(int x1, int y1, int x2, int y2, int borderPx) {
		Screen.fill(x1, y1, x1 + borderPx, y2, borderColor); //left edge
		Screen.fill(x1, y1, x2, y1 + borderPx, borderColor); //top edge
		Screen.fill(x2 - borderPx, y1, x2, y2, borderColor); //right edge
		Screen.fill(x1, y2 - borderPx, x2, y2, borderColor); //bottom edge
		Screen.fill(x1 + borderPx, y1 + borderPx, x2 - borderPx, y2 - borderPx, innerColor);
	}
	
	public void drawTexts(int x1, int y1, int x2, int y2) {
		int height = (y2 + y1 - minecraft.fontRenderer.FONT_HEIGHT) / 2;
		parent.drawCenteredString(minecraft.fontRenderer, text, (x2 + x1) / 2, height, 0xfff1f1f1);
	}
	
	public int getPercentValue(int full, int percent) {
		return (int)((full * percent) / 100d);
	}
}
