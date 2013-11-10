package il.ac.shenkar.in.bl;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.graphics.drawable.Drawable;

import il.ac.shenkar.cadan.MapManager;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInLocation;
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
	CampusInUser getCurrentUser();
	void updateViewModel(ControllerCallback<Integer> callBack);
	Drawable  getFreindProfilePicture(String parseId,int width,int height);
	void getAllCumpusInUsers(ControllerCallback<List<CampusInUser>> callback);
	CampusInUser getCampusInUser(String parseId);
	void drawAllEvents(MapManager manager);
	void addFriendsToCurrentUserFriendList(List<CampusInUser> friendsTOAdd, ControllerCallback<List<Exception>> callback);
	void removeFriendsFromCurrentUserFriendList(List<CampusInUser> friendsToRemove,ControllerCallback<String> callback);
	CampusInEvent getEvent(String eventId);
	void addNotificationToEvent(CampusInEvent event);
	Boolean isMyFriend(CampusInUser user);
	Boolean isMyFriendToSchool(CampusInUser user);
	void startAutoViewModelUpdatingService();
	void stopAutoViewModelUpdatingService();
	void pauseAutoViewModelUpdatingService();
	void resumeAutoViewModelUpdatingService();
	CampusInMessage getMessage(String messageId);
	List<CampusInMessage> getAllMessages();
	void closePreferanceView();
	float getMyDistanceFrom(String parseObjId);
	public void setMapManager(MapManager mapManager);
	public void navigateTo(String objId);
	void HideMe();
	Boolean CanISeeTheFriend(String userParseId);
	void deleteMeFromEvent(String eventId);
	void deleteMeFromMessage(String messageId);
	void addToWatchList(String id);
	CampusInLocation getLocationFromQRCode(String qrCode) throws Exception;
	
}
