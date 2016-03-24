package se.sics.beb;

import java.io.Serializable;

import riwcm.WriteBebDataMessage;
import se.sics.kompics.KompicsEvent;

public class BroadcastWriteRiwcm implements KompicsEvent, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WriteBebDataMessage writeBebDataMessage;
	
	public BroadcastWriteRiwcm(WriteBebDataMessage wbm){
		this.writeBebDataMessage = wbm;
	}
	
	public WriteBebDataMessage getWriteBebDataMessage(){
		return writeBebDataMessage;
	}
	
}
