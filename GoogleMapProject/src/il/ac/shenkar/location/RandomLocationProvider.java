package il.ac.shenkar.location;

import com.google.android.gms.maps.model.LatLng;

import il.ac.shenkar.common.CampusInLocation;

/**
 * This class will return random location in the campus
 * it was made only for testing 
 * @author cadan
 *
 */
public class RandomLocationProvider implements ILocationProvider
{

	@Override
	public CampusInLocation getLoction() {
		CampusInLocation cl=new CampusInLocation();
		cl.setMapLocation(new LatLng(100,105));
		cl.setName("cadan");
		return  cl;
	}
	

}
