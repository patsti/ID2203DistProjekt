package se.sics.riwcm;

import java.util.ArrayList;
import java.util.Set;

import se.sics.kompics.Init;
import se.sics.test.TAddress;

public class ReadImposeWriteConsultMajorityInit extends Init<ReadImposeWriteConsultMajority> {

	private final TAddress selfAddress;
	private final ArrayList<TAddress> allAddresses;
	private final Integer id;

	public ReadImposeWriteConsultMajorityInit(TAddress selfAddress, Integer id, ArrayList<TAddress> allAddresses) {
		this.selfAddress = selfAddress;
		this.allAddresses = allAddresses;
		this.id = id;
	}
	
	public final TAddress getSelfAddress() {
		return selfAddress;
	}

	public final ArrayList<TAddress> getAllAddresses() {
		return allAddresses;
	}
	
	public final Integer getId(){
		return id;
	}
	
}