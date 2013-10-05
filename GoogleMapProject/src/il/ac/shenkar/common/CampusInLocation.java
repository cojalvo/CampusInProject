package il.ac.shenkar.common;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class CampusInLocation {
	//the relative location on the overlay map of Shenkar
	private LatLng mapLocation;
	private Date date;
	public CampusInLocation()
	{
		super();
		date=new Date();
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	//the name of the location.
	// TODO perhaps we need to use Enum or something else.
	private String name;
	public LatLng getMapLocation() {
		return mapLocation;
	}
	public void setMapLocation(LatLng mapLocation) {
		this.mapLocation = mapLocation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//TODO This might be Unnecessary
	@Override
	public boolean equals(Object loc)
	{
		try
		{
			CampusInLocation cLoc=(CampusInLocation) loc;
			return cLoc.mapLocation.equals(mapLocation) && cLoc.name.equals(name);
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
}
