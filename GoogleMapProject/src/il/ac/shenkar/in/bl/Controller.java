package il.ac.shenkar.in.bl;

import java.util.List;
import java.util.Stack;

import android.content.Context;
import il.ac.shenkar.cadan.MessageHalper;
import il.ac.shenkar.cadan.ViewModel;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInMessage;
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
	private Boolean viewModelIsUpdating=false;
	private Context context;
	private IDataAccesObject cloudAccessObject;
	private ViewModel viewModel;
	private CampusInUser currentUser;
	
	private Object toReturn;
	
	private Controller (Context context)
	{
		// private c'tor
		this.context = context;
		cloudAccessObject = CloudAccessObject.getInstance();
		viewModel = new ViewModel(context);
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
	public void getCurrentUserFriendList(final ControllerCallback<List<CampusInUser>> callBack) 
	{
		MessageHalper.showProgressDialog("gettig "+ currentUser.getFirstName() +" friends", context);
		cloudAccessObject.getCurrentCampusInUserFriends(new DataAccesObjectCallBack<List<CampusInUser>>() {
			
			@Override
			public void done(List<CampusInUser> retObject, Exception e) {
				if(retObject!= null && e == null)
					callBack.done(retObject, null);
				else 
					callBack.done(retObject, e);
				
			}
		});
		
	}

	@Override
	public void getCurrentUser(final ControllerCallback<CampusInUser> callBack) 
	{
		if (currentUser != null)
			callBack.done(currentUser, null);
		else 
		{
			cloudAccessObject.loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>() {
				
				@Override
				public void done(CampusInUser retObject, Exception e) {
					if (retObject != null && e == null)
					{
						//load is a success -> return the recived Object 
						callBack.done(retObject, null);
					}
					else 
					{
						// en error occered -> retutn the Exeptiom
						callBack.done(null, e);
					}
					
				}
			});
		}
	}

	@Override
	public void addEvent(CampusInEvent toAdd,
			ControllerCallback<Integer> callBack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(CampusInMessage message,
			ControllerCallback<Integer> callBack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateViewModel() {
		// TODO Auto-generated method stub
		
	}

	
	
}
