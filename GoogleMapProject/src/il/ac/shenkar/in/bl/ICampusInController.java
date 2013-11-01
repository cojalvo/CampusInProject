package il.ac.shenkar.in.bl;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;

import il.ac.shenkar.cadan.MapManager;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;

public interface ICampusInController
{
	void getCurrentUserAllEvents(ControllerCallback<List<CampusInEvent>> callBack);
	void saveEvent(CampusInEvent toAdd,ControllerCallback<Integer> callBack);
	void getCurrentUserFriendList(ControllerCallback<List<CampusInUser>> callBack);
	void getCurrentUserFriendsLocationList(ControllerCallback<List<CampusInUserLocation>> callBack);
	void sendMessage(CampusInMessage message,ControllerCallback<Integer> callBack);
	void getCurrentUser(ControllerCallback<CampusInUser> callBack);
	void updateViewModel(ControllerCallback<Integer> callBack);
	
	void drawAllEvents(MapManager manager);
	
}
