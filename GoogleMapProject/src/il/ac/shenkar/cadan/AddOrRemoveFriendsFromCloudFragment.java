package il.ac.shenkar.cadan;

import il.ac.shenkar.cadan.ChooseFriendsFragment.ChooseFriendAction;
import il.ac.shenkar.cadan.ChooseFriendsFragment.onFriendsAddedListener;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserChecked;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;
import il.ac.shenkar.in.bl.ICampusInController;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddOrRemoveFriendsFromCloudFragment extends DialogFragment
{
    onFriendsAddedListener mCallback;
    private View view;
    private List<CampusInUser> friensList;
    private ChooseFriendAction action;
    private ICampusInController controller = null;

    static AddOrRemoveFriendsFromCloudFragment newInstance(ChooseFriendAction action)
    {
	AddOrRemoveFriendsFromCloudFragment f = new AddOrRemoveFriendsFromCloudFragment();

	// Supply num input as an argument.
	Bundle args = new Bundle();
	args.putSerializable("action", action);
	f.setArguments(args);
	f.action = action;
	return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
	super.onCreateDialog(savedInstanceState);
	controller = Controller.getInstance(getActivity());
	initFriendList();
	MessageHalper.showProgressDialog("Loading Friends", getActivity());
	view = getActivity().getLayoutInflater().inflate(R.layout.add_friends_fragment_layout, null, false);
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	if (action == ChooseFriendAction.ADD)
	{
	    builder.setTitle("׳‘׳—׳¨ ׳—׳‘׳¨׳™׳�").setPositiveButton("׳�׳™׳©׳•׳¨", new DialogInterface.OnClickListener()
	    {

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
		    ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
		    FriendListBaseAdapter adapter = (FriendListBaseAdapter) friendListView.getAdapter();

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
	    }).setNegativeButton("׳‘׳™׳˜׳•׳�", new DialogInterface.OnClickListener()
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
	    builder.setTitle("׳¨׳©׳™׳�׳× ׳—׳‘׳¨׳™׳�").setPositiveButton("׳�׳™׳©׳•׳¨", new DialogInterface.OnClickListener()
	    {

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
		    ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
		    FriendListBaseAdapter adapter = (FriendListBaseAdapter) friendListView.getAdapter();

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
	    }).setNegativeButton("׳‘׳™׳˜׳•׳�", new DialogInterface.OnClickListener()
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
		FriendListBaseAdapter adapter = (FriendListBaseAdapter) friendListView.getAdapter();
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

    /**
     * this method is ment to bring all of the friends from cloud right on the
     * fragment is beeing started
     */
    private void initFriendList()
    {
	if (action == ChooseFriendAction.ADD)
	{
	    // the user want to add friends to his friend list
	    // i need to get all users from the cloud
	    controller.getCurrentUser(new ControllerCallback<CampusInUser>()
	    {
		@Override
		public void done(final CampusInUser curretntUser, Exception e)
		{
		    if (e == null && curretntUser != null)
		    {
			controller.getAllCumpusInUsers(new ControllerCallback<List<CampusInUser>>()
			{
			    @Override
			    public void done(List<CampusInUser> retObject, Exception e)
			    {
				if (retObject != null)
				    friensList = retObject;
				Toast.makeText(getActivity(), "number of friends:" + retObject.size(), 3000).show();
				ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
				friendListView.setAdapter(new FriendListBaseAdapter(getActivity(), getFriends(), curretntUser));
				MessageHalper.closeProggresDialog();
			    }
			});
		    }
		}
	    });
	}
	else
	{
	    // the user want to remove friends from his friend list
	    // i need to get only his friends from the cloud
	    controller.getCurrentUser(new ControllerCallback<CampusInUser>()
	    {

		@Override
		public void done(final CampusInUser curretntUser, Exception e)
		{
		    if (e == null && curretntUser != null)
		    {
			controller.getCurrentUserFriendList(new ControllerCallback<List<CampusInUser>>()
			{

			    @Override
			    public void done(List<CampusInUser> retObject, Exception e)
			    {
				if (retObject != null)
				{
				    friensList = retObject;
				    Toast.makeText(getActivity(), "number of friends:" + retObject.size(), 3000).show();
				    ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
				    friendListView.setAdapter(new FriendListBaseAdapter(getActivity(), getFriends(), curretntUser));
				    MessageHalper.closeProggresDialog();
				}

			    }
			});
		    }
		}
	    });
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
	    u.setChecked(false);
	    toReturn.add(u);
	}
	return toReturn;
    }
}
