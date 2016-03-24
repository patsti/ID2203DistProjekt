package se.sics.test;


import org.apache.log4j.PropertyConfigurator;

import Heart.Heartbeat;
import se.sics.beb.BEBroadcast;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;


public class NodeHost extends ComponentDefinition {
	
	private TAddress self;
	
//	static {
//		PropertyConfigurator.configureAndWatch("log4j.properties");
//        Conversions.register(new TAddressConverter());
//        Serializers.register(TAddress.class, "netS");
//        Serializers.register(THeader.class, "netS");
//        Serializers.register(TMessage.class, "netS");
//        Serializers.register(new NetSerializer(), "netS");
//	}
	
	static {
		PropertyConfigurator.configureAndWatch("log4j.properties");

        // register
        Serializers.register(new NetSerializer(), "netS");
        Serializers.register(new PingPongSerializer(), "ppS");
        // map
        Serializers.register(TAddress.class, "netS");
        Serializers.register(THeader.class, "netS");
        Serializers.register(TMessage.class, "netS");
        
        // conversions
        Conversions.register(new TAddressConverter());
	}
    
	
	public NodeHost(Init init){
		self = config().getValue("project.self"+init.identifier, TAddress.class);
		int identifier = init.identifier;
		
		Component network = create(NettyNetwork.class, new NettyInit(self));
		Component timer = create(JavaTimer.class, Init.NONE);
		Component nodeParent = create(NodeParent.class, new NodeParent.Init(identifier));

		connect(nodeParent.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
		connect(nodeParent.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);	
	}
	
	public static class Init extends se.sics.kompics.Init<NodeHost>{
	
		private final Integer identifier;
		
		public Init(Integer identfier){
			this.identifier = identfier;
		}
	}
	
}
