package the_dark_jumper.cannontracer.tracking;

import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class TrackingData {
	private float time;
	public GetterAndSetter<Float> timeGNS;
	private float thickness;
	public GetterAndSetter<Float> thicknessGNS;
	private int red,green,blue,alpha;
	public GetterAndSetter<Integer> redGNS, greenGNS, blueGNS, alphaGNS;
	private boolean render;
	public GetterAndSetter<Boolean> renderGNS;

	//public LinkedHashMap<String, GetterAndSetter<Object>> gettersAndSetters = new LinkedHashMap<>();
	
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
		timeGNS = new GetterAndSetter<Float>(this::getTime, this::setTime);
		thicknessGNS = new GetterAndSetter<Float>(this::getThickness, this::setThickness);
		redGNS = new GetterAndSetter<Integer>(this::getRed, this::setRed);
		greenGNS = new GetterAndSetter<Integer>(this::getGreen, this::setGreen);
		blueGNS = new GetterAndSetter<Integer>(this::getBlue, this::setBlue);
		alphaGNS = new GetterAndSetter<Integer>(this::getAlpha, this::setAlpha);
		renderGNS = new GetterAndSetter<Boolean>(this::getRender, this::setRender);
	}
	
	public void setTime(float o) {
		time = o;
	}
	public float getTime() {
		return time;
	}
	public void setThickness(float o) {
		thickness = o;
	}
	public float getThickness(){
		return thickness;
	}
	public void setRed(int o) {
		red = o;
	}
	public int getRed() {
		return red;
	}
	public void setGreen(int o) {
		green = o;
	}
	public int getGreen() {
		return green;
	}
	public void setBlue(int o) {
		blue = o;
	}
	public int getBlue() {
		return blue;
	}
	public void setAlpha(int o) {
		alpha = o;
	}
	public int getAlpha() {
		return alpha;
	}
	public void setRender(boolean o) {
		render = o;
	}
	public boolean getRender() {
		return render;
	}
}