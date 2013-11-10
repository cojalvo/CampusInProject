package il.ac.shenkar.adapters;

import il.ac.shenkar.adapters.ChooseFriendListBaseAdapter;
import il.ac.shenkar.cadan.DisplayFriendFragment;
import il.ac.shenkar.cadan.R;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserChecked;
import il.ac.shenkar.in.bl.Controller;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayFriendListBaseAdapter extends ChooseFriendListBaseAdapter
{
    private DisplayFriendFragment dialog;

    public DisplayFriendListBaseAdapter(Context contect, ArrayList<CampusInUserChecked> list, CampusInUser currentUser, DisplayFriendFragment dialog)
    {
	super(contect, list, currentUser);
	this.dialog = dialog;
	// TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	ImageView currImageView;
	ImageView currProfilePicture;
	ViewHolder holder;

	if (convertView == null)
	{
	    convertView = l_Inflater.inflate(R.layout.display_friend_layout, null);
	    holder = new ViewHolder();
	    holder.txt_itemFullName = (TextView) convertView.findViewById(R.id.display_friend_name);
	    holder.imageView2 = (ImageView) convertView.findViewById(R.id.display_friend_navigate_button);
	    holder.imageView = (ImageView) convertView.findViewById(R.id.display_friend_profile_picture_imageView);
	    holder.status = (TextView) convertView.findViewById(R.id.display_friend_status);
	    convertView.setTag(holder);
	}
	else
	{
	    holder = (ViewHolder) convertView.getTag();
	}
	currImageView = (ImageView) convertView.findViewById(R.id.display_friend_navigate_button);
	currImageView.setTag(position);
	currImageView.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		Integer position = (Integer) v.getTag();
		CampusInUser navigateTo = filteredFriendsArrayList.get(position).getUser();
		dialog.dismiss();
		Controller controller = Controller.getInstance(null);
		controller.closePreferanceView();
		controller.navigateTo(navigateTo.getParseUserId());

	    }
	});

	// setting the facebook profile picture
	currProfilePicture = (ImageView) convertView.findViewById(R.id.display_friend_profile_picture_imageView);
	currProfilePicture.setImageDrawable(filteredFriendsArrayList.get(position).getProfilePicture());
	holder.status.setText(filteredFriendsArrayList.get(position).getUser().getStatus());
	holder.txt_itemFullName.setText(filteredFriendsArrayList.get(position).getUser().getFirstName() + " " + filteredFriendsArrayList.get(position).getUser().getLastName());
	
	
	return convertView;
    }

    static class ViewHolder
    {
	TextView txt_itemFullName;
	TextView status;
	ImageView imageView2;
	ImageView imageView;
    }

}
