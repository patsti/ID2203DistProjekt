package init;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class InitStorage implements KompicsEvent {
	private int id;
	private int min;
	private int max;
	private TAddress self;
	
	public InitStorage(int id, int min, int max, TAddress self){
		this.id = id;
		this.min = min;
		this.max = max;
		this.self = self;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public TAddress getSelf() {
		return self;
	}

	public void setSelf(TAddress self) {
		this.self = self;
	}
	
	
	
	
}
