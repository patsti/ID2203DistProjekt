package simulation;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.KompicsEvent;
import se.sics.kompics.network.Address;
import se.sics.kompics.simulator.adaptor.Operation;
import se.sics.kompics.simulator.adaptor.Operation1;
import se.sics.kompics.simulator.adaptor.Operation2;
import se.sics.kompics.simulator.events.system.StartNodeEvent;
import se.sics.test.TAddress;
import se.sics.test.*;
import java.net.InetAddress;
import java.util.Random;

import se.sics.kompics.config.Config;
import se.sics.client.*;

public class Operations extends ComponentDefinition {
	
	public final static String IP = "localhost";
	public final static int MIN = 1;
	public static int MAX = 100;


    static Operation2 startNode = new Operation2<StartNodeEvent, Integer, Integer>() {

		@Override
        public StartNodeEvent generate(final Integer port, final Integer identifier) {
            return new StartNodeEvent() {
            	
            	private TAddress self;
            	
            	{
	            	try{
	            		self = new TAddress(InetAddress.getByName(IP), port);

	            	}catch(Exception ex){
	            		ex.printStackTrace();
	            	}
            	}

				@Override
				public Class<? extends ComponentDefinition> getComponentDefinition() {
					return NodeParent.class;
				}

				@Override
				public Init getComponentInit() {
					return new NodeParent.Init(identifier);
				}

				@Override
				public Address getNodeAddress() {
					return self;
				}
				
				@Override
				public String toString(){
					return "Node<"+self.toString()+">";
				}
            };
        }
    };
    
    static Operation2 startClient = new Operation2<StartNodeEvent, Integer, Integer>() {

		@Override
        public StartNodeEvent generate(final Integer port, final Integer identifier) {
            return new StartNodeEvent() {
            	
            	private TAddress self;
            	
            	{
	            	try{
	            		self = new TAddress(InetAddress.getByName(IP), port);

	            	}catch(Exception ex){
	            		ex.printStackTrace();
	            	}
            	}

				@Override
				public Class<? extends ComponentDefinition> getComponentDefinition() {
					return ClientParent.class;
				}

				@Override
				public Init getComponentInit() {
//					return new ClientParent.Init(port, getParams(getRandomValueInInterval(1,10)));
					return new ClientParent.Init(port, "help:PUT<21,Mädchen>:PUT<45,Junge ala freuline>:GET<45>:GET<21>:PUT<21,Nytt värde>:GET<21>");
				}

				@Override
				public Address getNodeAddress() {
					return self;
				}
				
				@Override
				public String toString(){
					return "ClientNode<"+self.toString()+">";
				}
				
				private String getParams(int numberOfParams){
					StringBuilder sb = new StringBuilder();
					sb.append("help");
					for(int i = 0; i < numberOfParams; i++){
						int random = getRandomValueInInterval(1, numberOfParams);
						System.out.println("{i} "+i);
						sb.append(":");
						String command = Settings.COMMANDS[(random % Settings.COMMANDS.length)];
						String value = Settings.VALUES[random % Settings.VALUES.length];
						int key = getRandomValueInInterval(1,100);
						sb.append(command)
						.append("<");
						if(command.equals("GET")){
							sb.append(key).append(">");
							continue;
						}else if(command.equals("PUT")){
							sb.append(key).append(",").append(value).append(">");
							continue;
						}
					}
					
					System.out.println("\t\t[COMMANDS] "+sb.toString());
					return sb.toString();
				}
            };
        }
    };
    
    private static class Settings{
    	private static String[] COMMANDS = new String[]{"GET", "PUT"};
    	private static String[] VALUES = new String[]{"Senap", "Keso", "Honung", "Cheese", "Lavender", "Mädchen", "Junge", "Kvarg"};
    }
    
    private static Random rand = new Random();
    
    private static int getRandomValueInInterval(int MIN, int MAX){
    	
    	int key = rand.nextInt(MAX-MIN+1)+MIN;
    	return key;
    }  
   
}
