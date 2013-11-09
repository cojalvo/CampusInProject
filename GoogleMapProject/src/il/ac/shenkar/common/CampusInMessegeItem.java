package il.ac.shenkar.common;

public class CampusInMessegeItem extends CampusInMessage
{
    private boolean canISeeTheMessage;

    
    public CampusInMessegeItem(boolean canISeeTheMessage)
    {
	super();
	this.canISeeTheMessage = canISeeTheMessage;
    }
    

    public CampusInMessegeItem()
    {
	super();
    }

    public boolean isCanISeeTheMessage()
    {
        return canISeeTheMessage;
    }

    public void setCanISeeTheMessage(boolean canISeeTheMessage)
    {
        this.canISeeTheMessage = canISeeTheMessage;
    }
}
