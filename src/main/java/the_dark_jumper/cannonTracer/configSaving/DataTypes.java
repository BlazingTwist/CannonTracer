package the_dark_jumper.cannonTracer.configSaving;

public class DataTypes {
	public interface DataType{
		public String buildString();
		public boolean readValue(String str);
	}
	
	public static class ConfigInteger implements DataType{
		public String name;
		public int value;
		
		public ConfigInteger() {}
		
		public ConfigInteger(String name, Integer value) {
			this.name = name;
			this.value = value;
		}

		public String buildString() {
			String out = name + " = " + value;
			return out;
		}
		
		public boolean readValue(String str) {
			String keyValPair[] = str.split(" = ");
			if(keyValPair.length != 2) {
				return false;
			}
			this.name = keyValPair[0];
			this.value = Integer.parseInt(keyValPair[1]);
			return true;
		}
	}
	
	public static class ConfigFloat implements DataType{
		public String name;
		public float value;
		
		public ConfigFloat() {}
		
		public ConfigFloat(String name, float value) {
			this.name = name;
			this.value = value;
		}
		
		public String buildString() {
			String out = name + " = " + value;
			return out;
		}
		
		public boolean readValue(String str) {
			String keyValPair[] = str.split(" = ");
			if(keyValPair.length != 2) {
				return false;
			}
			this.name = keyValPair[0];
			this.value = Float.parseFloat(keyValPair[1]);
			return true;
		}
	}
	
	public static class ConfigDouble implements DataType{
		public String name;
		public double value;
		
		public ConfigDouble() {}
		
		public ConfigDouble(String name, double value) {
			this.name = name;
			this.value = value;
		}
		
		public String buildString() {
			String out = name + " = " + value;
			return out;
		}
		
		public boolean readValue(String str) {
			String keyValPair[] = str.split(" = ");
			if(keyValPair.length != 2) {
				return false;
			}
			this.name = keyValPair[0];
			this.value = Double.parseDouble(keyValPair[1]);
			return true;
		}
	}
	
	public static class ConfigBoolean implements DataType{
		public String name;
		public boolean value;
		
		public ConfigBoolean() {}
		
		public ConfigBoolean(String name, boolean value) {
			this.name = name;
			this.value = value;
		}
		
		public String buildString() {
			String out = name + " = " + value;
			return out;
		}
		
		public boolean readValue(String str) {
			String keyValPair[] = str.split(" = ");
			if(keyValPair.length != 2) {
				return false;
			}
			this.name = keyValPair[0];
			this.value = Boolean.parseBoolean(keyValPair[1]);
			return true;
		}
	}
	
	public static class ConfigString implements DataType{
		public String name;
		public String value;
		
		public ConfigString() {}
		
		public ConfigString(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		public String buildString() {
			String out = name + " = " + value;
			return out;
		}
		
		public boolean readValue(String str) {
			String keyValPair[] = str.split(" = ");
			if(keyValPair.length != 2) {
				return false;
			}
			this.name = keyValPair[0];
			this.value = keyValPair[1];
			return true;
		}
	}
}
