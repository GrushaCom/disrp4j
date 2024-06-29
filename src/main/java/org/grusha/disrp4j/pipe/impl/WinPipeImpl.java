package org.grusha.disrp4j.pipe.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.grusha.disrp4j.packet.Packet;
import org.grusha.disrp4j.packet.packets.ClosePacket;
import org.grusha.disrp4j.pipe.Pipe;
import org.grusha.disrp4j.pipe.PipeStatus;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WinPipeImpl extends Pipe {
	
	private final RandomAccessFile file;
	
	public WinPipeImpl(String location) {
		try {
			this.file = new RandomAccessFile(location, "rw");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Packet read() throws Exception {
		while (this.file.length() == 0 && this.getStatus().equals(PipeStatus.CONNECTED)) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException ignored) {}
		}
		
		if (this.getStatus().equals(PipeStatus.CONNECTING) || this.getStatus().equals(PipeStatus.CONNECTED)) {
			int packetCode = Integer.reverseBytes(this.file.readInt());
			int len = Integer.reverseBytes(this.file.readInt());
			byte[] bytes = new byte[len];
			
			this.file.readFully(bytes);
			
			return Packet.createByCode(packetCode, JsonParser.parseString(new String(bytes)).getAsJsonObject());
		} else if (this.getStatus().equals(PipeStatus.CLOSED)) {
			return new ClosePacket(new JsonObject());
		}
		
		return null;
	}
	
	@Override
	public void write(byte[] bytes) throws Exception {
		if (this.getStatus().equals(PipeStatus.CONNECTING) || this.getStatus().equals(PipeStatus.CONNECTED)) {
			this.file.write(bytes);
		}
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		
		this.file.close();
	}
}
