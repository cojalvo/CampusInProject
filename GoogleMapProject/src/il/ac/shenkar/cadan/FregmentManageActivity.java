package il.ac.shenkar.cadan;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.maps.MapFragment;

import il.ac.shenkar.cadan.PrefsFragment.OnPreferenceSelectedListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class FregmentManageActivity extends Activity  implements OnPreferenceSelectedListener
{

	BroadcastReceiver reciever;
	FragmentManager fm;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		fm=getFragmentManager();
		fm.beginTransaction().add(R.id.myfragment, new PrefsFragment()).commit();
		
	}
	@Override
	public void onPreferenceSelected(String selected) {
//		if(fm==null) fm=getFragmentManager();
//	
//		EventsFregment mf=new EventsFregment();
//		FragmentTransaction ft=fm.beginTransaction();
//		ft.replace(R.id.myfragment, mf);
//		ft.commit();
	}
	

}
