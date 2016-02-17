//package Epfd;
//
//import java.io.Serializable;
//
//import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
//import se.sics.kompics.address.Address;
//
//public class HeartbeatRequestMessage extends Pp2pDeliver implements Serializable {
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -8498974667690579031L;
//	private final String message;
//
//	protected HeartbeatRequestMessage(Address source, String message) {
//		super(source);
//		this.message = message;
//	}
//	
//	public final String getMessage(){
//		return message;
//	}
//
//}
