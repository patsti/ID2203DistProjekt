package se.sics.beb;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ports.BebPort;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.riwcm.ReadBebDataMessage;
import se.sics.riwcm.WriteBebDataMessage;
import se.sics.test.Node;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;
import se.sics.test.TMessage;

public class BEBroadcast extends ComponentDefinition{
	
	Positive<Network> network = requires(Network.class); 
	Negative<BebPort> beb = provides(BebPort.class);
	
	private static final Logger LOG = LoggerFactory.getLogger(Node.class);
	
	private ArrayList<TAddress> replicationGroup = new ArrayList<TAddress>();
	private TAddress self;
	
	public BEBroadcast(Init init){
		replicationGroup = init.receivers;
		self = init.self;
	}
	
	
    Handler<BroadcastGet> broadcastGetHandler = new Handler<BroadcastGet>() {

        @Override
        public void handle(BroadcastGet content) {
        	for(TAddress addr: content.getReceivers()){
        		trigger(new TMessage(content.getSource(), addr, Transport.TCP, content.getGetOperationRequest()), network);
        	}
        	
        }
    };//READ-IMPOSE WRITE MAJORITY - kolla upp det!
    
    
    Handler<BroadcastPut> broadcastPutHandler = new Handler<BroadcastPut>() {

        @Override
        public void handle(BroadcastPut content) {
        	for(TAddress addr: content.getReceivers()){
        		trigger(new TMessage(content.getSource(), addr, Transport.TCP, content.getPutOperationRequest()), network);
        	}
        	
        }
    };
    

    
    
    Handler<BroadcastHeartbeat> broadcastHeartbeatHandler = new Handler<BroadcastHeartbeat>() {

        @Override
        public void handle(BroadcastHeartbeat content) {
        	for(TAddress addr: content.getReceivers()){
        		trigger(new TMessage(content.getSource(), addr, Transport.TCP, content.getGetHeartbeatRequest()), network);
        	}
        	
        }
    };

    
    Handler<ReadBebDataMessage> readBebDataMessage = new Handler<ReadBebDataMessage>(){
    	@Override
    	public void handle(ReadBebDataMessage content){
    		
    		ReadBebDataMessage readBebDataMessage = content;
    		
    		LOG.info("\t\t[Broadcasting readBebDataMessage] ");
    		for(TAddress addr: replicationGroup){
//    			LOG.info("\t\t[ADDRESS] "+addr.toString());
    			trigger(new TMessage(self, addr, Transport.TCP, content), network);
    		}
    		LOG.info("\t\t[ADDRESS SELF] "+self.toString());
    		trigger(new TMessage(self, self, Transport.TCP, readBebDataMessage), network);
    	}
    };
    
    
    Handler<WriteBebDataMessage> writeBebDataMessage = new Handler<WriteBebDataMessage>(){
    	@Override
    	public void handle(WriteBebDataMessage content){    		
    		LOG.info("\t\t[Broadcasting writeBebDataMessage] ");
    		for(TAddress addr: replicationGroup){
    			LOG.info("\t\t[ADDRESS] "+addr.toString());
    			trigger(new TMessage(self, addr, Transport.TCP, content), network);
    		}
    		LOG.info("\t\t[ADDRESS SELF] "+self.toString());
    		trigger(new TMessage(self, self, Transport.TCP, content), network);
    	}
    };
	


    
    Handler<BroadcastWriteRiwcm> riwcmWriteHandler = new Handler<BroadcastWriteRiwcm>() {

        @Override
        public void handle(BroadcastWriteRiwcm content) {
        	LOG.info("\t\t[Broadcasting WriteRiwcm] ");
        	for(TAddress addr: content.getWriteBebDataMessage().getReceivers()){
        		trigger(new TMessage(content.getWriteBebDataMessage().getSource(), addr, Transport.TCP, content.getWriteBebDataMessage()), network);
        	}
        	//Trigger to himself, very NOT neat 
        	trigger(new TMessage(content.getWriteBebDataMessage().getSource(), content.getWriteBebDataMessage().getSource(), Transport.TCP, content.getWriteBebDataMessage()), network);
        	
        }
    };
    
    
  
     
    
    {
    	subscribe(broadcastGetHandler, beb);
    	subscribe(broadcastHeartbeatHandler, beb);
    	subscribe(broadcastPutHandler, beb);
    	subscribe(riwcmWriteHandler, beb);
//    	subscribe(riwcmReadHandler, beb);
    	subscribe(readBebDataMessage, beb);
//    	subscribe(readBebDataMessage, network);
    	
    	Conversions.register(new TAddressConverter());
    }
    
    public static class Init extends se.sics.kompics.Init<BEBroadcast>{
    	
    	private TAddress self;
    	private ArrayList<TAddress> receivers = new ArrayList<TAddress>();
    	
    	public Init(TAddress self, ArrayList<TAddress> receivers){
    		this.self = self;
    		this.receivers = receivers;
    	}
    }
	
}
