package ports;

import se.sics.beb.BroadcastGet;
import se.sics.beb.BroadcastHeartbeat;
import se.sics.kompics.PortType;
import se.sics.storage.GetOperationRequest;

public class BebPort extends PortType{
	{
		request(BroadcastGet.class);
		request(BroadcastHeartbeat.class);
	}	
}
