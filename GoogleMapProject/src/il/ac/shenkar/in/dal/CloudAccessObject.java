package il.ac.shenkar.in.dal;

import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInLocation;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;
import il.ac.shenkar.common.ParsingHelper;
import il.ac.shenkar.common.PersonalSettings;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.animation.ArgbEvaluator;
import android.text.format.DateFormat;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CloudAccessObject implements IDataAccesObject {
	// single tone
	private static CloudAccessObject instace;

	private CloudAccessObject() {
		observers = new LinkedList<IObserver>();
	}

	public static CloudAccessObject getInstance() {
		if (instace == null)
			instace = new CloudAccessObject();
		return instace;
	}

	private List<IObserver> observers;

	@Override
	public void addObserver(IObserver ob) {
		if (ob != null)
			observers.add(ob);
	}

	@Override
	public void removeObserver(IObserver ob) {
		if (ob != null)
			observers.remove(ob);
	}

	@Override
	public PersonalSettings getPersonalSettings(String userID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putPersonalSettings(PersonalSettings ps) {
		ParseObject po = new ParseObject("UserSettings");
		po.add("trend", String.valueOf(ps.getTrend()));
		po.add("year", ps.getYear());
		po.add("displayMyFriendOnly", ps.getShowMyFriendOnly());
		po.add("displayAll", ps.getShowAll());
		po.add("UserId", ps.getUserId());
		po.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				boolean succed = true;
				if (e != null)
					succed = false;
				updateObserves(ActionCode.Settings, succed);

			}

		});
	}

	@Override
	public List<CampusInEvent> getEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CampusInMessage> getMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendMessage(CampusInMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendEvent(CampusInEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocation(final CampusInUserLocation userLoction) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLocation");
		query.whereEqualTo("userID", userLoction.getUserID());
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> scoreList, ParseException e) {
				if (e == null) {
					// if the user exist in the cloud then the size of the
					// return list will be 1
					if (scoreList.size() == 1) {
						if (userLoction.getLocation() != null) {
							scoreList.get(0).remove("lat");
							scoreList.get(0)
									.add("lat",
											userLoction.getLocation()
													.getMapLocation().latitude);
							scoreList.get(0).remove("lon");
							scoreList
									.get(0)
									.add("lon",
											userLoction.getLocation()
													.getMapLocation().longitude);
							scoreList.get(0).remove("locationName");
							scoreList.get(0).add("locationName",
									userLoction.getLocation().getName());
							scoreList.get(0).remove("dateTime");
							scoreList.get(0).add("dateTime",
									userLoction.getLocation().getDate());
						}
						scoreList.get(0).saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								boolean succed = true;
								if (e != null) {
									succed = false;
									Log.e("cadan", "Upadet location was failed");
								}
								updateObserves(ActionCode.UserLocation, succed);
							}
						});
					}
					// if the user location info doesn't exist in the cloud than
					// report it.
					else {
						ParseObject po = new ParseObject("UserLocation");
						po.add("userID", userLoction.getUserID());
						po.add("lat", userLoction.getLocation()
								.getMapLocation().latitude);
						po.add("lon", userLoction.getLocation()
								.getMapLocation().longitude);
						po.add("locationName", userLoction.getLocation()
								.getName());
						po.add("dateTime", userLoction.getLocation().getDate());
						po.add("userName", userLoction.getUserName());
						po.saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								boolean succed = true;
								if (e != null)
									succed = false;
								updateObserves(ActionCode.UserLocation, succed);

							}
						});
					}

				} else {
					// the query was failed, update the observers.
					updateObserves(ActionCode.UserLocation, false);
				}
			}
		});

	}

	private void updateObserves(ActionCode code, boolean succed) {
		for (IObserver observer : observers) {
			if (succed)
				observer.actionDone(code);
			else
				observer.actionFail(code);
		}
	}

	/*
	 * get all users location from the cloud
	 * 
	 * @see
	 * il.ac.shenkar.in.dal.IDataAccesObject#getUsersInBackground(il.ac.shenkar
	 * .in.dal.DataAccesObjectCallBack)
	 */
	@Override
	public void getUsersLocationInBackground(
			final DataAccesObjectCallBack<List<CampusInUserLocation>> callBack) {
		if (callBack != null) {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLocation");
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> userList, ParseException e) {
					List<CampusInUserLocation> result = new ArrayList<CampusInUserLocation>();
					for (ParseObject parseObject : userList) {
						result.add(getCampusInUserLocationFromParseObject(parseObject));
					}
					callBack.done(result, e);
				}
			});
		}
	}

	private CampusInUserLocation getCampusInUserLocationFromParseObject(
			ParseObject obj) {
		CampusInUserLocation r = null;
		if (obj != null) {
			LatLng ll = new LatLng(obj.getDouble("lat"), obj.getDouble("lon"));
			r = new CampusInUserLocation();
			r.setUserID(obj.getString("userID"));
			r.setUserName(obj.getString("userName"));
			r.setLocation(new CampusInLocation());
			r.getLocation().setDate(obj.getUpdatedAt());
		}
		return r;
	}

	@Override
	public void getAllUsersInBackground(
			DataAccesObjectCallBack<List<CampusInUser>> callBack) {
		// TODO Auto-generated method stub

	}

	@Override
	public void putCampusInUserInbackground(CampusInUser user,
			final DataAccesObjectCallBack<Integer> callback) {
		ParseObject newUser = new ParseObject("CampusInUser");
		newUser.add("fistName", user.getFirstName());
		newUser.add("lastName", user.getLastName());
		newUser.add("facebookId", user.getFaceBookUserId());
		newUser.add("parseUserId", user.getParseUserId());
		newUser.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				callback.done(0, e);
			}
		});

	}

}
