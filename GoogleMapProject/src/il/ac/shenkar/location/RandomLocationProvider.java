package il.ac.shenkar.location;

import com.google.android.gms.maps.model.LatLng;

import il.ac.shenkar.cadan.MapManager;
import il.ac.shenkar.common.CampusInLocation;

/**
 * This class will return random location in the campus it was made only for
 * testing
 * 
 * @author cadan
 * 
 */
public class RandomLocationProvider implements ILocationProvider
{

    @Override
    public CampusInLocation getLoction()
    {
	CampusInLocation cl = new CampusInLocation();
	LatLng loc = null;
	loc = soundLocation();
	if (loc == null)
	    loc = gpsLocation();
	if (loc == null)
	    return null;
	cl.setMapLocation(loc);
	cl.setLocationName("");
	return cl;
    }

    private LatLng gpsLocation()
    {
	return MapManager.getmyLocation();
    }

    private LatLng soundLocation()
    {
	return null;
    }

}
