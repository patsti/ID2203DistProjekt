package se.sics.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import se.sics.kompics.timer.Timeout;
import se.sics.kompics.timer.Timer;
import se.sics.test.Pinger.PingTimeout;

public class Node extends ComponentDefinition {
	
    private static final Logger LOG = LoggerFactory.getLogger(Pinger.class);
	
    Positive<Network> net = requires(Network.class);
    Positive<Timer> timer = requires(Timer.class);

    private long counter = 0;
    private UUID timerId;
    private final TAddress self;
    private TAddress predecessor;
    private TAddress successor;

    private ArrayList<TAddress> addresses = new ArrayList<>();
    private ArrayList<TAddress> replicationAddresses = new ArrayList<>();
    
    private HashMap<Integer, ArrayList<Integer>> storage = new HashMap<>();

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
        }
    };
    
    
    ClassMatchedHandler<Pong, TMessage> pongHandler = new ClassMatchedHandler<Pong, TMessage>() {

        @Override
        public void handle(Pong content, TMessage context) {
            counter++;
//            LOG.info("Got Pong #{}!", counter);
            if(!self.equals(context.getSource()))
            	LOG.info("["+self.toString()+"]"+" Got Pong #{}!"+" from ["+context.getSource().toString()+"]", counter);
        }
    };
    
    ClassMatchedHandler<Ping, TMessage> pingHandler = new ClassMatchedHandler<Ping, TMessage>() {
        @Override
        public void handle(Ping content, TMessage context) {
            counter++;
//            LOG.info("Got Ping #{}!", counter);
            LOG.info("["+self.toString()+"]"+" Got a Ping #{}!"+" from ["+context.getSource().toString()+"]", counter);
            trigger(new TMessage(self, context.getSource(), Transport.TCP, new Pong()), net);
        }
    };
    
    
    Handler<PingTimeout> timeoutHandler = new Handler<PingTimeout>() {
        @Override
        public void handle(PingTimeout event) {
        	for(TAddress addr: replicationAddresses){
        		trigger(new TMessage(self, addr, Transport.TCP, new Ping()), net);
        	}
        }
    };

    {
        subscribe(startHandler, control);
        subscribe(pongHandler, net);
        subscribe(timeoutHandler, timer);
        subscribe(pingHandler, net);
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
