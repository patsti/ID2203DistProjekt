package simulation;

import se.sics.client.ClientParent;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.Kompics;
import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.run.LauncherComp;
import se.sics.test.Main;
import se.sics.test.NetSerializer;
import se.sics.test.Ping;
import se.sics.test.PingPongSerializer;
import se.sics.test.Pong;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;
import se.sics.test.THeader;
import se.sics.test.TMessage;

public class SimulationMain extends ComponentDefinition {
	
	
//	public static void main(String[] args){
//		Kompics.createAndStart(SimulationMain.class, 2);
//	}
//	
//	public SimulationMain(){
//		Main main = new Main();
//		AppMain appMain = new AppMain("help");
//	}
	



    public static void main(String[] args){
    //    SimulationScenario startup = Scenarios.simpleStartUp();
    //    startup.simulate(LauncherComp.class);
        SimulationScenario simulationOfDeath = Scenarios.simulateNodeCrash();
        simulationOfDeath.simulate(LauncherComp.class);
    }

	
}

