package ports;

import se.sics.kompics.PortType;
import se.sics.storage.GetOperationReply;
import se.sics.storage.GetOperationRequest;
import se.sics.storage.PutOperationRequest;
import se.sics.storage.RIWCMGetOperationRequest;


public class RIWCMport extends PortType {
	{
		request(GetOperationRequest.class);
		request(PutOperationRequest.class);
		
		request(RIWCMGetOperationRequest.class);

		indication(GetOperationReply.class);
	}
}
