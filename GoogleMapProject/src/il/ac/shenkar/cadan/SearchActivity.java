package il.ac.shenkar.cadan;



import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SearchActivity extends Activity 
{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		// Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    	String query = intent.getStringExtra(SearchManager.QUERY);
	    	Toast.makeText(SearchActivity.this, 
	    			query, 
	    			Toast.LENGTH_LONG).show();
	    	
	    }
	}

	private void doMySearch(String query) {
		
	}

}
