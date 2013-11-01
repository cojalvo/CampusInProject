package il.ac.shenkar.cadan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class MessageHalper 
{
	 private static ProgressDialog pb=null;
	 public static void showProgressDialog(String message,Context context)
	 {
		 Activity ac;
		 try
		 {
			  ac=(Activity) context;
		 }
		 catch (Exception e)
		 {
			 return;
		 }
		 if(ac!=null && !ac.isFinishing())
		 {
			pb = new ProgressDialog(context);
			pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pb.setMessage(message);
			pb.show();
		 }
	 }
	 public static void closeProggresDialog(){
		 if(pb!=null)
			 pb.cancel();
	 }

}
