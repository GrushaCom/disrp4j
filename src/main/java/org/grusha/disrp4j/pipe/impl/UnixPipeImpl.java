package org.grusha.disrp4j.pipe.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.grusha.disrp4j.packet.Packet;
import org.grusha.disrp4j.packet.packets.ClosePacket;
import org.grusha.disrp4j.pipe.Pipe;
import org.grusha.disrp4j.pipe.PipeStatus;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class UnixPipeImpl extends Pipe {
	
	private final SocketChannel channel;
	
	public UnixPipeImpl(String location) {
		try {
			this.channel = SocketChannel.open(StandardProtocolFamily.UNIX);
			this.channel.connect(UnixDomainSocketAddress.of(location));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Packet read() throws Exception {
		if (this.getStatus().equals(PipeStatus.CONNECTING) || this.getStatus().equals(PipeStatus.CONNECTED)) {
			ByteBuffer buf = ByteBuffer.allocate(8);
			while (buf.position() < buf.capacity()) {
				this.channel.read(buf);
			}
			
			buf.flip();
			
			int packetCode = Integer.reverseBytes(buf.getInt());
			
			buf = ByteBuffer.allocate(Integer.reverseBytes(buf.getInt()));
			
			while (buf.position() < buf.capacity()) {
				this.channel.read(buf);
			}
			
			buf.flip();
			
			return Packet.createByCode(packetCode, JsonParser.parseString(new String(buf.array())).getAsJsonObject());
		} else if (this.getStatus().equals(PipeStatus.CLOSED)) {
			return new ClosePacket(new JsonObject());
		}
		
		return null;
	}
	
	@Override
	public void write(byte[] bytes) throws Exception {
		if (this.getStatus().equals(PipeStatus.CONNECTING) || this.getStatus().equals(PipeStatus.CONNECTED)) {
			ByteBuffer buf = ByteBuffer.allocate(bytes.length);
			
			buf.put(bytes);
			buf.flip();
			
			while (buf.hasRemaining()) {
				this.channel.write(buf);
			}
		}
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		
		this.channel.close();
	}
}
