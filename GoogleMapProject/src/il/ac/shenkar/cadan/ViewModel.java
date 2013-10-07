package il.ac.shenkar.cadan;

import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUserLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ViewModel
{
	private  HashMap<String, CampusInMessage> messages;
	private  HashMap<String, CampusInEvent> events;
	private  HashMap<String, CampusInUserLocation> users;
	
	public void updateViewModel()
	{
		
	}
	public void updateViewModelInBackground()
	{
		
	}
	public Collection<CampusInEvent> getAllEvents()
	{
		if(events!=null) return events.values();
		return new LinkedList<CampusInEvent>();
		
	}
	public Collection<CampusInMessage> getAllMessages()
	{
		if(messages!=null) return messages.values();
		return new LinkedList<CampusInMessage>();
	}
	
	public Collection<CampusInUserLocation> getAllUsers()
	{
		
		if(users!=null) return users.values();
		return new LinkedList<CampusInUserLocation>();
	}
}
