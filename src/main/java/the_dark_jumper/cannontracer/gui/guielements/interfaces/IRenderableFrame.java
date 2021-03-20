package the_dark_jumper.cannontracer.gui.guielements.interfaces;

import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public interface IRenderableFrame {
	FrameConfig getConfig();
	void setConfig(FrameConfig config);
	
	FrameColors getColors();
	
	default void render(int scaledScreenWidth, int scaledScreenHeight, int guiScale) {
		//outer corners
		float x1 = getPercentValue(scaledScreenWidth, getConfig().x);
		float x2 = getPercentValue(scaledScreenWidth, getConfig().xEnd);
		float y1 = getPercentValue(scaledScreenHeight, getConfig().y);
		float y2 = getPercentValue(scaledScreenHeight, getConfig().yEnd);
		doFills(x1, y1, x2, y2, getConfig().borderThickness / guiScale);
	}
	
	default void doFills(float x1, float y1, float x2, float y2, float borderPx) {
		int x1i = Math.round(x1);
		int x2i = Math.round(x2);
		int y1i = Math.round(y1);
		int y2i = Math.round(y2);
		int borderPxi = Math.round(borderPx);
		Screen.fill(x1i, y1i, x1i + borderPxi, y2i, getColors().borderColor); //left edge
		Screen.fill(x1i + borderPxi, y1i, x2i - borderPxi, y1i + borderPxi, getColors().borderColor); //top edge
		Screen.fill(x2i - borderPxi, y1i, x2i, y2i, getColors().borderColor); //right edge
		Screen.fill(x1i + borderPxi, y2i - borderPxi, x2i - borderPxi, y2i, getColors().borderColor); //bottom edge
		Screen.fill(x1i + borderPxi, y1i + borderPxi, x2i - borderPxi, y2i - borderPxi, getInnerColor());
	}
	
	default int getInnerColor() {
		return getColors().innerColor;
	}
	
	default float getPercentValue(float full, float percent) {
		return (full * percent) / 100;
	}
	
	default void drawTexts(float x1, float y1, float x2, float y2) {}
}
