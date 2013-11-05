package il.ac.shenkar.in.services;


import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;
import il.ac.shenkar.in.bl.ICampusInController;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ModelUpdateService  extends Service
{
	Handler handler=new Handler();
	private boolean lastUpdateFinish=true;
	private int interval=7000;
	ICampusInController controller=Controller.getInstance(getBaseContext());
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stop();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		start();
		return  START_STICKY;
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
						handler.postDelayed(autoRefresh, interval);
					}
				});
			}
		}

	};

	public void start()
	{
		autoRefresh.run();
	}

	public void stop()
	{
		handler.removeCallbacks(autoRefresh);
	}
	

}