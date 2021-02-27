package the_dark_jumper.cannontracer.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import jumpercommons.GetterAndSetter;
import jumpercommons.TestCannonCharge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.opengl.GL11;
import the_dark_jumper.cannontracer.Main;
import the_dark_jumper.cannontracer.gui.guielements.BasicTextFrame;
import the_dark_jumper.cannontracer.gui.guielements.ButtonFrame;
import the_dark_jumper.cannontracer.gui.guielements.ValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IKeyEventRepeaterFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.ITickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;
import the_dark_jumper.cannontracer.util.ChatUtils;

public class TestCannonGUI extends Screen implements IJumperGUI {
	private static class IntIncrementer {
		private final ValueFrame<Integer> valueFrame;
		private final GetterAndSetter<Integer> getterAndSetter;
		private final Integer offset;

		private IntIncrementer(ValueFrame<Integer> valueFrame, GetterAndSetter<Integer> getterAndSetter, Integer offset) {
			this.valueFrame = valueFrame;
			this.getterAndSetter = getterAndSetter;
			this.offset = offset;
		}

		public void onIncrement(boolean pressed) {
			if (pressed) {
				getterAndSetter.set(getterAndSetter.get() + offset);
				valueFrame.update();
			}
		}

		public void onDecrement(boolean pressed) {
			if (pressed) {
				getterAndSetter.set(getterAndSetter.get() - offset);
				valueFrame.update();
			}
		}
	}

	private static class DoubleIncrementer {
		private final ValueFrame<Double> valueFrame;
		private final GetterAndSetter<Double> getterAndSetter;
		private final Double offset;

		private DoubleIncrementer(ValueFrame<Double> valueFrame, GetterAndSetter<Double> getterAndSetter, Double offset){
			this.valueFrame = valueFrame;
			this.getterAndSetter = getterAndSetter;
			this.offset = offset;
		}

		public void onIncrement(boolean pressed) {
			if (pressed) {
				getterAndSetter.set(getterAndSetter.get() + offset);
				valueFrame.update();
			}
		}

		public void onDecrement(boolean pressed) {
			if (pressed) {
				getterAndSetter.set(getterAndSetter.get() - offset);
				valueFrame.update();
			}
		}
	}

	public final GuiManager guiManager;
	private final ObjectMapper objectMapper;
	public ArrayList<IRenderableFrame> guiComponents = new ArrayList<>();

	boolean detectedAutoGUI = false;
	public boolean leftDown = false;
	public boolean queueLeftUpdate = false;
	private boolean shouldClose = false;

	private TestCannonCharge testCannonCharge = new TestCannonCharge();

	public TestCannonGUI(GuiManager guiManager) {
		super(null);
		this.minecraft = Minecraft.getInstance();
		this.guiManager = guiManager;
		this.objectMapper = new ObjectMapper()
				.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES,
						MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS,
						MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
				.disable(MapperFeature.AUTO_DETECT_CREATORS,
						MapperFeature.AUTO_DETECT_FIELDS,
						MapperFeature.AUTO_DETECT_GETTERS,
						MapperFeature.AUTO_DETECT_IS_GETTERS);
	}

	public void closeButtonPressed(boolean isPressed) {
		if (isPressed) {
			shouldClose = true;
		}
	}

	public void generateScreenComponents() {
		guiComponents.clear();
		FrameConfig config = new FrameConfig();

		FrameColors colors = new FrameColors();
		FrameColors backGroundColors = new FrameColors();
		FrameColors headerColors = new FrameColors();
		headerColors.innerColor = 0x778f8f8f;
		headerColors.borderColor = 0xff7f7f7f;

		//alpha outliner
		config.init(5, 5, 95, 95, 8);
		backGroundColors.innerColor = backGroundColors.borderColor = 0x55000000;
		guiComponents.add(new BasicTextFrame(this, "", config.duplicate(), backGroundColors));

		//headline
		config.init(6, 10, 59, 14, 8);
		guiComponents.add(new BasicTextFrame(this, "Cannon Tester", config.duplicate(), colors));
		config.init(90, 10, 94, 14, 8);
		guiComponents.add(new ButtonFrame(this, "X", config.duplicate(), colors, this::closeButtonPressed));

		config.init(8, 20, 22, 24, 8);
		guiComponents.add(new BasicTextFrame(this, "General", config.duplicate(), headerColors));
		config.init(10, 25, 19, 29, 8);
		createIntegerIncrementerFrame(config, colors, "delay", testCannonCharge.getDelayGNS(), 1);
		config.init(25, 25, 34, 29, 8);
		createIntegerIncrementerFrame(config, colors, "amount", testCannonCharge.getAmountGNS(), 1);


		config.init(8, 35, 22, 39, 8);
		guiComponents.add(new BasicTextFrame(this, "BlockOffset", config.duplicate(), headerColors));
		config.init(10, 40, 19, 44, 8);
		createDoubleIncrementerFrame(config, colors, "X", testCannonCharge.getBlockOffset().x, 1d);
		config.init(25, 40, 34, 44, 8);
		createDoubleIncrementerFrame(config, colors, "Y", testCannonCharge.getBlockOffset().y, 1d);
		config.init(40, 40, 49, 44, 8);
		createDoubleIncrementerFrame(config, colors, "Z", testCannonCharge.getBlockOffset().z, 1d);

		config.init(8, 50, 22, 54, 8);
		guiComponents.add(new BasicTextFrame(this, "PixelOffset", config.duplicate(), headerColors));
		config.init(10, 55, 19, 59, 8);
		createDoubleIncrementerFrame(config, colors, "X", testCannonCharge.getPixelOffset().x, 1d);
		config.init(25, 55, 34, 59, 8);
		createDoubleIncrementerFrame(config, colors, "Y", testCannonCharge.getPixelOffset().y, 1d);
		config.init(40, 55, 49, 59, 8);
		createDoubleIncrementerFrame(config, colors, "Z", testCannonCharge.getPixelOffset().z, 1d);

		config.init(8, 65, 22, 69, 8);
		guiComponents.add(new BasicTextFrame(this, "Velocity", config.duplicate(), headerColors));
		config.init(10, 70, 19, 74, 8);
		createDoubleIncrementerFrame(config, colors, "X", testCannonCharge.getVelocity().x, 1d);
		config.init(25, 70, 34, 74, 8);
		createDoubleIncrementerFrame(config, colors, "Y", testCannonCharge.getVelocity().y, 1d);
		config.init(40, 70, 49, 74, 8);
		createDoubleIncrementerFrame(config, colors, "Z", testCannonCharge.getVelocity().z, 1d);
	}

	private void createIntegerIncrementerFrame(FrameConfig config, FrameColors colors, String text, GetterAndSetter<Integer> source, Integer offset){
		ValueFrame<Integer> valueFrame = new ValueFrame<>(this, config.duplicate(), colors, text, source, Integer.class, false);
		IntIncrementer incrementer = new IntIncrementer(valueFrame, source, offset);
		guiComponents.add(valueFrame);
		config.x = config.xEnd;
		config.xEnd = config.x + 2;
		guiComponents.add(new ButtonFrame(this, "+", config.duplicate(), colors, incrementer::onIncrement));
		config.x = config.xEnd;
		config.xEnd = config.x + 2;
		guiComponents.add(new ButtonFrame(this, "-", config.duplicate(), colors, incrementer::onDecrement));
	}

	private void createDoubleIncrementerFrame(FrameConfig config, FrameColors colors, String text, GetterAndSetter<Double> source, Double offset){
		ValueFrame<Double> valueFrame = new ValueFrame<>(this, config.duplicate(), colors, text, source, Double.class, false);
		DoubleIncrementer incrementer = new DoubleIncrementer(valueFrame, source, offset);
		guiComponents.add(valueFrame);
		config.x = config.xEnd;
		config.xEnd = config.x + 2;
		guiComponents.add(new ButtonFrame(this, "+", config.duplicate(), colors, incrementer::onIncrement));
		config.x = config.xEnd;
		config.xEnd = config.x + 2;
		guiComponents.add(new ButtonFrame(this, "-", config.duplicate(), colors, incrementer::onDecrement));
	}

	public void open(String testCannonString){
		try {
			TestCannonCharge testCannonCharge;
			if(testCannonString.isEmpty()){
				testCannonCharge = new TestCannonCharge();
			}else{
				testCannonCharge = objectMapper.readValue(testCannonString, TestCannonCharge.class);
			}
			load(testCannonCharge);
			generateScreenComponents();
			Minecraft.getInstance().displayGuiScreen(this);
		} catch (JsonProcessingException e) {
			ChatUtils.messagePlayer("", "Unable to parse testCannon-data from server!", false);
			e.printStackTrace();
		}
	}

	public void load(TestCannonCharge other){
		testCannonCharge.load(other);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		if (minecraft == null) {
			System.out.println("minecraft was null?");
			return;
		}

		int scaledScreenWidth = minecraft.getMainWindow().getScaledWidth();
		int scaledScreenHeight = minecraft.getMainWindow().getScaledHeight();
		int guiScale = minecraft.gameSettings.guiScale;
		if (guiScale == 0) {
			if (!detectedAutoGUI) {
				detectedAutoGUI = true;
				Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("please don't use 'auto' as a gui-scale, I don't have internal access to it"));
			}
			return;
		}
		if (detectedAutoGUI) {
			detectedAutoGUI = false;
			Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("thank you for your cooperation."));
		}

		for (IRenderableFrame renderable : guiComponents) {
			if (renderable instanceof IClickableFrame) {
				((IClickableFrame) renderable).mouseOver(mouseX, mouseY, scaledScreenWidth, scaledScreenHeight, this.leftDown, this.queueLeftUpdate);
			}
			if (renderable instanceof ITickableFrame) {
				((ITickableFrame) renderable).tick(this);
			}
			renderable.render(scaledScreenWidth, scaledScreenHeight, guiScale);
		}

		queueLeftUpdate = false;

		if(shouldClose){
			onClose();
			shouldClose = false;
		}
	}

	@Override
	public void keyEvent(InputEvent.KeyInputEvent event) {
		if (Main.getInstance().keyPressListener.pressedKeys.contains(1)) {
			onClose();
			return;
		}
		for (IRenderableFrame renderable : guiComponents) {
			if (renderable instanceof IFocusableFrame) {
				if (((IFocusableFrame) renderable).getFocused()) {
					((IFocusableFrame) renderable).keyEvent(event);
				}
			} else if (renderable instanceof IKeyEventRepeaterFrame) {
				((IKeyEventRepeaterFrame) renderable).keyEvent(event);
			}
		}
	}

	@Override
	public boolean getLeftDown() {
		return leftDown;
	}

	@Override
	public void mousePressEvent(boolean leftDown) {
		if (this.leftDown != leftDown) {
			this.leftDown = leftDown;
			queueLeftUpdate = true;
		}
	}

	@Override
	public void drawCenteredString(FontRenderer fontRenderer, String text, int xPos, int height, int color) {
		double configFontHeight = guiManager.getGuiConfig().getFontHeight();
		if (configFontHeight == 0) {
			return;
		}
		height -= (fontRenderer.FONT_HEIGHT * configFontHeight) / 2;
		xPos /= configFontHeight;
		height /= configFontHeight;

		GL11.glPushMatrix();
		GL11.glScaled(configFontHeight, configFontHeight, configFontHeight);
		super.drawCenteredString(fontRenderer, text, xPos, height, color);
		GL11.glPopMatrix();
	}

	@Override
	public void onClose() {
		if (minecraft.currentScreen != null) {
			minecraft.displayGuiScreen((Screen) null);
		}

		try {
			String json = objectMapper.writeValueAsString(testCannonCharge);
			Minecraft.getInstance().player.sendChatMessage("[TestCannonData]" + json);
			System.out.println("[TestCannonData]" + json);
		} catch (JsonProcessingException e) {
			ChatUtils.messagePlayer("", "failed to build testCannonData!", false);
			e.printStackTrace();
		}

		guiComponents.clear();
	}

	//overrides to avoid stupidness
	@Override
	public String getNarrationMessage() {
		return "";
	}

	@Override
	public boolean keyPressed(int noIdea1, int noIdea2, int noIdea3) {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void renderBackground() {
	}

	@Override
	public void renderBackground(int something) {
	}

	@Override
	public void renderDirtBackground(int something) {
	}
}
