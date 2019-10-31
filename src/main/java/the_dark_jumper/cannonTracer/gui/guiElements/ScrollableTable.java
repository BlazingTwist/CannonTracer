package the_dark_jumper.cannontracer.gui.guielements;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class ScrollableTable implements IRenderableFrame, IClickableFrame, IFocusableFrame{
	public static class FormatData{
		public FormatData(){}
		
		public FormatData(int start, int end){
			this.start = start;
			this.end = end;
		}
		
		public int start;
		public int end;
	}
	
	public final IJumperGUI parent;
	public final Minecraft minecraft;
	
	public FrameConfig config;
	@Override public FrameConfig getConfig() {return config;}
	@Override public void setConfig(FrameConfig config) {this.config = config;}
	
	public FrameColors colors;
	@Override public FrameColors getColors() {return colors;}
	
	@Override public boolean getIsClicked() {return false;}
	@Override public void setIsClicked(boolean isClicked) {}
	
	@Override public boolean getHovered() {return false;}
	@Override public void setHovered(boolean hovered) {}
	
	@Override public boolean getFocused() {return false;}
	@Override public void setFocused(boolean focused) {}
	
	@Override public String getValue() {return "";}
	@Override public void setValue(String value) {}
	
	private double horizontalScrollFactor = 1;
	private ScrollbarFrame horizontalScrollbar = null;
	private double verticalScrollFactor = 1;
	private ScrollbarFrame verticalScrollbar = null;
	private FormatData uniformColFormat = null;
	private FormatData uniformRowFormat = null;
	private ArrayList<FormatData> colFormat = new ArrayList<>();
	private ArrayList<FormatData> rowFormat = new ArrayList<>();
	//private ArrayList<RenderableFrame> headerRow = null;
	//private ArrayList<RenderableFrame> headerCol = null;
	private ArrayList<ArrayList<IRenderableFrame>> rows = new ArrayList<>();
	
	public ScrollableTable(IJumperGUI parent, FrameConfig config, FrameColors colors) {
		this.parent = parent;
		this.minecraft = parent.getMinecraft();
		this.config = config;
		this.colors = colors;
	}
	
	public int matchWidthToHeight(int height) {
		int tableHeight = getPercentValue(Minecraft.getInstance().mainWindow.getScaledHeight() ,config.yEnd - config.y);
		int tableWidth = getPercentValue(Minecraft.getInstance().mainWindow.getScaledWidth(), config.xEnd - config.x);
		return height * tableHeight / tableWidth;
	}
	
	public int matchHeightToWidth(int width) {
		int tableHeight = getPercentValue(Minecraft.getInstance().mainWindow.getScaledHeight() ,config.yEnd - config.y);
		int tableWidth = getPercentValue(Minecraft.getInstance().mainWindow.getScaledWidth(), config.xEnd - config.x);
		return width * tableWidth / tableHeight;
	}
	
	public void generateScrollbars(boolean useHorizontal, int height, boolean useVertical, int width) {
		if(useHorizontal) {
			horizontalScrollbar = new ScrollbarFrame(parent, new FrameConfig().init(0, 100, 100, 100 + height, config.borderThickness), colors, null);
			horizontalScrollbar.isVertical = false;
		}
		if(useVertical) {
			verticalScrollbar = new ScrollbarFrame(parent, new FrameConfig().init(100, 0, 100 + width, 100, config.borderThickness), colors, null);
			verticalScrollbar.isVertical = true;
		}
	}
	
	public int getAmountOfColumns() {
		int amount = 0;
		for(ArrayList<IRenderableFrame> row : rows) {
			if(row.size() > amount) {
				amount = row.size();
			}
		}
		return amount;
	}
	
	/*
	 * Call this whenever you modified the visual size of the table
	 * */
	public void updateScrollbarRanges() {
		if(horizontalScrollbar != null) {
			if(colFormat != null && !colFormat.isEmpty()) {
				horizontalScrollbar.setScrollbarSize(100d / colFormat.get(colFormat.size() - 1).end);
				horizontalScrollFactor = colFormat.get(colFormat.size() - 1).end / 100d;
			}else {
				int diff = uniformColFormat.end - uniformColFormat.start;
				int endX = uniformColFormat.start + (getAmountOfColumns() * diff) - 1;
				horizontalScrollbar.setScrollbarSize(100d / endX);
			}
		}
		if(verticalScrollbar != null) {
			if(rowFormat != null && !rowFormat.isEmpty()) {
				verticalScrollbar.setScrollbarSize(100d / rowFormat.get(rowFormat.size() - 1).end);
				verticalScrollFactor = rowFormat.get(rowFormat.size() - 1).end / 100d;
			}else {
				int diff = uniformRowFormat.end - uniformRowFormat.start;
				int endX = uniformRowFormat.start + (rows.size() * diff) - 1;
				horizontalScrollbar.setScrollbarSize(100d / endX);
			}
		}
	}
	
	public FrameConfig getFormatData(int row, int col) {
		int startX;
		int endX;
		if(uniformColFormat != null) {
			int diff = uniformColFormat.end - uniformColFormat.start;
			endX = uniformColFormat.start + (col * diff) - 1;
			startX = endX - diff + 1;
		}else {
			startX = colFormat.get(col).start;
			endX = colFormat.get(col).end;
		}
		int startY;
		int endY;
		if(uniformRowFormat != null) {
			int diff = uniformRowFormat.end - uniformRowFormat.start;
			endY = uniformRowFormat.start + (row * diff) - 1;
			startY = endY - diff + 1;
		}else {
			startY = rowFormat.get(row).start;
			endY = rowFormat.get(row).end;
		}
		return new FrameConfig().init(startX, startY, endX, endY, config.borderThickness);
	}
	
	public void setUniformRowFormat(boolean isScaled, FormatData format) {
		int tableHeight = config.yEnd - config.y;
		if(!isScaled) {
			format.start = (format.start - config.y) * 100 / tableHeight;
			format.end = (format.end - config.y) * 100 / tableHeight;
		}
		rowFormat = null;
		uniformRowFormat = format;
	}
	
	public void setRowFormat(boolean isScaled, FormatData... formats) {
		if(rowFormat == null) {
			rowFormat = new ArrayList<>();
		}else {
			rowFormat.clear();
		}
		uniformRowFormat = null;
		int tableHeight = config.yEnd - config.y;
		for(int i = 0; i < formats.length; i++) {
			FormatData formatData = formats[i];
			if(!isScaled) {
				formatData.start = (formatData.start - config.y) * 100 / tableHeight;
				formatData.end = (formatData.end - config.y) * 100 / tableHeight;
			}
			rowFormat.add(formatData);
		}
	}
	
	public void setUniformColFormat(boolean isScaled, FormatData format) {
		int tableWidth = config.xEnd - config.x;
		if(!isScaled) {
			format.start = (format.start - config.x) * 100 / tableWidth;
			format.end = (format.end - config.x) * 100 / tableWidth;
		}
		colFormat = null;
		uniformColFormat = format;
	}
	
	public void setColFormat(boolean isScaled, FormatData... formats) {
		if(colFormat == null) {
			colFormat = new ArrayList<>();
		}else {
			colFormat.clear();
		}
		uniformColFormat = null;
		int tableWidth = config.xEnd - config.x;
		for(int i = 0; i < formats.length; i++) {
			FormatData formatData = formats[i];
			if(!isScaled) {
				formatData.start = (formatData.start - config.x) * 100 / tableWidth;
				formatData.end = (formatData.end - config.x) * 100 / tableWidth;
			}
			colFormat.add(formatData);
		}
	}
	
	public void addRow(IRenderableFrame... frames) {
		ArrayList<IRenderableFrame> row = new ArrayList<>();
		for(int colIndex = 0; colIndex < frames.length; colIndex++) {
			if(frames[colIndex] != null) {
				frames[colIndex].setConfig(getFormatData(rows.size(), colIndex));
			}
			row.add(frames[colIndex]);
		}
		rows.add(row);
	}
	
	@Override
	public void mouseOver(int x, int y, int scaledScreenWidth, int scaledScreenHeight, boolean mouseLeftDown, boolean queueLeftUpdate) {
		int relX1 = getPercentValue(scaledScreenWidth, config.x);
		int relX2 = getPercentValue(scaledScreenWidth, config.xEnd);
		int relY1 = getPercentValue(scaledScreenHeight, config.y);
		int relY2 = getPercentValue(scaledScreenHeight, config.yEnd);
		int perceivedScreenWidth = relX2 - relX1;
		int perceivedScreenHeight = relY2 - relY1;
		double scrollOffsetX = horizontalScrollbar.scrollbarPos * (1 - horizontalScrollbar.getScrollbarSize());
		double scrollOffsetY = verticalScrollbar.scrollbarPos * (1 - verticalScrollbar.getScrollbarSize());
		scrollOffsetX *= (perceivedScreenWidth * horizontalScrollFactor);
		scrollOffsetY *= (perceivedScreenHeight * verticalScrollFactor);
		if(x >= relX1 && x <= relX2 && y >= relY1 && y <= relY2) {
			//mouse is hovering over table
			for(int i = 0; i < rows.size(); i++) {
				ArrayList<IRenderableFrame> row = rows.get(i);
				for(int i2 = 0; i2 < row.size(); i2++) {
					IRenderableFrame frame = row.get(i2);
					if(frame instanceof IClickableFrame) {
						((IClickableFrame)frame).mouseOver((int)(x - relX1 + scrollOffsetX), (int)(y - relY1 + scrollOffsetY), perceivedScreenWidth, perceivedScreenHeight, mouseLeftDown, queueLeftUpdate);
					}
				}
			}
		}
		//mouse still could be hovering over scrollbars
		if(horizontalScrollbar != null) {
			horizontalScrollbar.mouseOver(x - relX1, y - relY1, perceivedScreenWidth, perceivedScreenHeight, mouseLeftDown, queueLeftUpdate);
		}
		if(verticalScrollbar != null) {
			verticalScrollbar.mouseOver(x - relX1, y - relY1, perceivedScreenWidth, perceivedScreenHeight, mouseLeftDown, queueLeftUpdate);
		}
	}
	
	@Override
	public void doFills(int x1, int y1, int x2, int y2, int borderPx) {
		Screen.fill(x1, y1, x1 + borderPx, y2, getColors().borderColor); //left edge
		Screen.fill(x1 + borderPx, y1, x2 - borderPx, y1 + borderPx, getColors().borderColor); //top edge
		Screen.fill(x2 - borderPx, y1, x2, y2, getColors().borderColor); //right edge
		Screen.fill(x1 + borderPx, y2 - borderPx, x2 - borderPx, y2, getColors().borderColor); //bottom edge
		//System.out.println("doFills called with: "+x1+" | "+y1+" | "+x2+" | "+y2+" | "+borderPx);
		//System.out.println("Config is: "+getConfig().x+" | "+getConfig().y+" | "+getConfig().xEnd+" | "+getConfig().yEnd);
		int guiScale = Minecraft.getInstance().gameSettings.guiScale;
		if(horizontalScrollbar != null) {
			renderTableFrame(horizontalScrollbar, x1, y1, x2, y2, guiScale, horizontalScrollbar.config, true);
		}
		if(verticalScrollbar != null) {
			renderTableFrame(verticalScrollbar, x1, y1, x2, y2, guiScale, verticalScrollbar.config, true);
		}
		for(int i = 0; i < rows.size(); i++) {
			ArrayList<IRenderableFrame> row = rows.get(i);
			for(int i2 = 0; i2 < row.size(); i2++) {
				IRenderableFrame frame = row.get(i2);
				if(frame != null) {
					renderTableFrame(frame, x1, y1, x2, y2, guiScale, getFormatData(i, i2), false);
				}
			}
		}
	}
	
	public void renderTableFrame(IRenderableFrame frame, int perceivedX1, int perceivedY1, int perceivedX2, int perceivedY2, int guiScale, FrameConfig config, boolean allowOutOfBounds) {
		int width = perceivedX2 - perceivedX1;
		int height = perceivedY2 - perceivedY1;
		int x1 = getPercentValue(width, config.x) + perceivedX1;
		int x2 = getPercentValue(width, config.xEnd) + perceivedX1;
		int y1 = getPercentValue(height, config.y) + perceivedY1;
		int y2 = getPercentValue(height, config.yEnd) + perceivedY1;
		if(allowOutOfBounds) {
			//render without scrollbar shifting
			frame.doFills(x1, y1, x2, y2, config.borderThickness / guiScale);
			frame.drawTexts(x1, y1, x2, y2);
			return;
		}
		double scrollViewStartX = horizontalScrollbar.scrollbarPos * (1 - horizontalScrollbar.getScrollbarSize());
		double scrollViewEndX = scrollViewStartX + horizontalScrollbar.getScrollbarSize();
		double scrollViewStartY = verticalScrollbar.scrollbarPos * (1 - verticalScrollbar.getScrollbarSize());
		double scrollViewEndY = scrollViewStartY + verticalScrollbar.getScrollbarSize();
		scrollViewStartX = (scrollViewStartX * width * horizontalScrollFactor) + perceivedX1;
		scrollViewEndX = (scrollViewEndX * width * horizontalScrollFactor) + perceivedX1;
		scrollViewStartY = (scrollViewStartY * height * verticalScrollFactor) + perceivedY1;
		scrollViewEndY = (scrollViewEndY * height * verticalScrollFactor) + perceivedY1;
		//System.out.println("horizscrollbarpos: "+horizontalScrollbar.scrollbarPos+" | horizscrollbarsize: "+horizontalScrollbar.getScrollbarSize()+" | vertscrollbarpos: "+verticalScrollbar.scrollbarPos+" | vertscrollbarsize: "+verticalScrollbar.getScrollbarSize()+" | width: "+width+" | height: "+height);
		//System.out.println("rendertableframe: "+x1+" | "+scrollViewStartX+" | "+x2+" | "+scrollViewEndX+" | "+y1+" | "+scrollViewStartY+" | "+y2+" | "+scrollViewEndY);
		if(x1 < scrollViewStartX || x2 > scrollViewEndX || y1 < scrollViewStartY || y2 > scrollViewEndY) {
			//frame would be outside of table
			return;
		}
		x1 = (int)(x1 - scrollViewStartX + perceivedX1);
		x2 = (int)(x2 - scrollViewStartX + perceivedX1);
		y1 = (int)(y1 - scrollViewStartY + perceivedY1);
		y2 = (int)(y2 - scrollViewStartY + perceivedY1);
		frame.doFills(x1, y1, x2, y2, config.borderThickness / guiScale);
		frame.drawTexts(x1, y1, x2, y2);
	}
	
	@Override public void parseInput(){}
	
	@Override public void keyEvent(InputEvent.KeyInputEvent event) {
		for(int i = 0; i < rows.size(); i++) {
			ArrayList<IRenderableFrame> row = rows.get(i);
			for(int i2 = 0; i2 < row.size(); i2++) {
				IRenderableFrame frame = row.get(i2);
				if(frame instanceof IFocusableFrame && ((IFocusableFrame)frame).getFocused()) {
					((IFocusableFrame)frame).keyEvent(event);
				}
			}
		}
	}
}
