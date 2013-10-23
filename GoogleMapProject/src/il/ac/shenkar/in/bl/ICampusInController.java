package il.ac.shenkar.in.bl;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;

public interface ICampusInController
{
	public boolean addEvent(CampusInEvent toAdd);
	void getCurrentUserAllEvents(ControllerCallback<List<CampusInEvent>> callBack);
	void getCurrentUserFriendList(ControllerCallback<List<CampusInUser>> callBack);
	void getCurrentUser(ControllerCallback<CampusInUser> callBack);
	
}
