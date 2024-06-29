package org.grusha.disrp4j;

import com.google.gson.JsonObject;
import org.grusha.disrp4j.models.RichPresence;
import org.grusha.disrp4j.packet.packets.FramePacket;
import org.grusha.disrp4j.pipe.Pipe;
import org.grusha.disrp4j.pipe.PipeStatus;

import java.io.Closeable;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class DiscordRPC implements Closeable {
	
	private final long appIdentifier;
	private volatile Pipe pipe;
	
	public DiscordRPC(long appIdentifier) {
		this.appIdentifier = appIdentifier;
		
	}
	
	public long getAppIdentifier() {
		return this.appIdentifier;
	}
	
	public PipeStatus getStatus() {
		if (this.pipe != null) {
			return this.pipe.getStatus();
		}
		
		return PipeStatus.UNINITIALIZED;
	}
	
	public boolean isConnected() {
		return this.getStatus().equals(PipeStatus.CONNECTED);
	}
	
	public boolean connect(Runnable callback) {
		if (!this.getStatus().equals(PipeStatus.CONNECTED)) {
			this.pipe = Pipe.openPipe(this.getAppIdentifier());
			
			if (this.pipe != null) {
				if (callback != null) {
					callback.run();
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean connect() {
		return this.connect(null);
	}
	
	public void sendRichPresence(RichPresence presence) {
		if (this.getStatus().equals(PipeStatus.CONNECTED)) {
			JsonObject payload = new JsonObject();
			JsonObject args = new JsonObject();
			
			String pr = ManagementFactory.getRuntimeMXBean().getName();
			
			args.addProperty("pid", Integer.parseInt(pr.substring(0, pr.indexOf('@'))));
			args.add("activity", presence == null ? null : presence.toJsonObject());
			
			payload.addProperty("cmd", "SET_ACTIVITY");
			payload.add("args", args);
			
			this.pipe.send(new FramePacket(payload));
		}
	}
	
	@Override
	public void close() throws IOException {
		if (this.getStatus().equals(PipeStatus.CONNECTED)) {
			this.pipe.close();
		}
	}
}
