package se.sics.beb;

import java.util.ArrayList;

import se.sics.kompics.KompicsEvent;

import se.sics.storage.PutOperationRequest;
import se.sics.test.TAddress;

public class BroadcastPut implements KompicsEvent{
	
	private PutOperationRequest putOperationRequest;
	private ArrayList<TAddress> receivers;
	private TAddress source;
	
	public BroadcastPut(TAddress source, ArrayList<TAddress> receivers, PutOperationRequest putOperationRequest){
		this.source = source;
		this. receivers = receivers;
		this.putOperationRequest = putOperationRequest;
	}

	public PutOperationRequest getPutOperationRequest() {
		return putOperationRequest;
	}

	public void setPutOperationRequest(PutOperationRequest putOperationRequest) {
		this.putOperationRequest = putOperationRequest;
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