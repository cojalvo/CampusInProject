package il.ac.shenkar.in.bl;

public interface ControllerCallback<T> {
	public void done(T retObject, Exception e);
}
