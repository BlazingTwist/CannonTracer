package the_dark_jumper.cannontracer.configsaving;

import com.fasterxml.jackson.annotation.JsonProperty;
import the_dark_jumper.cannontracer.util.GetterAndSetter;

public class GuiConfig {
	@JsonProperty("xOffset")
	private GetterAndSetter<Double> xOffset = new GetterAndSetter<>(0d);

	@JsonProperty("yOffset")
	private GetterAndSetter<Double> yOffset = new GetterAndSetter<>(0d);

	@JsonProperty("fontHeight")
	private GetterAndSetter<Double> fontHeight = new GetterAndSetter<>(1d);

	public GuiConfig() {
	}

	public GuiConfig(double xOffset, double yOffset, double fontHeight) {
		this.xOffset.set(xOffset);
		this.yOffset.set(yOffset);
		this.fontHeight.set(fontHeight);
	}

	public GetterAndSetter<Double> getXOffsetGNS() {
		return xOffset;
	}

	public double getXOffset() {
		return xOffset.get();
	}

	public GuiConfig setXOffset(double xOffset) {
		this.xOffset.set(xOffset);
		return this;
	}

	public GetterAndSetter<Double> getYOffsetGNS() {
		return yOffset;
	}

	public double getYOffset() {
		return yOffset.get();
	}

	public GuiConfig setYOffset(double yOffset) {
		this.yOffset.set(yOffset);
		return this;
	}

	public GetterAndSetter<Double> getFontHeightGNS(){
		return fontHeight;
	}

	public double getFontHeight() {
		return fontHeight.get();
	}

	public GuiConfig setFontHeight(double fontHeight) {
		this.fontHeight.set(fontHeight);
		return this;
	}
}
