package il.ac.shenkar.location;

import com.parse.ParseUser;

import il.ac.shenkar.cadan.PrefsFragment;
import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.common.CampusInUserLocation;
import il.ac.shenkar.common.CampusInLocation;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.dal.IDataAccesObject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

public class LocationoReporter implements ILocationReporter
{

    private String TAG = LocationoReporter.class.getName();
    private CampusInLocation lastLocation;
    private int interval;
    private Handler handler = new Handler();
    // provide the current location of the user.
    private ILocationProvider locationProvider = new RandomLocationProvider();
    // cloud access
    private IDataAccesObject dao;
    private static boolean lastUpdateFinish = true;
    private Context context = null;

    public LocationoReporter(Context context)
    {

    }

    public LocationoReporter(int interval, Context context)
    {
	this.interval = interval;
	dao = CloudAccessObject.getInstance();
	IntentFilter filterSend = new IntentFilter();
	// set the filter for the intent reciever
	filterSend.addAction(CampusInConstant.CLOUD_ACTION_LOCATION_UPDATE);
	// register a receiver
	this.context = context;
    }

    // this runnable is running every interval time.
    private final Runnable autoRefresh = new Runnable()
    {
	@Override
	public void run()
	{
	    if (lastUpdateFinish)
	    {
		lastUpdateFinish = false;
		reportLocation();
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
	if (currerntLoaction == null)
	{
	    lastUpdateFinish = true;
	    return;
	}
	if (currerntLoaction.equals(lastLocation))
	    return;
	String userId = ParseUser.getCurrentUser().getObjectId();
	dao.updateLocation(currerntLoaction, new DataAccesObjectCallBack<Integer>()
	{

	    @Override
	    public void done(Integer retObject, Exception e)
	    {
		lastUpdateFinish = true;
		if (e == null)
		{
		    Log.i(TAG, "Update location succed");
		}
		else
		{
		    Log.i(TAG, "Update location was failed");
		}

	    }
	});
    }

    /**
     * ILocationReporet implementation
     */
    @Override
    public void start()
    {
	// TODO Check if this call in the second time will not effect.
	autoRefresh.run();
    }

    @Override
    public void stop()
    {
	handler.removeCallbacks(autoRefresh);
    }
}
