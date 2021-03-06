package il.ac.shenkar.adapters;

import il.ac.shenkar.cadan.DiaplayEventListFragment;
import il.ac.shenkar.cadan.R;
import il.ac.shenkar.cadan.R.id;
import il.ac.shenkar.cadan.R.layout;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ICampusInController;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class EventListBaseAdapter extends BaseAdapter implements Filterable
{
    private static ArrayList<CampusInEvent> eventArrayList;
    private static ArrayList<CampusInEvent> filteredEventArrayList;
    private LayoutInflater l_Inflater;
    private DiaplayEventListFragment dialog;
    private ICampusInController controller;

    public EventListBaseAdapter(Context context, ArrayList<CampusInEvent> arrayList, DiaplayEventListFragment dialog)
    {
	this.eventArrayList = arrayList;
	this.filteredEventArrayList = arrayList;
	this.l_Inflater = LayoutInflater.from(context);
	this.dialog = dialog;
    }

    @Override
    public int getCount()
    {
	return filteredEventArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
	return filteredEventArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
    controller=Controller.getInstance(null);
	ImageView naviButton;
	ViewHolder holder;

	if (convertView == null)
	{
	    convertView = l_Inflater.inflate(R.layout.event_layout, null);
	    holder = new ViewHolder();
	    holder.txt_itemDescription = (TextView) convertView.findViewById(R.id.eventDescription);
	    holder.txt_itemTitle = (TextView) convertView.findViewById(R.id.eventTitle);
	    holder.txt_distance = (TextView) convertView.findViewById(R.id.event_distance);

	    convertView.setTag(holder);
	}
	else
	{
	    holder = (ViewHolder) convertView.getTag();
	}
	naviButton = (ImageView) convertView.findViewById(R.id.event_navigation_button);
	naviButton.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		// navigate to the the event;
		Integer position = (Integer) v.getTag();
		CampusInEvent navigateTo = filteredEventArrayList.get(position);
		dialog.dismiss();
		Controller controller = Controller.getInstance(null);
		controller.closePreferanceView();
		controller.navigateTo(navigateTo.getParseId());

	    }
	});
	holder.txt_itemDescription.setText(filteredEventArrayList.get(position).getDescription());
	holder.txt_itemTitle.setText(filteredEventArrayList.get(position).getHeadLine());
	holder.txt_distance.setText(getDistanceString(filteredEventArrayList.get(position).getParseId()));
	naviButton.setTag(position);
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
	TextView txt_itemDescription;
	TextView txt_itemTitle;
	TextView txt_distance;
    }

    @Override
    public Filter getFilter()
    {
	return new Filter()
	{

	    @Override
	    protected void publishResults(CharSequence constraint, FilterResults results)
	    {

		filteredEventArrayList = (ArrayList<CampusInEvent>) results.values;
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
		    results.values = eventArrayList;
		    results.count = eventArrayList.size();
		}
		else
		{
		    /* filtering the list */
		    ArrayList<CampusInEvent> filteredData = new ArrayList<CampusInEvent>();
		    String[] titleWords;
		    String[] descWords;
		    for (CampusInEvent curr : eventArrayList)
		    {
			// compare the Char Sequence i received to the event
			// title
			titleWords = curr.getHeadLine().split(" ");
			for (int i = 0; i < titleWords.length; i++)
			{
			    if (titleWords[i].startsWith((String) constraint))
			    {
				filteredData.add(curr);
			    }
			}
			if (!filteredData.contains(curr))
			{
			    // compare the description words
			    descWords = curr.getDescription().split(" ");
			    for (int i = 0; i < descWords.length; i++)
			    {
				if (descWords[i].startsWith((String) constraint))
				{
				    filteredData.add(curr);
				}
			    }
			}
		    }
		    results.values = filteredData;
		    results.count = filteredData.size();
		}
		return results;
	    }
	};
    }
    private String getDistanceString(String eventId)
    {
    	float dist = controller.getMyDistanceFrom(eventId);
    	if (dist > 0)
    	{
    	    String unit;
    	    String finalDist;
    	    if (dist > 1000)
    	    {
    		unit = "ק'מ";

    		finalDist = String.format("%.2f", dist / 1000);
    	    }
    	    else
    	    {
    		unit = "מטרים";
    		finalDist = String.format("%.0f", dist);
    	    }

    	    return ("האירוע נמצא: " + finalDist + " " + unit + " " + "ממני");
    	}
    	return "אתה נמצא באירוע";
    }
}
