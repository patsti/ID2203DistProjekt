/**
 * This file is part of the ID2203 course assignments kit.
 * 
 * Copyright (C) 2009-2013 KTH Royal Institute of Technology
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.sics.client;



import org.apache.log4j.PropertyConfigurator;


import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Fault;
import se.sics.kompics.Handler;
import se.sics.kompics.Init;
import se.sics.kompics.Kompics;

import se.sics.kompics.timer.java.JavaTimer;

public class Main extends ComponentDefinition {
	static {
		PropertyConfigurator.configureAndWatch("log4j.properties");
	}
	private static int selfId;
	private static String commandScript;
//	private Topology topology = Topology.load(System.getProperty("topology"), selfId);

	public static void main(String[] args) {
//		selfId = Integer.parseInt(args[0]);
//		commandScript = args[1];
		Kompics.createAndStart(Main.class);
	}

	public Main() {
//		Address self = topology.getSelfAddress();
//		Set<Address> pi = topology.getAllAddresses();

		Component time = create(JavaTimer.class, Init.NONE);
//		Component network = create(NettyNetwork.class, new NettyNetworkInit(self, 5));
		Component con = create(JavaConsole.class, Init.NONE);
//		Component pp2p = create(DelayLink.class, new DelayLinkInit(topology));
//		Component epfd = create(Epfd.class, new EpfdInit(self, pi, 1500, 500));
//		Component app = create(Application.class, new ApplicationInit(self, pi, commandScript));

//		subscribe(handleFault, time.control());
//		subscribe(handleFault, network.control());
//		subscribe(handleFault, con.control());
//		subscribe(handleFault, pp2p.control());
//		subscribe(handleFault, epfd.control());
//		subscribe(handleFault, app.control());

//		connect(app.required(EventuallyPerfectFailureDetector.class), epfd.provided(EventuallyPerfectFailureDetector.class));
//		connect(app.required(Console.class), con.provided(Console.class));
//		connect(app.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
//		connect(app.required(Timer.class), time.provided(Timer.class));

//		connect(epfd.required(Timer.class), time.provided(Timer.class));
//		connect(epfd.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));

//		connect(pp2p.required(Timer.class), time.provided(Timer.class));
//		connect(pp2p.required(Network.class), network.provided(Network.class));
	}

}
