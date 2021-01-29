package the_dark_jumper.cannontracer.gui.utils;

public class FrameConfig {
	public int x;
	public int y;
	public int xEnd;
	public int yEnd;
	public int borderThickness;
	
	public FrameConfig init(int x, int y, int xEnd, int yEnd, int borderThickness) {
		this.x = x;
		this.y = y;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		this.borderThickness = borderThickness;
		return this;
	}
	
	public FrameConfig init(FrameConfig other) {
		this.x = other.x;
		this.y = other.y;
		this.xEnd = other.xEnd;
		this.yEnd = other.yEnd;
		this.borderThickness = other.borderThickness;
		return this;
	}
	
	public FrameConfig duplicate() {
		FrameConfig temp = new FrameConfig();
		temp.init(this);
		return temp;
	}
}
