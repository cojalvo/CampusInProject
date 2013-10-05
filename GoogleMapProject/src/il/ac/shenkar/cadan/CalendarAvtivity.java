package il.ac.shenkar.cadan;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class CalendarAvtivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.week_timing);
	       CalendarView cal = (CalendarView) findViewById(R.id.calendar_id);
	        
	        cal.setOnDateChangeListener(new OnDateChangeListener() {
				
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getBaseContext(),"Selected Date is\n\n"
					+dayOfMonth+" : "+month+" : "+year , 
					Toast.LENGTH_LONG).show();
			}
		});
	        
	}
	

}
