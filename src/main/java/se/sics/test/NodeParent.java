package se.sics.test;

import java.util.List;

import com.google.common.primitives.Ints;

import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.config.Config;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.network.virtual.VirtualNetworkChannel;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;

public class NodeParent extends ComponentDefinition{
	
    public NodeParent() {
        
        int num = config().getValue("project.node.num", Integer.class);
        
        
        for (int i = 0; i < num; i++) {
        	String portKey = "project.self"+Integer.toString(i);
        	TAddress baseSelf = config().getValue(portKey, TAddress.class);
            Component timer = create(JavaTimer.class, Init.NONE);
            Component network = create(NettyNetwork.class, new NettyInit(baseSelf));
            Config.Builder cbuild = config().modify(id());
            cbuild.setValue("project.self", baseSelf);
            Component node = create(Node.class, Init.NONE, cbuild.finalise());
            connect(node.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
            connect(node.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);
        }

    }
}
