package Heart;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import init.InitHeartbeat;
import ports.BebPort;
import ports.HeartbeatPort;
import se.sics.beb.BroadcastHeartbeat;
import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.test.TAddress;
import se.sics.test.TMessage;

public class Heartbeat extends ComponentDefinition {
	
	private static final Logger LOG = LoggerFactory.getLogger(Heartbeat.class);
	private ArrayList<TAddress> alive = new ArrayList<>();
	private ArrayList<Suspect> suspected = new ArrayList<>();
	private int id;
	private int currentTime = 0;
	private TAddress self;
	private HashMap<String, Integer> timeMaster = new HashMap<>();
	
	Positive<Network> net = requires(Network.class);
    Positive<BebPort> beb = requires(BebPort.class);
	Negative<HeartbeatPort> heartbeatPort = provides(HeartbeatPort.class);

	public Heartbeat(){
	}
	
	
	Handler<InitHeartbeat> initHandler = new Handler<InitHeartbeat>(){
    	
    	@Override
    	public void handle(InitHeartbeat event){
    		self = event.self;
    		timeMaster = event.timeMaster;
    		alive = event.alive;
    	}
    };
    
    
    Handler<HeartbeatInitMessage> initHeartbeatHandler = new Handler<HeartbeatInitMessage>() {

        @Override
        public void handle(HeartbeatInitMessage event) {
        	String sName1 = String.valueOf(self.getPort())+String.valueOf(alive.get(0).getPort());
        	String sName2 = String.valueOf(self.getPort())+String.valueOf(alive.get(1).getPort());
        	if((int)timeMaster.get(sName1) != event.id){
        		LOG.info("Suspecting #{}Time is #{}, eventID is #{}", sName1, timeMaster.get(sName1), event.id);
        		suspected.add(new Suspect(alive.get(0)));
        	}
        	if((int)timeMaster.get(sName2) != event.id){
        		LOG.info("Suspecting #{} Time is #{}, eventID is #{}", sName2, timeMaster.get(sName2), event.id);
        		suspected.add(new Suspect(alive.get(1)));
        	}
        	ArrayList<TAddress> sendTo = new ArrayList<TAddress>();
        	for(TAddress addr: alive){
        		boolean found = false;
        		for(Suspect s : suspected){
        			if(s.getSource().equals(addr)){
        				found = true;
        			}
        		}
        		if(!found){
        			LOG.info("#{}{} not suspected in event #{}", self.getPort(), addr.getPort(),event.id);

        			sendTo.add(addr);
        		}
        	}
        	trigger(new BroadcastHeartbeat(self, sendTo, new HeartbeatRequestMessage(event.id)),beb);

        	/* Add Suspection if id> timeMaster */
        }
    };
    
    
    ClassMatchedHandler<HeartbeatRequestMessage, TMessage> heartRequestHandler = new ClassMatchedHandler<HeartbeatRequestMessage, TMessage>() {

        @Override
        public void handle(HeartbeatRequestMessage content, TMessage context) {
        	String sName = String.valueOf(self.getPort())+String.valueOf(context.getSource().getPort());
    		//int iName = Integer.valueOf(sName);	
        if(suspected.contains(context.getSource())){
        		//suspected.remove(context.getSource());
        		LOG.info("removes #{} from suspect, time is #{} and id is {}", context.getSource(), timeMaster.get(sName), content.getID());
        	}
        
        trigger(new TMessage(self, context.getSource(), Transport.TCP, new HeartbeatReplyMessage(sName, content.getID())), net);
        }
        
    };	
    
    
    ClassMatchedHandler<HeartbeatReplyMessage, TMessage> heartReplyHandler = new ClassMatchedHandler<HeartbeatReplyMessage, TMessage>() {

        @Override
        public void handle(HeartbeatReplyMessage content, TMessage context) {
    		String sName = content.getCheck();
    		String p1 = sName.substring(0, 4);
    		String p2 = sName.substring(4, 8);
    		sName = p2+p1;
        	if(timeMaster.containsKey(sName)){
        		timeMaster.replace(sName, timeMaster.get(sName)+1);
        	}
        	if(suspected.contains(context.getSource())){
        		LOG.info("Removed #{} from suspected!!", context.getSource());
        		suspected.remove(context.getSource());
        	}
        	currentTime++;
        	LOG.info("{} increased currTime to #{}", sName, currentTime);
        	//trigger(new TMessage(context.getSource(), context.getSource(), Transport.TCP, new HeartNodeMessage(sName,timeMaster.get(sName))), heartPort);
        }
    };	
   
    {
    	subscribe(heartRequestHandler, net);
    	subscribe(heartReplyHandler, net);
    	subscribe(initHandler, heartbeatPort);
    	subscribe(initHeartbeatHandler, heartbeatPort);
    }
}
