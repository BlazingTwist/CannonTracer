package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Color {
	@JsonProperty("red")
	private int red;

	@JsonProperty("green")
	private int green;

	@JsonProperty("blue")
	private int blue;

	@JsonProperty("alpha")
	private int alpha;

	public Color() {
	}

	public Color(int red, int green, int blue, int alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public int getRed() {
		return red;
	}

	public Color setRed(int red) {
		this.red = red;
		return this;
	}

	public int getGreen() {
		return green;
	}

	public Color setGreen(int green) {
		this.green = green;
		return this;
	}

	public int getBlue() {
		return blue;
	}

	public Color setBlue(int blue) {
		this.blue = blue;
		return this;
	}

	public int getAlpha() {
		return alpha;
	}

	public Color setAlpha(int alpha) {
		this.alpha = alpha;
		return this;
	}
}
