package the_dark_jumper.cannontracer.gui.utils;

public class FrameColors {
	public int borderColor = 0xff444444;
	public int innerColor = 0xff4a9bce;
	public int innerColor2 = 0xff3e82ad;
	public int colorOn = 0xff00ff00;
	public int colorOff = 0xffff0000;
	public int colorHover = 0xff4a88ce;
	public int colorHover2 = 0xff3e72ad;
	public int defaultValueColor = 0xfff1f1f1;

	public FrameColors duplicate() {
		FrameColors duplicate = new FrameColors();
		duplicate.borderColor = borderColor;
		duplicate.innerColor = innerColor;
		duplicate.innerColor2 = innerColor2;
		duplicate.colorOn = colorOn;
		duplicate.colorOff = colorOff;
		duplicate.colorHover = colorHover;
		duplicate.colorHover2 = colorHover2;
		duplicate.defaultValueColor = defaultValueColor;
		return duplicate;
	}
}
