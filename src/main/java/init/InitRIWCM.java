package init;

import java.util.ArrayList;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class InitRIWCM implements KompicsEvent {
	private TAddress self;
	private ArrayList<TAddress> replicationGroup;
	
	public InitRIWCM(TAddress self, ArrayList<TAddress> replicationGroup){
		this.self = self;
		this.replicationGroup = replicationGroup;
	}

	public TAddress getSelf() {
		return self;
	}

	public void setSelf(TAddress self) {
		this.self = self;
	}

	public ArrayList<TAddress> getReplicationGroup() {
		return replicationGroup;
	}

	public void setReplicationGroup(ArrayList<TAddress> replicationGroup) {
		this.replicationGroup = replicationGroup;
	}
	
	
	
}
