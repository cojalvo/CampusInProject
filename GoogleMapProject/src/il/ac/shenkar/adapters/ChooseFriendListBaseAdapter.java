package il.ac.shenkar.adapters;

import il.ac.shenkar.cadan.R;
import il.ac.shenkar.cadan.R.id;
import il.ac.shenkar.cadan.R.layout;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserChecked;

import java.util.ArrayList;
import java.util.Currency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class ChooseFriendListBaseAdapter extends BaseAdapter implements Filterable
{

    protected static ArrayList<CampusInUserChecked> friendsArrayList;
    protected static ArrayList<CampusInUserChecked> filteredFriendsArrayList;
    protected LayoutInflater l_Inflater;
    protected CampusInUser currentUser;

    public ChooseFriendListBaseAdapter(Context contect, ArrayList<CampusInUserChecked> list, CampusInUser currentUser)
    {
    	this.friendsArrayList = list;
    	this.filteredFriendsArrayList = list;
    	this.currentUser = currentUser;
    	this.l_Inflater = LayoutInflater.from(contect);
    }

    @Override
    public int getCount()
    {
	return filteredFriendsArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
	return filteredFriendsArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	CheckBox currCheckBox;
	ImageView currProfilePicture;
	ViewHolder holder;

	if (convertView == null)
	{
	    convertView = l_Inflater.inflate(R.layout.friend_layout, null);
	    holder = new ViewHolder();
	    holder.txt_itemFullName = (TextView) convertView.findViewById(R.id.friend_name);
	    holder.checkBox = (CheckBox) convertView.findViewById(R.id.friend_check_box);
	    holder.imageView = (ImageView) convertView.findViewById(R.id.friend_profile_picture_imageView);
	    convertView.setTag(holder);
	}
	else
	{
	    holder = (ViewHolder) convertView.getTag();
	}

	/*
	 * Here I get id for the button of this specific view and give him "tag"
	 * so i could delete it later on
	 */
	currCheckBox = (CheckBox) convertView.findViewById(R.id.friend_check_box);
	currCheckBox.setTag(position);
	currCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	    {
		filteredFriendsArrayList.get((Integer) buttonView.getTag()).setChecked(isChecked);
	    }
	});
	// setting the facebook profile picture
	currProfilePicture = (ImageView) convertView.findViewById(R.id.friend_profile_picture_imageView);
	currProfilePicture.setImageDrawable(filteredFriendsArrayList.get(position).getProfilePicture());
	
	holder.txt_itemFullName.setText(filteredFriendsArrayList.get(position).getUser().getFirstName() + " " + filteredFriendsArrayList.get(position).getUser().getLastName());
	if (filteredFriendsArrayList.get(position).isChecked())
	{
	    // currCheckBox = (CheckBox)
	    // convertView.findViewById(R.id.friend_check_box);
	    currCheckBox.setChecked(true);
	}
	else
	    currCheckBox.setChecked(false);
	return convertView;

    }

    // this method is been called any time an event is added or removed from the
    // list
    // it's like a refresh
    @Override
    public void notifyDataSetChanged()
    {
	super.notifyDataSetChanged();
    }

    static class ViewHolder
    {
	TextView txt_itemFullName;
	CheckBox checkBox;
	ImageView imageView;
    }

    @Override
    public Filter getFilter()
    {
	return new Filter()
	{

	    @Override
	    protected void publishResults(CharSequence constraint, FilterResults results)
	    {
		filteredFriendsArrayList = (ArrayList<CampusInUserChecked>) results.values;
		notifyDataSetChanged();

	    }

	    @Override
	    protected FilterResults performFiltering(CharSequence constraint)
	    {
		
		FilterResults results = new FilterResults();
		// If there's nothing to filter on, return the original data for
		// your list
		if (constraint == null || constraint.length() == 0)
		{
		    results.values = friendsArrayList;
		    results.count = friendsArrayList.size();
		}
		else
		{
		    // filtering the list
		    String prefixLowerCase = constraint.toString().toLowerCase();
		    ArrayList<CampusInUserChecked> filteredData = new ArrayList<CampusInUserChecked>();
		    CampusInUser user;
		    for (CampusInUserChecked curr : friendsArrayList)
		    {
			user = curr.getUser();
			if (user.getFirstName().toLowerCase().startsWith(prefixLowerCase) || user.getLastName().toLowerCase().startsWith(prefixLowerCase))
			{
			    filteredData.add(curr);
			}
		    }
		    results.values = filteredData;
		    results.count = filteredData.size();
		}

		return results;
	    }
	};
    }

}
