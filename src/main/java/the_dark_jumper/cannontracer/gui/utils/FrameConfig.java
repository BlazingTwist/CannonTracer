package the_dark_jumper.cannontracer.gui.utils;

public class FrameConfig {
	public float x;
	public float y;
	public float xEnd;
	public float yEnd;
	public float borderThickness;
	
	public FrameConfig init(float x, float y, float xEnd, float yEnd, float borderThickness) {
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
