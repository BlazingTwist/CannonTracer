package the_dark_jumper.cannontracer.gui.guielements;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class ScrollbarFrame implements IRenderableFrame, IClickableFrame {
	public final IJumperGUI parent;
	public final Minecraft minecraft;

	public FrameConfig config;

	@Override
	public FrameConfig getConfig() {
		return config;
	}

	@Override
	public void setConfig(FrameConfig config) {
		this.config = config;
	}

	public FrameColors colors;

	@Override
	public FrameColors getColors() {
		return colors;
	}

	public final Consumer<Double> onDragged;

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

	public boolean isVertical = true;
	public double minScrollbarSize = 0.1;
	private double scrollbarSize = 1;
	public double scrollbarPos = 0;

	public void setScrollbarSize(double scrollbarSize) {
		if (scrollbarSize < minScrollbarSize) {
			this.scrollbarSize = minScrollbarSize;
		} else if (scrollbarSize <= 1) {
			this.scrollbarSize = scrollbarSize;
		} else {
			this.scrollbarSize = 1;
		}
	}

	public double getScrollbarSize() {
		return scrollbarSize;
	}

	public ScrollbarFrame(IJumperGUI parent, FrameConfig config, FrameColors colors, @Nullable Consumer<Double> onDragged) {
		this.parent = parent;
		this.minecraft = parent.getMinecraft();
		this.config = config;
		this.colors = colors;
		this.onDragged = onDragged;
	}

	@Override
	public void mouseOver(float x, float y, float scaledScreenWidth, float scaledScreenHeight, boolean mouseLeftDown, boolean queueLeftUpdate) {
		IClickableFrame.super.mouseOver(x, y, scaledScreenWidth, scaledScreenHeight, mouseLeftDown, queueLeftUpdate);
		if (isClicked) {
			double current;
			double relative1;
			double relative2;
			if (isVertical) {
				//do vertical dragging
				current = y;
				relative1 = getPercentValue(scaledScreenHeight, config.y);
				relative2 = getPercentValue(scaledScreenHeight, config.yEnd);
			} else {
				//do horizontal dragging
				current = x;
				relative1 = getPercentValue(scaledScreenWidth, config.x);
				relative2 = getPercentValue(scaledScreenWidth, config.xEnd);
			}
			double scrollAreaStart = relative1 + ((relative2 - relative1) * scrollbarSize / 2);
			double scrollAreaEnd = relative2 - ((relative2 - relative1) * scrollbarSize / 2);
			if (current < scrollAreaStart) {
				doDrag(0);
			} else if (current > scrollAreaEnd) {
				doDrag(1);
			} else {
				doDrag((current - scrollAreaStart) / (scrollAreaEnd - scrollAreaStart));
			}
		}
	}

	public void doDrag(double pos) {
		if (pos == scrollbarPos) {
			return;
		}
		if (onDragged != null) {
			onDragged.accept(pos);
		}
		scrollbarPos = pos;
	}

	@Override
	public void doFills(float x1, float y1, float x2, float y2, float borderPx) {
		int x1i = Math.round(x1);
		int x2i = Math.round(x2);
		int y1i = Math.round(y1);
		int y2i = Math.round(y2);
		int borderPxi = Math.round(borderPx);
		float width = Math.abs(x2i - x1i);
		float height = Math.abs(y2i - y1i);

		Screen.fill(x1i, y1i, x1i + borderPxi, y2i, colors.borderColor); //left edge
		Screen.fill(x1i + borderPxi, y1i, x2i - borderPxi, y1i + borderPxi, colors.borderColor); //top edge
		Screen.fill(x2i - borderPxi, y1i, x2i, y2i, colors.borderColor); //right edge
		Screen.fill(x1i + borderPxi, y2i - borderPxi, x2i - borderPxi, y2i, colors.borderColor); //bottom edge

		int relative1;
		int relative2;
		if (isVertical) {
			relative1 = y1i;
			relative2 = y2i;
		} else {
			relative1 = x1i;
			relative2 = x2i;
		}
		int halfBarSize = (int) ((relative2 - relative1) * scrollbarSize / 2);
		int relativeBarPos = (int) (relative1 + halfBarSize + ((relative2 - relative1 - (2 * halfBarSize)) * scrollbarPos));

		if (isVertical) {
			int xOffset = width <= (2 * borderPxi)
					? (int) Math.ceil((width - (2 * borderPxi)) / 2d)
					: 0;
			Screen.fill(x1i + borderPxi - xOffset, relativeBarPos - halfBarSize + borderPxi, x2i - borderPxi + xOffset, relativeBarPos + halfBarSize - borderPxi, getInnerColor());
		} else {
			int yOffset = height <= (2 * borderPxi)
					? (int) Math.ceil((height - (2 * borderPxi)) / 2d)
					: 0;
			Screen.fill(relativeBarPos - halfBarSize + borderPxi, y1i + borderPxi - yOffset, relativeBarPos + halfBarSize - borderPxi, y2i - borderPxi + yOffset, getInnerColor());
		}
	}
}
