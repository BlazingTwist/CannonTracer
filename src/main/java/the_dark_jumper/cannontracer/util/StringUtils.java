package the_dark_jumper.cannontracer.util;

public class StringUtils {
	public static boolean isBlank(String string) {
		int length;
		if (string == null || (length = string.length()) == 0) {
			return true;
		}
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
