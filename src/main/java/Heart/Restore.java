
package Heart;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class Restore implements KompicsEvent, Serializable {

	private final TAddress source;

	public Restore(TAddress source) {
		this.source = source;
	}

	public final TAddress getSource() {
		return source;
	}
}