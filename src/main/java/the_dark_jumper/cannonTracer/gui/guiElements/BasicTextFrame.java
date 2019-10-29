package the_dark_jumper.cannonTracer.gui.guiElements;

import net.minecraft.client.Minecraft;
import the_dark_jumper.cannonTracer.gui.JumperGUI;
import the_dark_jumper.cannonTracer.gui.JumperGUI.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.RenderableFrame;

public class BasicTextFrame implements RenderableFrame{
	public final JumperGUI parent;
	public final Minecraft minecraft;
	public String text;
	
	public FrameConfig config;
	@Override public FrameConfig getConfig() {return config;}
	
	public FrameColors colors;
	@Override public FrameColors getColors() {return colors;}
	
	//all values are percentages of the full screen
	public BasicTextFrame(JumperGUI parent, String text, FrameConfig config, FrameColors colors) {
		this.parent = parent;
		this.minecraft = parent.getMinecraft();
		this.text = text;
		this.config = config;
		this.colors = colors;
	}
	
	@Override
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
	
	public void drawTexts(int x1, int y1, int x2, int y2) {
		int height = (y2 + y1 - minecraft.fontRenderer.FONT_HEIGHT) / 2;
		parent.drawCenteredString(minecraft.fontRenderer, text, (x2 + x1) / 2, height, 0xfff1f1f1);
	}
}
