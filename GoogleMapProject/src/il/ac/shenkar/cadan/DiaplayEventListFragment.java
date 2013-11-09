package il.ac.shenkar.cadan;

import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class DiaplayEventListFragment extends DialogFragment
{
    private View view;
    private ArrayList<CampusInEvent> eventList;
    private CampusInUser currentUser;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
	super.onCreateDialog(savedInstanceState);

	// get the current user
	Controller.getInstance(getActivity()).getCurrentUser(new ControllerCallback<CampusInUser>()
	{

	    @Override
	    public void done(CampusInUser retObject, Exception e)
	    {
		currentUser = retObject;

	    }
	});

	Controller.getInstance(getActivity()).getCurrentUserAllEvents(new ControllerCallback<List<CampusInEvent>>()
	{

	    @Override
	    public void done(List<CampusInEvent> retObject, Exception e)
	    {
		// if the return object is not null and the exception is null
		// asign the return object to the eventList variable
		// else agign an empty List
		eventList = retObject != null && e == null ? (ArrayList<CampusInEvent>) retObject : new ArrayList<CampusInEvent>();
	    }
	});

	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(R.string.my_event_list).setPositiveButton(R.string.close,
		new DialogInterface.OnClickListener()
		{

		    @Override
		    public void onClick(DialogInterface dialog, int which)
		    {

			dialog.dismiss();

		    }
		});
	// set the view
	view = getActivity().getLayoutInflater().inflate(R.layout.events_list_layout, null, false);

	// set the adapter to the list
	final ListView eventsList = (ListView) view.findViewById(R.id.eventsList);
	eventsList.setAdapter(new EventListBaseAdapter(getActivity(), this.eventList, this));

	// attache a listener to the text box in order to filter the events
	EditText searchBox = (EditText) view.findViewById(R.id.event_search_edit_text);
	searchBox.addTextChangedListener(new TextWatcher()
	{

	    @Override
	    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
	    {
		// When user changed the Text
		ListView eventList = (ListView) view.findViewById(R.id.eventsList);
		EventListBaseAdapter adapter = (EventListBaseAdapter) eventList.getAdapter();
		adapter.getFilter().filter(cs);
	    }

	    @Override
	    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
	    {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable arg0)
	    {
		// TODO Auto-generated method stub

	    }

	});

	builder.setIcon(R.drawable.campus_in_ico);
	builder.setView(view);
	return builder.create();

    }
}
