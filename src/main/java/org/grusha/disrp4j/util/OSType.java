package org.grusha.disrp4j.util;

import java.util.Locale;

public enum OSType {
	WINDOWS("Windows"),
	LINUX("Linux"),
	DARWIN("Darwin"),
	UNIX("Unix"),
	UNKNOWN(null);
	
	public final String name;
	
	OSType(String name) {
		this.name = name;
	}
	
	public static OSType getOperationSystem() {
		String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
		
		if (osName.contains("windows")) {
			return WINDOWS;
		} else if (osName.contains("linux")) {
			return LINUX;
		} else if (osName.contains("mac") || osName.contains("darwin")) {
			return DARWIN;
		} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
			return UNIX;
		}
		
		return UNKNOWN;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isUnixBased() {
		return (this.equals(LINUX) || this.equals(DARWIN) || this.equals(UNIX));
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
