package Epfd;

import java.io.Serializable;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

public class CheckTimeout extends Timeout implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8763232936706659471L;

	protected CheckTimeout(ScheduleTimeout request) {
		super(request);
	}
		
}
