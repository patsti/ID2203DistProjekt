package se.sics.storage;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class PutOperationRequestFromClient implements KompicsEvent, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int key;
	private String value;
	
	public PutOperationRequestFromClient(int key, String value){
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
