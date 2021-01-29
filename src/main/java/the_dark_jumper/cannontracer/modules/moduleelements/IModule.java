package the_dark_jumper.cannontracer.modules.moduleelements;

import the_dark_jumper.cannontracer.modules.moduleelements.behaviours.IModuleBehaviour;

public interface IModule {
	public String getName();

	public boolean isRender();

	public boolean isGlobal();

	public IModuleBehaviour getBehaviour();

	public void onKeyEvent(int keycode);
}
