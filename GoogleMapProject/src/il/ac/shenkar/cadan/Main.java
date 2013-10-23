package il.ac.shenkar.cadan;

import java.util.List;

import il.ac.shenkar.cadan.PrefsFragment.OnPreferenceSelectedListener;
import il.ac.shenkar.common.CampusInConstant;
import java.util.ArrayList;

import il.ac.shenkar.cadan.ChooseFriendsFragment.ChooseFriendAction;
import il.ac.shenkar.cadan.ChooseFriendsFragment.onFriendsAddedListener;
import il.ac.shenkar.cadan.AddNewEventFragment.onNewEventAdded;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.services.InitLocations;
import il.ac.shenkar.in.services.LocationReporterServise;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.internal.ac;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.*;
import android.support.v4.widget.DrawerLayout;

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
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

public class Main extends Activity implements OnPreferenceSelectedListener,
		OnMapLongClickListener, OnMarkerClickListener, onFriendsAddedListener,
		onNewEventAdded {
	CameraPosition lastPos = new CameraPosition(new LatLng(0, 0), 2, 2, 2);
	GoogleMap map = null;
	private DrawerLayout mDrawerLayout;
	private LatLng lastMapLongClick = null;
	private Marker lastMarkerClicked = null;
	private ActionBarDrawerToggle mDrawerToggle;
	static final LatLng HAMBURG = new LatLng(20, 25);
	static final LatLng KIEL = new LatLng(15, 10);
	private PopupWindow pwindo;
	private MapManager mapManager = null;
	private Vibrator vibrator = null;
	FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if (savedInstanceState == null
				|| !savedInstanceState.getBoolean("initLocationDone")) {
			new InitLocations().execute(this);
		}
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE",
				"UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
		ParseFacebookUtils.initialize("635010643194002");
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// inflate the drawerLayour
		this.mDrawerLayout = (DrawerLayout) this.getLayoutInflater().inflate(
				R.layout.main, null);
		// set as content view
		this.setContentView(this.mDrawerLayout);
		mapManager = new MapManager(((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap(), GoogleMap.MAP_TYPE_NONE);

		mapManager.addGroundOverlay(R.drawable.shenkarmap_1, new LatLng(0, 0),
				new LatLng(40, 50), (float) 0.1);
		mapManager.moveCameraToLocation(new LatLng(20, 25), 15);
		mapManager.setOnMapLongClickListener(this);
		mapManager.setOnMarkerClickListener(this);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu(); // creates call to
			}

			public void onDrawerOpened(View drawerView) {
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
		SearchableInfo searchInfo = searchManager
				.getSearchableInfo(getComponentName());
		if (searchInfo == null) {
			Toast.makeText(getApplicationContext(), "Search info is null", 500)
					.show();
		}
		searchView.setSearchableInfo(searchInfo);
		searchView.setIconifiedByDefault(false); // Do not iconify the widget;
													// expand it by default
		// if the bundle is null than it the first time the application is
		// running
		// if not check if the locationServiceStart flag is true; (this might be
		// redundant but maybe in the future we will need to use it
		if (savedInstanceState == null
				|| !savedInstanceState.getBoolean("locationServiceStart")) {
			startLocationReportServise();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		// update the bundle that the service is running to prevent many
		// binding.
		outState.putBoolean("locationServiceStart", true);
		outState.putBoolean("initLocationDone", true);
	}

	private void startLocationReportServise() {
		Intent i = new Intent(this, LocationReporterServise.class);
		// potentially add data to the intent
		i.putExtra("KEY1", "Value to be used by the service");
		this.startService(i);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle presses on the action bar items
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_add_event:
			android.app.FragmentTransaction ft = getFragmentManager()
					.beginTransaction();
			android.app.Fragment prev = getFragmentManager().findFragmentByTag(
					"dialog");
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);

			// Create and show the dialog.
			AddNewEventFragment newFragment = AddNewEventFragment
					.newInstance(2);
			newFragment.show(ft, "dialog");

			/*
			 * startActivity(intent);
			 * overridePendingTransition(R.anim.slide_in_left,
			 * R.anim.slide_out_left);
			 */
			return true;
		case R.id.action_show_all_events:
			/*intent = new Intent(this, JacobEventActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_left);*/
			
			
			// try to move it to Fragment 
			android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
			Fragment fragment = getFragmentManager().findFragmentByTag("dialog");
			if (fragment != null)
			{
				transaction.remove(fragment);
			}
			transaction.addToBackStack(null);
			DiaplayEventListFragment newDiaplayEventListFragment = new DiaplayEventListFragment();
			newDiaplayEventListFragment.show(transaction, "dialog");
			return true;

		case R.id.action_add_friends:
			android.app.FragmentTransaction ft1 = getFragmentManager()
					.beginTransaction();
			android.app.Fragment prev1 = getFragmentManager()
					.findFragmentByTag("dialog");
			if (prev1 != null) {
				ft1.remove(prev1);
			}
			ft1.addToBackStack(null);

			// Create and show the dialog.
			ChooseFriendsFragment newFragment1 = ChooseFriendsFragment.newInstance(ChooseFriendAction.ADD);
			newFragment1.show(ft1, "dialog");
		case R.id.action_remove_friends:
			android.app.FragmentTransaction ft11 = getFragmentManager().beginTransaction();
			android.app.Fragment prev11 = getFragmentManager().findFragmentByTag("dialog");
			if (prev11 != null) {
				ft11.remove(prev11);
			}
			ft11.addToBackStack(null);

			// Create and show the dialog.
			ChooseFriendsFragment newFragment11 = ChooseFriendsFragment.newInstance(ChooseFriendAction.REMOVE);
			newFragment11.show(ft11, "dialog");
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onPreferenceSelected(String selected) {
		if (selected.equals(CampusInConstant.SETTINGS_EVENTS)) {
			startActivity(new Intent(this, EventsActivity.class));
			return;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if the user press the back button that doExit will invoke
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Log.d(this.getClass().getName(), "back button pressed");
			// in case of process of long press than reset it
			if (lastMapLongClick != null && pwindo.isShowing()) {
				lastMapLongClick = null;
				pwindo.dismiss();
			}
			// else ask if he would like to exit
			else {
				doExit();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	//
	// Exit the application will ask the user if he sure.
	//
	private void doExit() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setPositiveButton("כן", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Main.this.stopService(new Intent(Main.this,
						LocationReporterServise.class));
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
	public void onMapLongClick(LatLng point) {
		vibrator.vibrate(50);
		lastMapLongClick = point;
		initiatePopupWindow();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		lastMarkerClicked = marker;
		return true;

	}

	public void showPopup(View v) {
		PopupMenu popup = new PopupMenu(this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.main, popup.getMenu());
		popup.show();
	}

	/*
	 * popup the menu in long press on the map
	 */
	private void initiatePopupWindow() {
		try {
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) Main.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.popup_menu, null);
			pwindo = new PopupWindow(layout, 750, 350, true);
			pwindo.setAnimationStyle(R.style.Animation);
			pwindo.setFocusable(true);
			ColorDrawable bcolor = new ColorDrawable();
			pwindo.setBackgroundDrawable(bcolor);
			pwindo.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addEventClicked(View v) {
		Toast.makeText(
				this,
				"add event was clicked on location: lat:"
						+ lastMapLongClick.latitude + " long: "
						+ lastMapLongClick.longitude, 300).show();
		pwindo.dismiss();
		CloudAccessObject.getInstance().getAllCampusInUsersStartWith("R",
				new DataAccesObjectCallBack<List<CampusInUser>>() {

					@Override
					public void done(final List<CampusInUser> retObject,
							Exception e) {
						if (e == null && retObject != null) {
							if (retObject.size() > 0) {
								CloudAccessObject
										.getInstance()
										.removeFriendFromFriendList(
												retObject.get(0),
												new DataAccesObjectCallBack<Integer>() {

													@Override
													public void done(
															Integer intRet,
															Exception e) {
														if (e == null) {
															Toast.makeText(
																	Main.this,
																	" friend wass removed:"
																			+ retObject
																					.get(0)
																					.getFirstName(),
																	300).show();
														}

													}
												});
							}
						}

					}
				});
		lastMapLongClick = null;
	}

	public void addMessageClicked(View v) {
		Toast.makeText(this, "add message was clicked", 300).show();
		pwindo.dismiss();
		CloudAccessObject.getInstance().getCurrentCampusInUserFriends(
				new DataAccesObjectCallBack<List<CampusInUser>>() {

					@Override
					public void done(List<CampusInUser> retObject, Exception e) {
						Toast.makeText(Main.this,
								"Number of friends is:" + retObject.size(), 500)
								.show();

					}
				});
		lastMapLongClick = null;
	}

	public void addTestClicked(View v) {
		Toast.makeText(this, "add test was clicked", 300).show();
		CloudAccessObject.getInstance().getAllCampusInUsersStartWith("R",
				new DataAccesObjectCallBack<List<CampusInUser>>() {

					@Override
					public void done(final List<CampusInUser> retObject,
							Exception e) {
						if (e == null && retObject != null) {
							if (retObject.size() > 0) {
								CloudAccessObject
										.getInstance()
										.addFriendToFriendList(
												retObject.get(0),
												new DataAccesObjectCallBack<Integer>() {

													@Override
													public void done(
															Integer intRet,
															Exception e) {
														if (e == null) {
															Toast.makeText(
																	Main.this,
																	"new friend wass add:"
																			+ retObject
																					.get(0)
																					.getFirstName(),
																	300).show();
														}

													}
												});
							}
						}

					}
				});
		pwindo.dismiss();
		lastMapLongClick = null;
	}

	private void createEventProcess() {

	}

	public void FriendChoose(View v) {
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
	public void onFriendsWereChoosen(ArrayList<CampusInUser> friensList,
			Fragment targetedFragment,ChooseFriendAction action) 
	{
		if (action == ChooseFriendAction.ADD)
		{
			if (targetedFragment != null) {
				// the calling fragment is add event
				AddNewEventFragment tmp = (AddNewEventFragment) targetedFragment;
				tmp.setAddedFriends(friensList);
			}
		}
		else 
		{
			// do something with the friendList to Remove
			Toast.makeText(getApplication(), "Removed friendList Size: "+ friensList.size(), 3000).show();
		}
		
	}

	@Override
	public void onEventCreated(CampusInEvent addedEvent) {
		// event was added
		Toast.makeText(getApplicationContext(),
				"Event Was Added - from Acrivity", 3000).show();

	}

	// this code is for hiding The soft keyboard when a touch is done anywhere
	// outside the EditText
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		View v = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (v instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	BroadcastReceiver viewModelUpdatedReciever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(CampusInConstant.VIEW_MODEL_UPDATED)) {
				Toast.makeText(Main.this, "view model was updated", 500).show();
			}

		}
	};

}
