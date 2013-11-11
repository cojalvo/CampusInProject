package il.ac.shenkar.in.dal;

public interface DataAccesObjectCallBack<T>
{
    public void done(T retObject, Exception e);
}
