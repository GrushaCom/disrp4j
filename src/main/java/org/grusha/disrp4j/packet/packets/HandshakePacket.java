package org.grusha.disrp4j.packet.packets;

import com.google.gson.JsonObject;
import org.grusha.disrp4j.packet.Packet;
import org.grusha.disrp4j.packet.PacketCode;

public class HandshakePacket extends Packet {

	public static final int VERSION = 1;
	
	public HandshakePacket(JsonObject json) {
		super(json);
	}

	@Override
	public PacketCode getPacketCode() {
		return PacketCode.HANDSHAKE;
	}
	
	public static HandshakePacket createHandshakePacket(long client) {
		JsonObject payload = new JsonObject();
		
		payload.addProperty("v", VERSION);
		payload.addProperty("client_id", Long.toString(client));
		
		return new HandshakePacket(payload);
	}
}