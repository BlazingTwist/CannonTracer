package the_dark_jumper.cannonTracer.gui.guiElements;

import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGui;
import the_dark_jumper.cannonTracer.gui.JumperGui.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.TickableFrame;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleToggle;

public class ToggleFrame extends DoubleSegmentFrame implements TickableFrame{
	public final ModuleToggle module;
	public boolean state;
	
	public ToggleFrame(Main main, JumperGui parent, FrameConfig config, FrameColors colors, ModuleToggle module) {
		super(main, parent, module.name, Boolean.toString(module.state), (module.state ? colors.colorOn : colors.colorOff), config, colors);
		this.module = module;
		init(module.state);
	}
	
	public void init(boolean state) {
		this.state = state;
	}
	
	public void tick(JumperGui gui) {
		if(this.state != module.state) {
			this.state = module.state;
			super.valueColor = (this.state ? colors.colorOn : colors.colorOff);
			super.value = Boolean.toString(this.state);
		}
	}
	
	@Override
	public void onClicked() {
		ignoreInput = true;
		module.toggle();
	}
}
