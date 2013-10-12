package il.ac.shenkar.cadan;

import il.ac.shenkar.cadan.EventListBaseAdapter.ViewHolder;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserChecked;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class FriendListBaseAdapter extends BaseAdapter {

	private static ArrayList<CampusInUserChecked> friendsArrayList;
	private LayoutInflater l_Inflater;
	
	public FriendListBaseAdapter(Context contect, ArrayList<CampusInUserChecked> list)
	{
		this.friendsArrayList =list;
		this.l_Inflater = LayoutInflater.from(contect);
	}
	@Override
	public int getCount() {
		return friendsArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return friendsArrayList.get(position);
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
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = l_Inflater.inflate(R.layout.friend_layout, null);
			holder = new ViewHolder();
			holder.txt_itemFullName = (TextView) convertView.findViewById(R.id.friend_name);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.friend_check_box);
			convertView.setTag(holder);
		} else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		/*
		 * Here I get id for the button of this specific view and give him "tag" so i could delete it later on
		 * */
		currCheckBox = (CheckBox) convertView.findViewById(R.id.friend_check_box);
		currCheckBox.setTag(position);
		currCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				friendsArrayList.get((Integer) buttonView.getTag()).setChecked(isChecked);				
			}
		});
		
		holder.txt_itemFullName.setText(friendsArrayList.get(position).getUser().getFirstName() + " " + friendsArrayList.get(position).getUser().getLastName());			
		if (friendsArrayList.get(position).isChecked())
		{
			//currCheckBox = (CheckBox) convertView.findViewById(R.id.friend_check_box);
			currCheckBox.setChecked(true);
		}
		else
			currCheckBox.setChecked(false);
		return convertView;
		
		
	}

	// this method is been called any time an event is added or removed from the list 
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
	}	

}
