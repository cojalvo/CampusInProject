package il.ac.shenkar.cadan;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import il.ac.shenkar.cadan.R;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;

import android.location.Location;
import android.provider.ContactsContract.CommonDataKinds.Event;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
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
    private static MapManager instance = null;
    private GoogleMap map = null;
    private HashMap<String, Marker> personMarkerdictionary;
    private HashMap<String, Marker> eventMarkerdictionary;
    private HashMap<String, Marker> messageMarkerdictionary;
    private HashMap<Marker, String> markerPersondictiobnary = new HashMap<Marker, String>();
    private HashMap<Marker, String> markerEventDictionary = new HashMap<Marker, String>();
    private final LatLng shenkarLatLong=new LatLng(32.090049, 34.802807);
    private GroundOverlayOptions campusOverlay;
    private float myLastDistance;
    private final int lowDist=800;
    private final int mediumDist=2000;
    
    
    // the key is the lat+long -as string
    private HashMap<String, HashMap<String, Marker>> positionMarkerDic;
    private LatLng lastlongClicked = null;

    public void resetLastLongClicked()
    {
	lastlongClicked = null;
    }

    public void setOnMapLongClickListener(OnMapLongClickListener listener)
    {
	if (listener != null)
	    this.map.setOnMapLongClickListener(listener);
    }

    public void setOnMarkerClickListener(OnMarkerClickListener listener)
    {
	if (listener != null)
	    this.map.setOnMarkerClickListener(listener);
    }
    private void setOnMyLocationChangedListener()
    {
    	if(map!=null)
    		map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
				
				@Override
				public void onMyLocationChange(Location location) {
					float currDistance=getDistanceFromMe(shenkarLatLong);
					if(shouldupdateMapState(currDistance))
						setMapState(currDistance);	
					myLastDistance=currDistance;
				}
			});
    }
    
    private Boolean shouldupdateMapState(float currentDist)
    {
    	if(currentDist==myLastDistance) return false;
    	if(currentDist<lowDist && myLastDistance<lowDist && myLastDistance>0) return false;
    	if((currentDist>lowDist && currentDist<mediumDist) && (myLastDistance>lowDist && myLastDistance<mediumDist)) return false;
    	if(currentDist>mediumDist && myLastDistance>mediumDist) return false;
    	return true;
    	
    }
    private void setMapState(float myDistanceFromCampus)
    {
    	//if the user didnt put ground overlay
    	if(campusOverlay==null)
    	{
    		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    		return;
    	}
    	if(myDistanceFromCampus<lowDist && myDistanceFromCampus >0)
    	{
    		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    		return;
    	}	
    		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public LatLng getLastLongClicked()
    {
	return lastlongClicked;
    }

    private MapManager(GoogleMap map, int mapType)
    {
	this.map = map;
	this.map.setMapType(mapType);
	this.map.getUiSettings().setZoomControlsEnabled(false);
	this.map.setMyLocationEnabled(true);
	personMarkerdictionary = new HashMap<String, Marker>();
	eventMarkerdictionary = new HashMap<String, Marker>();
	positionMarkerDic = new HashMap<String, HashMap<String, Marker>>();
	setOnMyLocationChangedListener();
	myLastDistance=getDistanceFromMe(shenkarLatLong);
	setMapState(myLastDistance);
    }

    public static void resetInstance()
    {
	instance = null;
    }

    public static MapManager getInstance(GoogleMap map, int mapType)
    {
	if (instance == null)
	    instance = new MapManager(map, mapType);
	return instance;
    }

    public void addGroundOverlay(int imageResource, LatLng southWest, LatLng northEast, float transparency)
    {
	BitmapDescriptor image = BitmapDescriptorFactory.fromResource(imageResource); // get
										      // an
										      // image.
	LatLngBounds bounds = new LatLngBounds(southWest, northEast); // get a
								      // bounds
	// Adds a ground overlay with 10% transparency.
	campusOverlay=new GroundOverlayOptions().image(image).positionFromBounds(bounds).transparency(transparency);
	map.addGroundOverlay(campusOverlay);
    }

    // clear the map
    public void clearMap()
    {
	for (Marker toRemove : eventMarkerdictionary.values())
	{
	    toRemove.remove();
	}
	for (Marker toRemove : personMarkerdictionary.values())
	{
	    toRemove.remove();

	}
	personMarkerdictionary.clear();
	markerPersondictiobnary.clear();
	eventMarkerdictionary.clear();
	markerEventDictionary.clear();
    }
    public void disableMap()
    {
    	if(map!=null)
    		map.getUiSettings().setAllGesturesEnabled(false);
    }
    public void enableMap()
    {
    	if(map!=null)
    		map.getUiSettings().setAllGesturesEnabled(true);
    }
    private void InsertMarkers()
    {

    }

    
    public void moveCameraToEvent(String eventId)
    {
	Marker marker;
	if (eventId != null)
	{
	    marker= eventMarkerdictionary.get(eventId);
	    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 40); 
	    map.animateCamera(cameraUpdate);
	    marker.showInfoWindow();
	}
    }

    public void moveCameraToMessage(String messageId)
    {
    		
    }

    public void moveCameraToPerson(String PersonId)
    {
	Marker m = this.personMarkerdictionary.get(PersonId);
	if (m != null)
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 15));
	// Zoom in, animating the camera.
	map.animateCamera(CameraUpdateFactory.zoomTo(2), 2000, null);
    }

    public void moveCameraToLocation(LatLng location, int zoom)
    {
	map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
	map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 3000, null);
    }

    public void addOrUpdatePersonMarker(CampusInUserLocation user)
    {
	Marker marker;
	if (personMarkerdictionary.containsKey(user.getUser().getParseUserId()))
	{
	    marker = personMarkerdictionary.get(user.getUser().getParseUserId());
	    // we lost the location of the user than we remove him
	    if (user.getUser() == null || user.getLocation() == null)
	    {
		marker.remove();
		personMarkerdictionary.remove(user.getUser().getParseUserId());
		return;
	    }

	    marker.setTitle(user.getUser().getFirstName() + " " + user.getUser().getLastName());
	    marker.setPosition(user.getLocation().getMapLocation());
	}

	else
	{
	    // if we dont know the location it will ne null than don't show his
	    // marker
	    if (user == null || user.getLocation() == null)
	    {
		return;
	    }
	    marker = map.addMarker(new MarkerOptions().position(user.getLocation().getMapLocation()).title(user.getUser().getFirstName() + " " + user.getUser().getLastName())
		    .icon(BitmapDescriptorFactory.fromResource(R.drawable.student_marker)));
	    this.personMarkerdictionary.put(user.getUser().getParseUserId(), marker);
	    markerPersondictiobnary.put(marker, user.getUser().getParseUserId());
	}
    }

    public void addOrUpdateEventMarker(CampusInEvent event)
    {
	// create and config the marker Option
	MarkerOptions markerOptions = new MarkerOptions();
	markerOptions.position(event.getLocation().getMapLocation());
	markerOptions.title(event.getHeadLine());
	markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.event_marker));
	markerOptions.snippet(event.getDescription());
	Marker eventMarker = map.addMarker(markerOptions);
	eventMarkerdictionary.put(event.getParseId(), eventMarker);
	markerEventDictionary.put(eventMarker, event.getParseId());
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

    public MarkerType getMarkerType(Marker marker)
    {
	if (personMarkerdictionary.containsValue(marker))
	    return MarkerType.Person;
	if (eventMarkerdictionary.containsValue(marker))
	    return MarkerType.Event;
	return MarkerType.Message;
    }

    public static LatLng getmyLocation()
    {
	if (instance != null)
	{
	    Location loc = instance.map.getMyLocation();
	    if (loc == null)
		return null;
	    return new LatLng(loc.getLatitude(), loc.getLongitude());
	}
	return null;
    }

    public enum MarkerType
    {
	Person, Event, Message
    }

    public String getCampusInUserIdFromMarker(Marker marker)
    {
	if (markerPersondictiobnary.containsKey(marker))
	    return markerPersondictiobnary.get(marker);
	return null;
    }

    public String getEventIdFromMarker(Marker marker)
    {
	if (markerEventDictionary.containsKey(marker))
	    return markerEventDictionary.get(marker);
	return null;
    }

    public GoogleMap getMap()
    {
        return map;
    }
    public float getDistanceFromMe(String id)
    {
    	Marker dest;
    	if(personMarkerdictionary.containsKey(id))
    		dest=personMarkerdictionary.get(id);
    	else if(eventMarkerdictionary.containsKey(id))
    		dest=eventMarkerdictionary.get(id);
    	else if(messageMarkerdictionary.containsKey(id))
    		dest=messageMarkerdictionary.get(id);
    	else
    		return -1;
    	return getDistanceFromMe(dest.getPosition());
    }
    
    private float getDistanceFromMe(LatLng p)
    {
    	Location destLocation = new Location("point A");
    	destLocation.setLatitude(p.latitude);
    	destLocation.setLongitude(p.longitude);
    	Location myLocation=map.getMyLocation();
    	if(myLocation==null) return -1;
    	return myLocation.distanceTo(destLocation);
    }
    
    public float getDistanceFromMe(Marker marker)
    {
    	String id="";
    	if(markerEventDictionary.containsKey(marker))
    		id=markerEventDictionary.get(marker);
    	else if(markerPersondictiobnary.containsKey(marker))
    		id=markerPersondictiobnary.get(marker);
    	else
    		return -1;
    	return getDistanceFromMe(id);
    }
}
