package il.ac.shenkar.in.bl;

import com.google.android.gms.maps.model.LatLng;

public interface ILocationHelper
{
	String getLocationNameFromMapCord(LatLng mapCord);
	String getLocationNameFromSound(int soundCode);
	String getLocationNameFromGpsCord(LatLng gpsCord);
	LatLng getMapLocationFromSound(int code);
	LatLng getMapLocationFromGps(LatLng gpsLatLong);
	LatLng getMapLocationFromLocationName(String name);
}
