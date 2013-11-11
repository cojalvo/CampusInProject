package il.ac.shenkar.in.bl;

import java.util.Collection;
import java.util.HashMap;

import il.ac.shenkar.common.CampusInLocation;
import il.ac.shenkar.common.JacocDBLocation;
import il.ac.shenkar.in.dal.DataBaseHealper;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.entity.mime.content.ContentBody;

public class LocationHelper implements ILocationHelper
{

    private Collection<JacocDBLocation> locationList;
    private HashMap<String, JacocDBLocation> campusInLocationMap;

    /**
     * The c'tor received context in order to access the DataBaseHelper Object
     * to get the locations
     * 
     * @param context
     */
    public LocationHelper(Context context)
    {
	this.locationList = (Collection<JacocDBLocation>) DataBaseHealper.getInstance(context).getAllLocations();
	campusInLocationMap = new HashMap<String, JacocDBLocation>();
	// initialize the hashMap 
	initHase();
    }

    private void initHase()
    {
	for (JacocDBLocation location: locationList)
	{
	    campusInLocationMap.put(location.getLocationName(), location);
	}
    }

    @Override
    public CampusInLocation getLocationFromMapCord(LatLng mapCord)
    {
	for (JacocDBLocation location : locationList)
	{
	    if (this.contains(location, mapCord))
	    {
		return new CampusInLocation(location);
	    }
	}
	return null;
    }

    @Override
    public CampusInLocation getLocationFromSound(double soundCode)
    {
	for (JacocDBLocation location : locationList)
	{
	    if (this.isBetween(location, soundCode))
	    {
		return new CampusInLocation(location);
	    }
	}
	return null;
    }

    @Override
    public CampusInLocation getLocationFromGpsCord(LatLng gpsCord)
    {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * return true if the point in inside the location border
     * 
     * @param location
     * @param mapCord
     * @return
     */
    public boolean contains(JacocDBLocation location, LatLng mapCord)
    {
	double maxX = location.getMapLocation().getNorthEast().getLat();
	double minX = location.getMapLocation().getSouthWest().getLat();
	double maxY = location.getMapLocation().getNorthEast().getLng();
	double minY = location.getMapLocation().getSouthWest().getLng();

	double testX = mapCord.latitude;
	double testY = mapCord.longitude;

	if (testX >= minX && testX <= maxX)
	{
	    // test x is inside the x border
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

    /**
     * the QR code String is the name of the location and the key to the location HaseMap 
     * therefor i need only to query the hashMap for the qrCode parameter and get the Location object 
     */
    @Override
    public CampusInLocation getLocationFromQRCode(String qrCode) throws Exception
    {
	if (qrCode == null)
	    throw new Exception("QR Code is not availble...");
	if (campusInLocationMap.size() == 0)
	    initHase();
	// get the location object
	JacocDBLocation dbLocation = campusInLocationMap.get(qrCode);
	if (dbLocation == null)
	    throw new Exception("Unknown Code...");
	//create the CampusIn Location
	CampusInLocation toReturn = new CampusInLocation(dbLocation);
	//manipulate the point with random
	toReturn = manipulateRandomLocationCordinate(dbLocation, toReturn);
	return toReturn;
    }
    
    private CampusInLocation manipulateRandomLocationCordinate(JacocDBLocation toManipulate,CampusInLocation toReturn)
    {
	double maxX = toManipulate.getRealLocation().getNorthEast().getLat();
	double minX = toManipulate.getRealLocation().getSouthWest().getLat();
	double maxY = toManipulate.getRealLocation().getNorthEast().getLng();
	double minY = toManipulate.getRealLocation().getSouthWest().getLng();
	
	double deltaX = maxX - minX;
	double deltaY = maxY - minY;
	
	double newX = minX + deltaX * Math.random(); 
	double newY = minY + deltaY * Math.random();
	
	if (newX > maxX)
	    Log.i("LocationHealper", "newX > maxX -> algoritem failed");
	else 
	    Log.i("LocationHealper", "newX <= maxX -> algoritem working");
	
	if (newY > maxY)
	    Log.i("LocationHealper", "newY > maxY  -> algoritem failed");
	else 
	    Log.i("LocationHealper", "newY <= maxY  -> algoritem working");
	
	toReturn.setMapLocation(new LatLng(newX, newY));
	
	
	return 	toReturn;
    }
}
