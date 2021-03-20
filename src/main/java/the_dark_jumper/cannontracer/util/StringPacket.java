package the_dark_jumper.cannontracer.util;

import java.nio.charset.StandardCharsets;
import net.minecraft.network.PacketBuffer;

public class StringPacket {
	private String data;
	private byte[] bytes;

	public StringPacket(){
	}

	public StringPacket(PacketBuffer buffer){
		int readableBytes = buffer.readableBytes();
		bytes = new byte[readableBytes];
		for(int i = 0; i < readableBytes; i++){
			bytes[i] = buffer.readByte();
		}

		data = new String(bytes, StandardCharsets.UTF_8);
	}

	public String getData() {
		return data;
	}

	public StringPacket setData(String data) {
		this.data = data;
		return this;
	}

	public void encode(PacketBuffer buffer){
		buffer.writeBytes(data.getBytes());
	}
}
