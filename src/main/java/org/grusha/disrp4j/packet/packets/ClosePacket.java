package org.grusha.disrp4j.packet.packets;

import com.google.gson.JsonObject;
import org.grusha.disrp4j.packet.Packet;
import org.grusha.disrp4j.packet.PacketCode;

public class ClosePacket extends Packet {

	public ClosePacket(JsonObject json) {
		super(json);
	}

	@Override
	public PacketCode getPacketCode() {
		return PacketCode.CLOSE;
	}
}