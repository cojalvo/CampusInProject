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
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SendMassageFragment extends AddNewEventFragment
{
    private onNewMassagecreated callBack;
    private SeekBar sb;
    private CheckBox cb;

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
	builder.setTitle("׳©׳�׳— ׳”׳•׳“׳¢׳”");
	builder.setPositiveButton("׳©׳�׳—", new DialogInterface.OnClickListener()
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
	cb = (CheckBox) view.findViewById(R.id.distance_check);
	sb = (SeekBar) view.findViewById(R.id.seekBar1);
	sb.setEnabled(false);
	cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	    {
		sb.setEnabled(isChecked);

	    }
	});
	final TextView tv = (TextView) view.findViewById(R.id.distance_text);
	sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
	{

	    @Override
	    public void onStopTrackingTouch(SeekBar seekBar)
	    {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onStartTrackingTouch(SeekBar seekBar)
	    {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	    {
		tv.setText(" ׳ ׳™׳×׳ ׳× ׳�׳§׳¨׳™׳�׳” ׳‘׳¨׳“׳™׳•׳¡ ׳©׳� " + progress + " ׳�׳˜׳¨׳™׳� ");

	    }
	});
	addFriendsButton.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		// show add Friends fragment
		ChooseFriendsFragment newFragment = ChooseFriendsFragment.newInstance(ChooseFriendAction.ADD, isfriendsAdded, addedFriends);
		newFragment.setTargetFragment(SendMassageFragment.this, 0);
		newFragment.show(getFragmentManager(), "addFriendsPicher");
	    }
	});

	EditText messageContent = (EditText) view.findViewById(R.id.massage_content);
	messageContent.requestFocus();

	builder.setIcon(R.drawable.campus_in_ico);
	builder.setView(view);
	Dialog dialog = builder.create();
	dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | LayoutParams.SOFT_INPUT_ADJUST_PAN);
	return dialog;
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
		recivers.setText(user.getFirstName() + " " + user.getLastName() + ", ...");
		Button editFriends = (Button) view.findViewById(R.id.massage_add_friends_button);
		editFriends.setText(R.string.edit_friends);
		isfriendsAdded = true;
	    }
	    else
	    {
		recivers.setText(R.string.no_friend_added);
		isfriendsAdded = false;
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
	toReturn.setSenderFullName(currentUser.getFirstName() + " " + currentUser.getLastName());
	if (this.addedFriends == null || this.addedFriends.isEmpty())
	{
	    Toast.makeText(getActivity(), "please choose friends to send the massage to", 3000).show();
	    return null;
	}
	else
	{
	    toReturn.setReciversList(addedFriends);
	}
	EditText massageContent = (EditText) view.findViewById(R.id.massage_content);
	if (cb.isChecked())
	{
	    toReturn.setReadInRadius(sb.getProgress());
	}
	else
	{
	    // -1 mean that there is no limitation
	    toReturn.setReadInRadius(-1);
	}
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
