package se.sics.storage;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class RIWCMGetOperationRequest implements KompicsEvent, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int key;
	private String value;
	
	
	public RIWCMGetOperationRequest(int key, String value){
		this.key = key;
		this.value = value;

	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}