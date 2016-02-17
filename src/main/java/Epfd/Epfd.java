//package Epfd;
//
//import java.util.HashSet;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import se.kth.ict.id2203.pa.epfd.ApplicationContinue;
//import se.kth.ict.id2203.pa.epfd.Pp2pMessage;
//import se.kth.ict.id2203.ports.epfd.EventuallyPerfectFailureDetector;
//import se.kth.ict.id2203.ports.epfd.Restore;
//import se.kth.ict.id2203.ports.epfd.Suspect;
//import se.kth.ict.id2203.ports.pp2p.PerfectPointToPointLink;
//import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
//import se.kth.ict.id2203.ports.pp2p.Pp2pSend;
//import se.sics.kompics.ComponentDefinition;
//import se.sics.kompics.Handler;
//import se.sics.kompics.Negative;
//import se.sics.kompics.Positive;
//import se.sics.kompics.Start;
//import se.sics.kompics.address.Address;
//import se.sics.kompics.timer.ScheduleTimeout;
//import se.sics.kompics.timer.Timer;
//
//public class Epfd extends ComponentDefinition {
//
//	private static final Logger logger = LoggerFactory.getLogger(Epfd.class);
//	
//	Negative<EventuallyPerfectFailureDetector> epfd = provides(EventuallyPerfectFailureDetector.class);
//	Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
//	Positive<Timer> timer = requires(Timer.class);
//	
//	HashSet<Address> alive = new HashSet<Address>();
//	HashSet<Address> suspected = new HashSet<Address>();
//	
//	long initialDelay;
//	long deltaDelay;
//	long delay;
//	int sequenceNumber;
//	Address self;
//	
//
//	public Epfd(EpfdInit init) {
//		logger.info("Epfd started at {}", init.getSelfAddress());
//		
//		subscribe(handleStart, control);
//		subscribe(handleCheckTimeout, timer);	
//		subscribe(handleHeartbeatReply, pp2p);
//		subscribe(handleHeartbeatRequest, pp2p);
//		
//		this.initialDelay = init.getInitialDelay();
//		this.deltaDelay = init.getDeltaDelay();
//		this.delay = initialDelay;
//		this.sequenceNumber = 0;
//		this.alive = (HashSet<Address>) init.getAllAddresses();
//		alive.remove(self);
//		this.self = init.getSelfAddress();
//		
//	}
//	
//	Handler<Start> handleStart = new Handler<Start>(){
//		@Override
//		public void handle(Start event) {
//			logger.info(event.toString());
//			startTimer(initialDelay);
//		}
//	};
//	
//	
//	Handler<HeartbeatRequestMessage> handleHeartbeatRequest = new Handler<HeartbeatRequestMessage>(){
//		@Override
//		public void handle(HeartbeatRequestMessage event){
//			logger.info("Got a HeartbeatRequest from {} with sequence number: #{} ", event.getSource(), event.getMessage());
//			Pp2pDeliver deliverEvent = new HeartbeatReplyMessage(self, ""+sequenceNumber);
//			trigger(new Pp2pSend(event.getSource(), deliverEvent), pp2p);
//		}
//	};
//	
//	Handler<HeartbeatReplyMessage> handleHeartbeatReply = new Handler<HeartbeatReplyMessage>(){
//		@Override
//		public void handle(HeartbeatReplyMessage event){
//			logger.info("Got a HeartbeatReply from {} with sequence number: #{} ", event.getSource(), event.getMessage());
//			int sn = Integer.parseInt(event.getMessage());
//			
//			if(sn == sequenceNumber || suspected.contains(event.getSource()))
//				alive.add(event.getSource());
//		}
//	};
//	
//	
//	Handler<CheckTimeout> handleCheckTimeout = new Handler<CheckTimeout>(){
//		@Override
//		public void handle(CheckTimeout event){
//			if(commonAliveSuspect()){
//				delay += deltaDelay;
//				logger.info("Delay is increased in {}", self);
//			}
//			sequenceNumber+=1;
//			
//			for(Address peer: alive){
//				if(!(alive.contains(peer) && suspected.contains(peer))){
//					suspected.add(peer);
//					trigger(new Suspect(peer), epfd);
//				} else if(alive.contains(peer) && suspected.contains(peer)) {
//					suspected.remove(peer);
//					trigger(new Restore(peer), epfd);
//				}
//				Pp2pDeliver deliverEvent = new HeartbeatRequestMessage(self, ""+sequenceNumber);
//				trigger(new Pp2pSend(peer, deliverEvent), pp2p);
//			}
//			alive.clear();
//			startTimer(delay);
//		}
//		
//		private boolean commonAliveSuspect() {
//			for (Address peer : suspected) {
//				if (alive.contains(peer))
//					return false;
//			}
//			return true;
//		}
//	};
//		
//	private void startTimer(long delay){
//		logger.info("Sleeping {} milliseconds...", delay);
//		
//		ScheduleTimeout st = new ScheduleTimeout(delay);
//		st.setTimeoutEvent(new CheckTimeout(st));
//		trigger(st, timer);
//	}
//	
//
//	
//}