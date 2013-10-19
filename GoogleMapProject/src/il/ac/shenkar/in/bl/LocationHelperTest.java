package il.ac.shenkar.in.bl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import il.ac.shenkar.common.JacocDBLocation;
import il.ac.shenkar.common.LatLng;
import il.ac.shenkar.common.LocationBorder;
import junit.framework.TestCase;

public class LocationHelperTest extends TestCase {

	public LocationHelperTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetLocationFromSound() {
		fail("Not yet implemented");
	}

	public void testGetLocationFromGpsCord() {
		fail("Not yet implemented");
	}

	public void testContains() {
		LocationHelper helper = new LocationHelper(null);
		JacocDBLocation ob = new JacocDBLocation("yaki", new LocationBorder(new LatLng(50, 60), new LatLng(40, 50)),
				new LocationBorder(new LatLng(2, 4), new LatLng(1, 2)), 800, 600);
		helper.contains(ob, new com.google.android.gms.maps.model.LatLng(1, 3));
		
		fail("Not yet implemented");
	}

}
