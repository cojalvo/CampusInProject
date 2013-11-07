package il.ac.shenkar.cadan;

import il.ac.shenkar.cadan.ChooseFriendsFragment.ChooseFriendAction;
import il.ac.shenkar.cadan.R.id;
import il.ac.shenkar.common.CampusInElement;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInLocation;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.dal.DataBaseHealper;
import il.ac.shenkar.in.dal.IDataAccesObject;
import il.ac.shenkar.in.dal.IDataBaseHealper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TooManyListenersException;

import com.google.android.gms.internal.bu;
import com.google.android.gms.internal.ca;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AddNewEventFragment extends DialogFragment
{
    protected View view;
    protected ArrayList<CampusInUser> addedFriends;
    protected Calendar cal;
    protected CampusInUser currentUser;
    onNewEventAdded mCallBack;

    @Override
    public void onAttach(Activity activity)
    {
	super.onAttach(activity);
	try
	{
	    mCallBack = (onNewEventAdded) activity;
	}
	catch (ClassCastException e)
	{
	    throw new ClassCastException(activity.toString() + " must implement onNewEventAdded");
	}

    }

    public void setDate(int year, int month, int day)
    {
	if (cal == null)
	    cal = new GregorianCalendar();
	cal.set(year, month, day, cal.HOUR_OF_DAY, cal.MINUTE);
	Log.i("AddNewEventFragment", "date is set to " + cal.toString());
    }

    public void setTime(int hourOfDay, int minute)
    {
	if (cal == null)
	    cal = new GregorianCalendar();
	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
	Log.i("AddNewEventFragment", "date is set to " + cal.toString());
    }

    public ArrayList<CampusInUser> getAddedFriends()
    {
	return addedFriends;
    }

    public void setAddedFriends(ArrayList<CampusInUser> addedFriends)
    {
	this.addedFriends = addedFriends;
	// after the friend list was added i will change the text on the button
	Button b = (Button) view.findViewById(R.id.event_add_friends_button);
	b.setText(R.string.success_adding_friends_to_event);
    }

    static AddNewEventFragment newInstance(Bundle args)
    {
	AddNewEventFragment f = new AddNewEventFragment();

	// // Supply num input as an argument.
	// Bundle args = new Bundle();
	// args.putInt("num", num);
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

	Controller.getInstance(getActivity()).getCurrentUser(new ControllerCallback<CampusInUser>()
	{

	    @Override
	    public void done(CampusInUser retObject, Exception e)
	    {
		currentUser = retObject;

	    }
	});
	Boolean showLocationsSpinner = true;
	Bundle args = this.getArguments();
	if (args != null)
	{
	    showLocationsSpinner = args.getBoolean("showLocationSpinner");
	}
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(R.string.add_event_title)
		.setPositiveButton(R.string.add_event_positiv_button, new DialogInterface.OnClickListener()
		{

		    @Override
		    public void onClick(DialogInterface dialog, int which)
		    {
			// do nothing in here, i overidded the listener on
			// onStart method
			// this could validate the data in the fields before
			// closing the dialog
		    }
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{

		    @Override
		    public void onClick(DialogInterface dialog, int which)
		    {
			dialog.dismiss();
		    }
		});

	view = getActivity().getLayoutInflater().inflate(R.layout.add_event_activity_layout, null, false);

	// dislay data to the spinner

	Spinner locatinSrinner = (Spinner) view.findViewById(R.id.event_location_spinner);
	View locationLabel = view.findViewById(R.id.event_Location_text_view);
	IDataBaseHealper DBHelper = DataBaseHealper.getInstance(getActivity());
	List<String> LocationList = (List<String>) DBHelper.getAllLocationsForSpinner();
	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LocationList);
	locatinSrinner.setAdapter(dataAdapter);
	if (!showLocationsSpinner)
	{
	    locatinSrinner.setVisibility(View.GONE);
	    if (locationLabel != null)
		locationLabel.setVisibility(View.GONE);
	}

	// becouse it's a fragment and i want to deal with the events in here i
	// will put listenets manualy -
	// else it will search the method name on the Activity class
	Button dateButton = (Button) view.findViewById(R.id.event_pick_date_button);
	dateButton.setOnClickListener(new OnClickListener()
	{

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
	timeButton.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		DialogFragment newDialogFragment = new TimePickerFragment();
		newDialogFragment.setTargetFragment(AddNewEventFragment.this, 0);
		newDialogFragment.show(getFragmentManager(), "TimePicker");
	    }
	});

	// set the toogle button
	ToggleButton isPublicButton = (ToggleButton) view.findViewById(R.id.event_is_public_toggle_button);
	isPublicButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
	{
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	    {
		if (isChecked)
		{
		    // set the add friends button to be visible
		    view.findViewById(R.id.event_add_friends_button).setVisibility(View.VISIBLE);
		}
		else
		{
		    // set the add friends button to be invisible
		    view.findViewById(R.id.event_add_friends_button).setVisibility(View.INVISIBLE);
		}
	    }
	});

	Button addFriendsButton = (Button) view.findViewById(R.id.event_add_friends_button);
	addFriendsButton.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		// show add Friends fragment
		ChooseFriendsFragment newFragment = ChooseFriendsFragment.newInstance(ChooseFriendAction.ADD);
		newFragment.setTargetFragment(AddNewEventFragment.this, 0);
		newFragment.show(getFragmentManager(), "addFriendsPicher");
	    }
	});

	builder.setView(view);
	return builder.create();
    }

    // this override is preventing the Dialog from closing before the data is
    // being validate;
    @Override
    public void onStart()
    {
	super.onStart(); // super.onStart() is where dialog.show() is actually
			 // called on the underlying dialog, so we have to do it
			 // after this point
	AlertDialog d = (AlertDialog) getDialog();
	this.getDialog().setCanceledOnTouchOutside(false);
	if (d != null)
	{
	    Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
	    positiveButton.setOnClickListener(new View.OnClickListener()
	    {
		@Override
		public void onClick(View v)
		{
		    CampusInEvent toReturn = (CampusInEvent) validate();
		    if (toReturn != null)
		    {
			Log.i(getTag(), "new Event was created");
			mCallBack.onEventCreated(toReturn);
			dismiss();
		    }
		}
	    });
	}
    }

    // *********************************************************************************************
    // DATE PICKER
    // *********************************************************************************************
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
	    Button b = (Button) targetFragment.view.findViewById(R.id.event_pick_date_button);
	    b.setText(dayOfMonth + "/" + (1 + monthOfYear) + "/" + year);
	    targetFragment.setDate(year, monthOfYear, dayOfMonth);
	}

    }

    // *********************************************************************************************
    // TIME PICKER
    // *********************************************************************************************

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
    {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
	    // Use the current time as the default values for the picker
	    final Calendar c = Calendar.getInstance();
	    int hour = c.get(Calendar.HOUR_OF_DAY);
	    int minute = c.get(Calendar.MINUTE);

	    // Create a new instance of TimePickerDialog and return it
	    return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute)
	{
	    AddNewEventFragment targetFragment = (AddNewEventFragment) getTargetFragment();
	    Button b = (Button) targetFragment.view.findViewById(R.id.event_pick_time);
	    b.setText(hourOfDay + ":" + minute);
	    targetFragment.setTime(hourOfDay, minute);

	}

    }

    public interface onNewEventAdded
    {
	public void onEventCreated(CampusInEvent addedEvent);
    }

    public CampusInElement validate()
    {
	CampusInEvent toReturn = new CampusInEvent();
	Calendar rightNow = Calendar.getInstance();
	EditText eventName = (EditText) view.findViewById(R.id.event_name_edit_text);
	EditText eventDesc = (EditText) view.findViewById(R.id.event_description_edit_text);
	Spinner eventTypeSpinner = (Spinner) view.findViewById(R.id.event_type_spinner);
	ToggleButton isGlobalButton = (ToggleButton) view.findViewById(R.id.event_is_public_toggle_button);
	Spinner rommSrinner = (Spinner) view.findViewById(R.id.event_location_spinner);
	String eventNameString = eventName.getText().toString();

	// cherck the time and date input
	if (cal == null)
	{
	    Toast.makeText(getActivity(), "no Date choosen for the event ", 3000).show();
	    return null;
	}
	if (cal.before(rightNow))
	{
	    Toast.makeText(getActivity(), "Date is not valid cannot add past envents", 3000).show();
	    return null;
	}
	else
	    toReturn.setDate(cal);
	// check the event name input
	if (eventNameString == null || eventNameString.isEmpty())
	{
	    Toast.makeText(getActivity(), "Please set a name to the event", 3000).show();
	    eventName.setHintTextColor(Color.RED);
	    return null;
	}
	else
	    toReturn.setHeadLine(eventNameString);

	// check if public
	if (isGlobalButton.isChecked())
	{
	    // the user want private event and might add friends
	    toReturn.setGlobal(false);
	    toReturn.setReciversList(addedFriends);
	}
	else
	{
	    toReturn.setGlobal(true);
	}
	// set the location
	toReturn.setLocation(new CampusInLocation(DataBaseHealper.getInstance(getActivity()).getLocationObjectFRomName(rommSrinner.getSelectedItem().toString())));
	toReturn.setOwnerId(this.currentUser.getParseUserId());
	toReturn.setDescription(eventDesc.getText().toString());
	toReturn.setEventType(eventTypeSpinner.getSelectedItemPosition());

	return toReturn;
    }

}
