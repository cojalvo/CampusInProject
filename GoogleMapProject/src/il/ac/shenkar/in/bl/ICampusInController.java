package il.ac.shenkar.in.bl;

import java.util.List;

import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInUser;

public interface ICampusInController
{
	public boolean addEvent(CampusInEvent toAdd);
	public List<CampusInUser> getCurrentUserFriendList();
	public CampusInUser getCurrentUser();
}
