package se.sics.storage;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;


public class GetOperationRequest implements KompicsEvent, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int key;
	
	
	public GetOperationRequest(int key){
		this.key = key;

	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
}
