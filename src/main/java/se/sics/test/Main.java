package se.sics.test;

import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Kompics;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.netty.serialization.Serializers;

public class Main extends ComponentDefinition {

    static {
        // register
        Serializers.register(new NetSerializer(), "netS");
        Serializers.register(new PingPongSerializer(), "ppS");
        // map
        Serializers.register(TAddress.class, "netS");
        Serializers.register(THeader.class, "netS");
        Serializers.register(TMessage.class, "netS");
        Serializers.register(Ping.class, "ppS");
        Serializers.register(Pong.class, "ppS");

        // conversions
        Conversions.register(new TAddressConverter());
    }
    

    
    public static void main(String[] args){
     
    	Kompics.createAndStart(Main.class);	 
    	System.out.println("Starting Node");
    }
    
    public Main(){

//    	Component node1 = create(NodeParent.class, new NodeParent.Init(20));
//    	Component node2 = create(NodeParent.class, new NodeParent.Init(40));
//    	Component node3 = create(NodeParent.class, new NodeParent.Init(60));
//    	Component node4 = create(NodeParent.class, new NodeParent.Init(80));
//    	Component node5 = create(NodeParent.class, new NodeParent.Init(100));
    	
    	Component node1 = create(NodeHost.class, new NodeHost.Init(20));
    	Component node2 = create(NodeHost.class, new NodeHost.Init(40));
    	Component node3 = create(NodeHost.class, new NodeHost.Init(60));
    	Component node4 = create(NodeHost.class, new NodeHost.Init(80));
    	Component node5 = create(NodeHost.class, new NodeHost.Init(100));
    	
    }
}
