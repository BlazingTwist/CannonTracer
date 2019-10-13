package the_dark_jumper.cannonTracer.gui.guiElements;

import java.util.Optional;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGui;
import the_dark_jumper.cannonTracer.gui.JumperGui.FrameConfig;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.FocusableFrame;
import the_dark_jumper.cannonTracer.gui.guiElements.interfaces.TickableFrame;
import the_dark_jumper.cannonTracer.util.GetterAndSetter;
import the_dark_jumper.cannonTracer.util.KeyLibrary;

public class ValueFrame<T> extends DoubleSegmentFrame implements TickableFrame, FocusableFrame{
	public boolean isFocused = false;
	public final GetterAndSetter<T> source;
	public final Class<T> clazz;
	
	public ValueFrame(Main main, JumperGui parent, FrameConfig config, FrameColors colors, String text, GetterAndSetter<T> source, Class<T> clazz) {
		super(main, parent, text, source.getter.get().toString(), config, colors);
		this.source = source;
		this.clazz = clazz;
	}
	
	@Override
	public void onClicked() {
		ignoreInput = true;
		focused();
	}

	public void focused() {
		isFocused = true;
		this.value = "";
	}

	public void focusLost() {
		isFocused = false;
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
				//use inputted faulty data
				this.value = source.getter.get().toString();
			}
		}
	}

	public boolean getFocused() {
		return isFocused;
	}

	public void keyEvent(KeyInputEvent event) {
		if(event.getAction() == GLFW.GLFW_PRESS) {
			if(event.getScanCode() == 1) {
				//escape
				this.value = "";
				focusLost();
				return;
			}else if(event.getScanCode() == 28 || event.getScanCode() == 284) {
				//either enter button
				focusLost();
				return;
			}
			if(manageKeyCombinations(event)) {
				return;
			}
			Optional<Character> optional = getKeyValue(main.keyLibrary.getKeyContent(event.getScanCode()));
			if(!optional.isPresent()) {
				return;
			}
			this.value += optional.get();
		}
	}
	
	/*
	 * returns whether a keyCombinations was detected or not
	 * */
	public boolean manageKeyCombinations(KeyInputEvent event) {
		if(main.keyPressListener.pressedKeys.contains(56)) {
			return false;
		}
		if(event.getScanCode() == 29 || event.getScanCode() == 285) {
			if(main.keyPressListener.pressedKeys.contains(30)) {
				//ctrl + a
				return true;
			}
			if(main.keyPressListener.pressedKeys.contains(46)) {
				//ctrl + c
				minecraft.keyboardListener.setClipboardString(this.value);
				return true;
			}
			if(main.keyPressListener.pressedKeys.contains(47)) {
				//ctrl + v
				this.value += minecraft.keyboardListener.getClipboardString();
				return true;
			}
			if(main.keyPressListener.pressedKeys.contains(45)) {
				//ctrl + x
				minecraft.keyboardListener.setClipboardString(this.value);
				this.value = "";
				return true;
			}
		}else if(main.keyPressListener.pressedKeys.contains(29) || main.keyPressListener.pressedKeys.contains(285)) {
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
	
	public Optional<Character> getKeyValue(KeyLibrary.KeyContent keyContent) {
		char keyValue;
		if(keyContent instanceof KeyLibrary.KeyCharComplex) {
			if(main.keyPressListener.pressedKeys.contains(42) || main.keyPressListener.pressedKeys.contains(54)) {
				keyValue = ((KeyLibrary.KeyCharComplex)keyContent).shifted;
			}else if(main.keyPressListener.pressedKeys.contains(312) || main.keyPressListener.pressedKeys.contains(56)
					&& (main.keyPressListener.pressedKeys.contains(29) || main.keyPressListener.pressedKeys.contains(285))) {
				keyValue = ((KeyLibrary.KeyCharComplex)keyContent).alted;
			}else {
				keyValue = ((KeyLibrary.KeyCharComplex)keyContent).base;
			}
		}else if(keyContent instanceof KeyLibrary.KeyCharNormal) {
			if(main.keyPressListener.pressedKeys.contains(42) || main.keyPressListener.pressedKeys.contains(54)) {
				keyValue = ((KeyLibrary.KeyCharNormal)keyContent).shifted;
			}else {
				keyValue = ((KeyLibrary.KeyCharNormal)keyContent).base;
			}
		}else if(keyContent instanceof KeyLibrary.KeyCharSimple) {
			keyValue = ((KeyLibrary.KeyCharSimple)keyContent).base;
		}else {
			return Optional.empty();
		}
		return Optional.of(keyValue);
	}

	public void tick(JumperGui gui) {
		if(isFocused && !this.ignoreInput && gui.getLeftDown() && !this.hovered
				&& (!main.keyPressListener.pressedKeys.contains(42) && !main.keyPressListener.pressedKeys.contains(54))) {
			focusLost();
		}		
	}
	
}
