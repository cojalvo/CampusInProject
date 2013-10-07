package il.ac.shenkar.cadan;

import il.ac.shenkar.common.CampusInEvent;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class EventListBaseAdapter extends BaseAdapter
{
	private static ArrayList<CampusInEvent> eventArrayList;
	private LayoutInflater l_Inflater;
	
	
	public EventListBaseAdapter(Context context,ArrayList<CampusInEvent> arrayList)
	{
		this.eventArrayList = arrayList;
		this.l_Inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount()
	{
		return  eventArrayList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return eventArrayList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Button currButton;
		ViewHolder holder;
		
		if (convertView == null)
		{
			convertView = l_Inflater.inflate(R.layout.event_layout, null);
			holder = new ViewHolder();
			holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.eventDescription);
			holder.txt_itemTitle = (TextView) convertView.findViewById(R.id.eventTitle);
			
			convertView.setTag(holder);
		} else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		/*
		 * Here I get id for the button of this specific view and give him "tag" so i could delete it later on
		 * */
		currButton = (Button) convertView.findViewById(R.id.eventButton);
		holder.txt_itemDescription.setText(eventArrayList.get(position).getDescription());
		holder.txt_itemTitle.setText(eventArrayList.get(position).getHeadLine());
		currButton.setTag(position);
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
		TextView txt_itemDescription;
		TextView txt_itemTitle;
	}
}
