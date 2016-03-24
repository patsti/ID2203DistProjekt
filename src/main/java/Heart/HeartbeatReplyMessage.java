package Heart;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class HeartbeatReplyMessage implements KompicsEvent, Serializable {
	String check;
	int id;
	public HeartbeatReplyMessage(String check, int id){
		this.check = check;
		this.id = id;
	}
	
	public String getCheck(){
		return check;
	}
	
	public int getID(){
		return id;
	}
}
