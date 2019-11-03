package the_dark_jumper.cannontracer.configsaving;

public class IngameOffsetContent implements Header.Content{
	public double xOffset;
	public double yOffset;
	
	public IngameOffsetContent setXOffset(double xOffset) {
		this.xOffset = xOffset;
		return this;
	}
	
	public IngameOffsetContent setYOffset(double yOffset) {
		this.yOffset = yOffset;
		return this;
	}
	
	public String buildContent() {
		return new DataTypes.ConfigDouble("xOffset", xOffset).buildString() + " | " + new DataTypes.ConfigDouble("yOffset", yOffset).buildString();
	}
	
	public boolean readContent(String content) {
		String data[] = content.split(" \\| ");
		DataTypes.ConfigDouble configDouble = new DataTypes.ConfigDouble();
		for(String str : data) {
			if(!configDouble.readValue(str)) {
				return false;
			}
			if(configDouble.name.equals("xOffset")) {
				xOffset = configDouble.value;
			}else if(configDouble.name.equals("yOffset")) {
				yOffset = configDouble.value;
			}
		}
		return true;
	}
}
