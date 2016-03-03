package se.sics.beb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ports.BebPort;
import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.storage.GetOperationRequest;
import se.sics.test.Node;
import se.sics.test.Pong;
import se.sics.test.TAddress;
import se.sics.test.TMessage;

public class BEBroadcast extends ComponentDefinition{
	
	Positive<Network> net = requires(Network.class);
	Negative<BebPort> beb = provides(BebPort.class);
	
	private static final Logger LOG = LoggerFactory.getLogger(Node.class);
	
	public BEBroadcast(){}
	
	
    Handler<BroadcastGet> broadcastGetHandler = new Handler<BroadcastGet>() {

        @Override
        public void handle(BroadcastGet content) {
        	for(TAddress addr: content.getReceivers()){
        		trigger(new TMessage(content.getSource(), addr, Transport.TCP, content.getGetOperationRequest()), net);
        	}
        	
        }
    };//READ-IMPOSE WRITE MAJORITY - kolla upp det!
    
    
    Handler<BroadcastPut> broadcastPutHandler = new Handler<BroadcastPut>() {

        @Override
        public void handle(BroadcastPut content) {
        	for(TAddress addr: content.getReceivers()){
        		trigger(new TMessage(content.getSource(), addr, Transport.TCP, content.getPutOperationRequest()), net);
        	}
        	
        }
    };
    

    
    
    Handler<BroadcastHeartbeat> broadcastHeartbeatHandler = new Handler<BroadcastHeartbeat>() {

        @Override
        public void handle(BroadcastHeartbeat content) {
        	for(TAddress addr: content.getReceivers()){
        		trigger(new TMessage(content.getSource(), addr, Transport.TCP, content.getGetHeartbeatRequest()), net);
        	}
        	
        }
    };
     
    
    {
    	subscribe(broadcastGetHandler, beb);
    	subscribe(broadcastHeartbeatHandler, beb);
    	subscribe(broadcastPutHandler, beb);
    }
	
}
