package riwcm;

import java.io.Serializable;
import java.util.ArrayList;
import se.sics.kompics.KompicsEvent;
import se.sics.storage.RIWCMGetOperationRequest;
import se.sics.test.TAddress;


public class ReadBebDataMessage implements Serializable, KompicsEvent {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private TAddress source;
	private Integer r;
	private ArrayList<TAddress> receivers;
	private RIWCMGetOperationRequest getRIWCMOperationRequest;
	
	public ReadBebDataMessage(){}
	
	
	public ReadBebDataMessage(TAddress source, Integer r, ArrayList<TAddress> receivers, RIWCMGetOperationRequest getOperationRequest ){
		this.source = source;
		this.r = r;
		this.receivers = receivers;
		this.getRIWCMOperationRequest = getOperationRequest;
	}
	
	public Integer getR(){
		return r;
	}
	
	public ArrayList<TAddress> getReceivers(){
		return receivers;
	}
	
	public RIWCMGetOperationRequest getRIWCMGetOperationRequest(){
		return getRIWCMOperationRequest;
	}
	
	
	public TAddress getSource(){
		return source;
	}
}
