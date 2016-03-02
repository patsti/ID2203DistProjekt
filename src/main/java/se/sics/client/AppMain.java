
package se.sics.client;



import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.PropertyConfigurator;

import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.Kompics;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.timer.java.JavaTimer;
import se.sics.test.NetSerializer;
import se.sics.test.Ping;
import se.sics.test.PingPongSerializer;
import se.sics.test.Pong;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;
import se.sics.test.THeader;
import se.sics.test.TMessage;

public class AppMain extends ComponentDefinition {
	static {
		PropertyConfigurator.configureAndWatch("log4j.properties");
		
	
        // register
        Serializers.register(new NetSerializer(), "netS");
        Serializers.register(new PingPongSerializer(), "ppS");
        // map
        Serializers.register(TAddress.class, "netS");
        Serializers.register(THeader.class, "netS");
        Serializers.register(TMessage.class, "netS");
        Serializers.register(Ping.class, "ppS");
        Serializers.register(Pong.class, "ppS");

        // conversions
        Conversions.register(new TAddressConverter());

	}
	private static int selfId;
	private static String commandScript;
	private ArrayList<TAddress> addresses = new ArrayList<TAddress>();

	
	public static void main(String[] args) {
		Kompics.createAndStart(AppMain.class);
	}

	public AppMain() {
		
		TAddress self = config().getValue("project.master", TAddress.class);
		getAddresses();
		
		commandScript = "help";
			
		Component time = create(JavaTimer.class, Init.NONE);
//		Component network = create(NettyNetwork.class, new NettyNetworkInit(self, 5));
		Component con = create(JavaConsole.class, Init.NONE);
		Component network = create(NettyNetwork.class, new NettyInit(self));
		Component app = create(Application.class, new ApplicationInit(self, addresses, commandScript));
		
        
        connect(app.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
		
		connect(app.required(Console.class), con.provided(Console.class));

	}
	
    private void getAddresses(){
    	LinkedList list = (LinkedList) this.config().getValues("project.addresses");

    	for(int i = 0; i < list.size(); i++){
    		TAddress temp = Conversions.convert(list.get(i), TAddress.class);
    		this.addresses.add(temp);
    	}
    }

}
