package Heart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Heart.*;
import ports.BebPort;
import se.sics.beb.BroadcastGet;
import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.network.virtual.VirtualNetworkChannel;
import se.sics.kompics.timer.CancelPeriodicTimeout;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;
import se.sics.kompics.timer.Timer;
import se.sics.pingpong.Pinger.PingTimeout;
import se.sics.storage.GetOperationReply;
import se.sics.storage.GetOperationRequest;
import se.sics.storage.GetOperationRequestFromClient;
import se.sics.storage.Storage;
import se.sics.test.Ping;
import se.sics.test.Pong;
import se.sics.test.TAddress;
import se.sics.test.TMessage;

public class Heartbeat extends ComponentDefinition {
	
	private static final Logger LOG = LoggerFactory.getLogger(Heartbeat.class);
	private HashMap<Integer, String> storage = new HashMap<>();
	private HashMap<Integer, String> replica1 = new HashMap<>();
	private HashMap<Integer, String> replica2 = new HashMap<>();
	private int id;
	
	Positive<Network> net = requires(Network.class);

	public Heartbeat(){
		storage.put(1337, "senaps frö");
	}
	
    Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start event) {
        	id = 1;
        	LOG.info("[JHFLKJHVKLJDHNLKJVNLKJNBFLKJNVÖFLKNBLKFJNBKLFNVÖLKENBLKJNDLJKVBLKJNVDKB DLKÖNVBV]");
        }
    };
    
    
    ClassMatchedHandler<HeartbeatRequestMessage, TMessage> heartRequestHandler = new ClassMatchedHandler<HeartbeatRequestMessage, TMessage>() {

        @Override
        public void handle(HeartbeatRequestMessage content, TMessage context) {

        		LOG.info("---GOT a Heartbeat!---");
        	//	trigger(new TMessage(context.getSource(), context.getSource(), Transport.TCP, new GetOperationReply(key, value)), net);
        	
        }
    };	
    
    {
    	subscribe(heartRequestHandler, net);
    }
}
