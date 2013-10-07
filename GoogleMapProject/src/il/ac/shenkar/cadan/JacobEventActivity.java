package il.ac.shenkar.cadan;


import il.ac.shenkar.common.CampusInEvent;


import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class JacobEventActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_list_layout);
		
		 final ListView eventsList = (ListView) findViewById(R.id.eventsList);
		 eventsList.setAdapter(new EventListBaseAdapter(this, getEvents()));
		 
	}
	
	
	public ArrayList<CampusInEvent> getEvents()
	{
		ArrayList<CampusInEvent> toReturn = new ArrayList<CampusInEvent>();
		CampusInEvent currEvent;
		
				
		for (int i=0; i<40; i++)
		{
			currEvent = new CampusInEvent();
			currEvent.setDate(new Date());
			currEvent.setDescription("description " + i);
			currEvent.setHeadLine("Title "+i);
			toReturn.add(currEvent);	
		}
		
		return toReturn;
	}
	
	public void eventButtonClick(View v)
	{
		int position = (Integer) v.getTag();
		
		ListView eventList =  (ListView) findViewById(R.id.eventsList);
		CampusInEvent currEvent = (CampusInEvent) eventList.getAdapter().getItem(position);
		Toast.makeText(this,currEvent.getDescription(), 3000).show();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
