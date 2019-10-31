package the_dark_jumper.cannontracer.util;

public class KeyLibrary {
	public class KeyContent{
		public String keyName;
		
		public KeyContent(String keyName) {
			this.keyName = keyName;
		}
	}
	
	public class KeyCharSimple extends KeyContent{
		public char base;
		
		public KeyCharSimple(String keyName, char base){
			super(keyName);
			this.base = base;
		}
	}
	
	public class KeyCharNormal extends KeyContent{
		public char base, shifted;
		
		public KeyCharNormal(String keyName, char base, char shifted) {
			super(keyName);
			this.base = base;
			this.shifted = shifted;
		}
	}
	
	public class KeyCharComplex extends KeyContent{
		public char base, shifted, alted;
		
		public KeyCharComplex(String keyName, char base, char shifted, char alted) {
			super(keyName);
			this.base = base;
			this.shifted = shifted;
			this.alted = alted;
		}
	}
	
	public KeyContent getKeyContent(int key) {
		if(key == 1){
			return new KeyContent("escape");
		}else if(key == 2){
			return new KeyCharNormal("1", '1', '!');
		}else if(key == 3){
			return new KeyCharComplex("2", '2', '"', '²');
		}else if(key == 4){
			return new KeyCharComplex("3", '3', '§', '³');
		}else if(key == 5){
			return new KeyCharNormal("4", '4', '$');
		}else if(key == 6){
			return new KeyCharNormal("5", '5', '%');
		}else if(key == 7){
			return new KeyCharNormal("6", '6', '&');
		}else if(key == 8){
			return new KeyCharComplex("7", '7', '/', '{');
		}else if(key == 9){
			return new KeyCharComplex("8", '8', '(', '[');
		}else if(key == 10){
			return new KeyCharComplex("9", '9', ')', ']');
		}else if(key == 11){
			return new KeyCharComplex("0", '0', '=', '}');
		}else if(key == 12){
			return new KeyCharComplex("ß", 'ß', '?', '\\');
		}else if(key == 13){
			return new KeyCharNormal("accent_aigu", '´', '`');
		}else if(key == 14){
			return new KeyContent("backspace");
		}else if(key == 15){
			return new KeyContent("tab");
		}else if(key == 16){
			return new KeyCharComplex("q", 'q', 'Q', '@');
		}else if(key == 17){
			return new KeyCharNormal("w", 'w', 'W');
		}else if(key == 18){
			return new KeyCharComplex("e", 'e', 'E', '€');
		}else if(key == 19){
			return new KeyCharNormal("r", 'r', 'R');
		}else if(key == 20){
			return new KeyCharNormal("t", 't', 'T');
		}else if(key == 21){
			return new KeyCharNormal("z", 'z', 'Z');
		}else if(key == 22){
			return new KeyCharNormal("u", 'u', 'U');
		}else if(key == 23){
			return new KeyCharNormal("i", 'i', 'I');
		}else if(key == 24){
			return new KeyCharNormal("o", 'o', 'O');
		}else if(key == 25){
			return new KeyCharNormal("p", 'p', 'P');
		}else if(key == 26){
			return new KeyCharNormal("ü", 'ü', 'Ü');
		}else if(key == 27){
			return new KeyCharComplex("plus", '+', '*', '~');
		}else if(key == 28){
			return new KeyContent("enter");
		}else if(key == 29){
			return new KeyContent("ctrl");
		}else if(key == 30){
			return new KeyCharNormal("a", 'a', 'A');
		}else if(key == 31){
			return new KeyCharNormal("s", 's', 'S');
		}else if(key == 32){
			return new KeyCharNormal("d", 'd', 'D');
		}else if(key == 33){
			return new KeyCharNormal("f", 'f', 'F');
		}else if(key == 34){
			return new KeyCharNormal("g", 'g', 'G');
		}else if(key == 35){
			return new KeyCharNormal("h", 'h', 'H');
		}else if(key == 36){
			return new KeyCharNormal("j", 'j', 'J');
		}else if(key == 37){
			return new KeyCharNormal("k", 'k', 'K');
		}else if(key == 38){
			return new KeyCharNormal("l", 'l', 'L');
		}else if(key == 39){
			return new KeyCharNormal("ö", 'ö', 'Ö');
		}else if(key == 40){
			return new KeyCharNormal("ä", 'ä', 'Ä');
		}else if(key == 41){
			return new KeyCharNormal("caret", '^', '°');
		}else if(key == 42){
			return new KeyContent("shift");
		}else if(key == 43){
			return new KeyCharNormal("octothorpe", '#', '\'');
		}else if(key == 44){
			return new KeyCharNormal("y", 'y', 'Y');
		}else if(key == 45){
			return new KeyCharNormal("x", 'x', 'X');
		}else if(key == 46){
			return new KeyCharNormal("c", 'c', 'C');
		}else if(key == 47){
			return new KeyCharNormal("v", 'v', 'V');
		}else if(key == 48){
			return new KeyCharNormal("b", 'b', 'B');
		}else if(key == 49){
			return new KeyCharNormal("n", 'n', 'N');
		}else if(key == 50){
			return new KeyCharNormal("m", 'm', 'M');
		}else if(key == 51){
			return new KeyCharNormal("comma", ',', ';');
		}else if(key == 52){
			return new KeyCharNormal("period", '.', ':');
		}else if(key == 53){
			return new KeyCharNormal("minus", '-', '_');
		}else if(key == 54){
			return new KeyContent("shift_r");
		}else if(key == 55){
			return new KeyCharSimple("pad[*]", '*');
		}else if(key == 56){
			return new KeyContent("alt");
		}else if(key == 57){
			return new KeyCharSimple("space", ' ');
		}else if(key == 58){
			return new KeyContent("caps");
		}else if(key == 59){
			return new KeyContent("f1");
		}else if(key == 60){
			return new KeyContent("f2");
		}else if(key == 61){
			return new KeyContent("f3");
		}else if(key == 62){
			return new KeyContent("f4");
		}else if(key == 63){
			return new KeyContent("f5");
		}else if(key == 64){
			return new KeyContent("f6");
		}else if(key == 65){
			return new KeyContent("f7");
		}else if(key == 66){
			return new KeyContent("f8");
		}else if(key == 67){
			return new KeyContent("f9");
		}else if(key == 68){
			return new KeyContent("f10");
		}else if(key == 69){
			return new KeyContent("pause");
		}else if(key == 70){
			return new KeyContent("roll");
		}else if(key == 71){
			return new KeyCharSimple("pad[7]", '7');
		}else if(key == 72){
			return new KeyCharSimple("pad[8]", '8');
		}else if(key == 73){
			return new KeyCharSimple("pad[9]", '9');
		}else if(key == 74){
			return new KeyCharSimple("pad[-]", '-');
		}else if(key == 75){
			return new KeyCharSimple("pad[4]", '4');
		}else if(key == 76){
			return new KeyCharSimple("pad[5]", '5');
		}else if(key == 77){
			return new KeyCharSimple("pad[6]", '6');
		}else if(key == 78){
			return new KeyCharSimple("pad[+]", '+');
		}else if(key == 79){
			return new KeyCharSimple("pad[1]", '1');
		}else if(key == 80){
			return new KeyCharSimple("pad[2]", '2');
		}else if(key == 81){
			return new KeyCharSimple("pad[3]", '3');
		}else if(key == 82){
			return new KeyCharSimple("pad[0]", '0');
		}else if(key == 83){
			return new KeyCharSimple("pad[,]", ',');
		}else if(key == 86){
			return new KeyCharComplex("less_than", '<', '>', '|');
		}else if(key == 87){
			return new KeyContent("f11");
		}else if(key == 88){
			return new KeyContent("f12");
		}else if(key == 284){
			return new KeyContent("pad[enter]");
		}else if(key == 285){
			return new KeyContent("ctrl_r");
		}else if(key == 309){
			return new KeyCharSimple("pad[/]", '/');
		}else if(key == 311){
			return new KeyContent("print");
		}else if(key == 312){
			return new KeyContent("alt_r");
		}else if(key == 325){
			return new KeyContent("numlock");
		}else if(key == 327){
			return new KeyContent("home");
		}else if(key == 328){
			return new KeyContent("arrow_up");
		}else if(key == 329){
			return new KeyContent("page_up");
		}else if(key == 331){
			return new KeyContent("arrow_left");
		}else if(key == 333){
			return new KeyContent("arrow_right");
		}else if(key == 335){
			return new KeyContent("end");
		}else if(key == 336){
			return new KeyContent("arrow_down");
		}else if(key == 337){
			return new KeyContent("page_down");
		}else if(key == 338){
			return new KeyContent("insert");
		}else if(key == 339){
			return new KeyContent("delete");
		}else if(key == 347){
			return new KeyContent("windows");
		}
		return null;
	}
}
