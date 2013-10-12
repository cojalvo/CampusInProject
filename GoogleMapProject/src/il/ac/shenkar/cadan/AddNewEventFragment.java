package il.ac.shenkar.cadan;

import java.util.Calendar;

import com.google.android.gms.internal.bu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

public class AddNewEventFragment extends DialogFragment  
{
	private View view;
	
	static AddNewEventFragment newInstance(int num) 
	{
		AddNewEventFragment f = new AddNewEventFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }
	
	
	
	 @Override
	public void onCreate(Bundle savedInstanceState) 
	 {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}



	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("הוסף אירוע חדש")
				.setPositiveButton("הוסף", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//do somthing with Positive input;
						//validate the data insert is OK 
						
					}
				})
				.setNegativeButton("בטל", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();	
					}
				});
		
		view = getActivity().getLayoutInflater().inflate(R.layout.add_event_activity_layout, null, false);
		
		 // becouse it's a fragment and i want to deal with the events in here i will put listenets manualy - 
        //else it will search the method name on the Activity class
        Button dateButton = (Button) view.findViewById(R.id.event_pick_date_button);
        dateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// show the Date Picker
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.setTargetFragment(AddNewEventFragment.this, 0);
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
        

        builder.setView(view);
        return builder.create();
	}

/*
	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	 {
			view = inflater.inflate(R.layout.add_event_activity_layout, container, false);
			
			// set the title
			 getDialog().setTitle("הוסף אירוע חדש");
			
	        
	        // becouse it's a fragment and i want to deal with the events in here i will put listenets manualy - 
	        //else it will search the method name on the Activity class
	        Button dateButton = (Button) view.findViewById(R.id.event_pick_date_button);
	        dateButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					// show the Date Picker
					DialogFragment newFragment = new DatePickerFragment();
					newFragment.setTargetFragment(AddNewEventFragment.this, 0);
					newFragment.show(getFragmentManager(), "datePicker");
				}
			});
	        return view;
	 }
	*/
	
	
	
	//*********************************************************************************************
    //									DATE PICKER
    //*********************************************************************************************
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener 
	{
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
			{
				AddNewEventFragment targetFragment = (AddNewEventFragment) getTargetFragment();
				Button b = (Button)targetFragment.view.findViewById(R.id.event_pick_date_button);	
				b.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
			}
		 
	}
	


}
