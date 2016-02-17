package se.sics.storage;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.test.Node;
import se.sics.test.Pong;
import se.sics.test.TMessage;
import se.sics.test.Node.PingTimeout;

public class Storage extends ComponentDefinition {
	
	private static final Logger LOG = LoggerFactory.getLogger(Storage.class);
	private HashMap<Integer, String> storage = new HashMap<>();
	private HashMap<Integer, String> replica1 = new HashMap<>();
	private HashMap<Integer, String> replica2 = new HashMap<>();
	private int id;
	
	Positive<Network> net = requires(Network.class);

	public Storage(){
		storage.put(1337, "senaps frö");
	}
	
    Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start event) {
        	id = 1;
        	LOG.info("[JHFLKJHVKLJDHNLKJVNLKJNBFLKJNVÖFLKNBLKFJNBKLFNVÖLKENBLKJNDLJKVBLKJNVDKB DLKÖNVBV]");
        }
    };
    
    
    ClassMatchedHandler<GetOperationRequest, TMessage> getOperationRequestHandler = new ClassMatchedHandler<GetOperationRequest, TMessage>() {

        @Override
        public void handle(GetOperationRequest content, TMessage context) {
        	int key = content.getKey();
        	if(!storage.containsKey(key)){
        		LOG.info("[GetOperation] key: {} isn't in my range",key);
        	}else{
        		String value = storage.get(key);
        		LOG.info("[GetOperation] key: {} value: {}",key, value);
        		trigger(new TMessage(context.getSource(), context.getSource(), Transport.TCP, new GetOperationReply(key, value)), net);
        	}
        }
    };	
    
    {
    	subscribe(getOperationRequestHandler, net);
    }
    
    }
