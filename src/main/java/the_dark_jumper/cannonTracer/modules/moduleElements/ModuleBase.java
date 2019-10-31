package the_dark_jumper.cannontracer.modules.moduleelements;

public class ModuleBase {
	public String name;
	public int keybind1, keybind2;
	public boolean render;
	public boolean isGlobal;
	
	public ModuleBase(String name, boolean render, boolean isGlobal, int keybind1, int keybind2) {
		this.name = name;
		this.render = render;
		this.isGlobal = isGlobal;
		this.keybind1 = keybind1;
		this.keybind2 = keybind2;
	}
	
	public void updateKeybind(int index, int keybind) {
		if(index == 0) {
			keybind1 = keybind;
		}else if(index == 1) {
			keybind2 = keybind;
		}
	}
	
	public void keyPressed(int key) {}
	
	public void keyReleased(int key) {}
}