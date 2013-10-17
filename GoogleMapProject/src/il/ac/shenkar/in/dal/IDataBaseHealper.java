package il.ac.shenkar.in.dal;

import java.util.Collection;

import il.ac.shenkar.common.LatLng;
import il.ac.shenkar.common.LocationBorder;

import il.ac.shenkar.common.JacocDBLocation;

public interface IDataBaseHealper 
{
	public boolean addNewLocation(JacocDBLocation toAdd);
	public boolean addNewLocation(String locationName, LocationBorder realLocation, LocationBorder mapLocation, double highSpectrumRange, double lowSpectrumRange);
	public Collection<?> getAllLocations();
	public Collection<?> getAllLocationsForSpinner();
	public void cleanDB();
}
