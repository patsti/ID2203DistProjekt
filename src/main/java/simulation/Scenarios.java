package simulation;

import se.sics.kompics.config.Conversions;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.simulator.SimulationScenario;
import se.sics.kompics.simulator.adaptor.distributions.Distribution;
import se.sics.kompics.simulator.adaptor.distributions.extra.BasicIntSequentialDistribution;
import se.sics.test.NetSerializer;
import se.sics.test.Ping;
import se.sics.test.PingPongSerializer;
import se.sics.test.Pong;
import se.sics.test.TAddress;
import se.sics.test.TAddressConverter;
import se.sics.test.THeader;
import se.sics.test.TMessage;

public class Scenarios {
	
	private static final int[] IDENTIFIERS = {20,40,60,80,100};

    public static SimulationScenario simpleStartUp(){
        return new SimulationScenario() {
            //constructor!
            {
                SimulationScenario.StochasticProcess node1 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2001), new BasicIntSequentialDistribution(20));
                    }
                };
                
                SimulationScenario.StochasticProcess node2 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2002), new BasicIntSequentialDistribution(40));
                    }
                };
                
                
                SimulationScenario.StochasticProcess node3 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2003), new BasicIntSequentialDistribution(60));
                    }
                };
                
                
                SimulationScenario.StochasticProcess node4 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2004), new BasicIntSequentialDistribution(80));
                    }
                };
                
                SimulationScenario.StochasticProcess node5 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2005), new BasicIntSequentialDistribution(100));
                    }
                };

                SimulationScenario.StochasticProcess client = new SimulationScenario.StochasticProcess(){
                    //nested constructor 2 :v
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(2, Operations.startClient, new BasicIntSequentialDistribution(2000), new BasicIntSequentialDistribution(20));
                    }
                };

                node1.start(); //start the bootstrapper
                node2.startAfterStartOf(1000, node1);
                node3.startAfterStartOf(1000, node2);
                node4.startAfterStartOf(1000, node3);
                node5.startAfterStartOf(1000, node4);
                client.startAfterStartOf(1000, node5);
//                node.startAfterStartOf(1000, bootstrapper); //then start the nodes
                terminateAt(15000);
            }
        };
    }
    
    public static SimulationScenario simulateNodeCrash(){
        return new SimulationScenario() {
            //constructor!
            {
                SimulationScenario.StochasticProcess node1 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2001), new BasicIntSequentialDistribution(20));
                    }
                };
                
                SimulationScenario.StochasticProcess node2 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2002), new BasicIntSequentialDistribution(40));
                    }
                };
                
                
                SimulationScenario.StochasticProcess node3 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2003), new BasicIntSequentialDistribution(60));
                    }
                };
                
                
                SimulationScenario.StochasticProcess node4 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2004), new BasicIntSequentialDistribution(80));
                    }
                };
                
                SimulationScenario.StochasticProcess node5 = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.startNode, new BasicIntSequentialDistribution(2005), new BasicIntSequentialDistribution(100));
                    }
                };

                SimulationScenario.StochasticProcess client = new SimulationScenario.StochasticProcess(){
                    //nested constructor 2 :v
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(2, Operations.startClient, new BasicIntSequentialDistribution(2000), new BasicIntSequentialDistribution(20));
                    }
                };
                
                SimulationScenario.StochasticProcess killMe = new SimulationScenario.StochasticProcess(){
                    //nested constructor ;o
                    {
                        eventInterArrivalTime(constant(1000));
                        raise(1, Operations.killNode, new BasicIntSequentialDistribution(2002));
                    }
                };

                node1.start(); //start the bootstrapper
                node2.startAfterStartOf(1000, node1);
                node3.startAfterStartOf(1000, node2);
                node4.startAfterStartOf(1000, node3);
                node5.startAfterStartOf(1000, node4);
                client.startAfterStartOf(1000, node5);
                killMe.startAfterStartOf(10000, node2);
                
                
                
//                node.startAfterStartOf(1000, bootstrapper); //then start the nodes
                terminateAt(20000);
            }
        };
    }
}
