package org.grusha.disrp4j.packet;

public enum PacketCode {
	HANDSHAKE(0),
	FRAME(1),
	CLOSE(2),
	PING(3),
	PONG(4);
	
	
	private final int code;
	
	PacketCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static PacketCode ofCode(int code) {
		for (PacketCode packetCode : values()) {
			if (packetCode.getCode() == code) {
				return packetCode;
			}
		}
		
		return null;
	}
}
