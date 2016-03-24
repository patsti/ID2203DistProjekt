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
     
    	 //if(args.length == 0){
    		 //Kompics.createAndStart(PongerParent.class, 2);
//    		 Kompics.createAndStart(NodeParent.class, 2);
//    		 Kompics.createAndStart(NodeParent.class, new NodeParent.Init(20));
//    		 Kompics.createAndStart(NodeParent.class, new NodeParent.Init(40));
//    		 Kompics.createAndStart(NodeParent.class, new NodeParent.Init(60));
//    		 Kompics.createAndStart(NodeParent.class, new NodeParent.Init(80));
//    		 Kompics.createAndStart(NodeParent.class, new NodeParent.Init(100));
    	Kompics.createAndStart(Main.class);
    		 
    		 
    		 System.out.println("Starting Node");
    		 // no shutdown this time...act like a server and keep running until externally exited
    		 
//	          try {
//	              Thread.sleep(10000);
//	          } catch (InterruptedException ex) {
//	              System.exit(1);
//	          }
//	          Kompics.shutdown();
//	          System.exit(0);
//      
    	/* } else {
		      System.err.println("Invalid number of parameters");
		      System.exit(1);
    	 }*/
    }
    
    public Main(){
    	Component node1 = create(NodeParent.class, new NodeParent.Init(20));
    	Component node2 = create(NodeParent.class, new NodeParent.Init(40));
    	Component node3 = create(NodeParent.class, new NodeParent.Init(60));
    	Component node4 = create(NodeParent.class, new NodeParent.Init(80));
    	Component node5 = create(NodeParent.class, new NodeParent.Init(100));
    	
    }
}
