package the_dark_jumper.cannonTracer.gui.guiElements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGui;
import the_dark_jumper.cannonTracer.gui.JumperGui.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.RenderableFrame;

public class BasicTextFrame implements RenderableFrame{
	public final Main main;
	public final JumperGui parent;
	public final Minecraft minecraft;
	public String text;
	public FrameConfig config;
	public FrameColors colors;
	
	//all values are percentages of the full screen
	public BasicTextFrame(Main main, JumperGui parent, String text, FrameConfig config, FrameColors colors) {
		this.main = main;
		this.parent = parent;
		this.minecraft = parent.getMinecraft();
		init(text, config, colors);
	}
	
	public void init(String text, FrameConfig frameConfig, FrameColors colors) {
		this.text = text;
		this.config = frameConfig;
		this.colors = colors;
	}
	
	public void render(int scaledScreenWidth, int scaledScreenHeight, int guiScale) {
		//outer corners
		int x1 = getPercentValue(scaledScreenWidth, this.config.x);
		int x2 = getPercentValue(scaledScreenWidth, this.config.xEnd);
		int y1 = getPercentValue(scaledScreenHeight, this.config.y);
		int y2 = getPercentValue(scaledScreenHeight, this.config.yEnd);
		doFills(x1, y1, x2, y2, config.borderThickness / guiScale);
		if(!text.equals("")) {
			drawTexts(x1, y1, x2, y2);
		}
	}
	
	public void doFills(int x1, int y1, int x2, int y2, int borderPx) {
		Screen.fill(x1, y1, x1 + borderPx, y2, colors.borderColor); //left edge
		Screen.fill(x1 + borderPx, y1, x2 - borderPx, y1 + borderPx, colors.borderColor); //top edge
		Screen.fill(x2 - borderPx, y1, x2, y2, colors.borderColor); //right edge
		Screen.fill(x1 + borderPx, y2 - borderPx, x2 - borderPx, y2, colors.borderColor); //bottom edge
		Screen.fill(x1 + borderPx, y1 + borderPx, x2 - borderPx, y2 - borderPx, colors.innerColor);
	}
	
	public void drawTexts(int x1, int y1, int x2, int y2) {
		int height = (y2 + y1 - minecraft.fontRenderer.FONT_HEIGHT) / 2;
		parent.drawCenteredString(minecraft.fontRenderer, text, (x2 + x1) / 2, height, 0xfff1f1f1);
	}
	
	public int getPercentValue(int full, int percent) {
		return (int)((full * percent) / 100d);
	}
}
