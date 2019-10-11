package the_dark_jumper.cannonTracer.configSaving;

import the_dark_jumper.cannonTracer.settings.GuiSettings;

public class GuiSettingsContent implements Header.Content{
	public GuiSettings guiSettings;
	
	public GuiSettingsContent(GuiSettings guiSettings) {
		this.guiSettings = guiSettings;
	}
	
	public String buildContent() {
		/*String out = new DataTypes.ConfigDouble("screenModifier", guiSettings.screenModifier).buildString() +
				" | " +
				new DataTypes.ConfigDouble("mouseModifier", guiSettings.mouseModifier).buildString();
		return out;*/
		return "";
	}
	
	public boolean readContent(String content) {
		String data[] = content.split(" \\| ");
		if(data.length != 2) {
			return false;
		}
		//double screenModifier = 0;
		//double mouseModifier = 0;
		DataTypes.ConfigDouble configDouble = new DataTypes.ConfigDouble();
		for(String str : data) {
			if(!configDouble.readValue(str)) {
				return false;
			}
			/*if(configDouble.name.equals("screenModifier")) {
				screenModifier = configDouble.value;
			}else if(configDouble.name.equals("mouseModifier")) {
				mouseModifier = configDouble.value;
			}*/
		}
		//setGuiSettings(screenModifier, mouseModifier);
		return true;
	}
	
	public void setGuiSettings() {
		//guiSettings.screenModifier = screenModifier;
		//guiSettings.mouseModifier = mouseModifier;
	}
}
