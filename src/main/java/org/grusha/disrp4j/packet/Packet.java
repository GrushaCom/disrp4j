package org.grusha.disrp4j.packet;

import com.google.gson.JsonObject;
import org.grusha.disrp4j.packet.packets.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class Packet {
	
	private final JsonObject data;
	
	public Packet(JsonObject data) {
		this.data = data;
	}
	
	public JsonObject getData() {
		return this.data;
	}
	
	public byte[] toBytes() {
		byte[] data = this.data.toString().getBytes(StandardCharsets.UTF_8);
		
		ByteBuffer packet = ByteBuffer.allocate(data.length + 2 * Integer.BYTES);
		
		packet.putInt(Integer.reverseBytes(getPacketCode().getCode()));
		packet.putInt(Integer.reverseBytes(data.length));
		packet.put(data);
		
		return packet.array();
	}
	
	public abstract PacketCode getPacketCode();
	
	@Override
	public String toString() {
		return String.format("Packet(%s): (%s)", this.getPacketCode(), this.getData().toString());
	}
	
	public static Packet createByCode(PacketCode code, JsonObject json) {
		if (code != null) {
			if (code.equals(PacketCode.HANDSHAKE)) {
				return new HandshakePacket(json);
			} else if (code.equals(PacketCode.FRAME)) {
				return new FramePacket(json);
			} else if (code.equals(PacketCode.CLOSE)) {
				return new ClosePacket(json);
			} else if (code.equals(PacketCode.PING)) {
				return new PingPacket(json);
			} else if (code.equals(PacketCode.PONG)) {
				return new PongPacket(json);
			}
		}
		
		return null;
	}
	
	public static Packet createByCode(int code, JsonObject json) {
		return createByCode(PacketCode.ofCode(code), json);
	}
}
