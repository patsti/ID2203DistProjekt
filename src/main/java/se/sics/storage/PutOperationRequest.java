package se.sics.storage;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;

public class PutOperationRequest implements KompicsEvent, Serializable {
	private int key;
	private String value;
	
	public PutOperationRequest(int key, String value){
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
