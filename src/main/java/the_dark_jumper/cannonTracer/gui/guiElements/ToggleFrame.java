package the_dark_jumper.cannonTracer.gui.guiElements;

import net.minecraft.client.gui.screen.Screen;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGui;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleToggle;

public class ToggleFrame extends DoubleSegmentFrame implements Tickable{
	public final ModuleToggle module;
	public boolean state;
	public int colorOn;
	public int colorOff;
	
	public ToggleFrame(Main main, Screen parent, int x, int y, int xEnd, int yEnd, int borderThickness, int innerColor,
			int innerColor2, int borderColor, int colorOn, int colorOff, int colorHover, int colorHover2,
			ModuleToggle module) {
		super(main, parent, module.name, Boolean.toString(module.state), x, y, xEnd, yEnd, borderThickness, innerColor, innerColor2, borderColor, (module.state ? colorOn : colorOff), colorHover, colorHover2);
		this.module = module;
		init(module.state, colorOn, colorOff);
	}
	
	public void init(boolean state, int colorOn, int colorOff) {
		this.state = state;
		this.colorOn = colorOn;
		this.colorOff = colorOff;
	}
	
	public void tick(JumperGui gui) {
		if(this.state != module.state) {
			this.state = module.state;
			super.valueColor = (this.state ? colorOn : colorOff);
			super.value = Boolean.toString(this.state);
		}
	}
	
	@Override
	public void onClicked() {
		module.toggle();
	}
}
