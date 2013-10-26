package il.ac.shenkar.cadan;

import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;
import il.ac.shenkar.in.dal.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ViewModel {
	private Context context;
	private IDataAccesObject dao = null;

	// TODO -remove it the context should be in he controller.
	public ViewModel(Context context) {
		super();
		this.context = context;
		dao = CloudAccessObject.getInstance();
		loadCurrentUser();
	}

	private CampusInUser currentUser = null;

	private HashMap<String, CampusInMessage> messages=new HashMap<String, CampusInMessage>();
	private HashMap<String, CampusInEvent> meeting = new HashMap<String, CampusInEvent>();
	private HashMap<String, CampusInEvent> tests = new HashMap<String, CampusInEvent>();
	private HashMap<String, CampusInEvent> lesons = new HashMap<String, CampusInEvent>();
	private HashMap<String, CampusInUserLocation> friendsLocation = new HashMap<String, CampusInUserLocation>();
	private HashMap<String, CampusInUser> friendsHash = new HashMap<String, CampusInUser>();

	/*
	 * this method update the view model, this method is synchronized in order to prevent data integrity.
	 *All the updated is being done  
	 */
	private synchronized void updateViewModel() {
		dao.getEvents(new DataAccesObjectCallBack<List<CampusInEvent>>() {

			@Override
			public void done(List<CampusInEvent> retObject, Exception e) {
				if (e == null && retObject != null) {
						meeting = new HashMap<String, CampusInEvent>();
						tests = new HashMap<String, CampusInEvent>();
						lesons = new HashMap<String, CampusInEvent>();
					for (CampusInEvent campusInEvent : retObject) 
					{
						addEvent(campusInEvent);
					}
				}

			}
		});

		//the location and the campus in user are connected this is the reason it is not safe to it asynchronous
		dao.getAllCumpusInUsers(new DataAccesObjectCallBack<List<CampusInUser>>() {

			@Override
			public void done(List<CampusInUser> retObject, Exception e) {

				if (e == null && retObject != null) {
					friendsHash = new HashMap<String, CampusInUser>();
					for (CampusInUser campusInUser : retObject) {
						friendsHash.put(campusInUser.getParseUserId(),
								campusInUser);
					}
				}
				dao.getUsersLocationInBackground(new DataAccesObjectCallBack<List<CampusInUserLocation>>() {

					@Override
					public void done(List<CampusInUserLocation> retObject, Exception e) {
						friendsLocation = new HashMap<String, CampusInUserLocation>();
						if (e == null && retObject != null) {
							for (CampusInUserLocation campusInUserLocation : retObject) {
								friendsLocation.put(campusInUserLocation.getUser()
										.getParseUserId(), campusInUserLocation);
							}
						}

					}
				});

			}
		});

		//update the messages
		dao.getMessages(new DataAccesObjectCallBack<List<CampusInMessage>>() {

			@Override
			public void done(List<CampusInMessage> retObject, Exception e) {
				if (e == null && retObject != null) {
					 messages=new HashMap<String, CampusInMessage>();
					for (CampusInMessage campusInMessage : retObject) {
						messages.put(campusInMessage.getParseId(),
								campusInMessage);
					}
				}

			}
		});
	}

	// this method will cerate a new thread in order to update the view mpdel in
	// case a thread is allready doing it the current thread will
	// sleep since
	public void updateViewModelInBackground(final DataAccesObjectCallBack<Integer> callBack) {
		
		new UpdateViewModelInBackground().execute(callBack);
		/*new AsyncTask<integer, Integer, Integer>() {

			@Override
			protected Integer doInBackground(integer... params) {
				updateViewModel();
				return 0;
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				callBack.done(0, null);
			}
		};
*/
	}

	private void loadCurrentUser() {
		dao.loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>() {

			@Override
			public void done(CampusInUser retObject, Exception e) {
				if (e == null && retObject != null)
					currentUser = retObject;
			}
		});
	}

	/*
	 * After finish update the view model obj an intent will invoke to update
	 * the receivers. (they will update the view with the changes.)
	 */
	// TODO-I think the best location will be in the controller.
	public Collection<CampusInEvent> getAllEvents() {
		if (meeting != null)
			return meeting.values();
		return new LinkedList<CampusInEvent>();

	}
	
	public Collection<CampusInMessage> getAllMessages() {
		if (messages != null)
			return messages.values();
		return new LinkedList<CampusInMessage>();
	}

	public Collection<CampusInUserLocation> getAllFriendsLocation() {

		if (friendsLocation != null)
			return friendsLocation.values();
		return new LinkedList<CampusInUserLocation>();
	}
	
	public Collection<CampusInUser> getAllFriends()
	{
		if(friendsHash!=null)
		{
			return friendsHash.values();
		}
		return new LinkedList<CampusInUser>();
	}
	
	/**
	 * add the event to the correct Hash Map
	 * @param toAdd
	 */
	public void addEvent (CampusInEvent toAdd)
	{
		if (toAdd != null)
		{
			switch (toAdd.getEventType()) {
			case CLASS:
				lesons.put(toAdd.getParseId(),
						toAdd);
				break;
			case TEST:
				tests.put(toAdd.getParseId(), toAdd);
				break;
			case MEETING:
				meeting.put(toAdd.getParseId(),
						toAdd);
				break;
			default:
				break;
			}
		}
		
	}
	
	public class UpdateViewModelInBackground extends AsyncTask<DataAccesObjectCallBack<Integer> ,Integer, Integer>
	{
		DataAccesObjectCallBack<Integer> callBack;
		@Override
		protected Integer doInBackground(DataAccesObjectCallBack<Integer>... params) 
		{
			callBack = params[0];
			updateViewModel();
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) 
		{
			callBack.done(0, null);
		}

		

		
	}
	
}
