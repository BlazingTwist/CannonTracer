package the_dark_jumper.cannontracer.modules.moduleelements;

public interface IModule {
	public String getName();
	public boolean getRender();
	public boolean getIsGlobal();
	
	public IModuleBehaviour getBehaviour();
	
	public void onKeyEvent(int keycode);
}
