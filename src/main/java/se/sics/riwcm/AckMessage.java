package se.sics.riwcm;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class AckMessage implements KompicsEvent, Serializable {
	private static final long serialVersionUID = 2129744840108864619L;

	private final Integer r;
	
	public AckMessage(TAddress source, Integer r) {
		this.r = r;
	}

	public Integer getR() {
		return r;
	}
}
