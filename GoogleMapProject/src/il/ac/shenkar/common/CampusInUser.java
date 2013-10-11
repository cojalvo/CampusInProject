package il.ac.shenkar.common;

public class CampusInUser {
	public String getParseUserId() {
		return parseUserId;
	}
	public void setParseUserId(String parseUserId) {
		this.parseUserId = parseUserId;
	}
	public String getFaceBookUserId() {
		return faceBookUserId;
	}
	public void setFaceBookUserId(String faceBookUserId) {
		this.faceBookUserId = faceBookUserId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	private String parseUserId;
	private String faceBookUserId;
	private String firstName;
	private String lastName;
}
