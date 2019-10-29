package the_dark_jumper.cannonTracer.gui.guiElements;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGUI;
import the_dark_jumper.cannonTracer.gui.JumperGUI.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.FocusableFrame;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;
import the_dark_jumper.cannonTracer.util.KeyLibrary;

public class ValueFrame<T> extends DoubleSegmentFrame implements FocusableFrame{
	public boolean isFocused = false;
	@Override public boolean getFocused() {return isFocused;}
	@Override public void setFocused(boolean focused) {this.isFocused = focused;}
	
	public final GetterAndSetter<T> source;
	public final Class<T> clazz;
	
	public ValueFrame(JumperGUI parent, FrameConfig config, FrameColors colors, String text, GetterAndSetter<T> source, Class<T> clazz) {
		super(parent, text, source.getter.get().toString(), config, colors);
		this.source = source;
		this.clazz = clazz;
	}
	
	@Override
	public void parseInput() {
		if(this.value.equals("")) {
			this.value = source.getter.get().toString();
		}else {
			try {
				this.value.replace(',', '.');
				if(clazz == Integer.class) {
					source.setter.accept((T)new Integer(this.value));
				}else if(clazz == Float.class) {
					source.setter.accept((T)new Float(this.value));
				}else if(clazz == Double.class) {
					source.setter.accept((T)new Double(this.value));
				}else if(clazz == String.class) {
					source.setter.accept((T)this.value);
				}else if(clazz == Long.class) {
					source.setter.accept((T)new Long(this.value));
				}
			}catch(NumberFormatException e) {
				//user inputted faulty data
				this.value = source.getter.get().toString();
			}
		}
	}
	
	@Override
	public void keyEvent(KeyInputEvent event) {
		if(event.getAction() == GLFW.GLFW_PRESS) {
			if(event.getScanCode() == 1) {
				//escape
				this.value = "";
				onFocusChange(false);
				return;
			}else if(event.getScanCode() == 28 || event.getScanCode() == 284) {
				//either enter button
				onFocusChange(false);
				return;
			}
			if(manageKeyCombinations(event)) {
				return;
			}
			Character c = getKeyValue(Main.getInstance().keyLibrary.getKeyContent(event.getScanCode()));
			if(c == null) {
				return;
			}
			this.value += c;
		}
	}
	
	/*
	 * returns whether a keyCombinations was detected or not
	 * */
	public boolean manageKeyCombinations(KeyInputEvent event) {
		if(Main.getInstance().keyPressListener.pressedKeys.contains(56)) {
			return false;
		}
		if(event.getScanCode() == 29 || event.getScanCode() == 285) {
			if(Main.getInstance().keyPressListener.pressedKeys.contains(30)) {
				//ctrl + a
				return true;
			}
			if(Main.getInstance().keyPressListener.pressedKeys.contains(46)) {
				//ctrl + c
				minecraft.keyboardListener.setClipboardString(this.value);
				return true;
			}
			if(Main.getInstance().keyPressListener.pressedKeys.contains(47)) {
				//ctrl + v
				this.value += minecraft.keyboardListener.getClipboardString();
				return true;
			}
			if(Main.getInstance().keyPressListener.pressedKeys.contains(45)) {
				//ctrl + x
				minecraft.keyboardListener.setClipboardString(this.value);
				this.value = "";
				return true;
			}
		}else if(Main.getInstance().keyPressListener.pressedKeys.contains(29) || Main.getInstance().keyPressListener.pressedKeys.contains(285)) {
			if(event.getScanCode() == 30) {
				//ctrl + a
				return true;
			}else if(event.getScanCode() == 46) {
				//ctrl + c
				minecraft.keyboardListener.setClipboardString(this.value);
				return true;
			}else if(event.getScanCode() == 47) {
				//ctrl + v
				this.value += minecraft.keyboardListener.getClipboardString();
				return true;
			}else if(event.getScanCode() == 45) {
				//ctrl + x
				minecraft.keyboardListener.setClipboardString(this.value);
				this.value = "";
				return true;
			}
		}else if(event.getScanCode() == 14) {
			if(value.length() != 0) {
				//backspace
				this.value = value.substring(0, value.length() - 1);
				return true;
			}
		}
		return false;
	}
	
	public @Nullable Character getKeyValue(KeyLibrary.KeyContent keyContent) {
		char keyValue;
		if(keyContent instanceof KeyLibrary.KeyCharComplex) {
			if(Main.getInstance().keyPressListener.pressedKeys.contains(42) || Main.getInstance().keyPressListener.pressedKeys.contains(54)) {
				keyValue = ((KeyLibrary.KeyCharComplex)keyContent).shifted;
			}else if(Main.getInstance().keyPressListener.pressedKeys.contains(312) || Main.getInstance().keyPressListener.pressedKeys.contains(56)
					&& (Main.getInstance().keyPressListener.pressedKeys.contains(29) || Main.getInstance().keyPressListener.pressedKeys.contains(285))) {
				keyValue = ((KeyLibrary.KeyCharComplex)keyContent).alted;
			}else {
				keyValue = ((KeyLibrary.KeyCharComplex)keyContent).base;
			}
		}else if(keyContent instanceof KeyLibrary.KeyCharNormal) {
			if(Main.getInstance().keyPressListener.pressedKeys.contains(42) || Main.getInstance().keyPressListener.pressedKeys.contains(54)) {
				keyValue = ((KeyLibrary.KeyCharNormal)keyContent).shifted;
			}else {
				keyValue = ((KeyLibrary.KeyCharNormal)keyContent).base;
			}
		}else if(keyContent instanceof KeyLibrary.KeyCharSimple) {
			keyValue = ((KeyLibrary.KeyCharSimple)keyContent).base;
		}else {
			return null;
		}
		return keyValue;
	}	
}
