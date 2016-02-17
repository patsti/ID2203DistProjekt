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
        		//LOG.info("[BEBroadcast] is sending message from port: "+content.getSource().getPort()+"[ - TO -: ]"+addr.getPort());
        		trigger(new TMessage(content.getSource(), addr, Transport.TCP, content.getGetOperationRequest()), net);
        	}
        	
        }
    };
     
    
    {
    	subscribe(broadcastGetHandler, beb);
    }
	
}
