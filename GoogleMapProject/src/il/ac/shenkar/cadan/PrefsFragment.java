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

	private class UpdateProfilePictureInBackgroud extends
			AsyncTask<String, Void, String> {
		private String userId = null;

		UpdateProfilePictureInBackgroud(String id) {
			super();
			userId = id;
		}

		Drawable pic = null;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			pic = FacebookServices.getPictureForFacebookId(userId);
			return userId;
		}

		@Override
		protected void onPostExecute(String result) {
			Preference me = (Preference) findPreference("me");
			// Read your drawable from somewhere
			Bitmap bitmap = ((BitmapDrawable) pic).getBitmap();
			// Scale it to 50 x 50
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 150, 130, true));
			me.setIcon(d);
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
		updateMeDetails();
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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

	private void updateMeDetails() {
		FacebookServices.makeMeRequest(ParseFacebookUtils.getSession(),
				new GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (ParseFacebookUtils.getSession() == Session
								.getActiveSession()) {
							if (user != null) {
								String facebookId = user.getId();
								Preference me = (Preference) findPreference("me");
								me.setTitle(user.getFirstName() + " "
										+ user.getLastName());
								
								UpdateProfilePictureInBackgroud u=new UpdateProfilePictureInBackgroud(user.getId());
								u.execute("dummy");
							}
							;
							

						}
						if (response.getError() != null) {
							// Handle error
						}
					}
				});
	}
}
