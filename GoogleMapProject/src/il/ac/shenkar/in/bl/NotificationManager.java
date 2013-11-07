package il.ac.shenkar.in.bl;

import il.ac.shenkar.common.CampusInEvent;

import java.util.Collection;
import java.util.HashMap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * the Notification Manager Class is the class that manages the adding,
 * removing and updating the notifications for the user the class holds a
 * HashMap of the current Notification applied for the current user, every
 * time the updateNotification method is called the class then deside which
 * Notification to update remove or add.
 * 
 * @author Jacob
 * 
 */
public class NotificationManager
{
	HashMap<Integer, PendingIntent> norificationsList = new HashMap<Integer, PendingIntent>();
	HashMap<Integer, CampusInEvent> eventsList = new HashMap<Integer, CampusInEvent>();
	String currUserParseId;
	AlarmManager alarmManager;
	Context appContext;
	
	public NotificationManager(String userParseID, Context appContext)
	{
	    this.appContext = appContext;
	    currUserParseId = userParseID;
	    alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
	}
	/**
	 * this method is being used for new events created on this device
	 * 
	 * @param event
	 */
	public void updateNotification(CampusInEvent event)
	{
	    if (event != null)
	    {
		Integer key = event.getParseId().hashCode();
		PendingIntent NotifictionToAdd = this.builSndSendNotificationToEvent(event);
		//add notification
		norificationsList.put(key, NotifictionToAdd);
		//add event 
		eventsList.put(key, event);
	    }
	}
	
	public void updateNotificationsToAllEvents(Collection<CampusInEvent> events)
	{
	    if (norificationsList.size() > events.size())
	    {
		// we got more notifications then events - we need to remove some notifications
		RemoveNotifications(events);
	    }
	    for (CampusInEvent currEvent: events)
	    {
		if (!norificationsList.containsValue(currEvent) && currEvent.getReceiversId().contains(currUserParseId))
		{
		    // new event - add it like new event
		    updateNotification(currEvent);
		}
		
	    }
	}
	private void RemoveNotifications(Collection<CampusInEvent> eventsFromCloud)
	{
	    // go over all of my events with the notifications and check if the event is arrived from the cloud
	    for(CampusInEvent eventWithNotification : this.eventsList.values())
	    {
		if (!eventsFromCloud.contains(eventWithNotification))
		{
		    Integer keyToRemove = eventWithNotification.getParseId().hashCode();
		    PendingIntent pendingIntentToRemove = this.norificationsList.get(keyToRemove);
		    try
		    {
			alarmManager.cancel(pendingIntentToRemove);
			this.eventsList.remove(keyToRemove);
			this.norificationsList.remove(keyToRemove);
		    }
		    catch (Exception e)
		    {
			Log.i(this.toString(),"could not remove alarm manager notification for event name:  " + eventsList.get(keyToRemove).getHeadLine());
		    }
		    
		}
	    }
	}

	private PendingIntent builSndSendNotificationToEvent(CampusInEvent event)
	{
	    // add the event to the Alarm manager
	    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(appContext);
	    // default reminder is 5 minuts before
	    String reminderTime = sharedPrefs.getString("event_reminder", "300000");

	    long reminderTimeInMiliseconds = Long.parseLong(reminderTime);
	    /* value '0' mean no reminder is needed */
	    if (reminderTimeInMiliseconds > 0)
	    {
		Intent activityIntent = new Intent("il.ac.asenkar.brodcast_receiver_costum_reciver");
		activityIntent.putExtra("event_id", (String) event.getParseId());
		PendingIntent pendingInent = PendingIntent.getBroadcast(appContext, 0, activityIntent, 0);
		alarmManager.set(AlarmManager.RTC_WAKEUP, event.getDate().getTime() - reminderTimeInMiliseconds, pendingInent);
		return pendingInent;
	    }
	    return null;
	}

}