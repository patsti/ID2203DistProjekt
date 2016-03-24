package Heart;

import java.io.Serializable;
import java.util.ArrayList;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class AliveReplyMessage implements KompicsEvent, Serializable {
	ArrayList<TAddress> alive = new ArrayList();
	public AliveReplyMessage(ArrayList<TAddress> alive){
		this.alive = alive;
	}
	
	public ArrayList<TAddress> getAlive(){
		return alive;
	}
	
}
