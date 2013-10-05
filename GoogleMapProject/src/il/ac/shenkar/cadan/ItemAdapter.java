package il.ac.shenkar.cadan;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import il.ac.shenkar.in.dal.Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<String> {

	private final Context context;
	private final String[] Ids;
	private final int rowResourceId;
	private ArrayList<Integer> coloredItems = new ArrayList<Integer>();

	public ItemAdapter(Context context, int textViewResourceId, String[] objects) {

		super(context, textViewResourceId, objects);

		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(rowResourceId, parent, false);
		ImageView icon = (ImageView) rowView.findViewById(R.id.rowImageView);
		TextView header = (TextView) rowView.findViewById(R.id.rowHeader);
		TextView description = (TextView) rowView
				.findViewById(R.id.rowDescription);

		int id = Integer.parseInt(Ids[position]);
		int iconId = Model.GetbyId(id).getIconFile();
		description.setText(Model.GetbyId(id).getDescription());
		header.setText(Model.GetbyId(id).getHeader());
		icon.setImageAlpha(iconId);
		if (coloredItems.contains(position)) {
			//rowView.setBackgroundColor(Color.CYAN);
		} else {
			//rowView.setBackgroundColor(Color.WHITE); // or whatever was original
		}
		return rowView;

	}
	public void clearSelected()
	{
		coloredItems.clear();
	}
	public boolean hasSelected()
	{
		return coloredItems.size()>0;
	}
	public boolean isSelected(Integer position)
	{
		return coloredItems.contains(position);
	}
	public void UpdateColoredList(Integer position) {
		if(coloredItems.contains(position))
			coloredItems.remove(position);
		else coloredItems.add(position);
	}

}