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

import java.util.ArrayList;
import java.util.Set;

import se.sics.kompics.Init;
import se.sics.test.TAddress;

public final class ApplicationInit extends Init<Application> {

	private final TAddress selfAddress;
	private final ArrayList<TAddress> allAddresses;
	private final String commandScript;

	public ApplicationInit(TAddress selfAddress, ArrayList<TAddress> allAddresses, String commandScript) {
		this.selfAddress = selfAddress;
		this.allAddresses = allAddresses;
		this.commandScript = commandScript;
	}

	public final TAddress getSelfAddress() {
		return selfAddress;
	}
	
	public final ArrayList<TAddress> getAllAddresses() {
		return allAddresses;
	}

	public final String getCommandScript() {
		return commandScript;
	}
}
