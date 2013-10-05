package il.ac.shenkar.common;

public class CampusInMessage 
{
	private String headLine;		
	private String content;
	//the location that the owner put the message
	private CampusInLocation location;
	//the parse owner id 
	private String ownerId;	
	//global messages will be available for all, only the owner can delete the event
	private boolean isGlobal;	
	//the user that receive the message, if there is no receiver this will be the same as 
	// the owner id, only a global message can be without receiver,
	private String receiverId;
	public String getHeadLine() {
		return headLine;
	}
	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public CampusInLocation getLocation() {
		return location;
	}
	public void setLocation(CampusInLocation location) {
		this.location = location;
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


}
