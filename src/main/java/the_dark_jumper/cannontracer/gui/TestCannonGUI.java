package the_dark_jumper.cannontracer.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.function.Consumer;
import jumpercommons.GetterAndSetter;
import jumpercommons.TestCannon;
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
import the_dark_jumper.cannontracer.gui.guielements.ScrollableTable;
import the_dark_jumper.cannontracer.gui.guielements.ValueFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IClickableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IFocusableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IKeyEventRepeaterFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.IRenderableFrame;
import the_dark_jumper.cannontracer.gui.guielements.interfaces.ITickableFrame;
import the_dark_jumper.cannontracer.gui.utils.FormatData;
import the_dark_jumper.cannontracer.gui.utils.FrameColors;
import the_dark_jumper.cannontracer.gui.utils.FrameConfig;
import the_dark_jumper.cannontracer.util.ChatUtils;
import the_dark_jumper.cannontracer.util.DoubleIncrementer;
import the_dark_jumper.cannontracer.util.IntIncrementer;
import the_dark_jumper.cannontracer.util.StringPacket;

public class TestCannonGUI extends Screen implements IJumperGUI {
	private static class DeleteChargeCallback {
		private final int index;
		private final Consumer<Integer> deletionCallback;

		public DeleteChargeCallback(int index, Consumer<Integer> deletionCallback) {
			this.index = index;
			this.deletionCallback = deletionCallback;
		}

		public void onPressed(boolean pressed) {
			if (pressed) {
				deletionCallback.accept(index);
			}
		}
	}

	public final GuiManager guiManager;
	private final ObjectMapper objectMapper;
	public ArrayList<IRenderableFrame> guiComponents = new ArrayList<>();
	private ScrollableTable chargesTable = null;

	boolean detectedAutoGUI = false;
	public boolean leftDown = false;
	public boolean queueLeftUpdate = false;
	private boolean shouldClose = false;
	private boolean cancelCannonData = false;

	private final TestCannon testCannon = new TestCannon();

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

	public void addChargeButtonPressed(boolean isPressed) {
		if (isPressed) {
			testCannon.getCharges().add(new TestCannonCharge());
			generateChargesTable();
		}
	}

	public void removeCharge(int chargeIndex) {
		if (chargeIndex >= testCannon.getCharges().size()) {
			Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("Couldn't delete Charge " + chargeIndex + ", stop spamming that button."));
			return;
		}

		testCannon.getCharges().remove(chargeIndex);
		generateChargesTable();
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
		config.init(6, 10, 60, 14, 8);
		guiComponents.add(new BasicTextFrame(this, "Cannon Tester", config.duplicate(), colors));
		config.init(80, 10, 89, 14, 8);
		guiComponents.add(new ButtonFrame(this, "cancel", config.duplicate(), colors, this::closeWithoutSend));
		config.init(90, 10, 94, 14, 8);
		guiComponents.add(new ButtonFrame(this, "X", config.duplicate(), colors, this::closeButtonPressed));

		config.init(8, 15, 22, 19, 8);
		guiComponents.add(new BasicTextFrame(this, "BlockOffset", config.duplicate(), headerColors));
		config.init(10, 20, 19, 24, 8);
		createDoubleIncrementerFrame(config, colors, "X", testCannon.getBlockOffset().x);
		config.init(10, 25, 19, 29, 8);
		createDoubleIncrementerFrame(config, colors, "Y", testCannon.getBlockOffset().y);
		config.init(10, 30, 19, 34, 8);
		createDoubleIncrementerFrame(config, colors, "Z", testCannon.getBlockOffset().z);

		config.init(28, 15, 42, 19, 8);
		guiComponents.add(new BasicTextFrame(this, "PixelOffset", config.duplicate(), headerColors));
		config.init(30, 20, 39, 24, 8);
		createDoubleIncrementerFrame(config, colors, "X", testCannon.getPixelOffset().x);
		config.init(30, 25, 39, 29, 8);
		createDoubleIncrementerFrame(config, colors, "Y", testCannon.getPixelOffset().y);
		config.init(30, 30, 39, 34, 8);
		createDoubleIncrementerFrame(config, colors, "Z", testCannon.getPixelOffset().z);

		config.init(48, 15, 62, 19, 8);
		guiComponents.add(new BasicTextFrame(this, "Velocity", config.duplicate(), headerColors));
		config.init(50, 20, 59, 24, 8);
		createDoubleIncrementerFrame(config, colors, "X", testCannon.getVelocity().x);
		config.init(50, 25, 59, 29, 8);
		createDoubleIncrementerFrame(config, colors, "Y", testCannon.getVelocity().y);
		config.init(50, 30, 59, 34, 8);
		createDoubleIncrementerFrame(config, colors, "Z", testCannon.getVelocity().z);

		config.init(8, 40, 22, 44, 8);
		guiComponents.add(new BasicTextFrame(this, "Charges", config.duplicate(), headerColors));
		config.init(23, 40, 25, 44, 8);
		guiComponents.add(new ButtonFrame(this, "+", config.duplicate(), colors, this::addChargeButtonPressed));
		config.init(10, 45, 63, 94, 8);
		chargesTable = new ScrollableTable(this, config.duplicate(), colors);
		chargesTable.setUniformColFormat(false, 9, 1); // fallback value, shouldn't be needed
		chargesTable.setColFormat(false,
				new FormatData(2, 1), // delete button
				new FormatData(9, 0), // amount text field
				new FormatData(2, 0), // increment button
				new FormatData(2, 1), // decrement button
				new FormatData(9, 0), // delay text field
				new FormatData(2, 0), // increment button
				new FormatData(2, 1));// decrement button
		chargesTable.setUniformRowFormat(false, 4, 1);
		chargesTable.generateScrollbars(false, 0, true, chargesTable.matchHeightToWidth(1));
		generateChargesTable();
		guiComponents.add(chargesTable);
	}

	private void generateChargesTable() {
		chargesTable.getRows().clear();

		ArrayList<TestCannonCharge> charges = testCannon.getCharges();
		for (int i = 0; i < charges.size(); i++) {
			FrameColors colors = chargesTable.colors;
			TestCannonCharge charge = charges.get(i);
			ValueFrame<Integer> amountValueFrame = new ValueFrame<>(this, null, colors, "amount", charge.getAmountGNS(), Integer.class, false);
			IntIncrementer amountIncrementer = new IntIncrementer(amountValueFrame::update, charge.getAmountGNS(), 1, 0, null);
			ValueFrame<Integer> delayValueFrame = new ValueFrame<>(this, null, colors, "delay", charge.getDelayGNS(), Integer.class, false);
			IntIncrementer delayIncrementer = new IntIncrementer(delayValueFrame::update, charge.getDelayGNS(), 1, 0, null);

			chargesTable.addRow(
					new ButtonFrame(this, "X", null, colors, new DeleteChargeCallback(i, this::removeCharge)::onPressed),
					amountValueFrame,
					new ButtonFrame(this, "+", null, colors, amountIncrementer::onIncrement),
					new ButtonFrame(this, "-", null, colors, amountIncrementer::onDecrement),
					delayValueFrame,
					new ButtonFrame(this, "+", null, colors, delayIncrementer::onIncrement),
					new ButtonFrame(this, "-", null, colors, delayIncrementer::onDecrement)
			);
		}

		chargesTable.updateScrollbarRanges();
	}

	private void createDoubleIncrementerFrame(FrameConfig config, FrameColors colors, String text, GetterAndSetter<Double> source) {
		ValueFrame<Double> valueFrame = new ValueFrame<>(this, config.duplicate(), colors, text, source, Double.class, false);
		DoubleIncrementer incrementer = new DoubleIncrementer(valueFrame::update, source, 1.0, null, null);
		guiComponents.add(valueFrame);
		config.x = config.xEnd;
		config.xEnd = config.x + 2;
		guiComponents.add(new ButtonFrame(this, "+", config.duplicate(), colors, incrementer::onIncrement));
		config.x = config.xEnd;
		config.xEnd = config.x + 2;
		guiComponents.add(new ButtonFrame(this, "-", config.duplicate(), colors, incrementer::onDecrement));
	}

	public void open(String testCannonString) {
		try {
			TestCannon testCannon;
			if (testCannonString.isEmpty()) {
				testCannon = new TestCannon();
			} else {
				testCannon = objectMapper.readValue(testCannonString, TestCannon.class);
			}
			load(testCannon);
			generateScreenComponents();
			Minecraft.getInstance().displayGuiScreen(this);
		} catch (JsonProcessingException e) {
			ChatUtils.messagePlayer("", "Unable to parse testCannon-data from server!", false);
			e.printStackTrace();
		}
	}

	public void load(TestCannon other) {
		testCannon.load(other);
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

		if (shouldClose) {
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
		double configFontSize = guiManager.getGuiConfig().getFontHeight();
		if (configFontSize == 0) {
			return;
		}
		height -= (fontRenderer.FONT_HEIGHT * configFontSize) / 2;
		xPos /= configFontSize;
		height /= configFontSize;

		GL11.glPushMatrix();
		GL11.glScaled(configFontSize, configFontSize, configFontSize);
		super.drawCenteredString(fontRenderer, text, xPos, height, color);
		GL11.glPopMatrix();
	}

	private void closeWithoutSend(boolean isPressed){
		if(isPressed){
			cancelCannonData = true;
			onClose();
			cancelCannonData = false;
		}
	}

	@Override
	public void onClose() {
		guiManager.main.moduleManager.focusReleaseAllFrames();

		if (minecraft != null && minecraft.currentScreen != null) {
			minecraft.displayGuiScreen(null);
		}

		if (!cancelCannonData) {
			try {
				String json = objectMapper.writeValueAsString(testCannon);
				guiManager.main.serverChatListener.testCannonChannel.sendToServer(new StringPacket().setData(json));
				System.out.println("[TestCannonData]: " + json);
			} catch (JsonProcessingException e) {
				ChatUtils.messagePlayer("", "failed to build testCannonData!", false);
				e.printStackTrace();
			}
		}

		guiComponents.clear();
	}

	//overrides to avoid stupidity
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
