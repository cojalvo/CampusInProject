package il.ac.shenkar.common;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import com.google.android.gms.internal.j;

@Root
public class CampusInLocations 
{
	@ElementList
	private List<JacocDBLocation> locationsList;

	public List<JacocDBLocation> getLocationsList() {
		return locationsList;
	}

	public void setLocationsList(List<JacocDBLocation> locationsList) {
		this.locationsList = locationsList;
	}

	public CampusInLocations(List<JacocDBLocation> locationsList) {
		super();
		this.locationsList = locationsList;
	}

	public CampusInLocations() 
	{
		super();
		locationsList = new ArrayList<JacocDBLocation>();
	}
	
	

}
