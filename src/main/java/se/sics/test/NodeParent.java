package se.sics.test;

import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.PropertyConfigurator;

import Heart.Heartbeat;
import ports.BebPort;
import ports.RIWCMport;
import ports.StoragePort;
import se.sics.beb.BEBroadcast;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Positive;
import se.sics.kompics.config.Config;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;
import se.sics.riwcm.ReadImposeWriteConsultMajority;
import se.sics.riwcm.ReadImposeWriteConsultMajorityInit;
import se.sics.storage.Storage;

public class NodeParent extends ComponentDefinition{
	
	static {
		PropertyConfigurator.configureAndWatch("log4j.properties");
        Conversions.register(new TAddressConverter());
	}
	
	private ArrayList<TAddress> addresses = new ArrayList<>();
	
	Positive<Network> network = requires(Network.class);
	Positive<Timer> timer = requires(Timer.class);
	
    public NodeParent(Init init) {
    	System.out.println("ID: "+init.getId());
    	
    	int id = init.getId();
    	
        getAddresses();
    
    	TAddress self = config().getValue("project.self"+init.getId(), TAddress.class);
    	
//        TAddress self = init.self;
    	      
        Config.Builder cbuild = config().modify(id());
        cbuild.setValue("project.self", self);
        cbuild.setValue("project.self.id", id);
        cbuild.setValue("project.self.max", id);
        cbuild.setValue("project.self.min", id-19);
        Component node = create(Node.class, Init.NONE, cbuild.finalise());       
        
        //BACKUP
        Component storage = create(Storage.class, Init.NONE);
        Component beb = create(BEBroadcast.class, new BEBroadcast.Init(self, addresses));
        Component heart = create(Heartbeat.class, Init.NONE);
        Component riwcm = create(ReadImposeWriteConsultMajority.class, new ReadImposeWriteConsultMajorityInit(self, id, addresses));
        
        //BACKUP
//        connect(heart.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
//        connect(storage.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
//        connect(beb.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
        
        connect(heart.getNegative(Network.class), network, Channel.TWO_WAY);
        connect(storage.getNegative(Network.class), network, Channel.TWO_WAY);
        connect(beb.getNegative(Network.class), network, Channel.TWO_WAY);
        connect(node.getNegative(Network.class), network, Channel.TWO_WAY);
        connect(node.getNegative(Timer.class), timer, Channel.TWO_WAY);
        connect(node.getNegative(BebPort.class), beb.getPositive(BebPort.class), Channel.TWO_WAY);
        
//        connect(node.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);
//        connect(node.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
        connect(node.getNegative(StoragePort.class), storage.getPositive(StoragePort.class), Channel.TWO_WAY);
        connect(storage.getNegative(RIWCMport.class), riwcm.getPositive(RIWCMport.class), Channel.TWO_WAY);
        connect(riwcm.getNegative(BebPort.class), beb.getPositive(BebPort.class), Channel.TWO_WAY);
//        }
    }
    
    private void getAddresses(){
    	LinkedList list = (LinkedList) this.config().getValues("project.addresses");
    	for(int i = 0; i < list.size(); i++){
    		TAddress temp = Conversions.convert(list.get(i), TAddress.class);
    		this.addresses.add(temp);
    	
    	}
    }
    
    public static class Init extends se.sics.kompics.Init<NodeParent>{
    	
    	private final Integer id;
    	
    	public Init(Integer id){
    		this.id = id;
    	}
    	
    	public Integer getId(){
    		return id;
    	}
    }
}
