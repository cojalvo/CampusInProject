package il.ac.shenkar.cadan;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.adapters.MessagesBaseAdapter;
import il.ac.shenkar.cadan.ChooseFriendsFragment.ChooseFriendAction;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInMessegeItem;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class DisplayMyMessagesFragment extends DialogFragment
{
    private Controller controller;
    private ArrayList<CampusInMessegeItem> myMessages = new ArrayList<CampusInMessegeItem>();
    CampusInUser currUser;
    
    static DisplayMyMessagesFragment newInstance()
    {
	DisplayMyMessagesFragment f = new DisplayMyMessagesFragment();
	// Supply num input as an argument.
	Bundle args = new Bundle();
	f.setArguments(args);
	return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
	super.onCreateDialog(savedInstanceState);
	controller = Controller.getInstance(getActivity());
	// get the current campus in user 
	controller.getCurrentUser(new ControllerCallback<CampusInUser>()
	{
	    
	    @Override
	    public void done(CampusInUser retObject, Exception e)
	    {
		currUser = retObject;		
	    }
	});
	
	initMyMessages();
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	//set the dialog title 
	builder.setTitle(R.string.my_messages);
	builder.setPositiveButton(R.string.my_messages, new DialogInterface.OnClickListener()
	{
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which)
	    {
		dialog.dismiss();
	    }
	} );
	
	View view = getActivity().getLayoutInflater().inflate(R.layout.add_friends_fragment_layout, null, false);
	ListView friendListView = (ListView) view.findViewById(R.id.friends_list_view);
	friendListView.setAdapter(new MessagesBaseAdapter(getActivity(), this.myMessages, currUser, this));
	
	builder.setIcon(R.drawable.campus_in_ico);
	
	return builder.create();     
    }

    public void initMyMessages()
    {
	List<CampusInMessage> messagesFromContyroller = controller.getAllMessages();
	CampusInMessegeItem currItem;
	for (CampusInMessage fromController: messagesFromContyroller)
	{
	    currItem = new CampusInMessegeItem();
	    currItem.setContent(fromController.getContent());
	    currItem.setLocation(fromController.getLocation());
	    currItem.setOwnerId(fromController.getOwnerId());
	    currItem.setParseId(fromController.getParseId());
	    currItem.setReadInRadius(fromController.getReadInRadius());
	    currItem.setReceiverId(fromController.getReceiverId());
	    currItem.setSenderFullName(fromController.getSenderFullName());
	    currItem.setCanISeeTheMessage(canISeeTheMessage(fromController.getParseId()));
	    this.myMessages.add(currItem);
	}
    }
    
    public Boolean canISeeTheMessage(String messageID)
    {
    	if(messageID==null) return false;
    	CampusInMessage theMessage=controller.getMessage(messageID);
    	if(theMessage==null) return false;
    	if (currUser == null) return false;
    	if(theMessage.getOwnerId().equals(currUser.getParseUserId())) return true;
    	int radius=theMessage.getReadInRadius();
    	if(radius==-1) return true;
    	float mydist;
    	MapManager manager = MapManager.getInstance(null, 0);
    	mydist = manager.getDistanceFromMe(messageID);
    	if(mydist>radius) 
    	{
    	    return false;
    	}
    	return true;
    }
    
}
