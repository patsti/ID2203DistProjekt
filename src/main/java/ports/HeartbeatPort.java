package ports;

import Heart.HeartbeatInitMessage;
import init.InitHeartbeat;
import se.sics.kompics.PortType;

public class HeartbeatPort extends PortType {
	{
		request(InitHeartbeat.class);
		indication(InitHeartbeat.class);
		request(HeartbeatInitMessage.class);
		indication(HeartbeatInitMessage.class);
	}
}
