package se.sics.test;

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
        TAddress baseSelf = config().getValue("project.self", TAddress.class);

        Component timer = create(JavaTimer.class, Init.NONE);
        Component network = create(NettyNetwork.class, new NettyInit(baseSelf));
        VirtualNetworkChannel vnc = VirtualNetworkChannel.connect(network.getPositive(Network.class), proxy);
        int num = config().getValue("project.node.num", Integer.class);
        
        
        for (int i = 0; i < num; i++) {
            byte[] id = Ints.toByteArray(i);
            Config.Builder cbuild = config().modify(id());
            cbuild.setValue("project.self", baseSelf.withVirtual(id));
            
            Component node = create(Node.class, Init.NONE, cbuild.finalise());
            
            connect(node.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);
            
            vnc.addConnection(id, node.getNegative(Network.class));
            
           /* Component pinger = create(Pinger.class, Init.NONE, cbuild.finalise());

            connect(pinger.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);

            vnc.addConnection(id, pinger.getNegative(Network.class));*/
        }

    }
}
