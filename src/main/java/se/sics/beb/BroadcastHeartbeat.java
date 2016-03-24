package se.sics.beb;

import java.util.ArrayList;

import Heart.HeartbeatRequestMessage;
import se.sics.kompics.KompicsEvent;
import se.sics.storage.GetOperationRequest;
import se.sics.storage.GetOperationRequestFromClient;
import se.sics.test.TAddress;
import se.sics.test.TMessage;

public class BroadcastHeartbeat implements KompicsEvent{
	
	private HeartbeatRequestMessage getHeartbeatRequest;
	private ArrayList<TAddress> receivers;
	private TAddress source;
	
	public BroadcastHeartbeat(TAddress source, ArrayList<TAddress> receivers, HeartbeatRequestMessage getHeartbeatRequest){
		this.source = source;
		this. receivers = receivers;
		this.getHeartbeatRequest = getHeartbeatRequest;
	}

	public HeartbeatRequestMessage getGetHeartbeatRequest() {
		return getHeartbeatRequest;
	}

	public void setGetOperationRequest(HeartbeatRequestMessage getHeartbeatRequest) {
		this.getHeartbeatRequest = getHeartbeatRequest;
	}

	public ArrayList<TAddress> getReceivers() {
		return receivers;
	}

	public void setReceivers(ArrayList<TAddress> receivers) {
		this.receivers = receivers;
	}

	public TAddress getSource() {
		return source;
	}

	public void setSource(TAddress source) {
		this.source = source;
	}
	
	
	
	
}
