package the_dark_jumper.cannonTracer.listeners;

import java.util.Set;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_dark_jumper.cannonTracer.Main;
import the_dark_jumper.cannonTracer.gui.JumperGUI;

public class KeyPressListener {
	public final Main main;
	public Set<Integer> pressedKeys = Sets.<Integer>newHashSet();
	
	public KeyPressListener(Main main) {
		this.main = main;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void keyPressed(InputEvent.KeyInputEvent event) {
		if(event.getAction() == GLFW.GLFW_REPEAT) {
			return;
		}
		if(Minecraft.getInstance().world == null) {
			//prevents custom keybinds from firing when the player is not on a server (includes singleplayer)
			//clear pressed keys to prevent misinterpretations
			pressedKeys.clear();
			return;
		}
		if(Minecraft.getInstance().currentScreen != null && !(Minecraft.getInstance().currentScreen instanceof JumperGUI)) {
			//ignores keypresses when a screen is active
			//prevents custom keybinds from firing when chat / anvil-text / inventory search / etc. is receing data
			//clear pressed keys to prevent misinterpretations
			pressedKeys.clear();
			return;
		}
		if(event.getAction() == GLFW.GLFW_PRESS) {
			pressedKeys.add(event.getScanCode());
			main.moduleManager.keyPressed(event.getScanCode(), Minecraft.getInstance().currentScreen != null);
		}else if(event.getAction() == GLFW.GLFW_RELEASE) {
			pressedKeys.remove(event.getScanCode());
			main.moduleManager.keyReleased(event.getScanCode());
		}
		main.guiManager.keyEvent(event);
	}
	
	@SubscribeEvent
	public void mousePressed(InputEvent.MouseInputEvent event) {
		if(event.getAction() == GLFW.GLFW_REPEAT) {
			return;
		}
		if(Minecraft.getInstance().world == null) {
			main.guiManager.mousePressEvent(false);
			return;
		}
		if(Minecraft.getInstance().currentScreen != null && !(Minecraft.getInstance().currentScreen instanceof JumperGUI)) {
			main.guiManager.mousePressEvent(false);
			return;
		}
		if(event.getAction() == GLFW.GLFW_PRESS) {
			if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_1) {
				main.guiManager.mousePressEvent(true);
			}
		}else if(event.getAction() == GLFW.GLFW_RELEASE) {
			if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_1) {
				main.guiManager.mousePressEvent(false);
			}
		}
	}
}
