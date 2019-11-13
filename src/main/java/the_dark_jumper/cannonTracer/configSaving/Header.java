package the_dark_jumper.cannontracer.configsaving;

import java.io.BufferedWriter;

public class Header {
	public String name;
	public String content;
	public int level;
	
	public Header(String name, String content, int level) {
		init(name, content, level);
	}
	
	public void init(String name, String content, int level) {
		this.name = name;
		this.content = content;
		this.level = level;
	}
	
	public void write(BufferedWriter bwout) {
		try {
			if(name == null || content == null) {
				return;
			}
			String out = "";
			for(int i = 0; i < level; i++) {
				out += '[';
			}
			out += name;
			for(int i = 0; i < level; i++) {
				out += ']';
			}
			out += content;
			bwout.write(out);
			bwout.newLine();
		}catch(Exception e) {
			System.out.println("error while saving header");
			e.printStackTrace();
		}
	}
	
	public boolean readHeader(String str) {
		try {
			int level = 0;
			String name = null;
			String content = null;
			for(int i = 0; i < str.length(); i++) {
				if(str.charAt(i) != '[') {
					level = i;
					break;
				}
			}
			if(level == 0) {
				return false;
			}
			int end = 0;
			for(int i = level + 1; i < str.length(); i++) {
				if(str.charAt(i) == ']') {
					for(int i2 = 1; i2 < level; i2++) {
						if(str.charAt(i2 + i) != ']') {
							return false;
						}
					}
					end = i;
					break;
				}
			}
			name = str.substring(level, end);
			if(end + level < str.length()) {
				content = str.substring(end + level);
			}
			init(name, content, level);
			return true;
		}catch(IndexOutOfBoundsException e) {
			System.out.println("was not a header or header was incomplete");
			return false;
		}
	}
}
