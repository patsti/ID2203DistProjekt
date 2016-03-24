package riwcm;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class RIWCMDataMessage implements KompicsEvent, Serializable {

	private static final long serialVersionUID = -2590220477468259852L;

	private final TAddress source;
	private final Integer ts, r, wr, val;

	protected RIWCMDataMessage(TAddress source, Integer r, Integer ts, Integer wr, Integer val) {
		this.source = source;
		this.r = r;
		this.ts = ts;
		this.wr = wr;
		this.val = val;
	}

	public Integer getR() {
		return r;
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
}


