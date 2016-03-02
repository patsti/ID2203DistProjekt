
package se.sics.client;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;

import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;
import se.sics.storage.GetOperationReply;
import se.sics.storage.GetOperationRequestFromClient;
import se.sics.test.TAddress;
import se.sics.test.TMessage;

public final class Application extends ComponentDefinition {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private Positive<Timer> timer = requires(Timer.class);
	private Positive<Console> console = requires(Console.class);
	private Positive<Network> net = requires(Network.class);

	private TAddress self;
	private ArrayList<TAddress> nodes;

	private ArrayList<String> commands;
	private boolean blocking;
	


	public Application(ApplicationInit event) {
		subscribe(handleStart, control);
		subscribe(handleConsoleInput, console);
		
		
	
		nodes = event.getAllAddresses();
		self = event.getSelfAddress();
		
		commands = new ArrayList<String>(Arrays.asList(event.getCommandScript().split(":")));
        commands.add("$DONE");
        blocking = false;
	}

	private Handler<Start> handleStart = new Handler<Start>() {
		@Override
		public void handle(Start event) {
			doNextCommand();
		}
	};
	



	private Handler<ConsoleLine> handleConsoleInput = new Handler<ConsoleLine>() {
		@Override
		public void handle(ConsoleLine event) {
			if (event.getLine().equals("XX")) {
				doShutdown();
			} else {
				commands.addAll(Arrays.asList(event.getLine().trim().split(":")));
				doNextCommand();
			}
		}
	};


	private final void doNextCommand() {
		while (!blocking && !commands.isEmpty()) {
            doCommand(commands.remove(0));
		}
	}
	

	private void doCommand(String cmd) {
		
		if (cmd.startsWith("G")) {
			doPerfect(cmd.substring(3));
		} 
		else if (cmd.startsWith("S")) {
			doSleep(Integer.parseInt(cmd.substring(1)));
		} else if (cmd.startsWith("X")) {
			doShutdown();
		} else if (cmd.equals("help")) {
			doHelp();
		} else if (cmd.equals("$DONE")) {
			LOG.info("DONE ALL OPERATIONS");
		} else {
			LOG.info("Bad command: '{}'. Try 'help'", cmd);
		}
	}

	private final void doHelp() {
		LOG.info("Available commands: GET<key>, S<n>, help, X");
		LOG.info("Get<key>: sends a GET to a all nodes");
		LOG.info("Sn: sleeps 'n' milliseconds before the next command");
		LOG.info("help: shows this help message");
		LOG.info("X: terminates this process");
	}
	
    private final void doPerfect(String key) {
    	
    	int keyStart = key.indexOf("<");
    	int keyEnd = key.indexOf(">");
    	key = key.substring(keyStart+1, keyEnd);
    	System.out.println("[KEY] "+ key);
    	
    	TAddress node = nodes.get(1);
		LOG.info("Sending GET<key> {} to Port {}", key, node.getPort());
		trigger(new TMessage(self, node, Transport.TCP, new GetOperationRequestFromClient(Integer.parseInt(key)) ), net);
    	

//		for (TAddress node : nodes) {
//			LOG.info("Sending GET<key> {} to Port {}", key, node.getPort());
//			trigger(new TMessage(self, node, Transport.TCP, new GetOperationRequestFromClient(Integer.parseInt(key)) ), net);
//		}
	}
    
    
    ClassMatchedHandler<GetOperationReply, TMessage> getReplyHandler = new ClassMatchedHandler<GetOperationReply, TMessage>() {

        @Override
        public void handle(GetOperationReply content, TMessage context) {
        	LOG.info("[PORT: "+self.getPort()+"]"+"With my key: "+content.getKey()+" I got: "+content.getValue()+" From: "+context.getSource().getPort());
        }
    };

	private void doSleep(long delay) {
		LOG.info("Sleeping {} milliseconds...", delay);

		ScheduleTimeout st = new ScheduleTimeout(delay);
//		st.setTimeoutEvent(new ApplicationContinue(st));
		trigger(st, timer);
		
		blocking = true;
	}

	private void doShutdown() {
		System.out.println("2DIE");
		System.out.close();
		System.err.close();
		Kompics.shutdown();
		blocking = true;
	}	
	
	{
		subscribe(handleStart, control);
		subscribe(getReplyHandler, net);
	}
}
