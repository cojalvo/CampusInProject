package il.ac.shenkar.cadan;

import il.ac.shenkar.in.bl.Controller;
import il.ac.shenkar.in.bl.ControllerCallback;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoadingActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	Parse.initialize(this, "3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE", "UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
	ParseFacebookUtils.initialize("635010643194002");
	this.setContentView(R.layout.loading_page);
	Controller controller = Controller.getInstance(this);
	MessageHalper.showProgressDialog("Loading..", this);
	controller.updateViewModel(new ControllerCallback<Integer>()
	{

	    @Override
	    public void done(Integer retObject, Exception e)
	    {
		MessageHalper.closeProggresDialog();
		startActivity(new Intent(LoadingActivity.this, Main.class));
		finish();
	    }
	});
    }

}
