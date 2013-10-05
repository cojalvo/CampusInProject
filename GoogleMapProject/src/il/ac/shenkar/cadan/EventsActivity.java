package il.ac.shenkar.cadan;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import il.ac.shenkar.in.dal.Model;
import android.app.ListActivity;
import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class EventsActivity extends ListActivity {
	private ListView listView;
	private boolean selectMode = false;
	private ItemAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE",
				"UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
		ParseFacebookUtils.initialize("635010643194002");
		setContentView(R.layout.events);
		Model.LoadModel();
		adapter = new ItemAdapter(this, R.layout.row, Model.getIds());
		setListAdapter(adapter);
		ListView lv = getListView();
		// lv.setSelector(R.drawable.my_list_selector);
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adap, View v,
					int position, long id) {
				getListView().getItemAtPosition(position);
				Toast.makeText(EventsActivity.this,
						getListView().getItemAtPosition(position).toString(),
						Toast.LENGTH_LONG).show();
				if (!selectMode) {
					if (adapter.isSelected(position)) {
						v.setBackgroundColor(Color.TRANSPARENT);
					} else {
						v.setBackgroundColor(Color.CYAN);
					}
					adapter.UpdateColoredList(position);
				}
				return true;
			}
		});
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// return inflater.inflate(R.layout.events, container, false);
	// }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(this,
				getListView().getItemAtPosition(position).toString(),
				Toast.LENGTH_LONG).show();
		if (selectMode) {
			if (adapter.isSelected(position)) {
				v.setBackgroundColor(Color.WHITE);
			} else {
				v.setBackgroundColor(Color.CYAN);
			}
			adapter.UpdateColoredList(position);

		}

	}

	public void createEvent(View v) 
	{
		

	}

	public void deleteEvent(View v) {

	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		if (selectMode) {
			selectMode = false;
			resetList();
		} else
			super.onBackPressed();
	}

	private void resetList() 
	{	
		adapter = new ItemAdapter(this, R.layout.row, Model.getIds());
		setListAdapter(adapter);
		adapter.clearSelected();
	}
}