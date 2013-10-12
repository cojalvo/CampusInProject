package il.ac.shenkar.in.dal;

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class FacebookServices
{
	private static final String TAG = "cadan";

	public static List<GraphUser> getFriendsList()
	{
		final List<GraphUser> returnList=new LinkedList<GraphUser>();
		Session session=ParseFacebookUtils.getSession();
		if(session.isOpened())
		{
			Request friendRequest=Request.newMyFriendsRequest(session, new GraphUserListCallback() {
				
				@Override
				public void onCompleted(List<GraphUser> users, Response response) 
				{
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
	public static Drawable getPictureForFacebookId(String facebookId) {

	    Drawable picture = null;
	    InputStream inputStream = null;

	    try {
	        inputStream = new URL("https://graph.facebook.com/" + facebookId + "/picture?type=small").openStream();
	    } catch (Exception e) {        
	     e.printStackTrace();
	     return null;

	    }
	    picture = Drawable.createFromStream(inputStream, "facebook-pictures");
	    
	    
	    return picture;
	}
	public Bitmap getCurentUserPic()
	{
		Bitmap pic=null;
//
		return pic;
	}

	/*
	 * Get me from facebook
	 */
		public static void makeMeRequest(final Session session,Request.GraphUserCallback callBack) {
		    Request request = Request.newMeRequest(session, 
		    		callBack );
		    request.executeAsync();
	}

}
