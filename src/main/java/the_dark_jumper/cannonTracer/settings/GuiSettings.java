package the_dark_jumper.cannonTracer.settings;

import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;

public class GuiSettings {
	public final Main main;

	public boolean bLog = false;
	public GetterAndSetter<Boolean> bLogGNS;
	
	public GuiSettings(Main main) {
		this.main = main;
		setupAccessors();
	}
	
	public void setupAccessors() {
		bLogGNS = new GetterAndSetter<Boolean>(this::getBLog, this::setBLog);
	}
	
	public boolean getBLog() {
		return this.bLog;
	}
	public void setBLog(boolean b) {
		this.bLog = b;
	}
}
