package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrackingDataEntry {
	@JsonProperty("render")
	private boolean render = true;

	@JsonProperty("time")
	private float time = 10;

	@JsonProperty("thickness")
	private float thickness = 3;

	@JsonProperty("color")
	private Color color = new Color(0, 0, 0, 255);

	@JsonProperty("hitBoxRadius")
	private double hitBoxRadius = 0.5;

	public TrackingDataEntry() {
	}

	public TrackingDataEntry(boolean render, float time, float thickness, Color color) {
		this.render = render;
		this.time = time;
		this.thickness = thickness;
		this.color = color;
	}

	public boolean isRender() {
		return render;
	}

	public TrackingDataEntry setRender(boolean render) {
		this.render = render;
		return this;
	}

	public float getTime() {
		return time;
	}

	public TrackingDataEntry setTime(float time) {
		this.time = time;
		return this;
	}

	public float getThickness() {
		return thickness;
	}

	public TrackingDataEntry setThickness(float thickness) {
		this.thickness = thickness;
		return this;
	}

	public Color getColor() {
		return color;
	}

	public TrackingDataEntry setColor(Color color) {
		this.color = color;
		return this;
	}

	public double getHitBoxRadius() {
		return hitBoxRadius;
	}

	public TrackingDataEntry setHitBoxRadius(double hitBoxRadius) {
		this.hitBoxRadius = hitBoxRadius;
		return this;
	}
}
