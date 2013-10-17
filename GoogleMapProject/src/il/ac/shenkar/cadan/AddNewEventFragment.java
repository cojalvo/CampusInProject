package il.ac.shenkar.cadan;

import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.android.gms.internal.bu;
import com.google.android.gms.internal.ca;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class AddNewEventFragment extends DialogFragment  
{
	private View view;
	private ArrayList<CampusInUser> addedFriends;
	private Calendar cal;
	onNewEventAdded mCallBack;
	
	@Override
	public void onAttach(Activity activity) 
	{
		try {
			mCallBack = (onNewEventAdded) activity;
		}catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onNewEventAdded");
		}
	}


	public void setDate(int year,int month, int day)
	{
		if (cal == null)
			cal = new GregorianCalendar();
		cal.set(year, month, day);
		Log.i("AddNewEventFragment", "date is set to " + cal.toString());
	}
	
	
	public void setTime(int hourOfDay, int minute) 
	{
		if (cal == null)
			cal = new GregorianCalendar();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
		Log.i("AddNewEventFragment", "date is set to " + cal.toString());
	}
	
	
	public ArrayList<CampusInUser> getAddedFriends() {
		return addedFriends;
	}
	public void setAddedFriends(ArrayList<CampusInUser> addedFriends) 
	{
		this.addedFriends = addedFriends;
		// after the friend list was added i will change the text on the button
		Button b = (Button) view.findViewById(R.id.event_add_friends_button);
		b.setText("חברים התווספו בהצלחה");
	}



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
				.setTitle("הוסף אירוע")
				.setPositiveButton("הוסף", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//do somthing with Positive input;
						//validate the data insert is OK 
						//TODO: validate the data and return new Event to the main activity 
						
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
        
        Button timeButton = (Button) view.findViewById(R.id.event_pick_time);
        timeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				DialogFragment newDialogFragment = new TimePickerFragment();
				newDialogFragment.setTargetFragment(AddNewEventFragment.this,0);
				newDialogFragment.show(getFragmentManager(), "TimePicker");
			}
		});
        
        // set the toogle button
        ToggleButton isPublicButton = (ToggleButton) view.findViewById(R.id.event_is_public_toggle_button);
        isPublicButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) 
                {
                	// set the add friends button to be visible 
                	view.findViewById(R.id.event_add_friends_button).setVisibility(View.VISIBLE);
                } else
                {
                	//set the add friends button to be invisible 
                	view.findViewById(R.id.event_add_friends_button).setVisibility(View.INVISIBLE);
                }
            }
        });
        
        Button addFriendsButton = (Button) view.findViewById(R.id.event_add_friends_button);
        addFriendsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// show add Friends fragment
				AddFriendsFragment newFragment = new AddFriendsFragment();
				newFragment.setTargetFragment(AddNewEventFragment.this, 0);
				newFragment.show(getFragmentManager(), "addFriendsPicher");			
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
			 getDialog().setTitle("ג€°ֳ‚ֳ’ֳ› ג€¡ֳˆֲ¯ֳ‚ֳ� ֳ�ג€�ֻ˜");
			
	        
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
				targetFragment.setDate(year, monthOfYear, dayOfMonth);
			}
		 
	}
	 
    //*********************************************************************************************
    //    						    TIME PICKER
    //*********************************************************************************************
  

        public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
	        public Dialog onCreateDialog(Bundle savedInstanceState) {
		        // Use the current time as the default values for the picker
		        final Calendar c = Calendar.getInstance();
		        int hour = c.get(Calendar.HOUR_OF_DAY);
		        int minute = c.get(Calendar.MINUTE);
		        
		        // Create a new instance of TimePickerDialog and return it
		        return new TimePickerDialog(getActivity(), this, hour, minute,
		        DateFormat.is24HourFormat(getActivity()));
	        }
	        
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
	        {
	        	AddNewEventFragment targetFragment = (AddNewEventFragment) getTargetFragment();
				Button b = (Button)targetFragment.view.findViewById(R.id.event_pick_time);	
				b.setText(hourOfDay +":" + minute);
				targetFragment.setTime(hourOfDay,minute);
	        	
	        }
	        
	   }
        
    public interface onNewEventAdded
    {
    	public void onEventCreated(CampusInEvent addedEvent);
    }



}
