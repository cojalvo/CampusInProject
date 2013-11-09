package il.ac.shenkar.cadan;

import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Request.GraphUserCallback;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import il.ac.shenkar.cadan.ChooseFriendsFragment.ChooseFriendAction;
import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.dal.FacebookServices;
import il.ac.shenkar.in.dal.IDataAccesObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class PrefsFragment extends PreferenceFragment
{

    private String userName = null;
    private String faceId = null;
    private Drawable profilePic = null;
    private Bitmap profileAsBitmap = null;
    public static final String ACTION_INTENT = "il.ac.shenkar.CHANGE";
    OnPreferenceSelectedListener mCallback;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

	super.onCreate(savedInstanceState);
	Parse.initialize(getActivity(), "3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE", "UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
	ParseFacebookUtils.initialize("635010643194002");
	context = getActivity().getBaseContext();
	// update the full name and the profile picture of current user
	// Load the preferences from an XML resource
	addPreferencesFromResource(R.xml.preference);
	EditTextPreference me = (EditTextPreference) findPreference("me");
	me.setSummary(me.getText());
	me.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
	{

	    @Override
	    public boolean onPreferenceChange(Preference preference, Object arg1)
	    {
		EditTextPreference me = (EditTextPreference) preference;
		preference.setSummary(me.getText());
		CloudAccessObject.getInstance().updateCurrentUserStatus(new DataAccesObjectCallBack<Integer>()
		{

		    @Override
		    public void done(Integer retObject, Exception e)
		    {

		    }
		}, me.getText());
		return true;
	    }
	});

	final CheckBoxPreference showMe = (CheckBoxPreference) findPreference("display_me");
	showMe.setOnPreferenceClickListener(new OnPreferenceClickListener()
	{

	    @Override
	    public boolean onPreferenceClick(Preference preference)
	    {

		Intent inti = new Intent();
		inti.setAction(ACTION_INTENT);
		context.sendBroadcast(inti);
		return true;
	    }
	});
	Preference testCalender = findPreference("my_calendar");
	testCalender.setOnPreferenceClickListener(new OnPreferenceClickListener()
	{

	    @Override
	    public boolean onPreferenceClick(Preference preference)
	    {
		// startActivity(new Intent(
		// getActivity().getBaseContext(),
		// CalendarAvtivity.class));
		mCallback.onPreferenceSelected(CampusInConstant.SETTINGS_EVENTS);
		return true;
	    }
	});
	Preference addFriends = findPreference("add_friends");
	addFriends.setOnPreferenceClickListener(new OnPreferenceClickListener()
	{

	    @Override
	    public boolean onPreferenceClick(Preference preference)
	    {
		android.app.FragmentTransaction ft111 = getFragmentManager().beginTransaction();
		android.app.Fragment prev111 = getFragmentManager().findFragmentByTag("dialog");
		if (prev111 != null)
		{
		    ft111.remove(prev111);
		}
		ft111.addToBackStack(null);

		// Create and show the dialog.
		AddOrRemoveFriendsFromCloudFragment newFragment111 = AddOrRemoveFriendsFromCloudFragment.newInstance(ChooseFriendAction.ADD);
		newFragment111.show(ft111, "dialog");
		return true;
	    }
	});
	Preference removeFriends = findPreference("remove_friends");
	removeFriends.setOnPreferenceClickListener(new OnPreferenceClickListener()
	{
	    
	    @Override
	    public boolean onPreferenceClick(Preference preference)
	    {
		android.app.FragmentTransaction ft1111 = getFragmentManager().beginTransaction();
		android.app.Fragment prev1111 = getFragmentManager().findFragmentByTag("dialog");
		if (prev1111 != null)
		{
		    ft1111.remove(prev1111);
		}
		ft1111.addToBackStack(null);

		// Create and show the dialog.
		AddOrRemoveFriendsFromCloudFragment newFragment1111 = AddOrRemoveFriendsFromCloudFragment.newInstance(ChooseFriendAction.REMOVE);
		newFragment1111.show(ft1111, "dialog");
		return true;

	    }
	});
	Preference showCampusEvents = findPreference("show_campus_events");
	showCampusEvents.setOnPreferenceClickListener(new OnPreferenceClickListener()
	{

	    @Override
	    public boolean onPreferenceClick(Preference preference)
	    {
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

	    }
	});
	Preference showMyFriends = findPreference("show_my_friends");
	showMyFriends.setOnPreferenceClickListener(new OnPreferenceClickListener()
	{
	    
	    @Override
	    public boolean onPreferenceClick(Preference preference)
	    {
		android.app.FragmentTransaction ft1111 = getFragmentManager().beginTransaction();
		android.app.Fragment prev1111 = getFragmentManager().findFragmentByTag("dialog");
		if (prev1111 != null)
		{
		    ft1111.remove(prev1111);
		}
		ft1111.addToBackStack(null);

		// Create and show the dialog.
		DisplayFriendFragment newFragment1111 = (DisplayFriendFragment) DisplayFriendFragment.newInstance(null);
		newFragment1111.show(ft1111, "dialog");
		return true;
	    }
	});
	updateMeDetails(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface OnPreferenceSelectedListener
    {
	public void onPreferenceSelected(String selected);
    }

    @Override
    public void onAttach(Activity activity)
    {
	super.onAttach(activity);

	// This makes sure that the container activity has implemented
	// the callback interface. If not, it throws an exception
	try
	{
	    mCallback = (OnPreferenceSelectedListener) activity;
	}
	catch (ClassCastException e)
	{
	    throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
	}
    }

    private void updateMeDetails(Bundle savedInstanceState)
    {
	// use FacebookService object to get the user profile object
	final Preference me = (Preference) findPreference("me");
	final IDataAccesObject dao = CloudAccessObject.getInstance();
	dao.loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>()
	{

	    @Override
	    public void done(CampusInUser retObject, Exception e)
	    {
		if (e == null)
		{
		    userName = retObject.getFirstName() + " " + retObject.getLastName();
		    me.setTitle(userName);
		    dao.getProfilePicture(new DataAccesObjectCallBack<Drawable>()
		    {

			@Override
			public void done(Drawable retObject, Exception e)
			{
			    if (e == null && retObject != null)
			    {
				profilePic = retObject;

				try
				{
				    profilePic = resizePic(profilePic, 150, 130);
				}
				catch (Exception e1)
				{

				    Toast.makeText(getActivity(), e1.getMessage(), 500);
				}
				Toast.makeText(context, "Picture was loaded from facebook", 500).show();
				if (me != null)
				{
				    me.setIcon(profilePic);
				}
			    }

			}
		    });
		}

	    }
	});

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
	outState.putString("userName", userName);
	super.onSaveInstanceState(outState);
    }

    private Drawable resizePic(Drawable paramDrawable, int paramInt1, int paramInt2) throws Exception
    {
	Bitmap localBitmap = ((BitmapDrawable) paramDrawable).getBitmap();
	return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(localBitmap, paramInt1, paramInt2, true));
    }

}
