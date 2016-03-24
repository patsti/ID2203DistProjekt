package se.sics.beb;

import java.io.Serializable;

import riwcm.ReadBebDataMessage;
import se.sics.kompics.KompicsEvent;

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
