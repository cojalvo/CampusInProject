package il.ac.shenkar.common;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import il.ac.shenkar.common.LatLng;

@Root
public class JacocDBLocation
{
	@Element
	private String locationName;
	@Element
	private LatLng realLocation;
	@Element
	private LatLng mapLocation;
	@Element
	private double highSpectrumRange;
	@Element
	private double lowSpectrumRange;
	
	
	
	public JacocDBLocation(){}
	
	public JacocDBLocation(String locationName, LatLng realLocation,
			LatLng mapLocation, double highSpectrumRange,
			double lowSpectrumRange)
	{
		super();
		this.locationName = locationName;
		this.realLocation = realLocation;
		this.mapLocation = mapLocation;
		this.highSpectrumRange = highSpectrumRange;
		this.lowSpectrumRange = lowSpectrumRange;
	}
	public String getLocationName()
	{
		return locationName;
	}
	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}
	public LatLng getRealLocation()
	{
		return realLocation;
	}
	public void setRealLocation(LatLng realLocation)
	{
		this.realLocation = realLocation;
	}
	public LatLng getMapLocation()
	{
		return mapLocation;
	}
	public void setMapLocation(LatLng mapLocation)
	{
		this.mapLocation = mapLocation;
	}
	public double getHighSpectrumRange()
	{
		return highSpectrumRange;
	}
	public void setHighSpectrumRange(double highSpectrumRange)
	{
		this.highSpectrumRange = highSpectrumRange;
	}
	public double getLowSpectrumRange()
	{
		return lowSpectrumRange;
	}
	public void setLowSpectrumRange(double lowSpectrumRange)
	{
		this.lowSpectrumRange = lowSpectrumRange;
	}
	
	
	
}