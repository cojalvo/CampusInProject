package il.ac.shenkar.in.bl;

import il.ac.shenkar.common.CampusInLocation;

import com.google.android.gms.maps.model.LatLng;

public interface ILocationHelper
{
	CampusInLocation getLocationFromMapCord(LatLng mapCord);
	CampusInLocation getLocationFromSound(double soundCode);
	CampusInLocation getLocationFromGpsCord(LatLng gpsCord);
	CampusInLocation getLocationFromQRCode(String qrCode) throws Exception;

}
