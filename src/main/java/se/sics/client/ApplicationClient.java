package se.sics.client;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.InetAddresses;

import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.Kompics;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.test.Node;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;

public class ApplicationClient extends ComponentDefinition {
	
	private final TAddress self = config().getValue("project.master", TAddress.class);
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationClient.class);
	

		
	private ArrayList<String> commands = new ArrayList<>();
	Positive<Network> net = requires(Network.class);
	Negative<Network> nega = provides(Network.class);
	
	public ApplicationClient(){
		doHelp();
		
		
		
		
	}
	
	private static final void doHelp() {
		LOG.info("Available commands: P<m>, S<n>, help, X");
		LOG.info("Pm: sends perfect message 'm' to all neighbors");
		LOG.info("Sn: sleeps 'n' milliseconds before the next command");
		LOG.info("help: shows this help message");
		LOG.info("X: terminates this process");
	}
	
	
	
	public static void main(String[] args){
		Kompics.createAndStart(ApplicationClient.class, 1);	
		
		Scanner scanner = new Scanner(System.in);
		doHelp();
		
		while(scanner.hasNextLine()){
			
			LOG.info("waiting for next command ffs");
			
		}
		
	}
	
	
	

	


}
