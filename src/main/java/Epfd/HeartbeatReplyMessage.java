//package Epfd;
//
//import java.io.Serializable;
//
//import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
//import se.sics.kompics.address.Address;
//
//public class HeartbeatReplyMessage extends Pp2pDeliver implements Serializable {
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -1346831731362811395L;
//	private final String message;
//
//	protected HeartbeatReplyMessage(Address source, String message) {
//		super(source);
//		this.message = message;
//	}
//	
//	public final String getMessage(){
//		return message;
//	}
//
//}
