package se.sics.client;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Kompics;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;
import se.sics.test.NetSerializer;
import se.sics.test.Node;
import se.sics.test.Ping;
import se.sics.test.PingPongSerializer;
import se.sics.test.Pong;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;
import se.sics.test.THeader;
import se.sics.test.TMessage;

public class ClientHost extends ComponentDefinition{
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
	}
	
	static {
		PropertyConfigurator.configureAndWatch("log4j.properties");
        Conversions.register(new TAddressConverter());
	}
	
    private static final Logger LOG = LoggerFactory.getLogger(Node.class);
	
	public static void main(String[] args) {
		Kompics.createAndStart(ClientHost.class);
	}
	
	public ClientHost(){

		TAddress self = config().getValue("project.master", TAddress.class);
		
		Component network = create(NettyNetwork.class, new NettyInit(self));
		Component timer = create(JavaTimer.class, Init.NONE);
		Component client = create(ClientParent.class, new ClientParent.Init(2000, "PUT<10,hejsan din galna man>:GET<10>));//:PUT<34,Keso>:GET<34>"));
		
		connect(client.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
		connect(client.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);	

	}
	
	public static class Init extends se.sics.kompics.Init<ClientHost>{
		
		private final Integer identifier;
		
		public Init(Integer identfier){
			this.identifier = identfier;
		}
	}
	
}
