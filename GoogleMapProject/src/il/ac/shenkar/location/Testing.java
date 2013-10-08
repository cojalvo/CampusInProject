package il.ac.shenkar.location;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import il.ac.shenkar.common.LatLng;

import il.ac.shenkar.common.CampusInLocations;
import il.ac.shenkar.common.JacocDBLocation;

public class Testing {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		
		CampusInLocations locations = new CampusInLocations();
		List<JacocDBLocation> list = locations.getLocationsList();
		
		JacocDBLocation location1 = new JacocDBLocation("Cafiteria", new LatLng(55, 66), new LatLng(50, 60), 897.5, 968.6);
		JacocDBLocation location2 = new JacocDBLocation("room 44", new LatLng(55, 66), new LatLng(50, 60), 897.5, 968.6);
		list.add(location2);
		list.add(location1);
		
		
		Serializer serializer = new Persister();
		File result = new File("example.xml");
		serializer.write(locations, result);
	}

}
