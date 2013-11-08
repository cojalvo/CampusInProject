package il.ac.shenkar.cadan;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import il.ac.shenkar.cadan.PrefsFragment.OnPreferenceSelectedListener;
import il.ac.shenkar.common.CampusInConstant;
import java.util.ArrayList;

import il.ac.shenkar.cadan.ChooseFriendsFragment.ChooseFriendAction;
import il.ac.shenkar.cadan.ChooseFriendsFragment.onFriendsAddedListener;
import il.ac.shenkar.cadan.AddNewEventFragment.onNewEventAdded;
import il.ac.shenkar.cadan.MapManager.MarkerType;
import il.ac.shenkar.cadan.SendMassageFragment.onNewMassagecreated;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInLocation;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;
import il.ac.shenkar.common.GuiHelper;
import il.ac.shenkar.common.ParsingHelper;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;
import il.ac.shenkar.in.bl.ICampusInController;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.services.InitLocations;
import il.ac.shenkar.in.services.LocationReporterServise;
import il.ac.shenkar.in.services.ModelUpdateService;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import android.R.integer;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.*;
import android.support.v4.widget.DrawerLayout;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AnalogClock;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnPreferenceSelectedListener, OnMapLongClickListener, OnMarkerClickListener, onFriendsAddedListener, onNewEventAdded,
	onNewMassagecreated
{
    CameraPosition lastPos = new CameraPosition(new LatLng(0, 0), 2, 2, 2);
    GoogleMap map = null;
    private static DrawerLayout mDrawerLayout;
    private LatLng lastMapLongClick = null;
    private Marker lastMarkerClicked = null;
    private ActionBarDrawerToggle mDrawerToggle;
    static final LatLng HAMBURG = new LatLng(20, 25);
    static final LatLng KIEL = new LatLng(15, 10);
    private PopupWindow pwindo;
    private ICampusInController controller;
    private MapManager mapManager = null;
    private BroadcastReceiver viewModelUpdatedReciever;
    private Vibrator vibrator = null;
    FragmentManager fm;
    private int myScreenWidth;
    private int myScreenHeight;
    private float widthMultScreenFactor;
    private Float heightMultScreenFactor;
    private boolean viewModelServiceRunning = false;
    private boolean inExitProcess = false;
    CampusInUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

	if (savedInstanceState == null || !savedInstanceState.getBoolean("initLocationDone"))
	{
	    new InitLocations().execute(this);
	}
	Toast.makeText(this, "onCreate was called", 150).show();
	super.onCreate(savedInstanceState);
	Parse.initialize(this, "3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE", "UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
	ParseFacebookUtils.initialize("635010643194002");
	Log.i("Main", "onCreate was called");
	calcMyScreen();
	vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	// inflate the drawerLayour
	// set as content view
	this.setContentView(R.layout.main);
	this.mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
	controller = Controller.getInstance(this);
	initMapManager();
	// controller.setMapManager(mapManager);
	// ActionBarDrawerToggle ties together the the proper interactions
	// between the sliding drawer and the action bar app icon
	mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
	mDrawerLayout, /* DrawerLayout object */
	R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
	R.string.drawer_open, /* "open drawer" description for accessibility */
	R.string.drawer_close /* "close drawer" description for accessibility */
	)
	{
	    public void onDrawerClosed(View view)
	    {
	    	getActionBar().setTitle("CampusIn");
	    	invalidateOptionsMenu(); // creates call to
	    	
	    }

	    public void onDrawerOpened(View drawerView)
	    {
	    	getActionBar().setTitle("CampusIn");
		invalidateOptionsMenu(); // creates call to
		// onPrepareOptionsMenu()
	    }
	};
	
	// set listener to the menu
	mDrawerLayout.setDrawerListener(mDrawerToggle);
	getActionBar().setDisplayHomeAsUpEnabled(true);
	// getActionBar().setHomeButtonEnabled(true);
	/*
	 * // Move the camera instantly to hamburg with a zoom of 15.
	 * map.setOnCameraChangeListener(new OnCameraChangeListener() {
	 * 
	 * @Override public void onCameraChange(CameraPosition position) {
	 * if(position
	 * .target.latitude<0||position.target.latitude>40||position.target
	 * .longitude<0||position.target.longitude>50) {
	 * map.moveCamera(CameraUpdateFactory.newCameraPosition(lastPos)); }
	 * else { lastPos=position; } // TODO Auto-generated method stub
	 * 
	 * } });
	 */

	// Get the SearchView and set the searchable configuration
	SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	SearchView searchView = (SearchView) findViewById(R.id.searchView1);
	SearchableInfo searchInfo = searchManager.getSearchableInfo(getComponentName());
	if (searchInfo == null)
	{
	    Toast.makeText(getApplicationContext(), "Search info is null", 500).show();
	}
	searchView.setSearchableInfo(searchInfo);
	searchView.setIconifiedByDefault(false); // Do not iconify the widget;
						 // expand it by default
	// if the bundle is null than it the first time the application is
	// running
	// if not check if the locationServiceStart flag is true; (this might be
	// redundant but maybe in the future we will need to use it
	if (savedInstanceState == null || !savedInstanceState.getBoolean("locationServiceStart"))
	{
	    startLocationReportServise();
	    controller.startAutoViewModelUpdatingService();
	}
	
	//this is not done in seperate thread its just a mistake that the controller nead to get callback 
	controller.getCurrentUser(new ControllerCallback<CampusInUser>() {
		
		@Override
		public void done(CampusInUser retObject, Exception e) {
			currentUser=retObject;
		}
	});
	updateView();
	registerViewModelReciever();
	
    }

    private void calcMyScreen()
    {
	DisplayMetrics mat = GuiHelper.getDisplayMatric(this);
	if (mat != null)
	{
	    myScreenHeight = mat.heightPixels;
	    myScreenWidth = mat.widthPixels;
	    heightMultScreenFactor = (float) (GuiHelper.getHeightDimentionMultFactor(myScreenHeight) * (1.1));
	    widthMultScreenFactor = (float) (GuiHelper.getWidthDimentionMultFactor(myScreenWidth) * (1.1));
	}
    }

    @Override
    protected void onPause()
    {
	Log.i("Main", "onPause was called");
	Toast.makeText(this, "onPause was called", 150).show();
	if (!isFinishing())
	{
	    setPendingIntent();
	   // controller.pauseAutoViewModelUpdatingService();
	}

	super.onPause();
    }

    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	Toast.makeText(this, "onResume was called", 150).show();
	Log.i("Main", "onResume was called");
	//controller.resumeAutoViewModelUpdatingService();
	super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {

	super.onSaveInstanceState(outState);
	// update the bundle that the service is running to prevent many
	// binding.
	outState.putBoolean("locationServiceStart", true);
	outState.putBoolean("initLocationDone", true);
    }

    private void startLocationReportServise()
    {
	Intent i = new Intent(this, LocationReporterServise.class);
	// potentially add data to the intent
	i.putExtra("KEY1", "Value to be used by the service");
	this.startService(i);

    }

    private void initMapManager()
    {
	mapManager = MapManager.getInstance(((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(), GoogleMap.MAP_TYPE_NONE);

	mapManager.addGroundOverlay(R.drawable.shenkarmap_1, new LatLng(32.089518, 34.802128), new LatLng(32.090501, 34.803617), (float) 0.1);
	mapManager.moveCameraToLocation(new LatLng(32.089028, 34.80304), 18);
	mapManager.setOnMapLongClickListener(this);
	mapManager.setOnMarkerClickListener(this);
	mapManager.getMap().setInfoWindowAdapter(new InfoWindowAdapter()
	{

	    // use default look of the "container" info window
	    @Override
	    public View getInfoWindow(Marker arg0)
	    {
		return null;
	    }

	    @Override
	    public View getInfoContents(Marker marker)
	    {
		View v;
		String id;
		// get the marker type
		MarkerType markerType = mapManager.getMarkerType(marker);

		if (markerType == MarkerType.Person)
		{
			
		    // inflate the view
		    v = getLayoutInflater().inflate(R.layout.info_window_person_content_layout, null);

		    // get the marker (which is a person) information
		    id = mapManager.getCampusInUserIdFromMarker(marker);
		    CampusInUser currUser = controller.getCampusInUser(id);

		    // set the image view
		    ImageView imageView = (ImageView) v.findViewById(R.id.info_window_friend_profile_picture_imageView);
		    imageView.setImageDrawable(controller.getFreindProfilePicture(id, 40, 40));

		    // set the Name
		    TextView name = (TextView) v.findViewById(R.id.info_window_friend_name);
		    name.setText(currUser.getFirstName() + " " + currUser.getLastName());

		    // set the status
		    TextView status = (TextView) v.findViewById(R.id.info_window_status);
		    status.setText(currUser.getStatus());

		    return v;
		}
		else if (markerType == MarkerType.Event)
		{
		    v = getLayoutInflater().inflate(R.layout.info_window_event_content_layout, null);

		    // get the marker (which is a person) information
		    id = mapManager.getEventIdFromMarker(marker);
		    CampusInEvent currEvent = controller.getEvent(id);

		    // set the image view
		    /*
		     * ImageView imageView = (ImageView)
		     * v.findViewById(R.id.info_window_event_imageView);
		     * imageView.setImageDrawable();
		     */
		    // set the Name
		    TextView name = (TextView) v.findViewById(R.id.info_window_location_name);
		    name.setText(currEvent.getHeadLine() + " - " + currEvent.getLocation().getLocationName());

		    // set the status
		    TextView status = (TextView) v.findViewById(R.id.info_window_time);
		    status.setText(ParsingHelper.fromDateToString(currEvent.getDate(), "dd/MM/yyyy"));

		    return v;
		}
		else if (markerType == MarkerType.Message)
		{
		    v = getLayoutInflater().inflate(R.layout.info_windows_message_layout, null);

		    // get the marker (which is a person) information
		    id = mapManager.getMessageIdFromMarker(marker);
		    CampusInMessage currMessage = controller.getMessage(id);
		    LinearLayout contentLayout=(LinearLayout) v.findViewById(R.id.info_window_message_content_layout);

		    // set the image view
		    /*
		     * ImageView imageView = (ImageView)
		     * v.findViewById(R.id.info_window_event_imageView);
		     * imageView.setImageDrawable();
		     */
		    // set the image view
		    ImageView imageView = (ImageView) v.findViewById(R.id.info_window_message_friend_profile_picture);
		    imageView.setImageDrawable(controller.getFreindProfilePicture(currMessage.getOwnerId(), 40, 40));
		    // set the Name
		    TextView name = (TextView) v.findViewById(R.id.info_window_messge_friend_name);
		    name.setText(currMessage.getSenderFullName());

		    // set the status
		    TextView contennt = (TextView) v.findViewById(R.id.info_window_message_content);
		    contennt.setText(currMessage.getContent());

		    TextView distance = (TextView) v.findViewById(R.id.info_window_message_distance);
		    distance.setText(getDistanceStringFromMarker(marker));

		    return v;
		}
		return null;
	    }
	});
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
	super.onPostCreate(savedInstanceState);
	// Sync the toggle state after onRestoreInstanceState has occurred.
	mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
	super.onConfigurationChanged(newConfig);
	mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// Pass the event to ActionBarDrawerToggle, if it returns
	// true, then it has handled the app icon touch event
	if (mDrawerToggle.onOptionsItemSelected(item))
	{
	    return true;
	}

	// Handle presses on the action bar items
	Intent intent;
	switch (item.getItemId())
	{
	case R.id.action_add_event:
	    createEventProcess();
	    /*
	     * startActivity(intent);
	     * overridePendingTransition(R.anim.slide_in_left,
	     * R.anim.slide_out_left);
	     */
	    return true;
	case R.id.action_show_all_events:
	    /*
	     * intent = new Intent(this, JacobEventActivity.class);
	     * startActivity(intent);
	     * overridePendingTransition(R.anim.slide_in_left,
	     * R.anim.slide_out_left);
	     */

	    // try to move it to Fragment
	   /* android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
	    Fragment fragment = getFragmentManager().findFragmentByTag("dialog");
	    if (fragment != null)
	    {
		transaction.remove(fragment);
	    }
	    transaction.addToBackStack(null);
	    DiaplayEventListFragment newDiaplayEventListFragment = new DiaplayEventListFragment();
	    newDiaplayEventListFragment.show(transaction, "dialog");*/
	    return true;

	case R.id.action_add_friends:
	    android.app.FragmentTransaction ft1 = getFragmentManager().beginTransaction();
	    android.app.Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
	    if (prev1 != null)
	    {
		ft1.remove(prev1);
	    }
	    ft1.addToBackStack(null);

	    // Create and show the dialog.
	    ChooseFriendsFragment newFragment1 = ChooseFriendsFragment.newInstance(ChooseFriendAction.ADD, false, new ArrayList<CampusInUser>());
	    newFragment1.show(ft1, "dialog");
	case R.id.action_remove_friends:
	    android.app.FragmentTransaction ft11 = getFragmentManager().beginTransaction();
	    android.app.Fragment prev11 = getFragmentManager().findFragmentByTag("dialog");
	    if (prev11 != null)
	    {
		ft11.remove(prev11);
	    }
	    ft11.addToBackStack(null);

	    // Create and show the dialog.
	    ChooseFriendsFragment newFragment11 = ChooseFriendsFragment.newInstance(ChooseFriendAction.REMOVE, false, new ArrayList<CampusInUser>());
	    newFragment11.show(ft11, "dialog");
	    return true;
	case R.id.action_add_friends_from_cloud:
	//TODO: to remove, added to prefs fragment 
	    /*    android.app.FragmentTransaction ft111 = getFragmentManager().beginTransaction();
	    android.app.Fragment prev111 = getFragmentManager().findFragmentByTag("dialog");
	    if (prev111 != null)
	    {
		ft111.remove(prev111);
	    }
	    ft111.addToBackStack(null);

	    // Create and show the dialog.
	    AddOrRemoveFriendsFromCloudFragment newFragment111 = AddOrRemoveFriendsFromCloudFragment.newInstance(ChooseFriendAction.ADD);
	    newFragment111.show(ft111, "dialog");*/
	    return true;
	case R.id.action_remove_friends_from_cloud:
	  //TODO: to remove, added to prefs fragment 
	    /*android.app.FragmentTransaction ft1111 = getFragmentManager().beginTransaction();
	    android.app.Fragment prev1111 = getFragmentManager().findFragmentByTag("dialog");
	    if (prev1111 != null)
	    {
		ft1111.remove(prev1111);
	    }
	    ft1111.addToBackStack(null);

	    // Create and show the dialog.
	    AddOrRemoveFriendsFromCloudFragment newFragment1111 = AddOrRemoveFriendsFromCloudFragment.newInstance(ChooseFriendAction.REMOVE);
	    newFragment1111.show(ft1111, "dialog");*/
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {

	return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.main, menu);
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPreferenceSelected(String selected)
    {
	if (selected.equals(CampusInConstant.SETTINGS_EVENTS))
	{
	    startActivity(new Intent(this, EventsActivity.class));
	    return;
	}

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
	// if the user press the back button that doExit will invoke
	if ((keyCode == KeyEvent.KEYCODE_BACK))
	{
	    Log.d(this.getClass().getName(), "back button pressed");
	    // in case of process of long press than reset it
	    if (lastMapLongClick != null && pwindo.isShowing())
	    {
		lastMapLongClick = null;
		pwindo.dismiss();
	    }
	    // else ask if he would like to exit
	    else
	    {
		doExit();
	    }
	}
	else if (keyCode == KeyEvent.KEYCODE_HOME)
	{
	}
	return super.onKeyDown(keyCode, event);
    }

    //
    // Exit the application will ask the user if he sure.
    //
    private void doExit()
    {

	AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

	alertDialog.setPositiveButton("כן", new OnClickListener()
	{

	    @Override
	    public void onClick(DialogInterface dialog, int which)
	    {
		Main.this.stopService(new Intent(Main.this, LocationReporterServise.class));
		controller.stopAutoViewModelUpdatingService();
		inExitProcess = true;
		finish();
	    }
	});

	alertDialog.setNegativeButton("לא", null);

	alertDialog.setMessage("האם אתה בטוח שברצונך לצאת?");
	alertDialog.setTitle(" ");
	alertDialog.setIcon(R.drawable.campus_in_ico);
	alertDialog.show();
    }

    @Override
    public void onMapLongClick(LatLng point)
    {
	vibrator.vibrate(50);
	lastMapLongClick = point;
	initiatePopupWindow(PopUpKind.Menu);
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
	// we are in a middle of updating process
	try
	{
	    lastMarkerClicked = marker;

	    switch (mapManager.getMarkerType(marker))
	    {
	    case Event:
		initiatePopupWindow(PopUpKind.EventInfo);
		break;
	    case Person:
		initiatePopupWindow(PopUpKind.FriendInfo);
		break;
	    case Message:
	    	if(!canISeeTheMessageMarker(marker))
	    		{
	    			return true;
	    		}
	    	break;
	    default:
		break;
	    }
	    marker.showInfoWindow();

	}
	catch (Exception e)
	{
	    Toast.makeText(Main.this, "marker click was failed", 100).show();
	    Log.e("markerClick", "marker click was failed");
	}

	return true;

    }
    public Boolean canISeeTheMessageMarker(Marker marker)
    {
    	if(marker==null) return false;
    	String id= mapManager.getMessageIdFromMarker(marker);
    	if(id==null) return false;
    	CampusInMessage theMessage=controller.getMessage(id);
    	if(theMessage==null) return false;
    	if(theMessage.getOwnerId().equals(currentUser.getParseUserId())) return true;
    	int radius=theMessage.getReadInRadius();
    	if(radius==-1) return true;
    	float mydist=mapManager.getDistanceFromMe(marker);
    	if(mydist>radius) 
    	{
    		MessageHalper.showAlertDialog("אינך יכול לצפות בהודעה", " אתה לא מספיק קרוב עליך להדקדם עוד לפחות "+(String.format("%.2f", mydist-radius)) + "  מטר ", this);
    		return false;
    	}
    	return true;
    }
    public void showPopup(View v)
    {
	PopupMenu popup = new PopupMenu(this, v);
	MenuInflater inflater = popup.getMenuInflater();
	inflater.inflate(R.menu.main, popup.getMenu());
	popup.show();
    }

    /*
     * popup the menu in long press on the map
     */
    private void initiatePopupWindow(PopUpKind kind)
    {
	View layout = null;
	// We need to get the instance of the LayoutInflater
	LayoutInflater inflater = (LayoutInflater) Main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	try
	{
	    switch (kind)
	    {
	    case Menu:
		layout = inflater.inflate(R.layout.popup_menu, null);
		pwindo = new PopupWindow(layout, (int) (600 * widthMultScreenFactor), (int) (400 * heightMultScreenFactor), true);
		break;
	    case FriendInfo:
		layout = inflater.inflate(R.layout.friend_info_popup, null);
		CampusInUser user = controller.getCampusInUser(mapManager.getCampusInUserIdFromMarker(lastMarkerClicked));
		ImageView profilePictre = (ImageView) layout.findViewById(R.id.profile_picture);
		profilePictre
			.setImageDrawable(controller.getFreindProfilePicture(user.getParseUserId(), (int) (150 * widthMultScreenFactor), (int) (130 * heightMultScreenFactor)));
		TextView fulName = (TextView) layout.findViewById(R.id.full_name);
		TextView status = (TextView) layout.findViewById(R.id.face_status);
		TextView distance = (TextView) layout.findViewById(R.id.distance_from_me);
		distance.setText(getDistanceStringFromLastClickMarker());

		status.setText(user.getStatus());
		fulName.setText(user.getFirstName() + " " + user.getLastName());
		pwindo = new PopupWindow(layout, (int) (750 * widthMultScreenFactor), (int) (350 * heightMultScreenFactor), true);
		break;
	    case EventInfo:
		layout = inflater.inflate(R.layout.event_info_popup, null);
		LinearLayout l = (LinearLayout) layout.findViewById(R.id.event_pop_layout);

		pwindo = new PopupWindow(layout, (int) (750 * widthMultScreenFactor), (int) (800 * heightMultScreenFactor), true);
		CampusInEvent event = controller.getEvent(mapManager.getEventIdFromMarker(lastMarkerClicked));
		TextView title = (TextView) layout.findViewById(R.id.event_title);
		title.setText(event.getHeadLine());
		TextView description = (TextView) layout.findViewById(R.id.event_description);
		description.setText(event.getDescription());
		TextView location = (TextView) layout.findViewById(R.id.event_location);
		location.setText("מקום: " + event.getLocation().getLocationName());
		TextView time = (TextView) layout.findViewById(R.id.time_text);
		Date d = event.getDate();
		time.setText("שעה: " + ParsingHelper.fromDateToString(d, "HH:mm:ss"));
		TextView date = (TextView) layout.findViewById(R.id.date_text);
		TextView distanceEvent = (TextView) layout.findViewById(R.id.event_distance);
		distanceEvent.setText(getDistanceStringFromLastClickMarker());
		date.setText("תאריך: " + ParsingHelper.fromDateToString(d, "dd/MM/yyyy"));
		break;

	    default:
		break;
	    }
	    pwindo.setAnimationStyle(R.style.Animation);
	    pwindo.setFocusable(true);
	    ColorDrawable bcolor = new ColorDrawable(Color.WHITE);
	    pwindo.setBackgroundDrawable(bcolor);
	    pwindo.showAtLocation(layout, Gravity.BOTTOM, 0, 0);

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private String getDistanceStringFromMarker(Marker marker)
    {
	float dist = mapManager.getDistanceFromMe(marker);
	if (dist > 0)
	{
	    String unit;
	    String finalDist;
	    if (dist > 1000)
	    {
		unit = "ק״מ";

		finalDist = String.format("%.2f", dist / 1000);
	    }
	    else
	    {
		unit = "מטרים";
		finalDist = String.format("%.0f", dist);
	    }

	    return ("נמצא כ " + finalDist + " " + unit + " " + "ממני");
	}
	return "מרחק לא ידוע.";

    }

    private String getDistanceStringFromLastClickMarker()
    {
	return getDistanceStringFromMarker(lastMarkerClicked);
    }

    public void addEventClicked(View v)
    {
	Toast.makeText(this, "add event was clicked on location: lat:" + lastMapLongClick.latitude + " long: " + lastMapLongClick.longitude, 300).show();
	pwindo.dismiss();
	createEventProcess();
    }

    public void addMessageClicked(View v)
    {
	pwindo.dismiss();
	android.app.FragmentTransaction ft1 = getFragmentManager().beginTransaction();
	android.app.Fragment prev1 = getFragmentManager().findFragmentByTag("dialog");
	if (prev1 != null)
	{
	    ft1.remove(prev1);
	}
	ft1.addToBackStack(null);

	// Create and show the dialog.
	SendMassageFragment newFragment1 = SendMassageFragment.newInstance(new Bundle());
	newFragment1.show(ft1, "dialog");
    }

    public void addTestClicked(View v)
    {
	Toast.makeText(this, "add test was clicked", 300).show();
	CloudAccessObject.getInstance().getAllCampusInUsersStartWith("R", new DataAccesObjectCallBack<List<CampusInUser>>()
	{

	    @Override
	    public void done(final List<CampusInUser> retObject, Exception e)
	    {
		if (e == null && retObject != null)
		{
		    if (retObject.size() > 0)
		    {
			CloudAccessObject.getInstance().addFriendToFriendList(retObject.get(0), new DataAccesObjectCallBack<Integer>()
			{

			    @Override
			    public void done(Integer intRet, Exception e)
			    {
				if (e == null)
				{
				    Toast.makeText(Main.this, "new friend wass add:" + retObject.get(0).getFirstName(), 300).show();
				}

			    }
			});
		    }
		}

	    }
	});
    }

    private void createEventProcess()
    {
	android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
	android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	if (prev != null)
	{
	    ft.remove(prev);
	}
	ft.addToBackStack(null);

	// Create and show the dialog.
	Bundle args = new Bundle();
	args.putBoolean("showLocationSpinner", false);
	AddNewEventFragment newFragment = AddNewEventFragment.newInstance(args);
	newFragment.show(ft, "dialog");

    }

    public void FriendChoose(View v)
    {
	/*
	 * CheckBox checkBox = (CheckBox) v; // Fragment fragment =
	 * getFragmentManager();
	 * 
	 * ListView friendListView = (ListView)
	 * fragment.getView().findViewById(R.id.friends_list_view);
	 * CampusInUserChecked item = (CampusInUserChecked)
	 * friendListView.getAdapter().getItem((Integer) checkBox.getTag()); if
	 * (checkBox.isChecked()) item.setChecked(true); else
	 * item.setChecked(false);
	 */
    }

    @Override
    public void onFriendsWereChoosen(ArrayList<CampusInUser> friensList, Fragment targetedFragment, ChooseFriendAction action)
    {
	if (action == ChooseFriendAction.ADD)
	{
	    if (targetedFragment != null)
	    {
		// the calling fragment is "add event" fragment
		AddNewEventFragment tmp = (AddNewEventFragment) targetedFragment;
		tmp.setAddedFriends(friensList);
	    }
	    else
	    {
		// the finished fragment was
		// "AddOrRemoveFriendsFromCloudFragment"
		// we need to do something with the choose friends
		// TODO: yaki - add the friend list to the user friendList
		if (friensList != null)
		{
		    Toast.makeText(getApplicationContext(), "the user Added: " + friensList.size() + " friends", 3000).show();
		    controller.addFriendsToCurrentUserFriendList(friensList);
		}
		else
		    Toast.makeText(getApplicationContext(), "the user Added: " + 0 + " friends", 3000).show();
	    }
	}
	else
	{
	    // Remove friends from friend list
	    if (friensList != null)
	    {
		Toast.makeText(getApplication(), "Removed friendList Size: " + friensList.size(), 3000).show();
		controller.removeFriendsFromCurrentUserFriendList(friensList);
	    }
	    else
		Toast.makeText(getApplication(), "Removed friendList Size: " + 0, 3000).show();
	}

    }

    @Override
    public void onEventCreated(final CampusInEvent addedEvent)
    {
	addedEvent.setLocation(new CampusInLocation());
	addedEvent.getLocation().setMapLocation(lastMapLongClick);
	// TODO just for testing hard coded name
	addedEvent.getLocation().setLocationName("Shenkar");
	MessageHalper.showProgressDialog("Saving...", this);
	controller.saveEvent(addedEvent, new ControllerCallback<String>()
	{

	    @Override
	    public void done(String retObject, Exception e)
	    {
		if (retObject != null)
		{
		    // event was added
		    Toast.makeText(getApplicationContext(), "Event Was Added - from Acrivity", 3000).show();
		    // set a reminder
		    controller.addNotificationToEvent(controller.getEvent(retObject));
		    MessageHalper.closeProggresDialog();
		    controller.updateViewModel(null);
		}
		else
		{
		    Log.i(ALARM_SERVICE, "Error in saving Event: " + e.getMessage());
		}
	    }
	});
    }

    // this code is for hiding The soft keyboard when a touch is done anywhere
    // outside the EditText
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {

	View v = getCurrentFocus();
	boolean ret = super.dispatchTouchEvent(event);

	if (v instanceof EditText)
	{
	    View w = getCurrentFocus();
	    int scrcoords[] = new int[2];
	    w.getLocationOnScreen(scrcoords);
	    float x = event.getRawX() + w.getLeft() - scrcoords[0];
	    float y = event.getRawY() + w.getTop() - scrcoords[1];

	    if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()))
	    {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	    }
	}
	return ret;
    }

    private void registerViewModelReciever()
    {
	IntentFilter filterSend = new IntentFilter();
	filterSend.addAction(CampusInConstant.VIEW_MODEL_UPDATED);
	viewModelUpdatedReciever = new BroadcastReceiver()
	{

	    @Override
	    public void onReceive(Context context, Intent intent)
	    {
		if (intent.getAction().equals(CampusInConstant.VIEW_MODEL_UPDATED))
		{
		    Toast.makeText(Main.this, "view model was updated", 500).show();
		    updateView();
		}

	    }
	};
	registerReceiver(viewModelUpdatedReciever, filterSend);
    }

    private void unRegisterViewModelReciever()
    {
	if (viewModelUpdatedReciever != null)
	{
	    unregisterReceiver(viewModelUpdatedReciever);
	}
    }

    private void updateView()
    {
	//mapManager.clearMap();
	controller.getCurrentUserAllEvents(new ControllerCallback<List<CampusInEvent>>()
	{

	    @Override
	    public void done(List<CampusInEvent> retObject, Exception e)
	    {
		for (CampusInEvent campusInEvent : retObject)
		{
		    mapManager.addOrUpdateEventMarker(campusInEvent);
		}
	    }
	});
	controller.getCurrentUserFriendsLocationList(new ControllerCallback<List<CampusInUserLocation>>()
	{

	    @Override
	    public void done(List<CampusInUserLocation> retObject, Exception e)
	    {
		for (CampusInUserLocation campusInUserLocation : retObject)
		{
		    mapManager.addOrUpdatePersonMarker(campusInUserLocation);
		}
	    }
	});
	List<CampusInMessage> allMessages = controller.getAllMessages();
	for (CampusInMessage campusInMessage : allMessages)
	{
	    mapManager.addOrUpdateMessageMarker(campusInMessage);
	}
    }

    @Override
    protected void onDestroy()
    {
	// TODO Auto-generated method stub
	Toast.makeText(this, "onDestroy was called", 150).show();
	super.onDestroy();
	// stop the report location service
	Main.this.stopService(new Intent(Main.this, LocationReporterServise.class));
	controller.stopAutoViewModelUpdatingService();
	unRegisterViewModelReciever();
	MapManager.resetInstance();
    }

    public enum PopUpKind
    {
	Menu, FriendInfo, EventInfo
    }

    private void startAutoViewModelUpdatingService()
    {
	viewModelServiceRunning = true;
	Intent i = new Intent(this, ModelUpdateService.class);
	this.startService(i);
    }

    private void stopAutoViewModelUpdatingService()
    {
	viewModelServiceRunning = false;
	stopService(new Intent(Main.this, ModelUpdateService.class));
    }

    @SuppressLint("NewApi")
    private void setPendingIntent()
    {
	try
	{
	    Intent intent = new Intent(this, Main.class);
	    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
	    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

	    // build notification
	    // the addAction re-use the same intent to keep the example short
	    Notification n = new NotificationCompat.Builder(this).setContentTitle("CampusIn").setContentText("חזור ל CampusIn").setSmallIcon(R.drawable.ic_launcher)
		    .setContentIntent(pIntent).setAutoCancel(true).build();

	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	    notificationManager.notify(0, n);
	}
	catch (Exception e)
	{
	    Log.e("MainMap", e.getMessage());
	}
    }

    @SuppressLint("NewApi")
    @Override
    public void onMassageCreated(CampusInMessage sentMassage)
    {
	if (sentMassage != null)
	{
	    Toast.makeText(getApplicationContext(), "New Massage creted: '" + sentMassage.getContent() + "'", 4000).show();
	    CampusInLocation loc = new CampusInLocation();
	    loc.setLocationName("Shenkar");
	    loc.setMapLocation(lastMapLongClick);
	    sentMassage.setLocation(loc);
	    MessageHalper.showProgressDialog("Saving...", this);
	    controller.sendMessage(sentMassage, new ControllerCallback<Integer>()
	    {

		@Override
		public void done(Integer retObject, Exception e)
		{
		    if (e == null)
		    {
			MessageHalper.closeProggresDialog();
			controller.updateViewModel(null);
		    }
		    else
		    {
			Log.i(ALARM_SERVICE, "Error in saving message: " + e.getMessage());
		    }

		}
	    });
	}
    }

    public void deleteMessage()
    {
	Toast.makeText(this, "delete message was clicked", 100);

    }
    public static void closeDrawerLayout ()
    {
	mDrawerLayout.closeDrawers();
    }
}
