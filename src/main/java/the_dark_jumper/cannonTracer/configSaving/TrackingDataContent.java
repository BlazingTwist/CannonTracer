package the_dark_jumper.cannontracer.configsaving;

import java.util.HashMap;

import the_dark_jumper.cannontracer.util.TrackingData;

public class TrackingDataContent{
	public String entityID;
	public TrackingData trackingData;
	public HashMap<String, TrackingData> sourceMap;
	
	public TrackingDataContent(HashMap<String, TrackingData> sourceMap) {
		init(null, null, sourceMap);
	}
	
	public TrackingDataContent(String entityID, TrackingData trackingData) {
		init(entityID, trackingData, null);
	}
	
	public void init(String entityID, TrackingData trackingData, HashMap<String, TrackingData> sourceMap) {
		this.entityID = entityID;
		this.trackingData = trackingData;
		this.sourceMap = sourceMap;
	}
	
	public String buildContent() {
		String out = new DataTypes.ConfigString("EntityID", entityID).buildString() +
				" | " +
				new DataTypes.ConfigBoolean("render", trackingData.getRender()).buildString() +
				" | " +
				new DataTypes.ConfigFloat("time", trackingData.getTime()).buildString() +
				" | " +
				new DataTypes.ConfigFloat("thickness", trackingData.getThickness()).buildString() +
				" | " +
				new DataTypes.ConfigInteger("red", trackingData.getRed()).buildString() +
				" | " +
				new DataTypes.ConfigInteger("green", trackingData.getGreen()).buildString() +
				" | " +
				new DataTypes.ConfigInteger("blue", trackingData.getBlue()).buildString() +
				" | " +
				new DataTypes.ConfigInteger("alpha", trackingData.getAlpha()).buildString();
		return out;
	}

	public boolean readContent(String content) {
		String data[] = content.split(" \\| ");
		if(data.length != 8) {
			return false;
		}
		entityID = null;
		trackingData = new TrackingData();
		DataTypes.ConfigString configString = new DataTypes.ConfigString();
		for(String str : data) {
			if(!configString.readValue(str)) {
				return false;
			}
			if(configString.name.equals("EntityID")) {
				entityID = configString.value;
			}else if(configString.name.equals("render")) {
				trackingData.renderGNS.set(Boolean.parseBoolean(configString.value));
			}else if(configString.name.equals("time")) {
				trackingData.timeGNS.set(Float.parseFloat(configString.value));
			}else if(configString.name.equals("thickness")) {
				trackingData.thicknessGNS.set(Float.parseFloat(configString.value));
			}else if(configString.name.equals("red")) {
				trackingData.redGNS.set(Integer.parseInt(configString.value));
			}else if(configString.name.equals("green")) {
				trackingData.greenGNS.set(Integer.parseInt(configString.value));
			}else if(configString.name.equals("blue")) {
				trackingData.blueGNS.set(Integer.parseInt(configString.value));
			}else if(configString.name.equals("alpha")) {
				trackingData.alphaGNS.set(Integer.parseInt(configString.value));
			}
		}
		if(entityID == null) {
			return false;
		}
		sourceMap.put(entityID, trackingData);
		return true;
	}

}
