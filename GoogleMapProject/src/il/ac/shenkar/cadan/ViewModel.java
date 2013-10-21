package il.ac.shenkar.cadan;

import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;


public class ViewModel
{
	private Context context;
	//TODO -remove it the context should be in he controller.
	public ViewModel(Context context) {
		super();
		this.context = context;
	}

	private  HashMap<String, CampusInMessage> messages;
	private  HashMap<String, CampusInEvent> events;
	private HashMap<String, CampusInEvent> myEvents;
	private HashMap<String, CampusInEvent> tests;
	private HashMap<String, CampusInEvent> lesons;
	private  HashMap<String, CampusInUserLocation> friends;
	private HashMap<String , CampusInUser> friendsHash;
	
	public void updateViewModel()
	{
		invokeViewModelChanged();
	}
	/*
	 * After finish update the view model obj an intent will
	 * invoke to update the receivers. (they will update the view with the changes.)
	 * 
	 */
	//TODO-I think the best location will be in the controller.
	private void  invokeViewModelChanged()
	{
		Intent inti = new Intent();
		inti.setAction(CampusInConstant.VIEW_MODEL_UPDATED);
		context.sendBroadcast(inti);
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
		
		if(friends!=null) return friends.values();
		return new LinkedList<CampusInUserLocation>();
	}
}
