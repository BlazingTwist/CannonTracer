package the_dark_jumper.cannontracer.gui.guielements;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.InputEvent;
import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IKeyEventRepeaterFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.utils.FormatData;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;

public class ScrollableTable implements IRenderableFrame, IClickableFrame, IKeyEventRepeaterFrame{	
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
	
	private double horizontalScrollFactor = 1;
	private ScrollbarFrame horizontalScrollbar = null;
	private double verticalScrollFactor = 1;
	private ScrollbarFrame verticalScrollbar = null;
	private FormatData uniformColFormat = null;
	private FormatData uniformRowFormat = null;
	private ArrayList<FormatData> colFormat = null;
	private ArrayList<FormatData> rowFormat = null;
	
	private ArrayList<ArrayList<IRenderableFrame>> rows = new ArrayList<>();
	public ArrayList<ArrayList<IRenderableFrame>> getRows(){return this.rows;}
	
	public ScrollableTable(IJumperGUI parent, FrameConfig config, FrameColors colors) {
		this.parent = parent;
		this.minecraft = parent.getMinecraft();
		this.config = config;
		this.colors = colors;
	}
	
	public int matchWidthToHeight(int height) {
		int tableHeight = getPercentValue(Minecraft.getInstance().getMainWindow().getScaledHeight() ,config.yEnd - config.y);
		//int tableHeight = getPercentValue(Minecraft.getInstance().func_228018_at_().getScaledHeight() ,config.yEnd - config.y);
		//int tableHeight = getPercentValue(Minecraft.getInstance().mainWindow.getScaledHeight() ,config.yEnd - config.y);

		int tableWidth = getPercentValue(Minecraft.getInstance().getMainWindow().getScaledWidth(), config.xEnd - config.x);
		//int tableWidth = getPercentValue(Minecraft.getInstance().func_228018_at_().getScaledWidth(), config.xEnd - config.x);
		//int tableWidth = getPercentValue(Minecraft.getInstance().mainWindow.getScaledWidth(), config.xEnd - config.x);

		return height * tableHeight / tableWidth;
	}
	
	public int matchHeightToWidth(int width) {
		int tableHeight = getPercentValue(Minecraft.getInstance().getMainWindow().getScaledHeight() ,config.yEnd - config.y);
		//int tableHeight = getPercentValue(Minecraft.getInstance().func_228018_at_().getScaledHeight() ,config.yEnd - config.y);
		//int tableHeight = getPercentValue(Minecraft.getInstance().mainWindow.getScaledHeight() ,config.yEnd - config.y);

		int tableWidth = getPercentValue(Minecraft.getInstance().getMainWindow().getScaledWidth(), config.xEnd - config.x);
		//int tableWidth = getPercentValue(Minecraft.getInstance().func_228018_at_().getScaledWidth(), config.xEnd - config.x);
		//int tableWidth = getPercentValue(Minecraft.getInstance().mainWindow.getScaledWidth(), config.xEnd - config.x);

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
		if(horizontalScrollbar == null && verticalScrollbar == null){
			//nothing to do here;
			return;
		}
		if(rows.size() < 1 || getAmountOfColumns() < 1) {
			return;
		}
		FrameConfig lastConfig = getCellConfig(getAmountOfColumns() - 1, rows.size() - 1);
		if(horizontalScrollbar != null) {
			horizontalScrollbar.setScrollbarSize(100d / lastConfig.xEnd);
			horizontalScrollFactor = lastConfig.xEnd / 100d;
		}
		if(verticalScrollbar != null) {
			verticalScrollbar.setScrollbarSize(100d / lastConfig.yEnd);
			verticalScrollFactor = lastConfig.yEnd / 100d;
		}
	}
	
	private FormatData getColFormat(int col) {
		if(colFormat != null && colFormat.size() > col && colFormat.get(col) != null){
			//user defined format for this column
			return colFormat.get(col);
		}else if(uniformColFormat != null) {
			//user did not define format for this column, however it can be substituted with uniform formats
			return uniformColFormat;
		}else {
			System.out.println("couldn't determine colFormat");
			return null;
		}
	}
	
	private FormatData getRowFormat(int row) {
		if(rowFormat != null && rowFormat.size() > row && rowFormat.get(row) != null){
			//user defined format for this row
			return rowFormat.get(row);
		}else if(uniformRowFormat != null) {
			//user did not define format for this row, however it can be substituted with uniform formats
			return uniformRowFormat;
		}else {
			System.out.println("couldn't determine rowFormat");
			return null;
		}
	}
	
	public FrameConfig getCellConfig(int col, int row) {
		int startX = 0;
		for(int c = 0; c < col; c++) {
			FormatData format = getColFormat(c);
			startX += (format.width + format.offset);
		}
		int endX = startX + getColFormat(col).width;
		
		int startY = 0;
		for(int r = 0; r < row; r++) {
			FormatData format = getRowFormat(r);
			startY += (format.width + format.offset);
		}
		int endY = startY + getRowFormat(row).width;
		
		return new FrameConfig().init(startX, startY, endX, endY, this.config.borderThickness);
	}
	
	public void clearUniformRowFormat() {
		uniformRowFormat = null;
	}
	
	public void setUniformRowFormat(boolean isScaled, int height, int offset) {
		if(!isScaled) {
			int tableHeight = config.yEnd - config.y;
			height = (int)(height * 100d / tableHeight);
			offset = (int)(offset * 100d / tableHeight);
		}
		uniformRowFormat = new FormatData(height, offset);
	}
	
	public void clearNonUniformRowFormat() {
		if(rowFormat != null) {
			rowFormat.clear();
		}
		rowFormat = null;
	}
	
	public void setRowFormat(boolean isScaled, FormatData... formats) {
		if(rowFormat == null) {
			rowFormat = new ArrayList<>();
		}else {
			rowFormat.clear();
		}
		int tableHeight = config.yEnd - config.y;
		for(int i = 0; i < formats.length; i++) {
			FormatData formatData = formats[i];
			if(formatData != null) {
				if(!isScaled) {
					formatData.width = (int)(formatData.width * 100d / tableHeight);
					formatData.offset = (int)(formatData.offset * 100d / tableHeight);
				}
			}
			rowFormat.add(formatData);
		}
	}
	
	public void setRowFormat(int index, boolean isScaled, FormatData format) {
		if(rowFormat == null) {
			rowFormat = new ArrayList<>();
		}
		if(rowFormat.size() <= index) {
			for(int i = rowFormat.size(); i <= index; i++) {
				rowFormat.add(null);
			}
		}
		if(!isScaled) {
			int tableHeight = config.yEnd - config.y;
			format.width = (int)(format.width * 100d / tableHeight);
			format.offset = (int)(format.offset * 100d / tableHeight);
		}
		rowFormat.set(index, format);
	}
	
	public void clearUniformColFormat() {
		uniformColFormat = null;
	}
	
	public void setUniformColFormat(boolean isScaled, int width, int offset) {
		if(!isScaled) {
			int tableWidth = config.xEnd - config.x;
			width = (int)(width * 100d / tableWidth);
			offset = (int)(offset * 100d / tableWidth);
		}
		uniformColFormat = new FormatData(width, offset);
	}
	
	public void clearNonUniformColFormat() {
		if(colFormat != null) {
			colFormat.clear();
		}
		colFormat = null;
	}
	
	public void setColFormat(boolean isScaled, FormatData... formats) {
		if(colFormat == null) {
			colFormat = new ArrayList<>();
		}else {
			colFormat.clear();
		}
		int tableWidth = config.xEnd - config.x;
		for(int i = 0; i < formats.length; i++) {
			FormatData formatData = formats[i];
			if(formatData != null) {
				if(!isScaled) {
					formatData.width = (int)(formatData.width * 100d / tableWidth);
					formatData.offset = (int)(formatData.offset * 100d / tableWidth);
				}
			}
			colFormat.add(formatData);
		}
	}
	
	public void setColFormat(int index, boolean isScaled, FormatData format) {
		if(colFormat == null) {
			colFormat = new ArrayList<>();
		}
		if(colFormat.size() <= index) {
			for(int i = colFormat.size(); i <= index; i++) {
				colFormat.add(null);
			}
		}
		if(!isScaled) {
			int tableWidth = config.xEnd - config.x;
			format.width = (int)(format.width * 100d / tableWidth);
			format.offset = (int)(format.offset * 100d / tableWidth);
		}
		colFormat.set(index, format);
	}
	
	public void addRow(IRenderableFrame... frames) {
		ArrayList<IRenderableFrame> row = new ArrayList<>();
		for(IRenderableFrame frame : frames) {
			row.add(frame);
		}
		generateConfigsForRow(rows.size(), row);
		rows.add(row);
	}
	
	public void addRow(ArrayList<IRenderableFrame> frames) {
		generateConfigsForRow(rows.size(), frames);
		rows.add(frames);
	}
	
	public void setRow(int rowIndex, ArrayList<IRenderableFrame> frames) {
		generateConfigsForRow(rowIndex, frames);
		rows.set(rowIndex, frames);
	}
	
	public void generateConfigsForRow(int rowIndex, ArrayList<IRenderableFrame> frames) {
		for(int colIndex = 0; colIndex < frames.size(); colIndex++) {
			if(frames.get(colIndex) != null) {
				frames.get(colIndex).setConfig(getCellConfig(colIndex, rowIndex));
			}
		}
	}
	
	public void deleteRow(int rowIndex) {
		if(rowIndex >= rows.size()) {
			return;
		}
		rows.remove(rowIndex);
		for(int rIndex = rowIndex; rIndex < rows.size(); rIndex++) {
			ArrayList<IRenderableFrame> row = rows.get(rIndex);
			for(int cIndex = 0; cIndex < row.size(); cIndex++) {
				IRenderableFrame frame = row.get(cIndex);
				if(frame != null) {
					frame.setConfig(getCellConfig(cIndex, rIndex));
				}
			}
		}
	}
	
	@Override
	public void mouseOver(int x, int y, int scaledScreenWidth, int scaledScreenHeight, boolean mouseLeftDown, boolean queueLeftUpdate) {
		int relX1 = getPercentValue(scaledScreenWidth, config.x);
		int relX2 = getPercentValue(scaledScreenWidth, config.xEnd);
		int relY1 = getPercentValue(scaledScreenHeight, config.y);
		int relY2 = getPercentValue(scaledScreenHeight, config.yEnd);
		int perceivedScreenWidth = relX2 - relX1;
		int perceivedScreenHeight = relY2 - relY1;
		
		double scrollOffsetX = 0;
		if(horizontalScrollbar != null) {
			scrollOffsetX = horizontalScrollbar.scrollbarPos * (1 - horizontalScrollbar.getScrollbarSize());
			scrollOffsetX *= (perceivedScreenWidth * horizontalScrollFactor);
		}
		
		double scrollOffsetY = 0;
		if(verticalScrollbar != null) {
			scrollOffsetY = verticalScrollbar.scrollbarPos * (1 - verticalScrollbar.getScrollbarSize());
			scrollOffsetY *= (perceivedScreenHeight * verticalScrollFactor);
		}
		
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
					renderTableFrame(frame, x1, y1, x2, y2, guiScale, getCellConfig(i2, i), false);
				}
			}
		}
	}
	
	public void renderTableFrame(IRenderableFrame frame, int perceivedX1, int perceivedY1, int perceivedX2, int perceivedY2, int guiScale, FrameConfig frameConfig, boolean allowOutOfBounds) {
		int width = perceivedX2 - perceivedX1;
		int height = perceivedY2 - perceivedY1;
		int x1 = getPercentValue(width, frameConfig.x) + perceivedX1;
		int x2 = getPercentValue(width, frameConfig.xEnd) + perceivedX1;
		int y1 = getPercentValue(height, frameConfig.y) + perceivedY1;
		int y2 = getPercentValue(height, frameConfig.yEnd) + perceivedY1;
		if(allowOutOfBounds) {
			//render without scrollbar shifting
			frame.doFills(x1, y1, x2, y2, frameConfig.borderThickness / guiScale);
			frame.drawTexts(x1, y1, x2, y2);
			return;
		}
		double scrollViewStartX = perceivedX1;
		double scrollViewEndX = perceivedX2;
		double scrollViewStartY = perceivedY1;
		double scrollViewEndY = perceivedY2;
		if(horizontalScrollbar != null) {
			double factor = width * horizontalScrollFactor;
			double scrollpos = horizontalScrollbar.scrollbarPos * (1 - horizontalScrollbar.getScrollbarSize());
			scrollViewStartX = perceivedX1 + (factor * scrollpos);
			scrollViewEndX = perceivedX1 + (factor * (scrollpos + horizontalScrollbar.getScrollbarSize()));
		}
		if(verticalScrollbar != null) {
			double factor = height * verticalScrollFactor;
			double scrollpos = verticalScrollbar.scrollbarPos * (1 - verticalScrollbar.getScrollbarSize());
			scrollViewStartY = perceivedY1 + (factor * scrollpos);
			scrollViewEndY = perceivedY1 + (factor * (scrollpos + verticalScrollbar.getScrollbarSize()));
		}
		
		/*double scrollViewStartX = horizontalScrollbar.scrollbarPos * (1 - horizontalScrollbar.getScrollbarSize());
		double scrollViewEndX = scrollViewStartX + horizontalScrollbar.getScrollbarSize();
		double scrollViewStartY = verticalScrollbar.scrollbarPos * (1 - verticalScrollbar.getScrollbarSize());
		double scrollViewEndY = scrollViewStartY + verticalScrollbar.getScrollbarSize();
		scrollViewStartX = (scrollViewStartX * width * horizontalScrollFactor) + perceivedX1;
		scrollViewEndX = (scrollViewEndX * width * horizontalScrollFactor) + perceivedX1;
		scrollViewStartY = (scrollViewStartY * height * verticalScrollFactor) + perceivedY1;
		scrollViewEndY = (scrollViewEndY * height * verticalScrollFactor) + perceivedY1;*/
		if(x1 < scrollViewStartX || x2 > scrollViewEndX || y1 < scrollViewStartY || y2 > scrollViewEndY) {
			//frame would be outside of table
			//System.out.println("horizscrollbarpos: "+horizontalScrollbar.scrollbarPos+" | horizscrollbarsize: "+horizontalScrollbar.getScrollbarSize()+" | horizscrollfactor: "+horizontalScrollFactor+" | vertscrollbarpos: "+verticalScrollbar.scrollbarPos+" | vertscrollbarsize: "+verticalScrollbar.getScrollbarSize()+" | vertscrollfactor: "+verticalScrollFactor+" | width: "+width+" | height: "+height);
			//System.out.println("rendertableframe: "+x1+" | "+scrollViewStartX+" | "+x2+" | "+scrollViewEndX+" | "+y1+" | "+scrollViewStartY+" | "+y2+" | "+scrollViewEndY);
			return;
		}
		x1 = (int)(x1 - scrollViewStartX + perceivedX1);
		x2 = (int)(x2 - scrollViewStartX + perceivedX1);
		y1 = (int)(y1 - scrollViewStartY + perceivedY1);
		y2 = (int)(y2 - scrollViewStartY + perceivedY1);
		frame.doFills(x1, y1, x2, y2, frameConfig.borderThickness / guiScale);
		frame.drawTexts(x1, y1, x2, y2);
	}
	
	@Override public void keyEvent(InputEvent.KeyInputEvent event) {
		for(int i = 0; i < rows.size(); i++) {
			ArrayList<IRenderableFrame> row = rows.get(i);
			for(int i2 = 0; i2 < row.size(); i2++) {
				IRenderableFrame frame = row.get(i2);
				if(frame instanceof IFocusableFrame && ((IFocusableFrame)frame).getFocused()) {
					System.out.println("sending keyevent to "+frame.getClass());
					((IFocusableFrame)frame).keyEvent(event);
				}
			}
		}
	}
}
