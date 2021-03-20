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
	
	public float matchWidthToHeight(int height) {
		float tableHeight = getPercentValue(Minecraft.getInstance().getMainWindow().getScaledHeight() ,config.yEnd - config.y);
		//int tableHeight = getPercentValue(Minecraft.getInstance().func_228018_at_().getScaledHeight() ,config.yEnd - config.y);
		//int tableHeight = getPercentValue(Minecraft.getInstance().mainWindow.getScaledHeight() ,config.yEnd - config.y);

		float tableWidth = getPercentValue(Minecraft.getInstance().getMainWindow().getScaledWidth(), config.xEnd - config.x);
		//int tableWidth = getPercentValue(Minecraft.getInstance().func_228018_at_().getScaledWidth(), config.xEnd - config.x);
		//int tableWidth = getPercentValue(Minecraft.getInstance().mainWindow.getScaledWidth(), config.xEnd - config.x);

		return height * tableHeight / tableWidth;
	}
	
	public float matchHeightToWidth(int width) {
		float tableHeight = getPercentValue(Minecraft.getInstance().getMainWindow().getScaledHeight() ,config.yEnd - config.y);
		//int tableHeight = getPercentValue(Minecraft.getInstance().func_228018_at_().getScaledHeight() ,config.yEnd - config.y);
		//int tableHeight = getPercentValue(Minecraft.getInstance().mainWindow.getScaledHeight() ,config.yEnd - config.y);

		float tableWidth = getPercentValue(Minecraft.getInstance().getMainWindow().getScaledWidth(), config.xEnd - config.x);
		//int tableWidth = getPercentValue(Minecraft.getInstance().func_228018_at_().getScaledWidth(), config.xEnd - config.x);
		//int tableWidth = getPercentValue(Minecraft.getInstance().mainWindow.getScaledWidth(), config.xEnd - config.x);

		return width * tableWidth / tableHeight;
	}
	
	public void generateScrollbars(boolean useHorizontal, float height, boolean useVertical, float width) {
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
		float endX = startX + getColFormat(col).width;
		
		int startY = 0;
		for(int r = 0; r < row; r++) {
			FormatData format = getRowFormat(r);
			startY += (format.width + format.offset);
		}
		float endY = startY + getRowFormat(row).width;
		
		return new FrameConfig().init(startX, startY, endX, endY, this.config.borderThickness);
	}
	
	public void clearUniformRowFormat() {
		uniformRowFormat = null;
	}
	
	public void setUniformRowFormat(boolean isRelativeToTable, float height, float offset) {
		if(!isRelativeToTable) {
			float tableHeight = config.yEnd - config.y;
			height = height * 100f / tableHeight;
			offset = offset * 100f / tableHeight;
		}
		uniformRowFormat = new FormatData(height, offset);
	}
	
	public void clearNonUniformRowFormat() {
		if(rowFormat != null) {
			rowFormat.clear();
		}
		rowFormat = null;
	}
	
	public void setRowFormat(boolean isRelativeToTable, FormatData... formats) {
		if(rowFormat == null) {
			rowFormat = new ArrayList<>();
		}else {
			rowFormat.clear();
		}
		float tableHeight = config.yEnd - config.y;
		for(int i = 0; i < formats.length; i++) {
			FormatData formatData = formats[i];
			if(formatData != null) {
				if(!isRelativeToTable) {
					formatData.width = formatData.width * 100f / tableHeight;
					formatData.offset = formatData.offset * 100f / tableHeight;
				}
			}
			rowFormat.add(formatData);
		}
	}
	
	public void setRowFormat(int index, boolean isRelativeToTable, FormatData format) {
		if(rowFormat == null) {
			rowFormat = new ArrayList<>();
		}
		if(rowFormat.size() <= index) {
			for(int i = rowFormat.size(); i <= index; i++) {
				rowFormat.add(null);
			}
		}
		if(!isRelativeToTable) {
			float tableHeight = config.yEnd - config.y;
			format.width = format.width * 100f / tableHeight;
			format.offset = format.offset * 100f / tableHeight;
		}
		rowFormat.set(index, format);
	}
	
	public void clearUniformColFormat() {
		uniformColFormat = null;
	}
	
	public void setUniformColFormat(boolean isRelativeToTable, float width, float offset) {
		if(!isRelativeToTable) {
			float tableWidth = config.xEnd - config.x;
			width = width * 100f / tableWidth;
			offset = offset * 100f / tableWidth;
		}
		uniformColFormat = new FormatData(width, offset);
	}
	
	public void clearNonUniformColFormat() {
		if(colFormat != null) {
			colFormat.clear();
		}
		colFormat = null;
	}
	
	public void setColFormat(boolean isRelativeToTable, FormatData... formats) {
		if(colFormat == null) {
			colFormat = new ArrayList<>();
		}else {
			colFormat.clear();
		}
		float tableWidth = config.xEnd - config.x;
		for(int i = 0; i < formats.length; i++) {
			FormatData formatData = formats[i];
			if(formatData != null) {
				if(!isRelativeToTable) {
					formatData.width = formatData.width * 100f / tableWidth;
					formatData.offset = formatData.offset * 100f / tableWidth;
				}
			}
			colFormat.add(formatData);
		}
	}
	
	public void setColFormat(int index, boolean isRelativeToTable, FormatData format) {
		if(colFormat == null) {
			colFormat = new ArrayList<>();
		}
		if(colFormat.size() <= index) {
			for(int i = colFormat.size(); i <= index; i++) {
				colFormat.add(null);
			}
		}
		if(!isRelativeToTable) {
			float tableWidth = config.xEnd - config.x;
			format.width = format.width * 100f / tableWidth;
			format.offset = format.offset * 100f / tableWidth;
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
	public void mouseOver(float x, float y, float scaledScreenWidth, float scaledScreenHeight, boolean mouseLeftDown, boolean queueLeftUpdate) {
		float relX1 = getPercentValue(scaledScreenWidth, config.x);
		float relX2 = getPercentValue(scaledScreenWidth, config.xEnd);
		float relY1 = getPercentValue(scaledScreenHeight, config.y);
		float relY2 = getPercentValue(scaledScreenHeight, config.yEnd);
		float perceivedScreenWidth = relX2 - relX1;
		float perceivedScreenHeight = relY2 - relY1;
		
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
	public void doFills(float x1, float y1, float x2, float y2, float borderPx) {
		int x1i = Math.round(x1);
		int x2i = Math.round(x2);
		int y1i = Math.round(y1);
		int y2i = Math.round(y2);
		int borderPxi = Math.round(borderPx);

		Screen.fill(x1i, y1i, x1i + borderPxi, y2i, getColors().borderColor); //left edge
		Screen.fill(x1i + borderPxi, y1i, x2i - borderPxi, y1i + borderPxi, getColors().borderColor); //top edge
		Screen.fill(x2i - borderPxi, y1i, x2i, y2i, getColors().borderColor); //right edge
		Screen.fill(x1i + borderPxi, y2i - borderPxi, x2i - borderPxi, y2i, getColors().borderColor); //bottom edge
		//System.out.println("doFills called with: "+x1+" | "+y1+" | "+x2+" | "+y2+" | "+borderPx);
		//System.out.println("Config is: "+getConfig().x+" | "+getConfig().y+" | "+getConfig().xEnd+" | "+getConfig().yEnd);
		int guiScale = Minecraft.getInstance().gameSettings.guiScale;
		if(horizontalScrollbar != null) {
			renderTableFrame(horizontalScrollbar, x1i, y1i, x2i, y2i, guiScale, horizontalScrollbar.config, true);
		}
		if(verticalScrollbar != null) {
			renderTableFrame(verticalScrollbar, x1i, y1i, x2i, y2i, guiScale, verticalScrollbar.config, true);
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
	
	public void renderTableFrame(IRenderableFrame frame, float perceivedX1, float perceivedY1, float perceivedX2, float perceivedY2, int guiScale, FrameConfig frameConfig, boolean allowOutOfBounds) {
		float width = perceivedX2 - perceivedX1;
		float height = perceivedY2 - perceivedY1;
		float x1 = getPercentValue(width, frameConfig.x) + perceivedX1;
		float x2 = getPercentValue(width, frameConfig.xEnd) + perceivedX1;
		float y1 = getPercentValue(height, frameConfig.y) + perceivedY1;
		float y2 = getPercentValue(height, frameConfig.yEnd) + perceivedY1;
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
