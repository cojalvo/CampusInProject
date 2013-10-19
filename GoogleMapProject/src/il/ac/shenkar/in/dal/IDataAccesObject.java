package il.ac.shenkar.in.dal;

import java.util.List;

import android.graphics.drawable.Drawable;

import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.ParseObject;

import il.ac.shenkar.common.*;

public interface IDataAccesObject {
	
	void getEvents(DataAccesObjectCallBack<List<CampusInEvent>> callBack);
	void getMessages(DataAccesObjectCallBack<List<CampusInMessage>> callback);

	void sendMessage(CampusInMessage message,DataAccesObjectCallBack<Integer> callback);

	void sendEvent(CampusInEvent event,DataAccesObjectCallBack<Integer> callback);

	void updateLocation(CampusInLocation location,
			DataAccesObjectCallBack<Integer> callBack);

	void getUsersLocationInBackground(
			DataAccesObjectCallBack<List<CampusInUserLocation>> callBack);

	void getAllUsersInBackground(
			DataAccesObjectCallBack<List<CampusInUser>> callBack);

	void addFriendToFriendList(CampusInUser CampusInUser,
			DataAccesObjectCallBack<Integer> callBack);

	void loadCurrentCampusInUser(DataAccesObjectCallBack<CampusInUser> callBack);

	void putCurrentCampusInUserInbackground(CampusInUser currentCampusInUser,
			DataAccesObjectCallBack<Integer> callBack);

	void getProfilePicture(DataAccesObjectCallBack<Drawable> callBack);
	
	void getCurrentUserFriendsToScool(DataAccesObjectCallBack<List<CampusInUser>> callBack);
	
	void getAllCumpusInUsers(DataAccesObjectCallBack<List<CampusInUser>> callBack );
	
	void getAllCampusInUsersStartWith(String startWith,DataAccesObjectCallBack<List<CampusInUser>> callBack);

}
