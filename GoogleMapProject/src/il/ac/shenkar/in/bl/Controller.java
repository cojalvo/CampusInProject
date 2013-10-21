package il.ac.shenkar.in.bl;

import java.util.List;

import android.content.Context;
import il.ac.shenkar.cadan.MessageHalper;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.in.dal.CloudAccessObject;
import il.ac.shenkar.in.dal.DataAccesObjectCallBack;
import il.ac.shenkar.in.dal.IDataAccesObject;
import il.ac.shenkar.in.dal.Model;

/**
 * the Controller Object is a singleton object which responsible to keep the data of the Program consistentive with the DB
 * to get instance of controller you will have to supply with Context 
 * @author Jacob
 *
 */
public class Controller implements ICampusInController
{
	private static Controller instance = null;
	private Context context;
	private IDataAccesObject cloudAccessObject;
	private Model viewModel;
	private CampusInUser currentUser;
	
	private Object toReturn;
	
	private Controller (Context context)
	{
		// private c'tor
		this.context = context;
		cloudAccessObject = CloudAccessObject.getInstance();
		viewModel = new Model();
		//get the current logged in user
		
	
		cloudAccessObject.loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>() {
			
			@Override
			public void done(CampusInUser retObject, Exception e) {
				
				if (e == null && retObject!= null)
				{
					currentUser = retObject;
				}
				
			}
		});
		
	}

	public static Controller getInstance(Context context)
	{
		if(instance == null)
			instance = new Controller(context);
		return instance;
	}
	
	
	@Override
	public boolean addEvent(CampusInEvent toAdd) 
	{
		
		return false;
	}

	@Override
	public List<CampusInUser> getCurrentUserFriendList() 
	{
		MessageHalper.showProgressDialog("gettig "+ currentUser.getFirstName() +" friends", context);
		cloudAccessObject.getCurrentCampusInUserFriends(new DataAccesObjectCallBack<List<CampusInUser>>() {
			
			@Override
			public void done(List<CampusInUser> retObject, Exception e) {
				if(retObject!= null && e == null)
				toReturn = retObject;		
			}
		});
		while (toReturn == null)
		{
			// do nothing 
		}
		MessageHalper.closeProggresDialog();
		return (List<CampusInUser>) toReturn;
	}

	@Override
	public CampusInUser getCurrentUser() {return this.currentUser;}

	
	
}
