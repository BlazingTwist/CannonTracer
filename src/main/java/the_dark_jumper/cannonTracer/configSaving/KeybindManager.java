package the_dark_jumper.cannonTracer.configSaving;

import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import the_dark_jumper.cannonTracer.modules.moduleElements.ModuleBase;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;

public class KeybindManager {
	public final Main main;
	
	public LinkedHashMap<String, KeybindAccessors> variablesSP = new LinkedHashMap<>();
	public LinkedHashMap<String, KeybindAccessors> variablesMP = new LinkedHashMap<>();
	
	public class KeybindAccessors{
		public GetterAndSetter accessors[] = new GetterAndSetter[2];
		
		KeybindAccessors(GetterAndSetter accessor1, GetterAndSetter accessor2) {
			this.accessors[0] = accessor1;
			this.accessors[1] = accessor2;
		}
		
		KeybindAccessors(Supplier<Integer> getter1, Consumer<Integer> setter1, Supplier<Integer> getter2, Consumer<Integer> setter2){
			this.accessors[0] = new GetterAndSetter(getter1, setter1);
			this.accessors[1] = new GetterAndSetter(getter2, setter2);
		}
	}
	
	//singleplayer binds
	public int tracerBind1=20, tracerBind2=42;
	public int xRayTraces1=45, xRayTraces2=42;
	public int menu1=46, menu2=42;
	public int lastSecond1=19, lastSecond2=42;
	public int rendBox1=48, rendBox2=42;
	public int prevTick=331, nextTick=333;
	
	public KeybindManager(Main main) {
		this.main = main;
		variablesSP.put("TracerSP", new KeybindAccessors(this::getTracerBindSP1, this::setTracerBindSP1,
				this::getTracerBindSP2, this::setTracerBindSP2));
		variablesSP.put("XRaySP", new KeybindAccessors(this::getXRayBindSP1, this::setXRayBindSP1,
				this::getXRayBindSP2, this::setXRayBindSP2));
		variablesSP.put("MenuSP", new KeybindAccessors(this::getMenuBindSP1, this::setMenuBindSP1,
				this::getMenuBindSP2, this::setMenuBindSP2));
		variablesSP.put("LastSecSP", new KeybindAccessors(this::getLastSecondBindSP1, this::setLastSecondBindSP1,
				this::getLastSecondBindSP2, this::setLastSecondBindSP2));
		variablesSP.put("RendBoxSP", new KeybindAccessors(this::getRendBoxBindSP1, this::setRendBoxBindSP1,
				this::getRendBoxBindSP2, this::setRendBoxBindSP2));
		variablesSP.put("TickSP", new KeybindAccessors(this::getPrevTickBindSP, this::setPrevTickBindSP,
				this::getNextTickBindSP, this::setNextTickBindSP));
	}
	
	public void setModuleKeybind(String name, int index, int keycode) {
		for(ModuleBase module : main.moduleManager.activeModules) {
			if(module.name.equals(name)) {
				module.updateKeybind(index, keycode);
			}
		}
	}
	
	public int getTracerBindSP1() {
		System.out.println("getting tracer bind SP 1, value: "+tracerBind1);
		return tracerBind1;
	}
	public void setTracerBindSP1(int keycode) {
		System.out.println("setting tracer bind SP 1, value: "+keycode);
		tracerBind1=keycode;
		setModuleKeybind("Tracer Mode", 0, keycode);
	}	
	public int getTracerBindSP2() {
		System.out.println("getting tracer bind SP 2, value: "+tracerBind2);
		return tracerBind2;
	}
	public void setTracerBindSP2(int keycode) {
		System.out.println("setting tracer bind SP 2, value: "+keycode);
		tracerBind2=keycode;
		setModuleKeybind("Tracer Mode", 1, keycode);
	}
	public int getXRayBindSP1() {
		System.out.println("getting xRay bind SP 1, value: "+xRayTraces1);
		return xRayTraces1;
	}
	public void setXRayBindSP1(int keycode) {
		System.out.println("setting xRay bind SP 1, value: "+keycode);
		xRayTraces1=keycode;
		setModuleKeybind("XRay Traces", 0, keycode);
	}	
	public int getXRayBindSP2() {
		System.out.println("getting xRay bind SP 2, value: "+xRayTraces2);
		return xRayTraces2;
	}
	public void setXRayBindSP2(int keycode) {
		System.out.println("setting xRay bind SP 2, value: "+keycode);
		xRayTraces2=keycode;
		setModuleKeybind("XRay Traces", 1, keycode);
	}
	public int getMenuBindSP1() {
		System.out.println("getting menu bind SP 1, value: "+menu1);
		return menu1;
	}
	public void setMenuBindSP1(int keycode) {
		System.out.println("setting menu bind SP 1, value: "+keycode);
		menu1=keycode;
		setModuleKeybind("Menu", 0, keycode);
	}
	public int getMenuBindSP2() {
		System.out.println("getting menu bind SP 2, value: "+menu2);
		return menu2;
	}
	public void setMenuBindSP2(int keycode) {
		System.out.println("setting menu bind SP 2, value: "+keycode);
		menu2=keycode;
		setModuleKeybind("Menu", 1, keycode);
	}
	public int getLastSecondBindSP1() {
		System.out.println("getting lastsecond bind SP 1, value: "+lastSecond1);
		return lastSecond1;
	}
	public void setLastSecondBindSP1(int keycode) {
		System.out.println("setting lastsecond bind SP 1, value: "+keycode);
		lastSecond1=keycode;
		setModuleKeybind("Last Second", 0, keycode);
	}
	public int getLastSecondBindSP2() {
		System.out.println("getting lastsecond bind SP 2, value: "+lastSecond2);
		return lastSecond2;
	}
	public void setLastSecondBindSP2(int keycode) {
		System.out.println("setting lastsecond bind SP 2, value: "+keycode);
		lastSecond2=keycode;
		setModuleKeybind("Last Second", 1, keycode);
	}
	public int getRendBoxBindSP1() {
		System.out.println("getting rendbox bind SP 1, value: "+rendBox1);
		return rendBox1;
	}
	public void setRendBoxBindSP1(int keycode) {
		System.out.println("setting rendbox bind SP 1, value: "+keycode);
		rendBox1=keycode;
		setModuleKeybind("Render Boxes", 0, keycode);
	}
	public int getRendBoxBindSP2() {
		System.out.println("getting rendbox bind SP 2, value: "+rendBox2);
		return rendBox2;
	}
	public void setRendBoxBindSP2(int keycode) {
		System.out.println("setting rendbox bind SP 2, value: "+keycode);
		rendBox2=keycode;
		setModuleKeybind("Render Boxes", 1, keycode);
	}
	public int getNextTickBindSP() {
		System.out.println("getting nexttick bind SP, value: "+nextTick);
		return nextTick;
	}
	public void setNextTickBindSP(int keycode) {
		System.out.println("setting nexttick bind SP, value: "+keycode);
		nextTick=keycode;
		setModuleKeybind("Display Tick", 0, keycode);
	}
	public int getPrevTickBindSP() {
		System.out.println("getting prevTick bind SP, value: "+prevTick);
		return prevTick;
	}
	public void setPrevTickBindSP(int keycode) {
		System.out.println("setting prevtick bind SP, value: "+keycode);
		prevTick=keycode;
		setModuleKeybind("Display Tick", 1, keycode);
	}	
}