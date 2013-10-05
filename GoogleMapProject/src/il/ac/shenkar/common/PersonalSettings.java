package il.ac.shenkar.common;

import java.util.Date;


public class PersonalSettings
{
	private Date lastUpdate;
	private Boolean showMyFriendOnly;
	private Boolean showAll;
	private String year;
	private String trend;
	private String userId;
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Boolean getShowMyFriendOnly() {
		return showMyFriendOnly;
	}
	public void setShowMyFriendOnly(Boolean showMyFriendOnly) {
		this.showMyFriendOnly = showMyFriendOnly;
	}
	public Boolean getShowAll() {
		return showAll;
	}
	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTrend() {
		return trend;
	}
	public void setTrend(String trend) {
		this.trend = trend;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
