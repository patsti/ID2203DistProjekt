package se.sics.beb;

import java.io.Serializable;

import se.sics.kompics.KompicsEvent;
import se.sics.riwcm.ReadBebDataMessage;

public class BroadcastReadRiwcm implements KompicsEvent, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReadBebDataMessage readBebDataMessage;
	
	public BroadcastReadRiwcm(ReadBebDataMessage r){
		this.readBebDataMessage = r;
	}
	
	public ReadBebDataMessage getReadBebDataMessage(){
		return readBebDataMessage;
	}
	
}
