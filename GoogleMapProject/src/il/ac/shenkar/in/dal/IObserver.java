package il.ac.shenkar.in.dal;

import il.ac.shenkar.in.dal.ActionCode;

public interface IObserver {
	public void actionDone(ActionCode settings);
	public void actionFail(ActionCode settings);
}
