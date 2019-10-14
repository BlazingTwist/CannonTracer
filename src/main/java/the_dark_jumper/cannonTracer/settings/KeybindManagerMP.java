package the_dark_jumper.cannonTracer.settings;

import java.util.LinkedHashMap;

import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleBase;
import the_dark_jumper.cannonTracer.util.KeybindAccessors;

public class KeybindManagerMP {
	public final Main main;
	
	public LinkedHashMap<String, KeybindAccessors> variables = new LinkedHashMap<>();
	
	//multiplayer binds
	public int xRayTraces1=45, xRayTraces2=42;
	public int menu1=46, menu2=42;
	public int pullData1=19, pullData2=42;
	public int rendBox1=48, rendBox2=42;
	public int prevTick=331, nextTick=333;
	
	public KeybindManagerMP(Main main) {
		this.main = main;
		variables.put("XRayMP", new KeybindAccessors(this::getXRayBind1, this::setXRayBind1,
				this::getXRayBind2, this::setXRayBind2));
		variables.put("MenuMP", new KeybindAccessors(this::getMenuBind1, this::setMenuBind1,
				this::getMenuBind2, this::setMenuBind2));
		variables.put("pullDataMP", new KeybindAccessors(this::getLastSecondBind1, this::setLastSecondBind1,
				this::getLastSecondBind2, this::setLastSecondBind2));
		variables.put("RendBoxMP", new KeybindAccessors(this::getRendBoxBind1, this::setRendBoxBind1,
				this::getRendBoxBind2, this::setRendBoxBind2));
		variables.put("TickMP", new KeybindAccessors(this::getPrevTickBind, this::setPrevTickBind,
				this::getNextTickBind, this::setNextTickBind));
	}
	
	public void setModuleKeybind(String name, int index, int keycode) {
		for(ModuleBase module : main.moduleManager.activeModules) {
			if(module.name.equals(name)) {
				module.updateKeybind(index, keycode);
			}
		}
	}
	
	public int getXRayBind1() {
		System.out.println("getting xRay bind SP 1, value: "+xRayTraces1);
		return xRayTraces1;
	}
	public void setXRayBind1(int keycode) {
		System.out.println("setting xRay bind SP 1, value: "+keycode);
		xRayTraces1=keycode;
		setModuleKeybind("XRay Traces", 0, keycode);
	}	
	public int getXRayBind2() {
		System.out.println("getting xRay bind SP 2, value: "+xRayTraces2);
		return xRayTraces2;
	}
	public void setXRayBind2(int keycode) {
		System.out.println("setting xRay bind SP 2, value: "+keycode);
		xRayTraces2=keycode;
		setModuleKeybind("XRay Traces", 1, keycode);
	}
	public int getMenuBind1() {
		System.out.println("getting menu bind SP 1, value: "+menu1);
		return menu1;
	}
	public void setMenuBind1(int keycode) {
		System.out.println("setting menu bind SP 1, value: "+keycode);
		menu1=keycode;
		setModuleKeybind("Menu", 0, keycode);
	}
	public int getMenuBind2() {
		System.out.println("getting menu bind SP 2, value: "+menu2);
		return menu2;
	}
	public void setMenuBind2(int keycode) {
		System.out.println("setting menu bind SP 2, value: "+keycode);
		menu2=keycode;
		setModuleKeybind("Menu", 1, keycode);
	}
	public int getLastSecondBind1() {
		System.out.println("getting lastsecond bind SP 1, value: "+pullData1);
		return pullData1;
	}
	public void setLastSecondBind1(int keycode) {
		System.out.println("setting lastsecond bind SP 1, value: "+keycode);
		pullData1=keycode;
		setModuleKeybind("Last Second", 0, keycode);
	}
	public int getLastSecondBind2() {
		System.out.println("getting lastsecond bind SP 2, value: "+pullData2);
		return pullData2;
	}
	public void setLastSecondBind2(int keycode) {
		System.out.println("setting lastsecond bind SP 2, value: "+keycode);
		pullData2=keycode;
		setModuleKeybind("Last Second", 1, keycode);
	}
	public int getRendBoxBind1() {
		System.out.println("getting rendbox bind SP 1, value: "+rendBox1);
		return rendBox1;
	}
	public void setRendBoxBind1(int keycode) {
		System.out.println("setting rendbox bind SP 1, value: "+keycode);
		rendBox1=keycode;
		setModuleKeybind("Render Boxes", 0, keycode);
	}
	public int getRendBoxBind2() {
		System.out.println("getting rendbox bind SP 2, value: "+rendBox2);
		return rendBox2;
	}
	public void setRendBoxBind2(int keycode) {
		System.out.println("setting rendbox bind SP 2, value: "+keycode);
		rendBox2=keycode;
		setModuleKeybind("Render Boxes", 1, keycode);
	}
	public int getNextTickBind() {
		System.out.println("getting nexttick bind SP, value: "+nextTick);
		return nextTick;
	}
	public void setNextTickBind(int keycode) {
		System.out.println("setting nexttick bind SP, value: "+keycode);
		nextTick=keycode;
		setModuleKeybind("Display Tick", 0, keycode);
	}
	public int getPrevTickBind() {
		System.out.println("getting prevTick bind SP, value: "+prevTick);
		return prevTick;
	}
	public void setPrevTickBind(int keycode) {
		System.out.println("setting prevtick bind SP, value: "+keycode);
		prevTick=keycode;
		setModuleKeybind("Display Tick", 1, keycode);
	}	
}