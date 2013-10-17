package il.ac.shenkar.in.services;

import il.ac.shenkar.common.CampusInLocations;
import il.ac.shenkar.common.JacocDBLocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import il.ac.shenkar.common.LatLng;
import il.ac.shenkar.in.dal.DataBaseHealper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class InitLocations extends AsyncTask<Context, Integer, CampusInLocations>
{
	private final String downloadedFile = "downloaded.xml";
	private final String existingFile = "existing.xml";
	private Context context;

	@Override
	protected CampusInLocations doInBackground(Context... params) 
	{
		context = params[0];
		File outputFile = new File(params[0].getFilesDir(), downloadedFile );
		File existingXML = new File(params[0].getFilesDir() + existingFile);
		// this code takes xml configuration file from the internet and save it to local file 
		try 
		{
			// url to the file on the server 
			//URL url = new URL("	http://globalwork.co.il/uploadFiles/example");
			URL url = new URL("http://10.0.14.228:8080/test/example.xml");
			
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			
			// create the new file and streams
			FileOutputStream fileOutput = new FileOutputStream(outputFile);
			InputStream inputStream = urlConnection.getInputStream();
			
			byte[] buffer = new byte[1024];
			int bufferLength = 0; //used to store a temporary size of the buffer
			while ( (bufferLength = inputStream.read(buffer)) > 0 ) 
			{
				fileOutput.write(buffer, 0, bufferLength);
			}
			fileOutput.close();
			Log.i("CampusIn", "File was downloaded and save to file system!");
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!existingXML.exists())
		{
			// if the file does not exists meaning the DB is empty and we need to init it
			Log.i("CampusIn", "first rile does not exist - init the DB!");
			try 
			{
				
				CampusInLocations downloadedObjects = getObjectsFromXmlFile(outputFile);
				List<JacocDBLocation> resultList = downloadedObjects.getLocationsList();
				for (JacocDBLocation location: resultList)
				{
					if (location == null)
						break;
					this.addLocationToDB(params[0], location);
				}
				outputFile.renameTo(existingXML);
				Log.i("CampusIn", "DB was successfuky updated!");
				Log.i("CampusIn", "Downloaded file change to: " + existingFile);
				return downloadedObjects;
			} catch (Throwable e) {
				// TODO: handle exception
			}
		}
		else
		{
			/**
			 *  else mean there is a file existing 
			 *   we need to distinguish between two cases:
			 	1 the file is the same file -> in this case do nothing
			 	2 the file is different, we need to update our DB
			 */
			Log.i("CampusIn", "the file: " + existingFile +" does exists, comparing two files");
			if (compareFiles(existingXML, outputFile) == Idevtical.SAME)
			{
				// do nothing - DB is up do date
				Log.i("CampusIn", "Files are identical -> do nothing");
				try {
					CampusInLocations downloadedObjects = getObjectsFromXmlFile(outputFile);
					return downloadedObjects;
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else
			{
				Log.i("CampusIn", "Files are diffrent -> updating the DB");
				// the files are diffrent.
				//update the DB
				DataBaseHealper  dataBaseHealper = DataBaseHealper.getInstance(params[0]);
				dataBaseHealper.cleanDB();
				CampusInLocations downloadedObjects = null;
				try {
					downloadedObjects = getObjectsFromXmlFile(outputFile);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<JacocDBLocation> resultList = downloadedObjects.getLocationsList();
				for (JacocDBLocation location: resultList)
				{
					this.addLocationToDB(params[0], location);
				}
				// DB is updated change the file name from downloaded to existing 
				outputFile.renameTo(existingXML);
				Log.i("CampusIn", "DB was successfuly updated");
				Log.i("CampusIn", downloadedFile + " was renamed to: " +existingFile );
				return downloadedObjects;
			}
			
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(CampusInLocations result) 
	{
		super.onPostExecute(result);
		
		Toast.makeText(context, "init Locations Async Task Ended", 3000);
	}
	
	private CampusInLocations getObjectsFromXmlFile(File source) throws Throwable
	{
		Serializer serializer = new Persister();
		CampusInLocations result = serializer.read(CampusInLocations.class, source);
		
		return result;
	}
	
	private void addLocationToDB(Context context, JacocDBLocation toAdd)
	{
		DataBaseHealper.getInstance(context).addNewLocation(toAdd);
	}
	
	private Idevtical compareFiles(File existingFile, File newFile)
	{
		StringBuilder ExistingLine = new StringBuilder();
		StringBuilder newLine = new StringBuilder();
		
		String line = "";
		
		
        try {
        	
        		BufferedReader in = new BufferedReader(new FileReader(existingFile));
        		while ((line = in.readLine()) != null) 
        			ExistingLine.append(line);
        		
        		in = new BufferedReader(new FileReader(newFile));
        		while ((line = in.readLine()) != null) 
        			newLine.append(line);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if (ExistingLine.toString().equals((newLine.toString())))
        	return Idevtical.SAME;
        else
        	return Idevtical.DIFFRENT;
      
	}
	
	public enum Idevtical
	{
		DIFFRENT,SAME;
	}

}
