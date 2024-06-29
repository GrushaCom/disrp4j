package org.grusha.disrp4j.models.presence;

public class Button {

	private final String label;
	private final String url;

	public Button(String label, String url) {
		this.label = label;
		this.url = url;
	}

	public String getLabel() {
		return this.label;
	}

	public String getUrl() {
		return this.url;
	}
}