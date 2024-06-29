package org.grusha.disrp4j.models.presence;

import java.util.Date;

public class Session {
	
	private final Date start;
	private final Date end;
	
	public Session(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	public Session(long start, long end) {
		this(new Date(start), new Date(end));
	}
	
	public Session(Date start) {
		this(start, new Date(0));
	}
	
	public Session(long start) {
		this(start, 0);
	}
	
	public Date getStartData() {
		return this.start;
	}
	
	public Date getEndDate() {
		return this.end;
	}
}
