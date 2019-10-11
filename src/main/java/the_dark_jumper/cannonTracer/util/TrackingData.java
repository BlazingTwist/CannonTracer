package the_dark_jumper.cannonTracer.util;

import java.util.HashMap;
import java.util.function.Consumer;

public class TrackingData {
	private float time;
	private float thickness;
	private int red,green,blue,alpha;
	private boolean render;
	public HashMap<String, Consumer<Object>> setters = new HashMap<>();
	
	public TrackingData(float time, float thickness, int red, int green, int blue, int alpha, boolean render){
		this.time = time;
		this.thickness = thickness;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		this.render = render;
		initSetters();
	}
	
	public TrackingData() {
		initSetters();
	}
	
	public void initSetters() {
		setters.put("time", this::setTime);
		setters.put("thickness", this::setThickness);
		setters.put("red", this::setRed);
		setters.put("green", this::setGreen);
		setters.put("blue", this::setBlue);
		setters.put("alpha", this::setAlpha);
		setters.put("render", this::setRender);
	}
	
	public void setTime(Object o) {
		System.out.println("setTime receiving class: "+o.getClass());
		if(o instanceof Float) {
			time = ((Float)o).floatValue();
		}
	}
	public float getTime() {
		return time;
	}
	public void setThickness(Object o) {
		System.out.println("setThickness receiving class: "+o.getClass());
		if(o instanceof Float) {
			thickness = ((Float)o).floatValue();
		}
	}
	public float getThickness(){
		return thickness;
	}
	public void setRed(Object o) {
		System.out.println("setRed receiving class: "+o.getClass());
		if(o instanceof Integer) {
			red = ((Integer) o).intValue();
		}
	}
	public int getRed() {
		return red;
	}
	public void setGreen(Object o) {
		System.out.println("setGreen receiving class: "+o.getClass());
		if(o instanceof Integer) {
			green = ((Integer) o).intValue();
		}
	}
	public int getGreen() {
		return green;
	}
	public void setBlue(Object o) {
		System.out.println("setBlue receiving class: "+o.getClass());
		if(o instanceof Integer) {
			blue = ((Integer) o).intValue();
		}
	}
	public int getBlue() {
		return blue;
	}
	public void setAlpha(Object o) {
		System.out.println("setAlpha receiving class: "+o.getClass());
		if(o instanceof Integer) {
			alpha = ((Integer) o).intValue();
		}
	}
	public int getAlpha() {
		return alpha;
	}
	public void setRender(Object o) {
		System.out.println("setRender receiving class: "+o.getClass());
		if(o instanceof Boolean) {
			render = ((Boolean)o).booleanValue();
		}
	}
	public boolean getRender() {
		return render;
	}
}