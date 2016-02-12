package se.sics.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
	        	
	        	System.out.println("STARTEVENT [NODE] started with: "+ self);
	        	//System.out.println("handle start");
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
	            System.out.println(self.getPort()+" Got Pong from " +context.getSource());
	        }
	    };
	    Handler<PingTimeout> timeoutHandler = new Handler<PingTimeout>() {
	        @Override
	        public void handle(PingTimeout event) throws UnknownHostException {
	        	//System.out.println("handle time");
	        	for(int i=0; i<3; i++){
	        		String[] ports = self.getAllPorts();
	        		InetAddress ip = InetAddress.getByName("127.0.0.1");
	                final TAddress tadd = new TAddress(ip, Integer.valueOf(ports[i]), ports);
	                System.out.println("Send Pong from "+ self.getPort() +" to "+ tadd.getPort());
	        		trigger(new TMessage(self, tadd, Transport.TCP, new Pong()), net);
	        	}
	        }
	    };

	    {
	        subscribe(startHandler, control);
	        subscribe(pongHandler, net);
	        subscribe(timeoutHandler, timer);
	    }
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
