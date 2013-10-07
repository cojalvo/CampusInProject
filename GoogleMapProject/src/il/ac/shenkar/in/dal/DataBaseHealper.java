package il.ac.shenkar.in.dal;


import il.ac.shenkar.common.JacocDBLocation;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This Class is responsible to Access the DB and map between our map location to the real world location
 * 
 * @author Jacob
 *
 */
public class DataBaseHealper extends SQLiteOpenHelper
{
	private static DataBaseHealper instance = null;
	private ArrayList<JacocDBLocation> locationList;

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
		
		locationList = new ArrayList<JacocDBLocation>();
		
		
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
				LOW_SPEC + "REAL)";
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
	
	private void initArrayList()
	{
		// here i will insert the code that populate the Array List
	}

}
