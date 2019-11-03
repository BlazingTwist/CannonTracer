package the_dark_jumper.cannontracer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class Update {
	public static final String STARTED_MSG = "is updating...";
	public static final String FAILED_MSG = "could not be updated";
	public static final String SUCCESS_MSG = "was updated successfully, please restart your game to apply the update.";
	public static final String UP_TO_DATE_MSG = "is already up to date, no update needed";
	public static final String OUTDATED_MSG = "is outdated, please update using the button on the config screen!";
	public static final String MOD_NAME = "CannonTracer ";
	
	public static final String VERSION = "0.9.7";
	
	public static final TextFormatting HIGHLIGHT_COLOR = TextFormatting.AQUA;
	public static final TextFormatting POSITIVE_COLOR = TextFormatting.GREEN;
	public static final TextFormatting NEGATIVE_COLOR = TextFormatting.RED;
	
	public static void sendUpdateMessageIfNeeded() {
		if(checkUpdateAvailable()) {
			messagePlayer(MOD_NAME, OUTDATED_MSG, false);
		}
	}
	
	public static String getGithubVersionTag() {
		try {
			HttpURLConnection urlConnect = (HttpURLConnection)new URL("https://github.com/BlazingTwist/CannonTracer_1.14.4/releases/latest").openConnection();
			urlConnect.setInstanceFollowRedirects(false);
			urlConnect.getResponseCode();
			return urlConnect.getHeaderField("Location").replaceFirst(".*/", "");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean checkUpdateAvailable() {
		String version = getGithubVersionTag();
		if(version == null) {
			return false;
		}
		String versionNumbers[] = version.split("\\.");
		String localVersionNumbers[] = VERSION.split("\\.");
		if(localVersionNumbers.length != versionNumbers.length) {
			return true;
		}
		for(int i = 0; i < versionNumbers.length; i++) {
			int versionNumber = Integer.parseInt(versionNumbers[i]);
			int localVersionNumber = Integer.parseInt(localVersionNumbers[i]);
			if(localVersionNumber < versionNumber) {
				return true;
			}
			if(localVersionNumber > versionNumber) {
				//not sure how that would happen, but whatever
				return false;
			}
		}
		return false;
	}
	
	public static void updateMod() {
		if(!checkUpdateAvailable()) {
			messagePlayer(MOD_NAME, UP_TO_DATE_MSG, true);
		}else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					doModUpdate();
				}
			}).start();
		}
	}
	
	private static void doModUpdate() {
		messagePlayer(MOD_NAME, STARTED_MSG, true);
		try {
			File file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\.minecraft\\mods\\cannontracer.jar");
			writeToFile(file, new BufferedInputStream(new URL("https://github.com/BlazingTwist/CannonTracer_1.14.4/releases/latest/download/cannontracer.jar").openStream()));
			messagePlayer(MOD_NAME, SUCCESS_MSG, true);
		}catch(IOException e) {
			e.printStackTrace();
			messagePlayer(MOD_NAME, FAILED_MSG, false);
			return;
		}
	}
	
	public static void messagePlayer(String part1, String part2, boolean isPositive) {
		TranslationTextComponent textComponent = new TranslationTextComponent(part1);
		textComponent.setStyle(textComponent.getStyle().setColor(HIGHLIGHT_COLOR));
		TranslationTextComponent textComponent2 = new TranslationTextComponent(part2);
		textComponent2.setStyle(textComponent2.getStyle().setColor(isPositive ? POSITIVE_COLOR : NEGATIVE_COLOR));
		textComponent.appendSibling(textComponent2);
		Minecraft.getInstance().player.sendMessage(textComponent);
	}
	
	public synchronized static void writeToFile(final File file, final BufferedInputStream inputStream) throws IOException{
		try(BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))){
			if(!file.exists()) {
				Files.copy(inputStream, file.toPath());
			}else {
				final byte[] data = new byte[8192];
				for(int count; (count = inputStream.read(data, 0, 8192)) != -1; ) {
					outputStream.write(data, 0, count);
				}
			}
		}
	}
}
