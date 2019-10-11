package the_dark_jumper.cannonTracer.settings;

import the_dark_jumper.cannonTracer.Main;

public class GuiSettings {
	public final Main main;

	public boolean bLog = false;
	
	public GuiSettings(Main main) {
		this.main = main;
	}
	
	public void setBLogModifier(boolean b) {
		this.bLog = b;
	}
}
