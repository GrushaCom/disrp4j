package org.grusha.disrp4j.models.presence;

public class ButtonList {

	private Button first;
	private Button second;

	public ButtonList() {
		this.first = null;
		this.second = null;
	}

	public Button getFirst() {
		return this.first;
	}

	public Button getSecond() {
		return this.second;
	}

	public void setFirst(Button first) {
		this.first = first;
	}

	public void setSecond(Button second) {
		this.second = second;
	}

	public ButtonList addButton(Button button) {
		if (this.first == null) {
			this.first = button;

			return this;
		}

		if (this.second == null) {
			this.second = button;

			return this;
		}

		return this;
	}

	public boolean hasButton() {
		return (this.first != null || this.second != null);
	}
}