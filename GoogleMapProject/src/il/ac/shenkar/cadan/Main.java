package il.ac.shenkar.cadan;

import il.ac.shenkar.cadan.PrefsFragment.OnPreferenceSelectedListener;
import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.in.services.LocationReporterServise;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPositionCreator;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.*;
import android.support.v4.widget.DrawerLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

public class Main extends Activity
		implements
			OnPreferenceSelectedListener,
			OnMapLongClickListener,
			OnMarkerClickListener
{
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
	protected void onCreate(Bundle savedInstanceState)
	{
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
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView)
			{
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mapManager = new MapManager(((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap(), GoogleMap.MAP_TYPE_NONE);

		mapManager.addGroundOverlay(R.drawable.shenkarmap_1, new LatLng(0, 0),
				new LatLng(40, 50), (float) 0.1);
		mapManager.moveCameraToLocation(new LatLng(20, 25), 15);
		mapManager.setOnMapLongClickListener(this);
		mapManager.setOnMarkerClickListener(this);
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
		if (searchInfo == null)
		{
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
				|| !savedInstanceState.getBoolean("locationServiceStart"))
		{
			startLocationReportServise();
		}

	}
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{

		super.onSaveInstanceState(outState);
		// update the bundle that the service is running to prevent many
		// binding.
		outState.putBoolean("locationServiceStart", true);
	}
	private void startLocationReportServise()
	{
		Intent i = new Intent(this, LocationReporterServise.class);
		// potentially add data to the intent
		i.putExtra("KEY1", "Value to be used by the service");
		this.startService(i);

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
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			if (pwindo.isShowing()) 
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
				Main.this.stopService(new Intent(Main.this,
						LocationReporterServise.class));
				finish();
			}
		});

		alertDialog.setNegativeButton("לא", null);

		alertDialog.setMessage("האם אתה בטוח שברצונך לצאת?");
		alertDialog.setTitle(" ");
		alertDialog.setIcon(R.drawable.campusin_logo);
		alertDialog.show();
	}
	@Override
	public void onMapLongClick(LatLng point)
	{
		vibrator.vibrate(50);
		lastMapLongClick = point;
		initiatePopupWindow();
	}
	@Override
	public boolean onMarkerClick(Marker marker)
	{
		lastMarkerClicked = marker;
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
	private void initiatePopupWindow()
	{
		try
		{
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) Main.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.popup_menu, null);
			pwindo = new PopupWindow(layout, 750, 350, true);
			pwindo.setAnimationStyle(R.style.Animation);
			pwindo.setFocusable(true);
			ColorDrawable bcolor=new ColorDrawable();
			pwindo.setBackgroundDrawable(bcolor);
			pwindo.showAtLocation(layout, Gravity.BOTTOM,0, 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void addEventClicked(View v)
	{
		Toast.makeText(this, "add event was clicked on location: lat:" +lastMapLongClick.latitude+ " long: "+lastMapLongClick.longitude, 300).show();
		pwindo.dismiss();
		lastMapLongClick=null;
	}
	public void addMessageClicked(View v)
	{
		Toast.makeText(this, "add message was clicked", 300).show();
		pwindo.dismiss();
		lastMapLongClick=null;
	}
	public void addTestClicked(View v)
	{
		Toast.makeText(this, "add test was clicked", 300).show();
		pwindo.dismiss();
		lastMapLongClick=null;
	}
	private void createEventProcess()
	{
		
	}
}
