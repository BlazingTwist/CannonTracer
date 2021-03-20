package the_dark_jumper.cannontracer.gui.guielements.interfaces;

public interface IClickableFrame extends IRenderableFrame{
	boolean getIsClicked();
	void setIsClicked(boolean isClicked);
	
	boolean getHovered();
	void setHovered(boolean hovered);
	
	default void mouseOver(int x, int y, int scaledScreenWidth, int scaledScreenHeight, boolean mouseLeftDown, boolean queueLeftUpdate) {
		if(getIsClicked() && !mouseLeftDown) {
			setIsClicked(false);
		}
		int x1 = getPercentValue(scaledScreenWidth, this.getConfig().x);
		int x2 = getPercentValue(scaledScreenWidth, this.getConfig().xEnd);
		int y1 = getPercentValue(scaledScreenHeight, this.getConfig().y);
		int y2 = getPercentValue(scaledScreenHeight, this.getConfig().yEnd);
		if(x >= x1 && x <= x2 && y >= y1 && y <= y2) {
			setHovered(true);
			if(queueLeftUpdate && mouseLeftDown) {
				setIsClicked(true);
			}
		}else if(getHovered()) {
			setHovered(false);
		}
	}
	
	@Override
	default int getInnerColor() {
		return getHovered() ? getColors().colorHover : getColors().innerColor;
	}
	
	default int getInnerColor2() {
		return getHovered() ? getColors().colorHover2 : getColors().innerColor2;
	}
}
