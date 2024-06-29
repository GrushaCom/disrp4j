package org.grusha.disrp4j.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.grusha.disrp4j.models.presence.ButtonList;
import org.grusha.disrp4j.models.presence.ImageText;
import org.grusha.disrp4j.models.presence.Session;

public class RichPresence {
	
	private String details;
	private String state;
	private Session session;
	private ImageText large;
	private ImageText small;
	private ButtonList buttons;
	private boolean instance;
	
	public RichPresence() {
		this.buttons = new ButtonList();
	}
	
	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	public ImageText getLargeImage() {
		return large;
	}
	
	public void setLargeImage(ImageText large) {
		this.large = large;
	}
	
	public ImageText getSmallImage() {
		return small;
	}
	
	public void setSmallImage(ImageText small) {
		this.small = small;
	}
	
	public ButtonList getButtons() {
		return buttons;
	}
	
	public void setButtons(ButtonList buttons) {
		this.buttons = buttons;
	}
	
	public boolean isInstance() {
		return instance;
	}
	
	public void setInstance(boolean instance) {
		this.instance = instance;
	}
	
	public JsonObject toJsonObject() {
		JsonObject payload = new JsonObject();
		
		if (this.details != null && !this.details.isEmpty()) {
			payload.addProperty("details", this.details);
		}
		
		if (this.state != null && !this.state.isEmpty()) {
			payload.addProperty("state", this.state);
		}
		
		if (this.session.getStartData().getTime() > 0L) {
			JsonObject timestamps = new JsonObject();
			
			timestamps.addProperty("start", this.session.getStartData().getTime());
			
			if (this.session.getEndDate().getTime() >= this.session.getStartData().getTime()) {
				timestamps.addProperty("end", this.session.getEndDate().getTime());
			}
			
			payload.add("timestamps", timestamps);
		}
		
		JsonObject assets = new JsonObject();
		
		if (this.large != null) {
			if (this.large.getKey() != null && !this.large.getKey().isEmpty()) {
				assets.addProperty("large_image", this.large.getKey());
				
				if (this.large.getText() != null && !this.large.getText().isEmpty()) {
					assets.addProperty("large_text", this.large.getText());
				}
			}
		}
		
		if (this.small != null) {
			if (this.small.getKey() != null && !this.small.getKey().isEmpty()) {
				assets.addProperty("small_image", this.small.getKey());
				
				if (this.small.getText() != null && !this.small.getText().isEmpty()) {
					assets.addProperty("small_text", this.small.getText());
				}
			}
		}
		
		if (assets.has("large_image") || assets.has("small_image")) {
			payload.add("assets", assets);
		}
		
		if (this.buttons != null && this.buttons.hasButton()) {
			JsonArray buttonArray = new JsonArray();
			
			if (this.buttons.getFirst() != null) {
				JsonObject buttonJson = new JsonObject();
				
				buttonJson.addProperty("label", this.buttons.getFirst().getLabel());
				buttonJson.addProperty("url", this.buttons.getFirst().getUrl());
				
				buttonArray.add(buttonJson);
			}
			
			if (this.buttons.getSecond() != null) {
				JsonObject buttonJson = new JsonObject();
				
				buttonJson.addProperty("label", this.buttons.getSecond().getLabel());
				buttonJson.addProperty("url", this.buttons.getSecond().getUrl());
				
				buttonArray.add(buttonJson);
			}
			
			if (!buttonArray.isEmpty()) {
				payload.add("buttons", buttonArray);
			}
		}
		
		payload.addProperty("instance", this.instance);
		
		return payload;
	}
}
