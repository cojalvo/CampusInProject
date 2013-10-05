package il.ac.shenkar.in.dal;

public interface IProvider
{
	public void addObserver(IObserver ob);
	public void removeObserver(IObserver ob);

}
