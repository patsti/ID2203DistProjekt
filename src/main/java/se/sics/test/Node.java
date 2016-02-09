package se.sics.test;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
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
  

    public Node() {
        this.self = config().getValue("project.self", TAddress.class);
    }



	    Handler<Start> startHandler = new Handler<Start>() {
	        @Override
	        public void handle(Start event) {
	        	
	        	//System.out.println("[NODE] started with: "+ self);
	        
	        	long period = config().getValue("project.node.timeout", Long.class);
	            SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(0, period);
	            PingTimeout timeout = new PingTimeout(spt);
	            spt.setTimeoutEvent(timeout);
	            trigger(spt, timer);
	            timerId = timeout.getTimeoutId();
	        }
	    };
	    
	    {
	    	subscribe(startHandler, control);	    	
	    }
//	    ClassMatchedHandler<Pong, TMessage> pongHandler = new ClassMatchedHandler<Pong, TMessage>() {
//
//	        @Override
//	        public void handle(Pong content, TMessage context) {
//	            counter++;
//	            LOG.info("Got Pong #{}!", counter);
//	        }
//	    };
//	    Handler<PingTimeout> timeoutHandler = new Handler<PingTimeout>() {
//	        @Override
//	        public void handle(PingTimeout event) {
//	            trigger(new TMessage(self, ponger, Transport.TCP, new Ping()), net);
//	        }
//	    };
//
//	    {
//	        subscribe(startHandler, control);
//	        subscribe(pongHandler, net);
//	        subscribe(timeoutHandler, timer);
//	    }
//
//	    @Override
//	    public void tearDown() {
//	        trigger(new CancelPeriodicTimeout(timerId), timer);
//	    }
//
	    public static class PingTimeout extends Timeout {

	        public PingTimeout(SchedulePeriodicTimeout spt) {
	            super(spt);
	        }
	    }
	

}
