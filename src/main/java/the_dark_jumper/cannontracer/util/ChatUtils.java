package the_dark_jumper.cannontracer.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ChatUtils {
	public static final TextFormatting HIGHLIGHT_COLOR = TextFormatting.AQUA;
	public static final TextFormatting POSITIVE_COLOR = TextFormatting.GREEN;
	public static final TextFormatting NEGATIVE_COLOR = TextFormatting.RED;

	public static void messagePlayer(String part1, String part2, boolean isPositive) {
		ClientPlayerEntity player = Minecraft.getInstance().player;
		if(player != null){
			TranslationTextComponent textComponent = new TranslationTextComponent(part1);
			textComponent.setStyle(textComponent.getStyle().setColor(HIGHLIGHT_COLOR));
			TranslationTextComponent textComponent2 = new TranslationTextComponent(part2);
			textComponent2.setStyle(textComponent2.getStyle().setColor(isPositive ? POSITIVE_COLOR : NEGATIVE_COLOR));
			textComponent.appendSibling(textComponent2);
			player.sendMessage(textComponent);
		}
	}
}
