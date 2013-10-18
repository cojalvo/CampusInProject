package il.ac.shenkar.common;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root
public class JacocDBLocation
{
	@Element
	private String locationName;
	@Element
	private LocationBorder realLocation;
	@Element
	private LocationBorder mapLocation;
	@Element
	private double highSpectrumRange;
	@Element
	private double lowSpectrumRange;
	
	
	
	public LatLng calcCenter()
	{
		int latNorth = (int) this.getMapLocation().getNorthEast().getLat();
		int latSouth = (int) this.getMapLocation().getSouthWest().getLat();
		int lngNorth = (int) this.getMapLocation().getNorthEast().getLng();
		int lngSouth = (int) this.getMapLocation().getSouthWest().getLng();
		
		return new LatLng((latNorth-latSouth)/2, (lngNorth-lngSouth)/2);
	}
	public JacocDBLocation(){}
	
	
	public JacocDBLocation(String locationName, LocationBorder realLocation,
			LocationBorder mapLocation, double highSpectrumRange,
			double lowSpectrumRange) {
		super();
		this.locationName = locationName;
		this.realLocation = realLocation;
		this.mapLocation = mapLocation;
		this.highSpectrumRange = highSpectrumRange;
		this.lowSpectrumRange = lowSpectrumRange;
	}


	
	
	public String getLocationName() {
		return locationName;
	}


	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}


	public LocationBorder getRealLocation() {
		return realLocation;
	}


	public void setRealLocation(LocationBorder realLocation) {
		this.realLocation = realLocation;
	}


	public LocationBorder getMapLocation() {
		return mapLocation;
	}


	public void setMapLocation(LocationBorder mapLocation) {
		this.mapLocation = mapLocation;
	}


	public double getHighSpectrumRange() {
		return highSpectrumRange;
	}


	public void setHighSpectrumRange(double highSpectrumRange) {
		this.highSpectrumRange = highSpectrumRange;
	}


	public double getLowSpectrumRange() {
		return lowSpectrumRange;
	}


	public void setLowSpectrumRange(double lowSpectrumRange) {
		this.lowSpectrumRange = lowSpectrumRange;
	}




	
}
