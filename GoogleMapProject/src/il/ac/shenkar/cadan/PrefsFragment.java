package il.ac.shenkar.cadan;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import il.ac.shenkar.common.CampusInConstant;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrefsFragment extends PreferenceFragment {
	public static final String ACTION_INTENT = "il.ac.shenkar.CHANGE";
	OnPreferenceSelectedListener mCallback;
	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Parse.initialize(getActivity(), "3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE",
				"UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
		ParseFacebookUtils.initialize("635010643194002");
		context=getActivity().getBaseContext();
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preference);
		final CheckBoxPreference showMe=(CheckBoxPreference) findPreference("display_me");
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
		testCalender
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
//						startActivity(new Intent(
//								getActivity().getBaseContext(),
//								CalendarAvtivity.class));
						mCallback.onPreferenceSelected(CampusInConstant.SETTINGS_EVENTS);
						return true;
					}
				});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
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
}