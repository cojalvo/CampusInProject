package il.ac.shenkar.in.services;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import il.ac.shenkar.cadan.PrefsFragment;
import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.location.ILocationReporter;
import il.ac.shenkar.location.LocationoReporter;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class LocationReporterServise extends Service {

	private SharedPreferences sharedPreferences=null;
	private ILocationReporter locationReporter=null;
	//TODO Should add the interval time to the preference
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

		@Override
		public void onCreate() {

			super.onCreate();
			Parse.initialize(this, "3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE",
					"UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
			ParseFacebookUtils.initialize("635010643194002");
			locationReporter=new LocationoReporter(9000,getBaseContext());
		}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			Log.i("cadan","Service has started");
			Toast.makeText(getBaseContext(), "Servise has started", 50000).show();
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			if(sharedPreferences!=null)
			{
				if(sharedPreferences.contains(CampusInConstant.DISPLAY_ME))
				{
					if(sharedPreferences.getBoolean(CampusInConstant.DISPLAY_ME, false))
					{
						locationReporter.start();
					}
					
				}
			}
			
			IntentFilter filterSend = new IntentFilter();
			filterSend.addAction(PrefsFragment.ACTION_INTENT);
			registerReceiver(changeReceiver, filterSend);
			return START_STICKY;
		}


		@Override
		public void onDestroy()
		{
			// TODO Auto-generated method stub
			super.onDestroy();
			unregisterReceiver(changeReceiver);
			locationReporter.stop();
			
		}


		BroadcastReceiver changeReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(PrefsFragment.ACTION_INTENT)) 
				{
					if (sharedPreferences.contains(CampusInConstant.DISPLAY_ME))
						if (sharedPreferences.getBoolean(CampusInConstant.DISPLAY_ME, false)) {
							locationReporter.start();
						} else {
							locationReporter.stop();
						}
				}
			}

		};
	}