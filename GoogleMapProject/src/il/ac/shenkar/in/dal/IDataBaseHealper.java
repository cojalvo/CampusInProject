package il.ac.shenkar.in.dal;

import java.util.Collection;

import com.google.android.gms.maps.model.LatLng;

import il.ac.shenkar.common.JacocDBLocation;

public interface IDataBaseHealper 
{
	public boolean addNewLocation(JacocDBLocation toAdd);
	public boolean addNewLocation(String locationName, LatLng realLocation, LatLng mapLocation, double highSpectrumRange, double lowSpectrumRange);
	public Collection<?> getAllLocations();
}
