package org.grusha.disrp4j.models.presence;

public class ImageText {

	private final String key;
	private final String text;

	public ImageText(String key, String text) {
		this.key = key;
		this.text = text;
	}

	public ImageText(String key) {
		this(key, "");
	}

	public String getKey() {
		return this.key;
	}

	public String getText() {
		return this.text;
	}
}