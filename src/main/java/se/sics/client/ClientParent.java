
package se.sics.client;



import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.log4j.PropertyConfigurator;

import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Kompics;
import se.sics.kompics.Positive;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;

public class ClientParent extends ComponentDefinition {
//	static {
//		PropertyConfigurator.configureAndWatch("log4j.properties");
//
//        // register
//        Serializers.register(new NetSerializer(), "netS");
//        Serializers.register(new PingPongSerializer(), "ppS");
//        // map
//        Serializers.register(TAddress.class, "netS");
//        Serializers.register(THeader.class, "netS");
//        Serializers.register(TMessage.class, "netS");
//        Serializers.register(Ping.class, "ppS");
//        Serializers.register(Pong.class, "ppS");
//	}
	
	static {
		PropertyConfigurator.configureAndWatch("log4j.properties");
        Conversions.register(new TAddressConverter());
	}
	
	public static void main(String[] args) {
		Kompics.createAndStart(ClientParent.class, new ClientParent.Init(2000, "help"));
	}

	
	private static int selfId;
	private static String commandScript;
	private ArrayList<TAddress> addresses = new ArrayList<TAddress>();
	
	Positive<Network> network = requires(Network.class);
	Positive<Timer> timer = requires(Timer.class);


	public ClientParent(Init init) throws InterruptedException {
		
		TAddress self = config().getValue("project.master", TAddress.class);
		getAddresses();
		
		String commands = init.getCommands();
		Component application = create(Application.class, new ApplicationInit(self, addresses, commands));
		connect(application.getNegative(Network.class), network, Channel.TWO_WAY); 
		connect(application.getNegative(Timer.class), timer, Channel.TWO_WAY);
	}
	
    private void getAddresses(){
    	LinkedList list = (LinkedList) this.config().getValues("project.addresses");

    	for(int i = 0; i < list.size(); i++){
    		TAddress temp = Conversions.convert(list.get(i), TAddress.class);
    		this.addresses.add(temp);
    	}
    }
    
	public static class Init extends se.sics.kompics.Init<ClientParent>{
		
		private final Integer port;
		private final String commands;
		
		public Init(Integer port, String commands){
			this.port = port;
			this.commands = commands;
		}
		
		public Integer getPort(){
			return port;
		}
		
		public String getCommands(){
			return commands;
		}
	}

}
