package il.ac.shenkar.cadan;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.cadan.AddNewEventFragment.DatePickerFragment;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserChecked;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.dal.IDataAccesObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AddFriendsFragment extends DialogFragment 
{
	onFriendsAddedListener mCallback;
	private View view;
	private ArrayList<CampusInUser> friendList;
	private Exception exception = null;
	
	static AddFriendsFragment newInstance(int num) 
	{
		AddFriendsFragment f = new AddFriendsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        

        return f;//
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		MessageHalper.closeProggresDialog();
		MessageHalper.showProgressDialog("Geting Friends...", getActivity());
		IDataAccesObject cloud = CloudAccessObject.getInstance();
		cloud.getCurrentCampusInUserFriends(new DataAccesObjectCallBack<List<CampusInUser>>() {
			
			@Override
			public void done(List<CampusInUser> retObject, Exception e) {
				if (retObject != null && e == null)
					friendList = (ArrayList<CampusInUser>) retObject;
				else
				{
					e.printStackTrace();
					exception = e;
				}
				
			}
		});
		while (this.friendList == null && this.exception == null)
		{
			// the call back didnt happand yet
		}
		MessageHalper.closeProggresDialog();
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle("הוסף חברים")
				.setPositiveButton("הוסף", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
						FriendListBaseAdapter adapter = (FriendListBaseAdapter) friendListView.getAdapter();
						
						ArrayList<CampusInUser> toReturn = new ArrayList<CampusInUser>();
						CampusInUserChecked currUserChecked;
						// iterate the list of friends and add the check ones to the return list
						for (int i=0; i<adapter.getCount(); i++)
						{
							currUserChecked = (CampusInUserChecked) adapter.getItem(i);
							if (currUserChecked.isChecked())
							{
								toReturn.add(currUserChecked.getUser());
							}
						}
						
						mCallback.onFriendsWereAdded(toReturn, getTargetFragment());
					}
				})
				.setNegativeButton("בטל", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						// no button preesed - dismiss dialog 
						dialog.dismiss();	
					}
				});
		
		view = getActivity().getLayoutInflater().inflate(R.layout.add_friends_fragment_layout, null, false);
		
		ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
		friendListView.setAdapter(new FriendListBaseAdapter(getActivity(), getFriends()));
		 
	/*	CheckBox checkBox = (CheckBox) view.findViewById(R.id.friend_check_box);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
				CampusInUserChecked item = (CampusInUserChecked) friendListView.getAdapter().getItem((Integer) buttonView.getTag());
				item.setChecked(isChecked);
				
			}
		});*/
		
		
		// this part is for filtering the list of friends 
		EditText inputSearch = (EditText) view.findViewById(R.id.inputSearch);
		inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
            	FriendListBaseAdapter adapter = (FriendListBaseAdapter) friendListView.getAdapter();     
            	adapter.getFilter().filter(cs); 
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }

			@Override
			public void afterTextChanged(Editable arg0) {
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
        try {
            mCallback = (onFriendsAddedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IonFriendsAddedListener");
        }
	}

	private ArrayList<CampusInUserChecked> getFriends() 
	{
		
		//(ArrayList<CampusInUser>) Controller.getInstance(getActivity()).getCurrentUserFriendList();
		ArrayList<CampusInUserChecked> toReturn = new ArrayList<CampusInUserChecked>(); 
		// add all the friend to friendList 
		// wrap them with CheckedCampusInUSer Object 
		
		if(this.friendList == null)
		{
			return null;
		}
		for (CampusInUser friend: friendList)
		{
			toReturn.add(new CampusInUserChecked(friend));
		}
		return toReturn;
	}
	public interface friendChoice
	{
		public void onFriendChosed(ArrayList<CampusInUser> friendChosedList);
	}


/**
 * this interface should be implemented by any Activity or Fragment which want to add friend list
 * for example: invite some friends to some event or decide which friends will see me 
 * 
 * once the Friend Picker fragment is finished, the fragment will call OnFriendsWereAdded()
 * the method return a list of the added friends and reference to targeted fragment if the activity want to access the fragment which called the add friends picker
 * @author Jacob
 *
 */
public interface onFriendsAddedListener
{
	public void onFriendsWereAdded(ArrayList<CampusInUser> friensList, Fragment targetedFragment);
}


}
