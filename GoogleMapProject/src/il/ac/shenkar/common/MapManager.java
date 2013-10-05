package il.ac.shenkar.common;

import java.util.HashMap;

import il.ac.shenkar.cadan.R;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class MapManager
{
	private GoogleMap map=null;
	private HashMap<String, Marker> personMarkerdictionary;
	private HashMap<String, Marker> eventMarkerdictionary;
	private HashMap<String, Marker> messageMarkerdictionary;
	public MapManager(GoogleMap map,int mapType)
	{
		this.map=map;
		this.map.setMapType(mapType);
		personMarkerdictionary=new HashMap<String, Marker>();
		eventMarkerdictionary=new HashMap<String, Marker>();
	}
	public void addGroundOverlay(int imageResource,LatLng southWest,LatLng northEast,float transparency)
	{
		   BitmapDescriptor image = BitmapDescriptorFactory.fromResource(imageResource); // get an image.
		   LatLngBounds bounds = new LatLngBounds(southWest,northEast); // get a bounds
		    // Adds a ground overlay with 10% transparency.
		    map.addGroundOverlay(new GroundOverlayOptions()
		        .image(image)
		        .positionFromBounds(bounds)
		        .transparency(transparency));
	}
	
	public void addOrUpdatePersonMarker(CampusInUserLocation user)
	{
		
	}
	public void addOrUpdateEventMarker(CampusInEvent event)
	{
		
	}
	public void addOrUpdateMessageMarker(CampusInMessage event)
	{
		
	}
	public void removePersonMarker(String id)
	{
		
	}
	public void removeEventMarker(String id)
	{
		
	}
	public void removeMessageMarker(String id)
	{
		
	}
	}

}
