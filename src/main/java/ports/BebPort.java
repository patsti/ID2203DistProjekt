package ports;

import se.sics.beb.BroadcastGet;
import se.sics.beb.BroadcastHeartbeat;
import se.sics.beb.BroadcastPut;
import se.sics.kompics.PortType;

public class BebPort extends PortType{
	{
		request(BroadcastGet.class);
		request(BroadcastPut.class);
		request(BroadcastHeartbeat.class);
	}	
}
