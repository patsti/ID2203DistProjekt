package Heart;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class Suspect implements KompicsEvent, Serializable {

	private final TAddress source;

	public Suspect(TAddress source) {
		this.source = source;
	}

	public final TAddress getSource() {
		return source;
	}
}