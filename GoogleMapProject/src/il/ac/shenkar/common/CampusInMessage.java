package il.ac.shenkar.common;

import java.util.ArrayList;
import java.util.List;

public class CampusInMessage extends CampusInElement
{
    private String parseId;

    public String getParseId()
    {
	return parseId;
    }

    public void setParseId(String parseId)
    {
	this.parseId = parseId;
    }

    private String content;
    // the parse owner id
    private String ownerId;
    
    private int readInRadius;

    // the owner id, only a global message can be without receiver,
    private List<String> receiverId = new ArrayList<String>();


    public String getContent()
    {
	return content;
    }

    public void setContent(String content)
    {
	this.content = content;
    }

    public CampusInLocation getLocation()
    {
	return location;
    }

    public void setLocation(CampusInLocation location)
    {
	this.location = location;
    }

    public String getOwnerId()
    {
	return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
	this.ownerId = ownerId;
    }

    public List<String> getReceiverId()
    {
        return receiverId;
    }

    public void setReceiverId(List<String> receiverId)
    {
        this.receiverId = receiverId;
    }

    public void setReciversList(ArrayList<CampusInUser> userList)
    {
	if (userList == null)
	    return;
	for (CampusInUser currUser : userList)
	{
	    receiverId.add(currUser.getParseUserId());
	}
    }

	public int getReadInRadius() {
		return readInRadius;
	}

	public void setReadInRadius(int readInRadius) {
		this.readInRadius = readInRadius;
	}

}
