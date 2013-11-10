package il.ac.shenkar.adapters;

import il.ac.shenkar.adapters.ChooseFriendListBaseAdapter.ViewHolder;
import il.ac.shenkar.cadan.DisplayMyMessagesFragment;
import il.ac.shenkar.cadan.MapManager;
import il.ac.shenkar.cadan.R;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInMessegeItem;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserChecked;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ICampusInController;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MessagesBaseAdapter extends BaseAdapter
{
    protected static ArrayList<CampusInMessegeItem> MessagesArrayList;
    protected static ArrayList<CampusInMessegeItem> filteredMessagedsArrayList;
    protected LayoutInflater l_Inflater;
    private Context context;
    ICampusInController controller;
    protected CampusInUser currentUser;
    DisplayMyMessagesFragment dialog;

    public MessagesBaseAdapter(Context contect, ArrayList<CampusInMessegeItem> list, CampusInUser currentUser, DisplayMyMessagesFragment dialog)
    {
	MessagesArrayList = list;
	filteredMessagedsArrayList = list;
	this.currentUser = currentUser;
	this.l_Inflater = LayoutInflater.from(contect);
	this.dialog = dialog;
	this.context=contect;
	this.controller=Controller.getInstance(context);
    }

    @Override
    public int getCount()
    {
	return filteredMessagedsArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
	return filteredMessagedsArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	ViewHolder holder;
	TextView currMessageContent;
	ImageView currImageView;
	
	if (convertView == null)
	{
	    convertView = l_Inflater.inflate(R.layout.message_item_layout,null);
	    holder = new ViewHolder();
	    holder.txt_messageContent = (TextView) convertView.findViewById(R.id.message_description);
	    holder.navigateToMessage = (ImageView) convertView.findViewById(R.id.message_navigation_button);
	    holder.txt_senderName=(TextView) convertView.findViewById(R.id.message_display_friend_name);
	    holder.sender_picture=(ImageView) convertView.findViewById(R.id.message_display_friend_profile_picture_imageView);
	    holder.txt_distance=(TextView) convertView.findViewById(R.id.message_distance);
	    convertView.setTag(holder);
	}
	else 
	{
	    holder = (ViewHolder) convertView.getTag();
	}
	
	currImageView = (ImageView) convertView.findViewById(R.id.message_navigation_button);
	currImageView.setTag(position);
	currImageView.setOnClickListener(new OnClickListener()
	{
	    
	    @Override
	    public void onClick(View v)
	    {
		Integer position = (Integer) v.getTag();
		String messageId = filteredMessagedsArrayList.get(position).getParseId();
		controller.closePreferanceView();
		dialog.dismiss();
		controller.navigateTo(messageId);
	    }
	});
	CampusInMessegeItem currMessage = filteredMessagedsArrayList.get(position);
	if (currMessage.isCanISeeTheMessage())
	{
	    holder.txt_messageContent.setText(currMessage.getContent());
	    holder.txt_senderName.setText(currMessage.getSenderFullName());
	    holder.sender_picture.setImageDrawable(controller.getFreindProfilePicture(currMessage.getOwnerId(), 150, 1500));
	}
	else
	{
		holder.txt_senderName.setText("לא ניתן לצפות בהודעה");
	    holder.txt_messageContent.setText(R.string.come_closer_to_message);
	    holder.sender_picture.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
	}
	holder.txt_distance.setText(getDistanceString(currMessage.getParseId()));
	return convertView;
    }
    private String getDistanceString(String messageId)
    {
    	float dist = controller.getMyDistanceFrom(messageId);
    	if (dist > 0)
    	{
    	    String unit;
    	    String finalDist;
    	    if (dist > 1000)
    	    {
    		unit = "ק״מ";

    		finalDist = String.format("%.2f", dist / 1000);
    	    }
    	    else
    	    {
    		unit = "מטרים";
    		finalDist = String.format("%.0f", dist);
    	    }

    	    return ("נמצא כ " + finalDist + " " + unit + " " + "ממני");
    	}
    	return "מרחק לא ידוע.";
    }
    static class ViewHolder
    {
    	TextView txt_messageContent;
    	TextView txt_senderName;
    	TextView txt_distance;
    	ImageView sender_picture;
    	ImageView navigateToMessage;
    	
    }
}
