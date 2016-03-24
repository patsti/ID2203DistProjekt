package se.sics.beb;

import java.util.ArrayList;

import se.sics.kompics.KompicsEvent;
import se.sics.storage.GetOperationRequest;
import se.sics.storage.GetOperationRequestFromClient;
import se.sics.test.TAddress;
import se.sics.test.TMessage;

public class BroadcastGet implements KompicsEvent{
	
	private GetOperationRequest getOperationRequest;
	private ArrayList<TAddress> receivers;
	private TAddress source;
	
	public BroadcastGet(TAddress source, ArrayList<TAddress> receivers, GetOperationRequest getOperationRequest){
		this.source = source;
		this. receivers = receivers;
		this.getOperationRequest = getOperationRequest;
	}

	public GetOperationRequest getGetOperationRequest() {
		return getOperationRequest;
	}

	public void setGetOperationRequest(GetOperationRequest getOperationRequest) {
		this.getOperationRequest = getOperationRequest;
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
