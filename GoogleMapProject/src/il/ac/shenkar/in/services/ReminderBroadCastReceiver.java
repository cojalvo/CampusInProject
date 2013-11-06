package il.ac.shenkar.in.services;

import il.ac.shenkar.cadan.Login;
import il.ac.shenkar.cadan.Main;
import il.ac.shenkar.cadan.R;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.in.bl.Controller;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class ReminderBroadCastReceiver extends BroadcastReceiver
{
    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent)
    {
	final String eventId = intent.getStringExtra("event_id");
	CampusInEvent event = Controller.getInstance(context).getEvent(eventId);

	if (event != null)
	{
	    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    Intent resultIntent = new Intent();
	    resultIntent.setClass(context, Main.class);
	    PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, resultIntent, 0);
	    
	    NotificationCompat.Builder builder  = new NotificationCompat.Builder(context)
		    					.setContentTitle("Incoming Event: " + event.getHeadLine())
		    					.setContentText(event.getDescription())
		    					.setSmallIcon(R.drawable.ic_launcher)
		    					.setContentIntent(pendingIntent);
	    
	    notificationManager.notify(eventId.hashCode(), builder.build());
	}
    }

}
