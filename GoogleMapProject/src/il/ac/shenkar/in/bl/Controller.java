package il.ac.shenkar.in.bl;

import android.content.Context;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.in.dal.CloudAccessObject;
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
	private CloudAccessObject cloudAccessObject;
	private Model viewModel;
	
	private Controller (Context context)
	{
		// private c'tor
		this.context = context;
		cloudAccessObject = CloudAccessObject.getInstance();
		viewModel = new Model();
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

	
	
}
