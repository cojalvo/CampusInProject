package il.ac.shenkar.common;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class CampusInLocation {
	//the relative location on the overlay map of Shenkar
	private LatLng mapLocation;
	private String locationName;
	
	public CampusInLocation(JacocDBLocation location) {
		super();
		this.locationName = location.getLocationName();
		il.ac.shenkar.common.LatLng latlng = location.calcCenter();
		this.mapLocation = new LatLng(latlng.getLat(),latlng.getLng());
	}
	public CampusInLocation() {
		super();
	}
	public CampusInLocation(LatLng mapLocation, String locationName) {
		super();
		this.mapLocation = mapLocation;
		this.locationName = locationName;
	}
	public LatLng getMapLocation() {
		return mapLocation;
	}
	public void setMapLocation(LatLng mapLocation) {
		this.mapLocation = mapLocation;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	
	
}
