package ports;

import init.InitStorage;
import se.sics.kompics.PortType;

public class StoragePort extends PortType {
	{
		request(InitStorage.class);
		indication(InitStorage.class);
	}
}
