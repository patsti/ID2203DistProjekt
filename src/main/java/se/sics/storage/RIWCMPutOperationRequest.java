package se.sics.storage;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class RIWCMPutOperationRequest implements KompicsEvent, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int key;
	private String value;
	private TAddress source;
	
	
	public RIWCMPutOperationRequest(int key, String value, TAddress source){
		this.key = key;
		this.value = value;
		this.source = source;

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
	
	public TAddress getSource(){
		return source;
	}
	
}
