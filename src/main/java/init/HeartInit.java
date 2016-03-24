package init;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableSet;

import Heart.Heartbeat;
import se.sics.kompics.Init;
import se.sics.kompics.network.Address;
import se.sics.kompics.network.Transport;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.test.TAddress;

public class HeartInit extends Init<Heartbeat> {
    public final TAddress self;
    public final HashMap<String, Integer> timeMaster = new HashMap<>();
    
    public HeartInit(TAddress self, ArrayList<String> neighbours, int startValue) {
        this.self = self;
        for(String n : neighbours){
        	this.timeMaster.put(n, startValue);
        }

    }
    
    public HeartInit(TAddress self, ArrayList<String> allN){
        this(self, allN, 0);
    }
}
