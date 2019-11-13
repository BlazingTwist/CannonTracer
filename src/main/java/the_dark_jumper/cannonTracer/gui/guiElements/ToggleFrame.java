package the_dark_jumper.cannontracer.gui.guielements;

import the_dark_jumper.cannontracer.gui.IJumperGUI;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.ITickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;
import the_dark_jumper.cannontracer.modules.moduleelements.ToggleBehaviour;

public class ToggleFrame extends DoubleSegmentFrame implements ITickableFrame{
	public final ToggleBehaviour module;
	public boolean state;
	
	public ToggleFrame(IJumperGUI parent, FrameConfig config, FrameColors colors, ToggleBehaviour module) {
		super(parent, module.getParent().getName(), Boolean.toString(module.state), (module.state ? colors.colorOn : colors.colorOff), config, colors);
		this.module = module;
		this.state = module.state;
	}
	
	@Override
	public void tick(IJumperGUI gui) {
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
