package il.ac.shenkar.in.dal;

import java.util.List;

import il.ac.shenkar.common.*;

public interface IDataAccesObject extends IProvider
{
	public PersonalSettings getPersonalSettings(String userID);
	public void putPersonalSettings(PersonalSettings ps);
	public List<CampusInEvent> getEvents();
	public List<CampusInMessage> getMessages();
	public void sendMessage(CampusInMessage message);
	public void sendEvent(CampusInEvent event);
	public void updateLocation(CampusInUserLocation userLoction);
	
}
