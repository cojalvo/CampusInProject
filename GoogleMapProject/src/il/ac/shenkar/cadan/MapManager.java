package il.ac.shenkar.cadan;

import java.util.HashMap;

import il.ac.shenkar.cadan.R;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUserLocation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapManager
{
	private GoogleMap map = null;
	private HashMap<String, Marker> personMarkerdictionary;
	private HashMap<String, Marker> eventMarkerdictionary;
	private HashMap<String, Marker> messageMarkerdictionary;
	private LatLng lastlongClicked=null;
	public void resetLastLongClicked()
	{
		lastlongClicked=null;
	}
	public void setOnMapLongClickListener(OnMapLongClickListener listener)
	{
		if(listener!=null)
			this.map.setOnMapLongClickListener(listener);	
	}
	public void setOnMarkerClickListener(OnMarkerClickListener listener)
	{
		if(listener!=null)
			this.map.setOnMarkerClickListener(listener);	
	}
	public LatLng getLastLongClicked()
	{
		return lastlongClicked;
	}
	public MapManager(GoogleMap map, int mapType)
	{
		this.map = map;
		this.map.setMapType(mapType);
		this.map.getUiSettings().setZoomControlsEnabled(false);
		personMarkerdictionary = new HashMap<String, Marker>();
		eventMarkerdictionary = new HashMap<String, Marker>();
	}
	public void addGroundOverlay(int imageResource, LatLng southWest,
			LatLng northEast, float transparency)
	{
		BitmapDescriptor image = BitmapDescriptorFactory
				.fromResource(imageResource); // get an image.
		LatLngBounds bounds = new LatLngBounds(southWest, northEast); // get a
																		// bounds
		// Adds a ground overlay with 10% transparency.
		map.addGroundOverlay(new GroundOverlayOptions().image(image)
				.positionFromBounds(bounds).transparency(transparency));
	}
	public void moveCameraToEvent(String eventId)
	{
		
	}
	public void moveCameraToMessage(String messageId)
	{
		
	}
	public void moveCameraToPerson(String PersonId)
	{
		Marker m=this.personMarkerdictionary.get(PersonId);
		if(m!=null)
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 15));
	    // Zoom in, animating the camera.
	    map.animateCamera(CameraUpdateFactory.zoomTo(2), 2000, null);
	}
	public void moveCameraToLocation(LatLng location, int zoom)
	{
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
		 map.animateCamera(CameraUpdateFactory.zoomTo(2), 2000, null);
	}
	public void addOrUpdatePersonMarker(CampusInUserLocation user)
	{
	    Marker newUser = map.addMarker(new MarkerOptions().position(user.getLocation().getMapLocation())
		        .title(user.getUserName()));
	    this.personMarkerdictionary.put(user.getUserID(), newUser);
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
