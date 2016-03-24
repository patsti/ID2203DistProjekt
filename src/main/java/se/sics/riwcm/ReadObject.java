package se.sics.riwcm;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;

public class ReadObject implements KompicsEvent, Serializable {
	private final Integer ts, wr, val, nodeId;
	private String stringValue;
	
	public ReadObject(Integer ts, Integer wr, Integer val, Integer nodeId, String stringValue){
		this.ts = ts;
		this.wr = wr;
		this.val = val;
		this.nodeId = nodeId;
		this.stringValue = stringValue;
	}

	public Integer getTs() {
		return ts;
	}

	public Integer getWr() {
		return wr;
	}

	public Integer getVal() {
		return val;
	}
	public Integer getNodeId() {
		return nodeId;
	}
	
	public String stringValue(){
		return stringValue;
	}
}


