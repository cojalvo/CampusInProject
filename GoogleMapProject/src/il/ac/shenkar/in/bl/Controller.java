package il.ac.shenkar.in.bl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;
import il.ac.shenkar.cadan.Main;
import il.ac.shenkar.cadan.MapManager;
import il.ac.shenkar.cadan.MessageHalper;
import il.ac.shenkar.cadan.R;
import il.ac.shenkar.cadan.ViewModel;
import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.dal.IDataAccesObject;
import il.ac.shenkar.in.services.ModelUpdateService;

/**
 * the Controller Object is a singleton object which responsible to keep the
 * data of the Program consistentive with the DB to get instance of controller
 * you will have to supply with Context
 * 
 * @author Jacob
 * 
 */
public class Controller implements ICampusInController
{
    private static Controller instance = null;
    private Boolean viewModelIsUpdating = false;
    private Context context;
    private IDataAccesObject cloudAccessObject;
    private NotificationManager notificationManager;
    private ViewModel viewModel;
    private CampusInUser currentUser;
    private MapManager mapManager;
    private Boolean updatingViewModel = false;
    private Boolean updateAgainViewModel = false;
    private Boolean viewModelServiceRunning = false;
    /**
     * this List will hold all of the Events we want to save to the cloud only
     * if the save is successful the event object will be thrown from the list
     */
    private List<CampusInEvent> saveToCLoudEventQue;
    private List<CampusInMessage> saveToCloudMessageQue;

    private Controller(Context context)
    {
	// private c'tor
	this.context = context;
	cloudAccessObject = CloudAccessObject.getInstance();
	viewModel = new ViewModel(context);
	saveToCLoudEventQue = new ArrayList<CampusInEvent>();
	saveToCloudMessageQue = new ArrayList<CampusInMessage>();
	// get the current logged in user

	cloudAccessObject.loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>()
	{

	    @Override
	    public void done(CampusInUser retObject, Exception e)
	    {

		if (e == null && retObject != null)
		{
		    currentUser = retObject;
		}

	    }
	});

    }

    public static Controller getInstance(Context context)
    {
	if (instance == null)
	    instance = new Controller(context);
	return instance;
    }

    @Override
    public void getCurrentUserFriendList(final ControllerCallback<List<CampusInUser>> callBack)
    {
	MessageHalper.showProgressDialog("gettig " + currentUser.getFirstName() + " friends", context);
	if (callBack != null)
	    callBack.done(new ArrayList<CampusInUser>(viewModel.getAllFriends()), null);
    }

    @Override
    public CampusInUser getCurrentUser()
    {
    	return viewModel.getCurrentUser();
    }

    @Override
    public void getCurrentUserAllEvents(ControllerCallback<List<CampusInEvent>> callBack)
    {

	// in the mean tiime just a dummy implementation to display some data in
	// the list
	// TODO: yaki - later on to bring the real list of events from the
	// relevant object
	ArrayList<CampusInEvent> toReturn = new ArrayList<CampusInEvent>();
	CampusInEvent currEvent;
	if (callBack != null)
	    callBack.done(new ArrayList<CampusInEvent>(viewModel.getAllEvents()), null);

	// for (int i=0; i<40; i++)
	// {
	// currEvent = new CampusInEvent();
	// currEvent.setDate(new GregorianCalendar());
	// currEvent.setDescription("description " + i);
	// currEvent.setHeadLine("Title "+i);
	// toReturn.add(currEvent);
	// }
	//
	// callBack.done(toReturn, null);

    }

    @Override
    public void sendMessage(CampusInMessage message, final ControllerCallback<Integer> callBack)
    {
	// TODO: to draw the event to the user interface before saving it to the
	// cloud
	if (message != null)
	{
	    // add the toAdd Object to the messageQue;
	    saveToCloudMessageQue.add(message);
	    for (final CampusInMessage m : saveToCloudMessageQue)
	    {
		cloudAccessObject.sendMessage(m, new DataAccesObjectCallBack<Integer>()
		{

		    @Override
		    public void done(Integer retObject, Exception e)
		    {
			if (retObject != null && e == null)
			{
			    saveToCloudMessageQue.remove(m);
			}
			if (callBack != null)
			    callBack.done(retObject, e);
		    }
		});
	    }
	}
    }

    @Override
    public void saveEvent(CampusInEvent toAdd, final ControllerCallback<String> callBack)
    {
	// TODO: to draw the event to the user interface before saving it to the
	// cloud
	if (toAdd != null)
	{
	    // add the toAdd Object to the EventQue;
	    saveToCLoudEventQue.add(toAdd);
	    for (final CampusInEvent event : saveToCLoudEventQue)
	    {
		cloudAccessObject.sendEvent(event, new DataAccesObjectCallBack<String>()
		{

		    @Override
		    public void done(String retObject, Exception e)
		    {
			if (retObject != null)
			{
			    saveToCLoudEventQue.remove(event);
			    // viewModel
			    callBack.done(retObject, null);
			}
			else
			    callBack.done(null, e);
		    }
		});
	    }
	}
	else
	{
	    callBack.done(null, new NullPointerException("the Event you tried to save is null"));
	}
    }

    @Override
    public void updateViewModel(final ControllerCallback<Integer> callBack)
    {
	Log.i("CampusIn", "Start the updateing process of the view model");
	if (!updatingViewModel)
	{
	    Log.i("CampusIn", "updatingViewModel was false");
	    updatingViewModel = true;
	    viewModel.updateViewModelInBackground(new DataAccesObjectCallBack<Integer>()
	    {

		@Override
		public void done(Integer retObject, Exception e)
		{
		    if (callBack != null)
			callBack.done(retObject, e);
		    if (notificationManager == null)
			notificationManager = new NotificationManager(currentUser.getParseUserId(), context);
		    notificationManager.updateNotificationsToAllEvents(viewModel.getAllEvents());
		    Log.i("CampusIn", "updatig Notifications");
		    Log.i("CampusIn", "Finish updating the view model");
		    invokeViewModelUpdated();
		    updatingViewModel = false;
		}
	    });
	}
	else
	{
	    if (callBack != null)
		callBack.done(1, new Exception("View model is in a middle of updating process, try in the nex loop"));
	}
    }

    private void invokeViewModelUpdated()
    {
	Intent inti = new Intent();
	inti.setAction(CampusInConstant.VIEW_MODEL_UPDATED);
	if (context != null)
	    context.sendBroadcast(inti);
    }

    @Override
    public void drawAllEvents(MapManager manager)
    {
	Collection<CampusInEvent> events = viewModel.getAllEvents();
	for (CampusInEvent toDraw : events)
	{
	    mapManager.addOrUpdateEventMarker(toDraw);
	}
    }

    public void setMapManager(MapManager mapManager)
    {
	this.mapManager = mapManager;
    }

    @Override
    public void getCurrentUserFriendsLocationList(ControllerCallback<List<CampusInUserLocation>> callBack)
    {
	if (callBack != null)
	    callBack.done(new ArrayList<CampusInUserLocation>(viewModel.getAllFriendsLocation()), null);
    }

    @Override
    public CampusInEvent getEvent(String eventId)
    {
	return eventId == null ? null : viewModel.getEventById(eventId);
    }

    public void addEventToLocalMap(CampusInEvent toAdd)
    {
	viewModel.addEvent(toAdd);
    }

    @Override
    public void getAllCumpusInUsers(final ControllerCallback<List<CampusInUser>> callback)
    {

	if (callback != null)
	{
	    cloudAccessObject.getAllCumpusInUsers(new DataAccesObjectCallBack<List<CampusInUser>>()
	    {
		@Override
		public void done(List<CampusInUser> retObject, Exception e)
		{
		    List<CampusInUser> retList = retObject;
		    // remove all the friends to school -by default they are my
		    // friends and can't be removed
		    if (retList == null)
			retList = new ArrayList<CampusInUser>();
		    if (callback != null)
			callback.done(retList, e);
		}
	    });
	}
	else
	    return;
    }

    @Override
    public Drawable getFreindProfilePicture(String parseId, int width, int height)
    {
    	Drawable retPic;
    	if(currentUser.getParseUserId().equals(parseId))
    		retPic= viewModel.getCurrentUserProfilePic();
    	else
    		retPic = viewModel.getUserProfilePicture(parseId);
	if (retPic != null)
	    	return retPic;
	return context.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait);
    }

    private Drawable resizePic(Drawable paramDrawable, int paramInt1, int paramInt2) throws Exception
    {
	Bitmap localBitmap = ((BitmapDrawable) paramDrawable).getBitmap();
	return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(localBitmap, paramInt1, paramInt2, true));
    }

    @Override
    public CampusInUser getCampusInUser(String parseId)
    {
	return viewModel.getCampusInUser(parseId);
    }

    @Override
    public void addNotificationToEvent(CampusInEvent event)
    {
	if (notificationManager == null)
	    notificationManager = new NotificationManager(currentUser.getParseUserId(), context);
	notificationManager.updateNotification(event);
    }

    @Override
    public Boolean isMyFriend(CampusInUser user)
    {
	if (isMyFriendToSchool(user))
	    return true;
	List<CampusInUser> friends = new ArrayList<CampusInUser>(viewModel.getAllFriends());
	for (CampusInUser campusInUser : friends)
	{
	    if (campusInUser.getParseUserId().equals(user.getParseUserId()))
		return true;
	}
	return false;
    }

    @Override
    public Boolean isMyFriendToSchool(CampusInUser user)
    {
	if (user.getTrend().equals(currentUser.getTrend()) && user.getYear().equals(currentUser.getYear()))
	    return true;
	return false;
    }

    @Override
    public void startAutoViewModelUpdatingService()
    {
	if (ModelUpdateService.isRuning())
	    return;
	Toast.makeText(context, "vieModel service is not running- start it", 150).show();
	Intent i = new Intent(context, ModelUpdateService.class);
	context.startService(i);
    }

    @Override
    public void stopAutoViewModelUpdatingService()
    {
	context.stopService(new Intent(context, ModelUpdateService.class));
    }

    @Override
    public void pauseAutoViewModelUpdatingService()
    {
	Intent inti = new Intent();
	inti.setAction(ModelUpdateService.STOP_COMMAND);
	if (context != null)
	    context.sendBroadcast(inti);
    }

    @Override
    public void resumeAutoViewModelUpdatingService()
    {
	Intent inti = new Intent();
	inti.setAction(ModelUpdateService.START_COMMAND);
	if (context != null)
	    context.sendBroadcast(inti);
    }

    @Override
    public List<CampusInMessage> getAllMessages()
    {
	return new ArrayList<CampusInMessage>(viewModel.getAllMessages());
    }

    @Override
    public CampusInMessage getMessage(String messageId)
    {
	return messageId == null ? null : viewModel.getMessageById(messageId);
    }

    @Override
    public void closePreferanceView()
    {
	Main.closeDrawerLayout();
    }

    @Override
    public void removeFriendsFromCurrentUserFriendList(List<CampusInUser> friendsToRemove, final ControllerCallback<String> callback)
    {
	if (friendsToRemove != null && friendsToRemove.size() > 0)
	{
	    for (CampusInUser user : friendsToRemove)
	    {
		cloudAccessObject.removeFriendFromFriendList(user, new DataAccesObjectCallBack<Integer>()
		{

		    @Override
		    public void done(Integer retObject, Exception e)
		    {
			callback.done(retObject.toString(), e);
		    }
		});
	    }
	    updateViewModel(null);
	}

    }

    @Override
    public void addFriendsToCurrentUserFriendList(List<CampusInUser> friendsTOAdd, final ControllerCallback<List<Exception>> callback)
    {
	final List<Exception> toReturn = new ArrayList<Exception>();
	// for now i add it one by one
	// Cadan need to implement a method to save bulk of friends all at one
	if (friendsTOAdd != null && friendsTOAdd.size() > 0)
	{
	    for (CampusInUser user : friendsTOAdd)
	    {
		cloudAccessObject.addFriendToFriendList(user, new DataAccesObjectCallBack<Integer>()
		{

		    @Override
		    public void done(Integer retObject, Exception e)
		    {
			callback.done(toReturn, e);
		    }
		});
	    }
	    updateViewModel(null);

	}

    }

	@Override
	public float getMyDistanceFrom(String parseObjId) {
		return mapManager.getDistanceFromMe(parseObjId);
	}

	@Override
	public void navigateTo(String objId) {
		mapManager.moveCameraTo(objId, 30);
		
	}

	@Override
	public void HideMe() {
		cloudAccessObject.hideMe();
		
	}

	@Override
	public Boolean CanISeeTheFriend(String userParseId) {
		CampusInUserLocation ul=viewModel.getUserfriendLocation(userParseId);
		if(ul==null) return false;
		return ul.getLocation()!=null;
	}

}
