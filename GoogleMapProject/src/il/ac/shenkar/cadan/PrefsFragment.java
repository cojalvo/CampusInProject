package il.ac.shenkar.cadan;

import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Request.GraphUserCallback;
import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.in.dal.FacebookServices;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrefsFragment extends PreferenceFragment {

	private String userName = null;
	private String faceId = null;
	private Drawable profilePic = null;
	private Bitmap profileAsBitmap = null;

	private class UpdateProfilePictureInBackgroud extends
			AsyncTask<String, Void, String> {
		private String userId = null;

		UpdateProfilePictureInBackgroud(String id) {
			super();
			userId = id;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			PrefsFragment.this.profilePic = FacebookServices
					.getPictureForFacebookId(userId);
			Bitmap profileAsBitmap = ((BitmapDrawable) PrefsFragment.this.profilePic)
					.getBitmap();
			// Scale it to 50 x 50
			PrefsFragment.this.profilePic = new BitmapDrawable(getResources(),
					Bitmap.createScaledBitmap(profileAsBitmap, 150, 130, true));
			return userId;
		}

		@Override
		protected void onPostExecute(String result) {
			Preference me = (Preference) findPreference("me");
			if (me != null) {
				me.setIcon(PrefsFragment.this.profilePic);
			}
		}
	}

	public static final String ACTION_INTENT = "il.ac.shenkar.CHANGE";
	OnPreferenceSelectedListener mCallback;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Parse.initialize(getActivity(),
				"3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE",
				"UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
		ParseFacebookUtils.initialize("635010643194002");
		context = getActivity().getBaseContext();
		// update the full name and the profile picture of current user
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preference);
		final CheckBoxPreference showMe = (CheckBoxPreference) findPreference("display_me");
		showMe.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent inti = new Intent();
				inti.setAction(ACTION_INTENT);
				context.sendBroadcast(inti);
				return true;
			}
		});
		Preference testCalender = findPreference("my_calendar");
		testCalender
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						// startActivity(new Intent(
						// getActivity().getBaseContext(),
						// CalendarAvtivity.class));
						mCallback
								.onPreferenceSelected(CampusInConstant.SETTINGS_EVENTS);
						return true;
					}
				});
		updateMeDetails(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public interface OnPreferenceSelectedListener {
		public void onPreferenceSelected(String selected);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnPreferenceSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	private void updateMeDetails(Bundle savedInstanceState) {
		// use FacebookService object to get the user profile object
		if (savedInstanceState != null) {
			faceId=savedInstanceState.getString("faceId");
			userName=savedInstanceState.getString("userName");
			Preference me = (Preference) findPreference("me");
			if (me != null) {
				me.setTitle(userName);

				if (profilePic == null) {
				
					UpdateProfilePictureInBackgroud u = new UpdateProfilePictureInBackgroud(
							faceId);
					u.execute("dummy");
				}
			}
			return;
		}
		FacebookServices.makeMeRequest(ParseFacebookUtils.getSession(),
				new GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (ParseFacebookUtils.getSession() == Session
								.getActiveSession()) {
							if (user != null) {

								faceId = user.getId();
								// get me preference and update the full name
								Preference me = (Preference) findPreference("me");
								if (me != null) {
									PrefsFragment.this.userName = user
											.getFirstName()
											+ " "
											+ user.getLastName();
									me.setTitle(PrefsFragment.this.userName);

									// use asyncTask obj to update the profile
									// picture
									// TODO create AsyncTaskFacrory to manage
									// all
									// async task obj in the app
									UpdateProfilePictureInBackgroud u = new UpdateProfilePictureInBackgroud(
											faceId);
									u.execute("dummy");
								}
							}
							;

						}
						if (response.getError() != null) {
							// Handle error
						}
					}
				});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("userName", userName);
		outState.putString("faceId", faceId);
		super.onSaveInstanceState(outState);
	}
}
