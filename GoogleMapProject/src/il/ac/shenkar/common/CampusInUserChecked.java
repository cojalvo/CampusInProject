package il.ac.shenkar.common;

import il.ac.shenkar.in.bl.Controller;
import android.graphics.drawable.Drawable;

public class CampusInUserChecked
{
    private CampusInUser user;
    private boolean checked = false;
    private Drawable profilePicture;

    public Drawable getProfilePicture()
    {
	return profilePicture;
    }

    public void setProfilePicture(Drawable profilePicture)
    {
	this.profilePicture = profilePicture;
    }

    public CampusInUser getUser()
    {
	return user;
    }

    public void setUser(CampusInUser user)
    {
	this.user = user;
    }

    public boolean isChecked()
    {
	return checked;
    }

    public void setChecked(boolean checked)
    {
	this.checked = checked;
    }

    public CampusInUserChecked(CampusInUser user)
    {
	super();
	this.user = user;
	this.profilePicture = Controller.getInstance(null).getFreindProfilePicture(user.getParseUserId(), 40, 40);
    }

    public CampusInUserChecked()
    {
	super();
    }

}
