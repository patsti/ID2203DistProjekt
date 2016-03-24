package Heart;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class HeartbeatRequestMessage implements KompicsEvent, Serializable {
	int id;
	public HeartbeatRequestMessage(int id){
		this.id = id;
	}
	public int getID(){
		return id;
	}
}
