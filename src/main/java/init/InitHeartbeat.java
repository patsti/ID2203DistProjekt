package init;

import java.util.ArrayList;
import java.util.HashMap;

import se.sics.kompics.KompicsEvent;
import se.sics.test.TAddress;

public class InitHeartbeat implements KompicsEvent {
	public final TAddress self;
    public final HashMap<String, Integer> timeMaster = new HashMap<>();
    public ArrayList<TAddress> alive = new ArrayList();
    
    public InitHeartbeat(TAddress self, ArrayList<TAddress> neighbours, int startValue) {
        this.self = self;
        
        for(TAddress n : neighbours){
        	String n1 = String.valueOf(self.getPort())+String.valueOf(n.getPort());        	
        	this.timeMaster.put(n1, startValue);
        }
        alive = neighbours;

    }
    
    
	
}
