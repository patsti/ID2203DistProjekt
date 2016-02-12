package se.sics.test;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.common.primitives.Ints;

import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.config.Config;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;

public class NodeParent extends ComponentDefinition{
	
	private ArrayList<TAddress> addresses = new ArrayList<>();
	
    public NodeParent() {
        getAddresses();
        
        for(TAddress addr: addresses){
            Component timer = create(JavaTimer.class, Init.NONE);
            Component network = create(NettyNetwork.class, new NettyInit(addr));
            
            Config.Builder cbuild = config().modify(id());
            cbuild.setValue("project.self", addr);
            Component node = create(Node.class, Init.NONE, cbuild.finalise());
            
            connect(node.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);
            connect(node.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
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
