package the_dark_jumper.cannontracer.util;

public class KeyLibrary {
	public static class KeyContent {
		public String keyName;

		public KeyContent(String keyName) {
			this.keyName = keyName;
		}
	}

	public static class KeyCharSimple extends KeyContent {
		public char base;

		public KeyCharSimple(String keyName, char base) {
			super(keyName);
			this.base = base;
		}
	}

	public static class KeyCharNormal extends KeyContent {
		public char base, shifted;

		public KeyCharNormal(String keyName, char base, char shifted) {
			super(keyName);
			this.base = base;
			this.shifted = shifted;
		}
	}

	public static class KeyCharComplex extends KeyContent {
		public char base, shifted, alted;

		public KeyCharComplex(String keyName, char base, char shifted, char alted) {
			super(keyName);
			this.base = base;
			this.shifted = shifted;
			this.alted = alted;
		}
	}

	public static KeyContent getKeyContent(int key) {
		switch (key) {
			case 1:
				return new KeyContent("escape");
			case 2:
				return new KeyCharNormal("1", '1', '!');
			case 3:
				return new KeyCharComplex("2", '2', '"', '²');
			case 4:
				return new KeyCharComplex("3", '3', '§', '³');
			case 5:
				return new KeyCharNormal("4", '4', '$');
			case 6:
				return new KeyCharNormal("5", '5', '%');
			case 7:
				return new KeyCharNormal("6", '6', '&');
			case 8:
				return new KeyCharComplex("7", '7', '/', '{');
			case 9:
				return new KeyCharComplex("8", '8', '(', '[');
			case 10:
				return new KeyCharComplex("9", '9', ')', ']');
			case 11:
				return new KeyCharComplex("0", '0', '=', '}');
			case 12:
				return new KeyCharComplex("ß", 'ß', '?', '\\');
			case 13:
				return new KeyCharNormal("accent_aigu", '´', '`');
			case 14:
				return new KeyContent("backspace");
			case 15:
				return new KeyContent("tab");
			case 16:
				return new KeyCharComplex("q", 'q', 'Q', '@');
			case 17:
				return new KeyCharNormal("w", 'w', 'W');
			case 18:
				return new KeyCharComplex("e", 'e', 'E', '€');
			case 19:
				return new KeyCharNormal("r", 'r', 'R');
			case 20:
				return new KeyCharNormal("t", 't', 'T');
			case 21:
				return new KeyCharNormal("z", 'z', 'Z');
			case 22:
				return new KeyCharNormal("u", 'u', 'U');
			case 23:
				return new KeyCharNormal("i", 'i', 'I');
			case 24:
				return new KeyCharNormal("o", 'o', 'O');
			case 25:
				return new KeyCharNormal("p", 'p', 'P');
			case 26:
				return new KeyCharNormal("ü", 'ü', 'Ü');
			case 27:
				return new KeyCharComplex("plus", '+', '*', '~');
			case 28:
				return new KeyContent("enter");
			case 29:
				return new KeyContent("ctrl");
			case 30:
				return new KeyCharNormal("a", 'a', 'A');
			case 31:
				return new KeyCharNormal("s", 's', 'S');
			case 32:
				return new KeyCharNormal("d", 'd', 'D');
			case 33:
				return new KeyCharNormal("f", 'f', 'F');
			case 34:
				return new KeyCharNormal("g", 'g', 'G');
			case 35:
				return new KeyCharNormal("h", 'h', 'H');
			case 36:
				return new KeyCharNormal("j", 'j', 'J');
			case 37:
				return new KeyCharNormal("k", 'k', 'K');
			case 38:
				return new KeyCharNormal("l", 'l', 'L');
			case 39:
				return new KeyCharNormal("ö", 'ö', 'Ö');
			case 40:
				return new KeyCharNormal("ä", 'ä', 'Ä');
			case 41:
				return new KeyCharNormal("caret", '^', '°');
			case 42:
				return new KeyContent("shift");
			case 43:
				return new KeyCharNormal("octothorpe", '#', '\'');
			case 44:
				return new KeyCharNormal("y", 'y', 'Y');
			case 45:
				return new KeyCharNormal("x", 'x', 'X');
			case 46:
				return new KeyCharNormal("c", 'c', 'C');
			case 47:
				return new KeyCharNormal("v", 'v', 'V');
			case 48:
				return new KeyCharNormal("b", 'b', 'B');
			case 49:
				return new KeyCharNormal("n", 'n', 'N');
			case 50:
				return new KeyCharNormal("m", 'm', 'M');
			case 51:
				return new KeyCharNormal("comma", ',', ';');
			case 52:
				return new KeyCharNormal("period", '.', ':');
			case 53:
				return new KeyCharNormal("minus", '-', '_');
			case 54:
				return new KeyContent("shift_r");
			case 55:
				return new KeyCharSimple("pad[*]", '*');
			case 56:
				return new KeyContent("alt");
			case 57:
				return new KeyCharSimple("space", ' ');
			case 58:
				return new KeyContent("caps");
			case 59:
				return new KeyContent("f1");
			case 60:
				return new KeyContent("f2");
			case 61:
				return new KeyContent("f3");
			case 62:
				return new KeyContent("f4");
			case 63:
				return new KeyContent("f5");
			case 64:
				return new KeyContent("f6");
			case 65:
				return new KeyContent("f7");
			case 66:
				return new KeyContent("f8");
			case 67:
				return new KeyContent("f9");
			case 68:
				return new KeyContent("f10");
			case 69:
				return new KeyContent("pause");
			case 70:
				return new KeyContent("roll");
			case 71:
				return new KeyCharSimple("pad[7]", '7');
			case 72:
				return new KeyCharSimple("pad[8]", '8');
			case 73:
				return new KeyCharSimple("pad[9]", '9');
			case 74:
				return new KeyCharSimple("pad[-]", '-');
			case 75:
				return new KeyCharSimple("pad[4]", '4');
			case 76:
				return new KeyCharSimple("pad[5]", '5');
			case 77:
				return new KeyCharSimple("pad[6]", '6');
			case 78:
				return new KeyCharSimple("pad[+]", '+');
			case 79:
				return new KeyCharSimple("pad[1]", '1');
			case 80:
				return new KeyCharSimple("pad[2]", '2');
			case 81:
				return new KeyCharSimple("pad[3]", '3');
			case 82:
				return new KeyCharSimple("pad[0]", '0');
			case 83:
				return new KeyCharSimple("pad[,]", ',');
			case 86:
				return new KeyCharComplex("less_than", '<', '>', '|');
			case 87:
				return new KeyContent("f11");
			case 88:
				return new KeyContent("f12");
			case 284:
				return new KeyContent("pad[enter]");
			case 285:
				return new KeyContent("ctrl_r");
			case 309:
				return new KeyCharSimple("pad[/]", '/');
			case 311:
				return new KeyContent("print");
			case 312:
				return new KeyContent("alt_r");
			case 325:
				return new KeyContent("numlock");
			case 327:
				return new KeyContent("home");
			case 328:
				return new KeyContent("arrow_up");
			case 329:
				return new KeyContent("page_up");
			case 331:
				return new KeyContent("arrow_left");
			case 333:
				return new KeyContent("arrow_right");
			case 335:
				return new KeyContent("end");
			case 336:
				return new KeyContent("arrow_down");
			case 337:
				return new KeyContent("page_down");
			case 338:
				return new KeyContent("insert");
			case 339:
				return new KeyContent("delete");
			case 347:
				return new KeyContent("windows");
		}
		return null;
	}
}
