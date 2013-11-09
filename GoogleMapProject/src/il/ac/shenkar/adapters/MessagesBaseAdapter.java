package il.ac.shenkar.adapters;

import il.ac.shenkar.adapters.ChooseFriendListBaseAdapter.ViewHolder;
import il.ac.shenkar.cadan.DisplayMyMessagesFragment;
import il.ac.shenkar.cadan.R;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInMessegeItem;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserChecked;
import il.ac.shenkar.in.bl.Controller;

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
    protected CampusInUser currentUser;
    DisplayMyMessagesFragment dialog;

    public MessagesBaseAdapter(Context contect, ArrayList<CampusInMessegeItem> list, CampusInUser currentUser, DisplayMyMessagesFragment dialog)
    {
	MessagesArrayList = list;
	filteredMessagedsArrayList = list;
	this.currentUser = currentUser;
	this.l_Inflater = LayoutInflater.from(contect);
	this.dialog = dialog;
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
		Controller controller = Controller.getInstance(null);
		Integer position = (Integer) v.getTag();
		String messageId = filteredMessagedsArrayList.get(position).getParseId();
		controller.closePreferanceView();
		dialog.dismiss();
		//controller.navigateTo(messageId);
	    }
	});
	CampusInMessegeItem currMessage = filteredMessagedsArrayList.get(position);
	if (currMessage.isCanISeeTheMessage())
	{
	    holder.txt_messageContent.setText(currMessage.getContent());
	}
	else
	{
	    holder.txt_messageContent.setText(R.string.come_closer_to_message);
	}
	return convertView;
    }

    static class ViewHolder
    {
	TextView txt_messageContent;
	ImageView navigateToMessage;
    }
}
