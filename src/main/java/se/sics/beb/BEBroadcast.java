package se.sics.beb;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ports.BebPort;
import riwcm.ReadBebDataMessage;
import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.storage.GetOperationRequest;
import se.sics.storage.RIWCMGetOperationRequest;
import se.sics.test.NetSerializer;
import se.sics.test.Node;
import se.sics.test.Pong;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;
import se.sics.test.THeader;
import se.sics.test.TMessage;

public class BEBroadcast extends ComponentDefinition{
	
	Positive<Network> network = requires(Network.class); 
	Negative<BebPort> beb = provides(BebPort.class);
	
	private static final Logger LOG = LoggerFactory.getLogger(Node.class);
	
	public BEBroadcast(){}
	
	
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
    	//Uncomment before simulation
//	static {
//		PropertyConfigurator.configureAndWatch("log4j.properties");
//        Conversions.register(new TAddressConverter());
//        Serializers.register(TAddress.class, "netS");
//        Serializers.register(THeader.class, "netS");
//        Serializers.register(TMessage.class, "netS");
//        Serializers.register(new NetSerializer(), "netS");
//	}
	
    Handler<BroadcastReadRiwcm> riwcmReadHandler = new Handler<BroadcastReadRiwcm>() {

        @Override
        public void handle(BroadcastReadRiwcm content) {
        
        	
        	LOG.info("\t\t[BroadcastReadRiwcm] ");
        	
        	ReadBebDataMessage rbdm = content.getReadBebDataMessage();
        	
        	LOG.info("\t\t[BroadcastReadRiwcm DESTINATION] "+content.getReadBebDataMessage().getSource());
        	TAddress source = rbdm.getSource();
        	
        	for(TAddress addr: content.getReadBebDataMessage().getReceivers()){
        		trigger(new TMessage(source, addr, Transport.TCP, rbdm), network);
        	}
        	//Trigger to himself, very NOT neat davai
        	trigger(new TMessage(source, source, Transport.TCP, rbdm), network);
        }
    };
	


    
    Handler<BroadcastWriteRiwcm> riwcmWriteHandler = new Handler<BroadcastWriteRiwcm>() {

        @Override
        public void handle(BroadcastWriteRiwcm content) {
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
    	subscribe(riwcmReadHandler, beb);
    	
    	Conversions.register(new TAddressConverter());
    }
	
}
