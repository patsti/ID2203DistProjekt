package riwm;

import se.sics.kompics.KompicsEvent;
import se.sics.storage.PutOperationRequest;

public class TsValue implements KompicsEvent {
	
	private int timestamp;
	private PutOperationRequest value;
	
	public TsValue(int timestamp, PutOperationRequest value){
		this.timestamp = timestamp;
		this.value = value;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public PutOperationRequest getValue() {
		return value;
	}

	public void setValue(PutOperationRequest value) {
		this.value = value;
	}
	
	
	
	
	
}
