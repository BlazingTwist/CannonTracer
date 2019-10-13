package the_dark_jumper.cannonTracer.gui.guiElements.interfaces;

public interface ClickableFrame {
	public void mouseOver(int x, int y, int scaledScreenWidth, int scaledScreenHeight, boolean mouseLeftDown);
	
	public void onHovered();
	
	public void onUnHovered();
	
	public void onClicked();
	
	public int getInnerColor();
	
	public int getInnerColor2();
}
