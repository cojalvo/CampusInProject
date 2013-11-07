package il.ac.shenkar.cadan;

import il.ac.shenkar.cadan.AddNewEventFragment.onNewEventAdded;
import il.ac.shenkar.cadan.ChooseFriendsFragment.ChooseFriendAction;
import il.ac.shenkar.common.CampusInElement;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;

import java.util.ArrayList;
import java.util.Currency;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendMassageFragment extends AddNewEventFragment
{
    private onNewMassagecreated callBack;
    
    static SendMassageFragment newInstance(Bundle args)
    {
	SendMassageFragment f = new SendMassageFragment();

	// // Supply num input as an argument.
	// Bundle args = new Bundle();
	// args.putInt("num", num);
	f.setArguments(args);

	return f;
    }
    @Override
    public void onAttach(Activity activity)
    {
	super.onAttach(activity);
	try
	{
	    callBack = (onNewMassagecreated) activity;
	}
	catch (ClassCastException e)
	{
	    throw new ClassCastException(activity.toString() + " must implement onNewMassagecreated");
	}

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
	Controller.getInstance(null).getCurrentUser(new ControllerCallback<CampusInUser>()
	{
	    
	    @Override
	    public void done(CampusInUser retObject, Exception e)
	    {
		currentUser = retObject;
	    }
	});
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	builder.setTitle("השאר הודעה לחברים");
	builder.setPositiveButton("שלח", new DialogInterface.OnClickListener()
	{
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which)
	    {
		
		
	    }
	});
	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
	{
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which)
	    {
		dialog.dismiss();
	    }
	});
	view = getActivity().getLayoutInflater().inflate(R.layout.send_massage_layout, null);
	Button addFriendsButton = (Button) view.findViewById(R.id.massage_add_friends_button);
	addFriendsButton.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		// show add Friends fragment
		ChooseFriendsFragment newFragment = ChooseFriendsFragment.newInstance(ChooseFriendAction.ADD);
		newFragment.setTargetFragment(SendMassageFragment.this, 0);
		newFragment.show(getFragmentManager(), "addFriendsPicher");
	    }
	});
	builder.setView(view);

	return builder.create();
    }

    @Override
    public void setAddedFriends(ArrayList<CampusInUser> addedFriends)
    {
	this.addedFriends = addedFriends;
	TextView recivers = (TextView) view.findViewById(R.id.massage_recivers_text_view);
	if (addedFriends != null)
	{
	    if (!addedFriends.isEmpty())
	    {
		CampusInUser user = addedFriends.get(0);
		recivers.setText(user.getFirstName() +" " + user.getLastName() + ", ...");
	    }
	    else
	    {
		recivers.setText(R.string.no_friend_added);
	    }
	}
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
		    CampusInMessage toReturn = (CampusInMessage) validate();
		    if (toReturn != null)
		    {
			Log.i(getTag(), "new Event was created");
			callBack.onMassageCreated(toReturn);
			dismiss();
		    }
		}
	    });
	}
    }
    
    public interface onNewMassagecreated
    {
	public void onMassageCreated(CampusInMessage sentMassage);
    }

    @Override
    public CampusInElement validate()
    {
	CampusInMessage toReturn = new CampusInMessage();
	if (this.addedFriends  == null || this.addedFriends.isEmpty())
	{
	    Toast.makeText(getActivity(), "please choose friends to send the massage to",3000).show();
	    return null;
	}
	else
	{
	  toReturn.setReciversList(addedFriends);
	}
	EditText massageContent = (EditText) view.findViewById(R.id.massage_content);
	if (massageContent.getText().toString().isEmpty())
	{
	    massageContent.setHint(R.string.empty_massage);
	    massageContent.setHintTextColor(Color.RED);
	    return null;
	}
	else
	{
	   toReturn.setContent(massageContent.getText().toString());
	}
	toReturn.setOwnerId(currentUser.getParseUserId());
	
	return toReturn;
    }
  

}
