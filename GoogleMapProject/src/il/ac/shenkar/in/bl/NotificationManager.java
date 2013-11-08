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
 * the Notification Manager Class is the class that manages the adding, removing
 * and updating the notifications for the user the class holds a HashMap of the
 * current Notification applied for the current user, every time the
 * updateNotification method is called the class then deside which Notification
 * to update remove or add.
 * 
 * @author Jacob
 * 
 */
public class NotificationManager
{
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
	    this.builSndSendNotificationToEvent(event);
	    // add event
	    eventsList.put(key, event);
	}
    }

    public void updateNotificationsToAllEvents(Collection<CampusInEvent> eventsFromCloud)
    {
	if (eventsFromCloud.size() > eventsList.size())
	{
	    for (CampusInEvent currEvent : eventsFromCloud)
	    {
		if (currEvent.getReceiversId().contains(currUserParseId) && !eventsList.containsValue(currEvent))
		{
		    // new event - add it like new event
		    updateNotification(currEvent);
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
	    PendingIntent pendingInent = PendingIntent.getBroadcast(appContext, event.getParseId().hashCode(), activityIntent, 0);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, event.getDate().getTime() - reminderTimeInMiliseconds, pendingInent);
	    return pendingInent;
	}
	return null;
    }

}