package il.ac.shenkar.in.bl;

import java.util.ArrayList;
import java.util.GregorianCalendar;
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
	
	/**
	 * this List will hold all of the Events we want to save to the cloud
	 * only if the save is successful the event object will be thrown from the list 
	 */
	private List<CampusInEvent> saveToCLoudEventQue;
	
	private Controller (Context context)
	{
		// private c'tor
		this.context = context;
		cloudAccessObject = CloudAccessObject.getInstance();
		viewModel = new ViewModel(context);
		saveToCLoudEventQue = new ArrayList<CampusInEvent>();
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
	public void getCurrentUserAllEvents(
			ControllerCallback<List<CampusInEvent>> callBack) {
		
		//in the mean tiime just a dummy implementation to display some data in the list 
		//TODO: yaki - later on to bring the real list of events from the relevant object 
		ArrayList<CampusInEvent> toReturn = new ArrayList<CampusInEvent>();
		CampusInEvent currEvent;
		
				
		for (int i=0; i<40; i++)
		{
			currEvent = new CampusInEvent();
			currEvent.setDate(new GregorianCalendar());
			currEvent.setDescription("description " + i);
			currEvent.setHeadLine("Title "+i);
			toReturn.add(currEvent);	
		}
		
		callBack.done(toReturn, null);
		
	}

	@Override
	public void sendMessage(CampusInMessage message,
			ControllerCallback<Integer> callBack) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void saveEvent(CampusInEvent toAdd, final ControllerCallback<Integer> callBack) 
	{
		//TODO: to draw the event to the user interface before saving it to the cloud
		if (toAdd!= null)
		{
			// add the toAdd Object to the EventQue;
			saveToCLoudEventQue.add(toAdd);
			for (final CampusInEvent event: saveToCLoudEventQue)
			{
				cloudAccessObject.sendEvent(event, new DataAccesObjectCallBack<String>() {
					
					@Override
					public void done(String retObject, Exception e) 
					{
						if (retObject != null)
						{
							saveToCLoudEventQue.remove(event);
							//viewModel
							callBack.done(0, null);
						}
						else
							callBack.done(null, e);
					}
				});
			}
		}
		else 
		{
			callBack.done(0, new NullPointerException("the Event you tried to save is null"));
		}
	}

	@Override
	public void updateViewModel(final ControllerCallback<Integer> callBack) 
	{
		viewModel.updateViewModelInBackground(new DataAccesObjectCallBack<Integer>() {
			
			@Override
			public void done(Integer retObject, Exception e) 
			{
				callBack.done(retObject, e);				
			}
		});
		
	}

	
	
}