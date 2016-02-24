package se.sics.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Heart.*;
import ports.BebPort;
import se.sics.beb.BroadcastGet;
import se.sics.beb.BroadcastHeartbeat;
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

public class Node extends ComponentDefinition {
	
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);
	
    Positive<Network> net = requires(Network.class);
    Positive<Timer> timer = requires(Timer.class);
    Positive<BebPort> beb = requires(BebPort.class);

    private long counter = 0;
    private UUID timerId;
    private final TAddress self;
    private TAddress predecessor;
    private TAddress successor;
	private HashMap<Integer, String> storage = new HashMap<>();
	private HashMap<Integer, String> replica = new HashMap<>();

    private ArrayList<TAddress> addresses = new ArrayList<>();
    private ArrayList<TAddress> replicationAddresses = new ArrayList<>();
    
   

    public Node() {
        this.self = config().getValue("project.self", TAddress.class);
        System.out.println("[Node Address] IP: "+self.toString());
        getAddresses();
        getNeighbours();
    }
    
    private void getNeighbours(){
    	for(int i = 0; i < addresses.size(); i++){
    			
    		if(self.equals(addresses.get(i))){
    			if(i == 0){
        			replicationAddresses.add(addresses.get(i+1));
        			replicationAddresses.add(addresses.get(addresses.size()-1));
    			}else if(i == addresses.size()-1){
	    			replicationAddresses.add(addresses.get(0));
	    			replicationAddresses.add(addresses.get(i-1));
    			}else{
	    			replicationAddresses.add(addresses.get(i+1));
	    			replicationAddresses.add(addresses.get(i-1));
    			}
    		}
    	}
    }

    Handler<Start> startHandler = new Handler<Start>() {
        @Override
        public void handle(Start event) {
        	long period = config().getValue("project.node.timeout", Long.class);
            SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(0, period);
            
            PingTimeout timeout = new PingTimeout(spt);
            spt.setTimeoutEvent(timeout);
            trigger(spt, timer);
            timerId = timeout.getTimeoutId();
            
       /*     HeartbeatRequestMessage HRM = new HeartbeatRequestMessage(self,"0");
            trigger(new TMessage(self, self, Transport.TCP, HRM), timer);
            ScheduleTimeout st = new ScheduleTimeout(0);
    		st.setTimeoutEvent(new CheckTimeout(st));
    		trigger(st, timer);
        */
        }
    };
    
    
    ClassMatchedHandler<Pong, TMessage> pongHandler = new ClassMatchedHandler<Pong, TMessage>() {

        @Override
        public void handle(Pong content, TMessage context) {
            counter++;

            if(!self.equals(context.getSource()))
            	LOG.info("["+self.toString()+"]"+" Got Pong #{}!"+" from ["+context.getSource().toString()+"]", counter);
        }
    };
    
    ClassMatchedHandler<Ping, TMessage> pingHandler = new ClassMatchedHandler<Ping, TMessage>() {
        @Override
        public void handle(Ping content, TMessage context) {
            counter++;
            LOG.info("["+self.toString()+"]"+" Got a Ping #{}!"+" from ["+context.getSource().toString()+"]", counter);
            trigger(new TMessage(self, context.getSource(), Transport.TCP, new Pong()), net);
        }
    };
    
    
    Handler<PingTimeout> timeoutHandler = new Handler<PingTimeout>() {
        @Override
        public void handle(PingTimeout event) {
        	trigger(new BroadcastGet(self, replicationAddresses, new GetOperationRequest(1337)), beb);
        	trigger(new BroadcastHeartbeat(self, replicationAddresses, new HeartbeatRequestMessage()),beb);
//        	for(TAddress addr: replicationAddresses){
//        		trigger(new TMessage(self, addr, Transport.TCP, new Ping()), net);
//        	}
        }
    };
    
    
    
    ClassMatchedHandler<GetOperationRequestFromClient, TMessage> getOperationHandler = new ClassMatchedHandler<GetOperationRequestFromClient, TMessage>() {

        @Override
        public void handle(GetOperationRequestFromClient content, TMessage context) {
        	//trigger BEB-broadcast with GetOperationRequest-event to all neighbours
        }
    };
    
    ClassMatchedHandler<GetOperationReply, TMessage> getReplyHandler = new ClassMatchedHandler<GetOperationReply, TMessage>() {

        @Override
        public void handle(GetOperationReply content, TMessage context) {
        	LOG.info("With my key: "+content.getKey()+" I got: "+content.getValue());
        }
    };
    

    {
        subscribe(startHandler, control);
        subscribe(timeoutHandler, timer);
        subscribe(pingHandler, net);
        subscribe(getOperationHandler, net);
        subscribe(getReplyHandler, net);
    }

    @Override
    public void tearDown() {
        trigger(new CancelPeriodicTimeout(timerId), timer);
    }

    public static class PingTimeout extends Timeout {

        public PingTimeout(SchedulePeriodicTimeout spt) {
            super(spt);
        }
    }
    
    private void getAddresses(){
    	LinkedList list = (LinkedList) this.config().getValues("project.addresses");

    	for(int i = 0; i < list.size(); i++){
    		TAddress temp = Conversions.convert(list.get(i), TAddress.class);
    		this.addresses.add(temp);
    	}
    }
	

}
