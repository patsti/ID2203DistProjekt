package se.sics.client;

import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.Kompics;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;

public class ApplicationClientParent extends ComponentDefinition {
	
	static{
		// conversions
        Conversions.register(new TAddressConverter());
	}
	
	public ApplicationClientParent(){
		
		TAddress self = config().getValue("project.master", TAddress.class);
		Component network = create(NettyNetwork.class, new NettyInit(self));
		Component app = create(ApplicationClient.class, Init.NONE);
		
		
		
		connect(app.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
		
		
		
	}
	
	public static void main(String[] args){
		Kompics.createAndStart(ApplicationClientParent.class, 1);
	}

}
