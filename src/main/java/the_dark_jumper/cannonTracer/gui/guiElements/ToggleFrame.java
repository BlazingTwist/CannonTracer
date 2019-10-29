package the_dark_jumper.cannonTracer.gui.guiElements;

import the_dark_jumper.cannonTracer.gui.JumperGUI;
import the_dark_jumper.cannonTracer.gui.JumperGUI.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.TickableFrame;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleToggle;

public class ToggleFrame extends DoubleSegmentFrame implements TickableFrame{
	public final ModuleToggle module;
	public boolean state;
	
	public ToggleFrame(JumperGUI parent, FrameConfig config, FrameColors colors, ModuleToggle module) {
		super(parent, module.name, Boolean.toString(module.state), (module.state ? colors.colorOn : colors.colorOff), config, colors);
		this.module = module;
		this.state = module.state;
	}
	
	@Override
	public void tick(JumperGUI gui) {
		if(this.state != module.state) {
			this.state = module.state;
			super.valueColor = (this.state ? colors.colorOn : colors.colorOff);
			super.value = Boolean.toString(this.state);
		}
	}
	
	@Override
	public void setIsClicked(boolean isClicked) {
		if(isClicked) {
			module.toggle();
		}
	}
}
