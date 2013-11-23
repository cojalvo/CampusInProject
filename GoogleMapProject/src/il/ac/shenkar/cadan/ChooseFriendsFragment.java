package il.ac.shenkar.cadan;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.adapters.ChooseFriendListBaseAdapter;
import il.ac.shenkar.cadan.AddNewEventFragment.DatePickerFragment;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserChecked;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;
import il.ac.shenkar.in.bl.ICampusInController;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ChooseFriendsFragment extends DialogFragment
{
    onFriendsAddedListener mCallback;
    private View view;
    private List<CampusInUser> friensList;
    private List<CampusInUser> priviousAddedFriend = new ArrayList<CampusInUser>();
    private ChooseFriendAction action;
    private boolean isfriendAdded = false;
    private ICampusInController controller = null;
    ProgressDialog progressDialog;

    static ChooseFriendsFragment newInstance(ChooseFriendAction action, boolean isfriendAdded, List<CampusInUser> priviousAddedFriend)
    {
	ChooseFriendsFragment f = new ChooseFriendsFragment();

	// Supply num input as an argument.
	Bundle args = new Bundle();
	args.putSerializable("action", action);
	args.putBoolean("friendAdded", isfriendAdded);
	f.setArguments(args);
	f.action = action;
	if (isfriendAdded == true)
	{
	    f.isfriendAdded = isfriendAdded; // true
	    f.priviousAddedFriend = priviousAddedFriend;
	}
	return f;//
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
	super.onCreateDialog(savedInstanceState);
	controller = Controller.getInstance(getActivity());
	view = getActivity().getLayoutInflater().inflate(R.layout.add_friends_fragment_layout, null, false);
	initFriendList();
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	if (action == ChooseFriendAction.ADD)
	{
	    builder.setTitle(R.string.add_friends).setPositiveButton(R.string.add, new DialogInterface.OnClickListener()
	    {

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
		    ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
		    ChooseFriendListBaseAdapter adapter = (ChooseFriendListBaseAdapter) friendListView.getAdapter();

		    ArrayList<CampusInUser> toReturn = new ArrayList<CampusInUser>();
		    CampusInUserChecked currUserChecked;
		    // iterate the list of friends and add the
		    // check ones to the return list
		    for (int i = 0; i < adapter.getCount(); i++)
		    {
			currUserChecked = (CampusInUserChecked) adapter.getItem(i);
			if (currUserChecked.isChecked())
			{
			    toReturn.add(currUserChecked.getUser());
			}
		    }

		    mCallback.onFriendsWereChoosen(toReturn, getTargetFragment(), ChooseFriendAction.ADD);
		}
	    }).setNegativeButton("ביטול", new DialogInterface.OnClickListener()
	    {

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
		    // no button preesed - dismiss dialog
		    dialog.dismiss();
		}
	    });
	}
	else
	{
	    builder.setTitle(R.string.remove_friends).setPositiveButton(R.string.remove, new DialogInterface.OnClickListener()
	    {

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
		    ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
		    ChooseFriendListBaseAdapter adapter = (ChooseFriendListBaseAdapter) friendListView.getAdapter();

		    ArrayList<CampusInUser> toReturn = new ArrayList<CampusInUser>();
		    CampusInUserChecked currUserChecked;
		    // iterate the list of friends and add the
		    // check ones to the return list
		    for (int i = 0; i < adapter.getCount(); i++)
		    {
			currUserChecked = (CampusInUserChecked) adapter.getItem(i);
			if (currUserChecked.isChecked())
			{
			    toReturn.add(currUserChecked.getUser());
			}
		    }

		    mCallback.onFriendsWereChoosen(toReturn, getTargetFragment(), ChooseFriendAction.REMOVE);
		}
	    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
	    {

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
		    // no button preesed - dismiss dialog
		    dialog.dismiss();
		}
	    });
	}

	// this part is for filtering the list of friends
	EditText inputSearch = (EditText) view.findViewById(R.id.inputSearch);
	inputSearch.addTextChangedListener(new TextWatcher()
	{

	    @Override
	    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
	    {
		// When user changed the Text
		ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
		ChooseFriendListBaseAdapter adapter = (ChooseFriendListBaseAdapter) friendListView.getAdapter();
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

    @Override
    public void onAttach(Activity activity)
    {
	super.onAttach(activity);
	// This makes sure that the container activity has implemented
	// the callback interface. If not, it throws an exception
	try
	{
	    mCallback = (onFriendsAddedListener) activity;
	}
	catch (ClassCastException e)
	{
	    throw new ClassCastException(activity.toString() + " must implement IonFriendsAddedListener");
	}
    }

    private ArrayList<CampusInUserChecked> getFriends()
    {

	ArrayList<CampusInUserChecked> toReturn = new ArrayList<CampusInUserChecked>();
	// add all the friend to friendList
	// wrap them with CheckedCampusInUSer Object

	if (this.friensList == null)
	{
	    return null;
	}
	for (CampusInUser friend : friensList)
	{
	    CampusInUserChecked u = new CampusInUserChecked(friend);
	    
	    if (priviousAddedFriend.contains(friend))
	    {
		u.setChecked(true);
	    }
	    else
	    {
		u.setChecked(false);
	    }
	    toReturn.add(u);
	}

	return toReturn;
    }

    private void initFriendList()
    {
    	final CampusInUser curretntUser=controller.getCurrentUser();
		    controller.getCurrentUserFriendList(new ControllerCallback<List<CampusInUser>>()
		    {

			@Override
			public void done(List<CampusInUser> retObject, Exception e)
			{
			    if (retObject != null)
				friensList = retObject;
			    ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
			    friendListView.setAdapter(new ChooseFriendListBaseAdapter(getActivity(), getFriends(), curretntUser));
			    // progressDialog.dismiss();
			}
		    });
    }

    public interface friendChoice
    {
	public void onFriendChosed(ArrayList<CampusInUser> friendChosedList);
    }

    /**
     * this interface should be implemented by any Activity or Fragment which
     * want to add friend list for example: invite some friends to some event or
     * decide which friends will see me
     * 
     * once the Friend Picker fragment is finished, the fragment will call
     * OnFriendsWereAdded() the method return a list of the added friends and
     * reference to targeted fragment if the activity want to access the
     * fragment which called the add friends picker
     * 
     * @author Jacob
     * 
     */
    public interface onFriendsAddedListener
    {
	public void onFriendsWereChoosen(ArrayList<CampusInUser> friensList, Fragment targetedFragment, ChooseFriendAction action);
    }

    public enum ChooseFriendAction
    {
	ADD, REMOVE
    }

    @Override
    public void onStart()
    {
	super.onStart();
	this.getDialog().setCanceledOnTouchOutside(false);
    }

}
