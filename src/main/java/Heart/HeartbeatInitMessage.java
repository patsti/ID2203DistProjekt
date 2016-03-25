package Heart;

import java.io.Serializable;
import java.util.ArrayList;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class HeartbeatInitMessage implements KompicsEvent {
	int id;
	ArrayList<TAddress> group = new ArrayList<TAddress>();
	public HeartbeatInitMessage(int id, ArrayList<TAddress> al){
		this.id = id;
		this.group = al;
	}
	
	public int getID(){
		return id;
	}
	
	public ArrayList<TAddress> getGroup(){
		return group;
	}
}