package il.ac.shenkar.in.dal;


import il.ac.shenkar.common.JacocDBLocation;
import il.ac.shenkar.common.LocationBorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import il.ac.shenkar.common.LatLng;
import com.parse.CountCallback;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This Class is responsible to Access the DB and map between our map location to the real world location
 * 
 * @author Jacob
 *
 */
public class DataBaseHealper extends SQLiteOpenHelper implements IDataBaseHealper
{
	private static DataBaseHealper instance = null;
	private Collection<JacocDBLocation> locationList;
	private Collection<String> locationForSpinner;

	// DataBase Members
	private static final int DATABASE_VERSION =1;
	private final static String DATABASE_NAME = "CampusInDB";
	private final static String TABLE_NAME = "Locations";
	private final String LOCATION_NAME = "locationName";
	private final String NE_REAL_LAT = "northEastRealLocationLat";
	private final String NE_REAL_LNG = "northEastRealLocationlng";
	private final String SW_REAL_LAT = "southWestRealLocationLat";
	private final String SW_REAL_LNG = "southWestRealLocationlng";
	private final String NE_MAP_LAT = "northEastMapLocationlat";
	private final String NE_MAP_LNG = "northEastMapLocationlng";
	private final String SW_MAP_LAT = "southWestMapLocationlat";
	private final String SW_MAP_LNG = "SouthWestMapLocationlng";
	private final String HIGH_SPEC ="higeSpectrumRange";
	private final String LOW_SPEC = "lowSpectrumRange";
	
	
	private DataBaseHealper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		// allocate the location list and populate it from the DB
		locationList = new ArrayList<JacocDBLocation>();
		initArrayList();
		
	}
	
	/*Static Function to get the instance for the data structure
	 * 
	 * */			
	public static DataBaseHealper getInstance(Context context)
	{	
		if (instance == null)
			instance = new DataBaseHealper(context);
		return instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		createTable(db);
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);

	}
	
	// this method is been called every time an instance of this type is created
	// this method populate the Collection from the DB records
	private void initArrayList()
	{
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		
		if (cursor.moveToFirst())
		{
			do 
			{
				JacocDBLocation newLocation = new JacocDBLocation();
				newLocation.setLocationName(cursor.getString(1));
				newLocation.setRealLocation(new LocationBorder(new LatLng(cursor.getDouble(2), cursor.getDouble(3)),
											new LatLng(cursor.getDouble(4), cursor.getDouble(5))));
				newLocation.setMapLocation(new LocationBorder(new LatLng(cursor.getInt(6), cursor.getInt(7)),
											new LatLng(cursor.getInt(8), cursor.getInt(9))));
				newLocation.setHighSpectrumRange(cursor.getDouble(10));
				newLocation.setLowSpectrumRange(cursor.getDouble(11));
				
				// adding the new Location to the collection
				locationList.add(newLocation);
			}while (cursor.moveToNext()); // move to the next row in the DB
		
		}
		cursor.close();
		db.close();
	}
	
	private boolean writeLocationObjectToDB(JacocDBLocation toAdd)
	{
		// add the location to the DB
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(this.LOCATION_NAME, toAdd.getLocationName());
		values.put(this.NE_REAL_LAT, toAdd.getRealLocation().getNorthEast().getLat());
		values.put(this.NE_REAL_LNG, toAdd.getRealLocation().getNorthEast().getLng());
		values.put(this.SW_REAL_LAT, toAdd.getRealLocation().getSouthWest().getLat());
		values.put(this.SW_REAL_LNG, toAdd.getRealLocation().getSouthWest().getLng());	
		values.put(this.NE_MAP_LAT, toAdd.getMapLocation().getNorthEast().getLat());
		values.put(this.NE_MAP_LNG, toAdd.getMapLocation().getNorthEast().getLng());
		values.put(this.SW_MAP_LAT, toAdd.getMapLocation().getSouthWest().getLat());
		values.put(this.SW_MAP_LNG, toAdd.getMapLocation().getSouthWest().getLng());
		values.put(this.HIGH_SPEC, toAdd.getHighSpectrumRange());
		values.put(this.LOW_SPEC, toAdd.getLowSpectrumRange());
		
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection
		
		return true;
	}

	@Override
	public boolean addNewLocation(JacocDBLocation toAdd) 
	{
		// adding the task to the list 
		locationList.add(toAdd);
		
		return writeLocationObjectToDB(toAdd);
	}

	@Override
	public boolean addNewLocation	(String locationName, LocationBorder realLocation,
									LocationBorder mapLocation, double highSpectrumRange,
									double lowSpectrumRange) {
		// create JacobDBLocationObject
		JacocDBLocation toAdd = new JacocDBLocation(locationName,realLocation, mapLocation, highSpectrumRange, lowSpectrumRange);
		
		// add it to the DB by calling the "addNewLocation(JacocDBLocation toAdd)" 
		return this.addNewLocation(toAdd);
	}

	@Override
	public Collection<?> getAllLocations() 
	{
		return this.locationList;
	}

	@Override
	public void cleanDB() 
	{
		SQLiteDatabase db = getWritableDatabase();
		String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;
		db.execSQL(DROP_TABLE);
		
		createTable(db);
	}
	
	private void createTable(SQLiteDatabase db)
	{
		if (db == null)
			db = getWritableDatabase();	
		String CREATE_LOCATION_TABLE  =
				"CREATE TABLE " + TABLE_NAME +
				"(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				LOCATION_NAME + " TEXT, "+
				NE_REAL_LAT + " REAL,"+
				NE_REAL_LNG +" REAL,"+
				SW_REAL_LAT + " REAL,"+
				SW_REAL_LNG +" REAL,"+
				NE_MAP_LAT +" INTEGER,"+
				NE_MAP_LNG +" INTEGER,"+
				SW_MAP_LAT +" INTEGER,"+
				SW_MAP_LNG +" INTEGER,"+
				HIGH_SPEC +" REAL," +
				LOW_SPEC + " REAL)";
		db.execSQL(CREATE_LOCATION_TABLE);
		
	}

	@Override
	public Collection<?> getAllLocationsForSpinner()
	{
		if (locationForSpinner == null)
		{
			locationForSpinner = new ArrayList<String>();
			for(JacocDBLocation currLocation: this.locationList)
			{
				locationForSpinner.add(currLocation.getLocationName());
			}
		}
		return locationForSpinner;
	}

}
