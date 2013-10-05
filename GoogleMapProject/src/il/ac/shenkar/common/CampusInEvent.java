package il.ac.shenkar.common;

import java.util.Date;


/**
 * This class represent an event in the campus
 * There are many kind of event as describe in the CampusInEventType 
 * @author cadan
 *
 */
public class CampusInEvent {
	private String headLine;		
	private String description;
	private CampusInLocation location;
	private Date date;
	private CampusInEventType eventType;
	//the parse id 
	private String ownerId;		
	//global events will be available for all, only the owner can delete the event
	private boolean isGlobal;	
	//the user that receive the event, if there is no receiver this will be the same as 
	// the owner id
	private String receiverId;
	public String getHeadLine() {
		return headLine;
	}
	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CampusInLocation getLocation() {
		return location;
	}
	public void setLocation(CampusInLocation location) {
		this.location = location;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public boolean isGlobal() {
		return isGlobal;
	}
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	public CampusInEventType getEventType() {
		return eventType;
	}
	public void setEventType(CampusInEventType eventType) {
		this.eventType = eventType;
	}

	public enum CampusInEventType
	{
		TEST,MEETING
	}
}
