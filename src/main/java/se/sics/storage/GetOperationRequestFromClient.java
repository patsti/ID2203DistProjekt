package se.sics.storage;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;

public class GetOperationRequestFromClient implements KompicsEvent, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
