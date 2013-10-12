package il.ac.shenkar.common;

public class CampusInUserChecked 
{
	private CampusInUser user;
	private boolean checked = false;
	public CampusInUser getUser() {
		return user;
	}
	public void setUser(CampusInUser user) {
		this.user = user;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public CampusInUserChecked(CampusInUser user) {
		super();
		this.user = user;
	}
	public CampusInUserChecked() {
		super();
	}
	
	

}
