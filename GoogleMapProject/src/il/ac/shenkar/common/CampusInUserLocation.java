package il.ac.shenkar.common;

public class CampusInUserLocation
{
  private CampusInLocation location;
  private CampusInUser user;

  public CampusInUserLocation()
  {
  }

  public CampusInUserLocation(CampusInLocation paramCampusInLocation, CampusInUser paramCampusInUser)
  {
    this.location = paramCampusInLocation;
    this.user = paramCampusInUser;
  }

  public CampusInLocation getLocation()
  {
    return this.location;
  }

  public CampusInUser getUser()
  {
    return this.user;
  }

  public void setLocation(CampusInLocation paramCampusInLocation)
  {
    this.location = paramCampusInLocation;
  }

  public void setUser(CampusInUser paramCampusInUser)
  {
    this.user = paramCampusInUser;
  }
}
