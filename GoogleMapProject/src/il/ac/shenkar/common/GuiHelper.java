package il.ac.shenkar.common;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class GuiHelper {

	private static int s4Width=1080;
	private static int s4Height=1920;
	public static DisplayMetrics getDisplayMatric(Activity activity) 
	{
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}
	
	public  static Float getHeightDimentionMultFactor(int myScreenHeight)
	{
		return  ((float)myScreenHeight/(float)s4Height);
	}

	public static  Float getWidthDimentionMultFactor(int myScreenWidth)
	{
		return  ((float)myScreenWidth/(float)s4Width);
	}
}
