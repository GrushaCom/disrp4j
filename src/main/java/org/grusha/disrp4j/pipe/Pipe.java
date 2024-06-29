package org.grusha.disrp4j.pipe;

import com.google.gson.JsonObject;

import org.grusha.disrp4j.packet.Packet;
import org.grusha.disrp4j.packet.packets.ClosePacket;
import org.grusha.disrp4j.packet.packets.HandshakePacket;
import org.grusha.disrp4j.pipe.impl.UnixPipeImpl;
import org.grusha.disrp4j.pipe.impl.WinPipeImpl;
import org.grusha.disrp4j.util.OSType;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;

public abstract class Pipe implements Closeable {
	
	private PipeStatus status;
	
	public Pipe() {
		this.status = PipeStatus.UNINITIALIZED;
	}
	
	public PipeStatus getStatus() {
		return status;
	}
	
	public boolean isConnected() {
		return this.getStatus().equals(PipeStatus.CONNECTED);
	}
	
	public void connect(long id) throws Exception {
		try {
			this.status = PipeStatus.CONNECTING;
			
			this.send(HandshakePacket.createHandshakePacket(id));
			
			this.read();
			
			this.status = PipeStatus.CONNECTED;
		} catch (Exception e) {
			this.disconnect();
			
			throw e;
		}
	}
	
	public void disconnect() {
		this.status = PipeStatus.DISCONNECTED;
	}
	
	public void send(Packet packet) {
		try {
			packet.getData().addProperty("nonce", UUID.randomUUID().toString());
			
			this.write(packet.toBytes());
		} catch (Exception e) {
			this.disconnect();
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() throws IOException {
		this.send(new ClosePacket(new JsonObject()));
		
		this.status = PipeStatus.CLOSED;
	}
	
	public abstract Packet read() throws Exception;
	
	public abstract void write(byte[] bytes) throws Exception;
	
	public static Pipe openPipe(long id) {
		Pipe pipe = null;

		for (int i = 0; i < 10; i++) {
			try {
				String location = getPipeLocation(i);
				
				pipe = createPipe(location);
				
				pipe.connect(id);

				break;
			} catch (Exception e) {
				pipe = null;
			}
		}

		return pipe;
	}
	
	public static Pipe createPipe(String location) {
		if (OSType.getOperationSystem().equals(OSType.WINDOWS)) {
			return new WinPipeImpl(location);
		} else if (OSType.getOperationSystem().isUnixBased()) {
			return new UnixPipeImpl(location);
		} else {
			throw new RuntimeException("Unsupported OS: " + System.getProperty("os.name").toLowerCase());
		}
	}
	
	public static String getPipeLocation(int i) {
		String[] unixPaths = {"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"};
		
		OSType os = OSType.getOperationSystem();
		
		if (os.equals(OSType.WINDOWS)) {
			return "\\\\?\\pipe\\discord-ipc-" + i;
		} else if (os.isUnixBased()) {
			String tmppath = null;
			
			for (String str : unixPaths) {
				tmppath = System.getenv(str);
				
				if (tmppath != null) {
					break;
				}
			}
			
			if (tmppath == null) {
				tmppath = "/tmp";
			}
			
			return tmppath + "/discord-ipc-" + i;
		}
		
		return null;
	}
}
