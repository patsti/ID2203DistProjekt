package riwcm;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;

public class ReadObject implements KompicsEvent, Serializable {
	private final Integer ts, wr, val, nodeId;
	
	public ReadObject(Integer ts, Integer wr, Integer val, Integer nodeId){
		this.ts = ts;
		this.wr = wr;
		this.val = val;
		this.nodeId = nodeId;
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
}


