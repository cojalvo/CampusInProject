package il.ac.shenkar.in.dal;

import il.ac.shenkar.cadan.PrefsFragment;
import il.ac.shenkar.common.KeyValue;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;
import android.os.AsyncTask;
import android.preference.Preference;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class FacebookServices {

	private static final String TAG = "cadan";
	private static InputStream inputStream = null;
	private static Drawable picture = null;


	public static List<GraphUser> getFriendsList() {
		final List<GraphUser> returnList = new LinkedList<GraphUser>();
		Session session = ParseFacebookUtils.getSession();
		if (session.isOpened()) {
			Request friendRequest = Request.newMyFriendsRequest(session,
					new GraphUserListCallback() {

						@Override
						public void onCompleted(List<GraphUser> users,
								Response response) {
							for (GraphUser graphUser : users) {
								returnList.add(graphUser);
							}
						}
					});
			friendRequest.executeAsync();
		}
		return returnList;
	}

	/**
	 * Function loads the users facebook profile pic
	 * 
	 * @param userID
	 */
	public synchronized static void getPictureForFacebookId(final String userId,final String pictureTypeString,
			final DataAccesObjectCallBack<Drawable> callBack) {
		new AsyncTask<String, Integer, String>() {

			@Override
			protected String doInBackground(String... params) {
				 try
			        {
			          FacebookServices.inputStream = new URL("https://graph.facebook.com/" + userId + "/picture?"+pictureTypeString).openStream();
			          FacebookServices.picture = Drawable.createFromStream(FacebookServices.inputStream, "facebook-pictures");
			          return null;
			        }
			        catch (Exception localException)
			        {
			          localException.printStackTrace();
			        }
			        return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if(callBack!=null)
					callBack.done(picture, null);
				picture=null;
			}

		}.execute();
	}

	/*
	 * Get me from facebook
	 */
	public static void makeMeRequest(final Session session,
			Request.GraphUserCallback callBack) {
		Request request = Request.newMeRequest(session, callBack);
		request.executeAsync();
	}

}
