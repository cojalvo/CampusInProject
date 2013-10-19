package il.ac.shenkar.in.bl;

import java.util.Collection;

import il.ac.shenkar.common.CampusInLocation;
import il.ac.shenkar.common.JacocDBLocation;
import il.ac.shenkar.in.dal.DataBaseHealper;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.parse.entity.mime.content.ContentBody;

public class LocationHelper implements ILocationHelper {
	
	private Collection<JacocDBLocation> locationList;
	
	/**
	 * The c'tor received context in order to access the DataBaseHelper Object to get the locations
	 * @param context
	 */
	public LocationHelper(Context context)
	{
		this.locationList = (Collection<JacocDBLocation>) DataBaseHealper.getInstance(context).getAllLocations();
	}

	@Override
	public CampusInLocation getLocationFromMapCord(LatLng mapCord) 
	{
		for (JacocDBLocation location: locationList)
		{
			if (this.contains(location,mapCord))
			{
				return new CampusInLocation(location);
			}
		}
		return null;
	}

	@Override
	public CampusInLocation getLocationFromSound(double soundCode) 
	{
		for (JacocDBLocation location: locationList)
		{
			if (this.isBetween(location,soundCode))
			{
				return new CampusInLocation(location);
			}
		}
		return null;
	}

	@Override
	public CampusInLocation getLocationFromGpsCord(LatLng gpsCord) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * return true if the point in inside the location border
	 * @param location
	 * @param mapCord
	 * @return
	 */
	public boolean contains(JacocDBLocation location, LatLng mapCord)
	{
		int maxX = (int) location.getMapLocation().getNorthEast().getLat();
		int minX = (int) location.getMapLocation().getSouthWest().getLat();
		int maxY = (int) location.getMapLocation().getNorthEast().getLng();
		int minY = (int) location.getMapLocation().getSouthWest().getLng();
		
		int testX = (int) mapCord.latitude;
		int testY = (int) mapCord.longitude;
		
		
		if (testX >= minX  && testX <= maxX)
		{
			//test x is inside the x border 
			// now we should check Y border
			if (testY >= minY && testY <= maxY)
				return true;
		}
		return false;
	}

	private boolean isBetween(JacocDBLocation location, double input)
	{
		if (input <= location.getHighSpectrumRange() && input >= location.getLowSpectrumRange())
			return true;
		return false;
	}
}
