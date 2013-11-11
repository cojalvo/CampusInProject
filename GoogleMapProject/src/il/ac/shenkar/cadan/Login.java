package il.ac.shenkar.cadan;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.dal.IDataAccesObject;

public class Login extends Activity {

	private ProgressDialog pb = null;

	// the settings dialogs
	private Dialog stepOneDialog = null;
	private Dialog stepTwoDialog = null;
	private Dialog stepThreeDialog = null;

	// Controllers inside the settings dialog
	private Spinner trendSpinner = null;
	private Spinner yearSpinner = null;
	private RadioButton displayFriendOnlyRB = null;
	private RadioButton displayAllRB = null;
	private Button next1 = null;
	private Button next2 = null;
	private Button finish = null;

	IDataAccesObject dao=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "3kRz2kNhNu5XxVs3mI4o3LfT1ySuQDhKM4I6EblE",
				"UmGc3flrvIervInFbzoqGxVKapErnd9PKnXy4uMC");
		ParseFacebookUtils.initialize("635010643194002");
		setContentView(R.layout.activity_main);
		dao = CloudAccessObject.getInstance();
		// if the user is already log in than go the main activity
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			startActivity(new Intent(Login.this, LoadingActivity.class));
			finish();
		}

	}

	public void loginButtonClick(View v) {
		// after login the face book activity will call and the user will have
		// to login
		// after the login we return to the onActivityResult event and when the
		// finishAuthentication will invoke
		// we will return here
		MessageHalper.showProgressDialog("Login...", Login.this);
		ParseFacebookUtils.logIn(Login.this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					MessageHalper.closeProggresDialog();
					Log.d("MyApp",
							"Uh oh. The user cancelled the Facebook login.");
					finish();
				} else {
					Log.d("MyApp", "User logged in through Facebook!");
					MessageHalper.closeProggresDialog();
					Toast t = Toast.makeText(Login.this,
							"User  was logged in through Facebook!",
							Toast.LENGTH_SHORT);
					t.show();
					Login.this.startSettingsProcess();
				}
			}
		});
	}


	private void initSettingDialogs() {
		stepOneDialog = new Dialog(
				this,
				android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
		stepOneDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		stepOneDialog.setContentView(R.layout.settings_dialog_step1);
		stepTwoDialog = new Dialog(this,
				android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		stepTwoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		stepTwoDialog.setContentView(R.layout.settings_dialog_step2);
		stepThreeDialog = new Dialog(
				this,
				android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
		stepThreeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		stepThreeDialog.setContentView(R.layout.settings_dialog_step3);
	}

	private void initSettingDialogsButtons() {
		next1 = (Button) stepOneDialog.findViewById(R.id.btnNext1);
		next2 = (Button) stepTwoDialog.findViewById(R.id.btnNext2);
		finish = (Button) stepThreeDialog.findViewById(R.id.btnFinish);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.btnNext1) {
					stepTwoDialog.show();
				}
				if (v.getId() == R.id.btnNext2) {
					stepOneDialog.hide();
					stepTwoDialog.hide();
					updateCurrentUserYearTrend();
				}
				if (v.getId() == R.id.btnFinish) {
					stepThreeDialog.hide();
					stepOneDialog.hide();
					updateCurrentUserYearTrend();
				}

			}
		};
		next1.setOnClickListener(listener);
		next2.setOnClickListener(listener);
		finish.setOnClickListener(listener);
	}

	private void initSettingsContentController() {
		trendSpinner = (Spinner) stepOneDialog.findViewById(R.id.spinner1);
		yearSpinner = (Spinner) stepTwoDialog.findViewById(R.id.spinner2);
		displayFriendOnlyRB = (RadioButton) stepThreeDialog
				.findViewById(R.id.displayMyFriendRB);
		displayAllRB = (RadioButton) stepThreeDialog
				.findViewById(R.id.displayAllRB);

	}

	private void startSettingsProcess() {
		initSettingDialogs();
		initSettingDialogsButtons();
		initSettingsContentController();
		stepOneDialog.show();
	}

	private void startNewActivity(Context context, Class activityClass) {
		Intent intent = new Intent(context, activityClass);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void terminateApp()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setPositiveButton("אירעה שגיאה", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		});
		alertDialog.setMessage("נראה שהיתה בעיית חיבור לשרת, אנא נסה מאוחר יותר");
		alertDialog.setTitle(" ");
		alertDialog.setIcon(R.drawable.campus_in_ico);
		alertDialog.show();
	}
	 private void updateCurrentUserYearTrend()
	  {
		 MessageHalper.showProgressDialog("שומר...", Login.this);
	    this.dao.loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>()
	    {
	      public void done(CampusInUser currentCampusUser, Exception e)
	      {
	        if (e == null && currentCampusUser!=null)
	        {
	          currentCampusUser.setTrend(String.valueOf(Login.this.trendSpinner.getSelectedItem()));
	          currentCampusUser.setYear(String.valueOf(Login.this.yearSpinner.getSelectedItem()));
	          Login.this.dao.putCurrentCampusInUserInbackground(currentCampusUser, new DataAccesObjectCallBack<Integer>()
	          {
	            public void done(Integer retObj, Exception e)
	            {
	    			stepOneDialog.dismiss();
	            	stepTwoDialog.dismiss();
	            	stepThreeDialog.dismiss();
	              MessageHalper.closeProggresDialog();
	              if (e == null)
	              {
	                Login.this.startActivity(new Intent(Login.this, Main.class));
	                finish();
	                return;
	              }
	              Login.this.terminateApp();
	            }
	          });
	        }
	        else
	        {
	        	 Login.this.terminateApp();
	        }
	      }
	    });
	  }

		@Override
		protected void onPause() {
			super.onPause();	            
		}

}
