package il.ac.shenkar.common;

public class CampusInUserLocation 
{
	//the location in the campus
	private CampusInLocation location;
	//the parse user id
	private String userID;
	private String userName;
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getFaceBookId()
	{
		return faceBookId;
	}
	public void setFaceBookId(String faceBookId)
	{
		this.faceBookId = faceBookId;
	}
	private String faceBookId;
	
	public CampusInLocation getLocation() {
		return location;
	}
	public void setLocation(CampusInLocation location) {
		this.location = location;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public CampusInUserLocation(CampusInLocation location, String userID) {
		super();
		this.location = location;
		this.userID = userID;
	}

}
