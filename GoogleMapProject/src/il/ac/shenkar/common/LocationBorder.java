package il.ac.shenkar.common;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * this is a helper Class for keeping 2 points to each location -> north east
 * point and south west point
 * 
 * @author Jacob
 * 
 */
@Root
public class LocationBorder
{
    @Element
    private LatLng northEast;
    @Element
    private LatLng southWest;

    public LatLng getNorthEast()
    {
	return northEast;
    }

    public void setNorthEast(LatLng northEast)
    {
	this.northEast = northEast;
    }

    public LatLng getSouthWest()
    {
	return southWest;
    }

    public void setSouthWest(LatLng southWest)
    {
	this.southWest = southWest;
    }

    public LocationBorder(LatLng northEast, LatLng southWest)
    {
	super();
	this.northEast = northEast;
	this.southWest = southWest;
    }

    public LocationBorder()
    {
	super();
    }

}