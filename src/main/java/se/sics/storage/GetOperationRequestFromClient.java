package se.sics.storage;

import se.sics.kompics.KompicsEvent;

public class GetOperationRequestFromClient implements KompicsEvent {
	
	private int key;
	
	public GetOperationRequestFromClient(int key){
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	};
	
	
}
