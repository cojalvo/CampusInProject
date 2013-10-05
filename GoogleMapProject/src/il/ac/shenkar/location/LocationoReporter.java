package il.ac.shenkar.location;

import com.parse.ParseUser;

import il.ac.shenkar.cadan.PrefsFragment;
import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.common.CampusInUserLocation;
import il.ac.shenkar.common.CampusInLocation;
import il.ac.shenkar.in.dal.ActionCode;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.IDataAccesObject;
import il.ac.shenkar.in.dal.IObserver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class LocationoReporter implements ILocationReporter,IObserver
{

	private String TAG = LocationoReporter.class.getName();
	private CampusInLocation lastLocation;
	private int interval;
	private Handler handler = new Handler();
	//provide the current location of the user.
	private ILocationProvider locationProvider = new RandomLocationProvider();
	//cloud access
	private IDataAccesObject dao;
	private static boolean lastUpdateFinish=true;
	//determine if the user want to update his location
	private Context context=null;

	public LocationoReporter(Context context)
	{

	}
	
	public LocationoReporter(int interval,Context context)
	{
		this.interval = interval;
		dao = CloudAccessObject.getInstance();
		dao.addObserver(this);
		IntentFilter filterSend = new IntentFilter();
		//set the filter for the intent reciever
		filterSend.addAction(CampusInConstant.CLOUD_ACTION_LOCATION_UPDATE);
		//register a receiver
		this.context=context;
		if (context!=null) context.registerReceiver(receiver, filterSend);
	}
	
	//the broadcast receiver receive callback from the dao object when the updating has finished
	private BroadcastReceiver receiver=new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			lastUpdateFinish=true;
		}
	};
	//this runnable is running every interval time.
	private final Runnable autoRefresh = new Runnable()
	{
		@Override
		public void run()
		{
			if (lastUpdateFinish)
			{
				lastUpdateFinish=false;
				reportLocation();
				Toast.makeText(context, "Location was reported", 80).show();
			}
			handler.postDelayed(autoRefresh, interval);
		}

	};

	/**
	 * Report the current location of the user.
	 */
	private void reportLocation()
	{
		CampusInLocation currerntLoaction = locationProvider.getLoction();
		// if the location hasn't changed than return;
		if (currerntLoaction.equals(lastLocation)) return;
		try
		{
			String userId = ParseUser.getCurrentUser().getObjectId();
			dao.updateLocation(new CampusInUserLocation(currerntLoaction,
					userId));
		}
		catch (Exception e)
		{
			// TODO Create a class that will help me find out whether i have
			// connection to the internet
			// TODO if not, that create a warning message for the user.
			Log.i(TAG, "Update location was failed");
		}

	}



	/**
	 * ILocationReporet implementation
	 */
	@Override
	public void start()
	{
		
		Toast.makeText(context, "Location was start", 100).show();
		//TODO Check if this call in the second time will not effect.
		autoRefresh.run();
	}

	@Override
	public void stop()
	{
		handler.removeCallbacks(autoRefresh);
		Toast.makeText(context, "Location was stoped", 100).show();
	}

	@Override
	public void actionDone(ActionCode settings)
	{
		Toast.makeText(context, "Update location succed", 15).show();
		lastUpdateFinish=true;
		
	}

	@Override
	public void actionFail(ActionCode settings)
	{
		Toast.makeText(context, "Update location Failed", 15).show();
		lastUpdateFinish=true;
		
	}
	

}
