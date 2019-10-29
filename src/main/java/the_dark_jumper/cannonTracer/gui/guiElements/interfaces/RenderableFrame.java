package the_dark_jumper.cannonTracer.gui.guiElements.interfaces;

import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannonTracer.gui.JumperGUI.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.FrameColors;

public interface RenderableFrame {
	public FrameConfig getConfig();
	
	public FrameColors getColors();
	
	public default void render(int scaledScreenWidth, int scaledScreenHeight, int guiScale) {
		//outer corners
		int x1 = getPercentValue(scaledScreenWidth, getConfig().x);
		int x2 = getPercentValue(scaledScreenWidth, getConfig().xEnd);
		int y1 = getPercentValue(scaledScreenHeight, getConfig().y);
		int y2 = getPercentValue(scaledScreenHeight, getConfig().yEnd);
		doFills(x1, y1, x2, y2, getConfig().borderThickness / guiScale);
	}
	
	public default void doFills(int x1, int y1, int x2, int y2, int borderPx) {
		Screen.fill(x1, y1, x1 + borderPx, y2, getColors().borderColor); //left edge
		Screen.fill(x1 + borderPx, y1, x2 - borderPx, y1 + borderPx, getColors().borderColor); //top edge
		Screen.fill(x2 - borderPx, y1, x2, y2, getColors().borderColor); //right edge
		Screen.fill(x1 + borderPx, y2 - borderPx, x2 - borderPx, y2, getColors().borderColor); //bottom edge
		Screen.fill(x1 + borderPx, y1 + borderPx, x2 - borderPx, y2 - borderPx, getInnerColor());
	}
	
	public default int getInnerColor() {
		return getColors().innerColor;
	}
	
	public default int getPercentValue(int full, int percent) {
		return (full * percent) / 100;
	}
	
	public default void drawTexts(int x1, int y1, int x2, int y2) {}
}
