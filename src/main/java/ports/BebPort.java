package ports;

import se.sics.beb.BroadcastGet;
import se.sics.beb.BroadcastReadRiwcm;
import se.sics.beb.BroadcastWriteRiwcm;
import se.sics.beb.BroadcastHeartbeat;
import se.sics.beb.BroadcastPut;
import se.sics.kompics.PortType;

public class BebPort extends PortType{
	{
		request(BroadcastGet.class);
		request(BroadcastPut.class);
		request(BroadcastHeartbeat.class);
		
		
		request(BroadcastReadRiwcm.class);
		request(BroadcastWriteRiwcm.class);
		indication(BroadcastWriteRiwcm.class);
		indication(BroadcastReadRiwcm.class);
	
//		request(BroadcastPutRiwcm.class);
	}	
}
