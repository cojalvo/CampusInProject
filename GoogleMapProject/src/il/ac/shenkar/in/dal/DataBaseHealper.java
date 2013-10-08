package il.ac.shenkar.in.dal;


import il.ac.shenkar.common.JacocDBLocation;

import java.util.ArrayList;
import java.util.Collection;

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

	// DataBase Members
	private static final int DATABASE_VERSION =1;
	private final static String DATABASE_NAME = "CampusInDB";
	private final static String TABLE_NAME = "Locations";
	private final String LOCATION_NAME = "locationName";
	private final String REAL_LAT = "realLocationLat";
	private final String REAL_LNG = "realLocationlng";
	private final String MAP_LAT = "mapLocationlat";
	private final String MAP_LNG = "mapLocationlng";
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
		String CREATE_LOCATION_TABLE  =
				"CREATE TABLE " + TABLE_NAME +
				"(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				LOCATION_NAME + " TEXT, "+
				REAL_LAT + " REAL,"+
				REAL_LNG +" REAL,"+
				MAP_LAT +" INTEGER,"+
				MAP_LNG +" INTEGER,"+
				HIGH_SPEC +" REAL," +
				LOW_SPEC + " REAL)";
		db.execSQL(CREATE_LOCATION_TABLE);
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
				newLocation.setLocationName(cursor.getString(0));
				newLocation.setRealLocation(new LatLng(cursor.getDouble(1), cursor.getDouble(2)));
				newLocation.setMapLocation(new LatLng(cursor.getInt(3), cursor.getInt(4)));
				newLocation.setHighSpectrumRange(cursor.getDouble(5));
				newLocation.setLowSpectrumRange(cursor.getDouble(6));
				
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
		values.put(this.REAL_LAT, toAdd.getRealLocation().getLat());
		values.put(this.REAL_LNG, toAdd.getRealLocation().getLng());
		values.put(this.MAP_LAT, toAdd.getMapLocation().getLat());
		values.put(this.MAP_LNG, toAdd.getMapLocation().getLng());
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
	public boolean addNewLocation	(String locationName, LatLng realLocation,
									LatLng mapLocation, double highSpectrumRange,
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
		String DROP_TABLE = "DROP TABLE IF EXISTS "+ DATABASE_NAME + "." + TABLE_NAME;
		db.execSQL(DROP_TABLE);
		

		String CREATE_LOCATION_TABLE  =
				"CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
				" (id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				LOCATION_NAME + " TEXT, "+
				REAL_LAT + " REAL,"+
				REAL_LNG +" REAL,"+
				MAP_LAT +" INTEGER,"+
				MAP_LNG +" INTEGER,"+
				HIGH_SPEC +" REAL," +
				LOW_SPEC + " REAL)";
		db.execSQL(CREATE_LOCATION_TABLE);
	}

}
