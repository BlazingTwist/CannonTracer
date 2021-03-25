package the_dark_jumper.cannontracer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import the_dark_jumper.cannontracer.util.ChatUtils;

public class Update {
	public static final String STARTED_MSG = "is updating...";
	public static final String FAILED_MSG = "could not be updated";
	public static final String SUCCESS_MSG = "was updated successfully, please restart your game to apply the update.";
	public static final String UP_TO_DATE_MSG = "is already up to date, no update needed";
	public static final String OUTDATED_MSG = "is outdated, please update using the button on the config screen!";
	public static final String MOD_NAME = "CannonTracer ";
	
	public static final String VERSION = "1.1.1";
	
	public static void sendUpdateMessageIfNeeded() {
		if(checkUpdateAvailable()) {
			ChatUtils.messagePlayer(MOD_NAME, OUTDATED_MSG, false);
		}
	}
	
	public static String getGithubVersionTag() {
		try {
			HttpURLConnection urlConnect = (HttpURLConnection)new URL("https://github.com/BlazingTwist/CannonTracer/releases/latest").openConnection();
			urlConnect.setInstanceFollowRedirects(false);
			urlConnect.getResponseCode();
			return urlConnect.getHeaderField("Location").replaceFirst(".*/", "");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean checkUpdateAvailable() {
		String version = getGithubVersionTag();
		if(version == null) {
			return false;
		}
		String[] versionNumbers = version.split("\\.");
		String[] localVersionNumbers = VERSION.split("\\.");
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
			ChatUtils.messagePlayer(MOD_NAME, UP_TO_DATE_MSG, true);
		}else {
			new Thread(Update::doModUpdate).start();
		}
	}
	
	private static void doModUpdate() {
		String path = Main.getInstance().dataManager.updatePathGNS.get();
		if(path == null) {
			ChatUtils.messagePlayer(MOD_NAME, FAILED_MSG, false);
			ChatUtils.messagePlayer("Update", "failed due to missing updatePath, please specify in the config-screen", false);
			return;
		}

		ChatUtils.messagePlayer(MOD_NAME, STARTED_MSG, true);
		try {
			File file = new File(path);
			writeToFile(file, new BufferedInputStream(new URL("https://github.com/BlazingTwist/CannonTracer/releases/latest/download/cannontracer.jar").openStream()));
			ChatUtils.messagePlayer(MOD_NAME, SUCCESS_MSG, true);
		}catch(IOException e) {
			e.printStackTrace();
			ChatUtils.messagePlayer(MOD_NAME, FAILED_MSG, false);
			ChatUtils.messagePlayer("Update ", "failed due to IOException", false);
		}
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
