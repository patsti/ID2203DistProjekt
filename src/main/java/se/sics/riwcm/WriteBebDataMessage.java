package se.sics.riwcm;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

import java.io.Serializable;
import java.util.ArrayList;

public class WriteBebDataMessage implements KompicsEvent, Serializable {

	private static final long serialVersionUID = 2552875477046418121L;
	
	private final TAddress source;
	private final Integer wr, val, ts, rid;
	private final String stringValue;
	private final ArrayList<TAddress> receivers;
	
	public WriteBebDataMessage(TAddress source, ArrayList<TAddress> receivers, String stringValue, Integer rid, Integer ts, Integer wr, Integer val) {
		this.source = source;
		this.stringValue = stringValue;
		this.rid = rid;
		this.ts = ts;
		this.wr = wr;
		this.val = val;
		this.receivers = receivers;
	}

	public Integer getR() {
		return rid;
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
	
	public TAddress getSource(){
		return source;
	}
	
	public String getStringValue(){
		return stringValue;
	}
	
	public ArrayList<TAddress> getReceivers(){
		return receivers;
	}
}
