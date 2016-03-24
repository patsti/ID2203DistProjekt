package se.sics.test;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.common.primitives.Ints;

import Heart.Heartbeat;
import ports.BebPort;
import ports.StoragePort;
import ports.HeartbeatPort;
import se.sics.beb.BEBroadcast;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.config.Config;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.network.virtual.VirtualNetworkChannel;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;
import se.sics.storage.Storage;

public class NodeParent extends ComponentDefinition{
	
	private ArrayList<TAddress> addresses = new ArrayList<>();
	
    public NodeParent(Init init) {
    	System.out.println("ID: "+init.getId());
    	
    	int id = init.getId();
    	
        getAddresses();
        
//        int counter = 0;
        
//        for(TAddress addr: addresses){
               	
//        	counter += 20;
        	TAddress addr = config().getValue("project.self"+id, TAddress.class);
        	
        	
        	

        	      
            Config.Builder cbuild = config().modify(id());
            cbuild.setValue("project.self", addr);
            cbuild.setValue("project.self.id", id);
            cbuild.setValue("project.self.max", id);
            cbuild.setValue("project.self.min", id-19);
            Component node = create(Node.class, Init.NONE, cbuild.finalise());
            
            
            //BACKUP
//            Config.Builder cbuild = config().modify(id());
//            cbuild.setValue("project.self", addr);
//            cbuild.setValue("project.self.id", counter);
//            cbuild.setValue("project.self.max", counter);
//            cbuild.setValue("project.self.min", counter-19);
//            Component node = create(Node.class, Init.NONE, cbuild.finalise());
            
            
            Component timer = create(JavaTimer.class, Init.NONE);
            Component network = create(NettyNetwork.class, new NettyInit(addr));
            Component storage = create(Storage.class, Init.NONE);
            Component beb = create(BEBroadcast.class, Init.NONE);
            Component heart = create(Heartbeat.class, Init.NONE);
            
            
            connect(node.getNegative(BebPort.class), beb.getPositive(BebPort.class), Channel.TWO_WAY);
            connect(heart.getNegative(BebPort.class), beb.getPositive(BebPort.class), Channel.TWO_WAY);
            connect(heart.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
            connect(storage.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
            connect(beb.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
            connect(node.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);
            connect(node.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
            connect(node.getNegative(StoragePort.class), storage.getPositive(StoragePort.class), Channel.TWO_WAY);
            connect(node.getNegative(HeartbeatPort.class), heart.getPositive(HeartbeatPort.class), Channel.TWO_WAY);
//        }
    }
    
    private void getAddresses(){
    	LinkedList list = (LinkedList) this.config().getValues("project.addresses");
    	for(int i = 0; i < list.size(); i++){
    		TAddress temp = Conversions.convert(list.get(i), TAddress.class);
    		this.addresses.add(temp);
    	
    	}
    }
    
    public static final class Init extends se.sics.kompics.Init<NodeParent>{
    	private int id;
    	public Init(int id){
    		this.id = id;
    	}
    	
    	public int getId(){
    		return id;
    	}
    }
}
