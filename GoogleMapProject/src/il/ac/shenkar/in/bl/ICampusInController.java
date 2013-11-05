package il.ac.shenkar.in.bl;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.graphics.drawable.Drawable;

import il.ac.shenkar.cadan.MapManager;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;

public interface ICampusInController
{
	void getCurrentUserAllEvents(ControllerCallback<List<CampusInEvent>> callBack);
	void saveEvent(CampusInEvent toAdd,ControllerCallback<String> callBack);
	void getCurrentUserFriendList(ControllerCallback<List<CampusInUser>> callBack);
	void getCurrentUserFriendsLocationList(ControllerCallback<List<CampusInUserLocation>> callBack);
	void sendMessage(CampusInMessage message,ControllerCallback<Integer> callBack);
	void getCurrentUser(ControllerCallback<CampusInUser> callBack);
	void updateViewModel(ControllerCallback<Integer> callBack);
	Drawable  getFreindProfilePicture(String parseId,int width,int height);
	void getAllCumpusInUsers(ControllerCallback<List<CampusInUser>> callback);
	CampusInUser getCampusInUser(String parseId);
	void drawAllEvents(MapManager manager);
	void addFriendsToCurrentUserFriendList(List<CampusInUser> friendsTOAdd);
	void removeFriendsFromCurrentUserFriendList(List<CampusInUser> friendsToRemove);
	CampusInEvent getEvent(String eventId);
	void navigateToEvent(CampusInEvent event);
	void addNotificationToEvent(CampusInEvent event);
	
}
