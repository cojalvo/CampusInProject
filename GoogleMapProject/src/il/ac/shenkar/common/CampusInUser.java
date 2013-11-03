package il.ac.shenkar.common;

import java.io.Serializable;

public class CampusInUser implements Serializable
{
  private String faceBookUserId;
  private String firstName;
  private String lastName;
  private String parseUserId;
  private String trend;
  private String year;
  private String status;
  
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
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
public String getParseUserId() {
	return parseUserId;
}
public void setParseUserId(String parseUserId) {
	this.parseUserId = parseUserId;
}
public String getTrend() {
	return trend;
}
public void setTrend(String trend) {
	this.trend = trend;
}
public String getYear() {
	return year;
}
public void setYear(String year) {
	this.year = year;
}
}