package il.ac.shenkar.in.services;


import il.ac.shenkar.cadan.PrefsFragment;
import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;
import il.ac.shenkar.in.bl.ICampusInController;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ModelUpdateService  extends Service
{
	public static final  String START_COMMAND="il.ac.campusIn.Start";
	public static final String STOP_COMMAND="il.ac.campusIn.Stop";
	private static Boolean isRuning=false;
	Handler handler=new Handler();
	private boolean lastUpdateFinish=true;
	private int interval=7000;
	ICampusInController controller=Controller.getInstance(getBaseContext());
	public void pauseService()
	{
		stop();
	}
	
	public void resumeService()
	{
		start();
	}
	public static Boolean isRuning()
	{
		return isRuning;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filterSend = new IntentFilter();
		filterSend.addAction(START_COMMAND);
		filterSend.addAction(STOP_COMMAND);
		registerReceiver(changeReceiver, filterSend);
		isRuning=true;
	}

	@Override
	public void onDestroy() {
		stop();
		isRuning=false;
		super.onDestroy();
		unregisterReceiver(changeReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		start();
		return  START_NOT_STICKY;
	}
	
	//this runnable is running every interval time.
	private final Runnable autoRefresh = new Runnable()
	{

		@Override
		public void run()
		{
			if (lastUpdateFinish)
			{
				lastUpdateFinish=false;
				Toast.makeText(getBaseContext(), "Start update view model from service", 500).show();
				controller.updateViewModel(new ControllerCallback<Integer>() {
					
					@Override
					public void done(Integer retObject, Exception e) {
						lastUpdateFinish=true;
						//up
						if(retObject==1 && e!=null)
						{
							Toast.makeText(getBaseContext(), e.getMessage(), 500).show();
							handler.postDelayed(autoRefresh,(long)( interval*1.2));
						}
							
						handler.postDelayed(autoRefresh, interval);
					}
				});
			}
		}

	};

	public void start()
	{
		autoRefresh.run();
		isRuning=true;
	}

	public void stop()
	{
		handler.removeCallbacks(autoRefresh);
		isRuning=false;
	}
	
	BroadcastReceiver changeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(START_COMMAND)) 
			{
				start();
			}
			else if(intent.getAction().equals(STOP_COMMAND))
			{
				stop();
			}
		}

	};
	

}