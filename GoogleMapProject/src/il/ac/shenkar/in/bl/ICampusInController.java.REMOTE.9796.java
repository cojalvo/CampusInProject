package il.ac.shenkar.in.bl;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;

public interface ICampusInController
{
	void  addEvent(CampusInEvent toAdd,ControllerCallback<Integer> callBack);
	void getCurrentUserAllEvents(ControllerCallback<List<CampusInEvent>> callBack);
	void getCurrentUserFriendList(ControllerCallback<List<CampusInUser>> callBack);
	void sendMessage(CampusInMessage message,ControllerCallback<Integer> callBack);
	void getCurrentUser(ControllerCallback<CampusInUser> callBack);
	void updateViewModel();
	
}
