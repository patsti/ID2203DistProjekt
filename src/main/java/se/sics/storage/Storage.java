package se.sics.storage;

import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import init.InitStorage;
import ports.RIWCMport;
import ports.StoragePort;
import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.test.TAddress;
import se.sics.test.TMessage;

public class Storage extends ComponentDefinition {
	
	private static final Logger LOG = LoggerFactory.getLogger(Storage.class);
	private HashMap<Integer, String> storage = new HashMap<>();
	private HashMap<Integer, String> replica1 = new HashMap<>();
	private HashMap<Integer, String> replica2 = new HashMap<>();
	private int id;
	private int min;
	private int max;
	private TAddress self;
	
	Positive<Network> net = requires(Network.class);
	Negative<StoragePort> storagePort = provides(StoragePort.class);
//	Negative<RIWCMport> riwcmPort = provides(RIWCMport.class);
	Positive<RIWCMport> riwcmPort = requires(RIWCMport.class);

	public Storage(){
		
	}
	
    Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start event) {
        	id = 1;
        }
    };
    
    Handler<InitStorage> initHandler = new Handler<InitStorage>(){
    	
    	@Override
    	public void handle(InitStorage event){
    		id = event.getId();
    		min = event.getMin();
    		max = event.getMax();
    		self = event.getSelf();
    		
//            for(int i = 0; i < 10; i++){
//            	Random rand = new Random();
//            	int key = rand.nextInt(max-min+1)+min;
//            	storage.put(key, "String value "+key);
//            }
    	}
    };
    
    
    ClassMatchedHandler<GetOperationRequest, TMessage> getOperationRequestHandler = new ClassMatchedHandler<GetOperationRequest, TMessage>() {

        @Override
        public void handle(GetOperationRequest content, TMessage context) {
        	int key = content.getKey();
        	if(!storage.containsKey(key)){
        		LOG.info("[GetOperation] key: {} isn't in my range: My address is: {}",key, self.getPort());
        	}else{
        		String value = storage.get(key);
        		LOG.info("[GetOperation] From: {} key: {} value: {} - Sending it back to: {}",self.getPort() ,key, value, context.getSource().getPort());
//        		trigger(new TMessage(context.getSource(), context.getSource(), Transport.TCP, new GetOperationReply(key, value)), net);
        		trigger(new RIWCMGetOperationRequest(key, value, context.getSource()), riwcmPort);
        	}
        }
    };

    
    ClassMatchedHandler<PutOperationRequest, TMessage> putOperationRequestHandler = new ClassMatchedHandler<PutOperationRequest, TMessage>() {

        @Override
        public void handle(PutOperationRequest content, TMessage context) {
        	int key = content.getKey();
        	String value = content.getValue();
        	if(!storage.containsKey(key)){
        		LOG.info("[PutOperation] key: {} isn't in my range: My address is: {}",key, self.getPort());
        		if(key <= max && key >= min ){
            		LOG.info("[PutOperation] From: {} key: {} storing value: {}",self.getPort() ,key, value);
            		storage.put(key, value);
            		trigger(new RIWCMPutOperationRequest(key, value, context.getSource()), riwcmPort);
            	}
        	}
        }
    };
    
    {
    	subscribe(getOperationRequestHandler, net);
    	subscribe(putOperationRequestHandler, net);
    	subscribe(initHandler, storagePort);
    }
      
    
    }
