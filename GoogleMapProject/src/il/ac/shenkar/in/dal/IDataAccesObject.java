package il.ac.shenkar.in.dal;

import java.util.List;

import android.graphics.drawable.Drawable;

import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.ParseObject;

import il.ac.shenkar.common.*;

public interface IDataAccesObject
{

    void getEvents(DataAccesObjectCallBack<List<CampusInEvent>> callBack);

    void getMessages(DataAccesObjectCallBack<List<CampusInMessage>> callback);

    void sendMessage(CampusInMessage message, DataAccesObjectCallBack<Integer> callback);

    // Return the Parse Id if the Object we just saved
    // use it in order to enter the id to the Hash Maps in the View Model object
    void sendEvent(CampusInEvent event, DataAccesObjectCallBack<String> callback);

    void updateLocation(CampusInLocation location, DataAccesObjectCallBack<Integer> callBack);

    void getUsersLocationInBackground(DataAccesObjectCallBack<List<CampusInUserLocation>> callBack);

    void addFriendToFriendList(CampusInUser userToAdd, DataAccesObjectCallBack<Integer> callBack);

    void removeFriendFromFriendList(CampusInUser userToRemove, DataAccesObjectCallBack<Integer> callBack);

    // return all the friends of the current user
    // By default his class friend and all the rest he added manually
    void getCurrentCampusInUserFriends(DataAccesObjectCallBack<List<CampusInUser>> callBack);

    void loadCurrentCampusInUser(DataAccesObjectCallBack<CampusInUser> callBack);

    void putCurrentCampusInUserInbackground(CampusInUser currentCampusInUser, DataAccesObjectCallBack<Integer> callBack);

    void getProfilePicture(DataAccesObjectCallBack<Drawable> callBack);

    void getAllCumpusInUsers(DataAccesObjectCallBack<List<CampusInUser>> callBack);

    /*
     * return all the CampuInUser THAT THERE first or last name start with
     */
    void getAllCampusInUsersStartWith(String startWith, DataAccesObjectCallBack<List<CampusInUser>> callBack);

	void updateCurrentUserStatus(DataAccesObjectCallBack<Integer> callback,
			String status);

	void getFriendProfilePicture(String facebookId,
			DataAccesObjectCallBack<Drawable> callback);

}
