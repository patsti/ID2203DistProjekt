package se.sics.riwcm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ports.BebPort;
import ports.RIWCMport;
import se.sics.beb.BroadcastReadRiwcm;
import se.sics.beb.BroadcastWriteRiwcm;
import se.sics.kompics.ClassMatchedHandler;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.storage.RIWCMGetOperationRequest;
import se.sics.test.TAddress;
import se.sics.test.TMessage;

public class ReadImposeWriteConsultMajority extends ComponentDefinition {

	private static final Logger LOG = LoggerFactory.getLogger(ReadImposeWriteConsultMajority.class);
	
	private Positive<Network> network = requires(Network.class); 
	private Negative<RIWCMport> riwcmPort = provides(RIWCMport.class);
	private Positive<BebPort> bebPort = requires(BebPort.class);

	private final TAddress self;
	private final Set<TAddress> receivers;
	private final Integer numberOfNodes;
	private LinkedList<ReadObject> readList;
	private Integer rid;
	private Integer acks;
	private Integer wr, val, writeVal, readVal;
	private String stringValue;
	private Integer ts;
	private Boolean reading;
	private WriteBebDataMessage wBebMsg;
	private Integer rr;
	private Integer maxts;
	private Integer selfId;

	public ReadImposeWriteConsultMajority(ReadImposeWriteConsultMajorityInit event) {
		this.self = event.getSelfAddress();
		this.selfId = event.getId();
		this.receivers = new HashSet<TAddress>(event.getAllAddresses());
		this.readList = new LinkedList<ReadObject>();
		this.numberOfNodes = this.receivers.size();
		this.rid = 0;
		this.acks = 0;
		this.reading = false;
		this.ts = 0;
		this.wr = 0;
		this.val = 0;
		this.writeVal = null;
		this.readVal = null;
		this.rr = 0;
		this.maxts = -1;
		this.stringValue = "";
		
		subscribe(startHandler, control);
		subscribe(readRequest, riwcmPort);
		subscribe(bebDeliver, network);
		subscribe(arDeliver, network);
//		subscribe(arDeliver, bebPort);
//		subscribe(bebDeliver, bebPort);
		
//		subscribe(readRequest, ar);
//		subscribe(writeRequest, ar);
//		
//		subscribe(bebDeliver, beb);
//		subscribe(writeValue, beb);
//		
//		subscribe(arDeliver, pp2p);
//		subscribe(ackHandler, pp2p);
	}

	private Handler<Start> startHandler = new Handler<Start>() {

		@Override
		public void handle(Start event) {
			LOG.info("ReadImposeWriteConsultMajority component started");
		}
	};

	//ReadRequest
	private Handler<RIWCMGetOperationRequest> readRequest = new Handler<RIWCMGetOperationRequest>() {

		@Override
		public void handle(RIWCMGetOperationRequest event) {
			LOG.info("Got read request from Node");
			rid++;
			acks = 0;
			readList.clear();
			reading = true;
			val = event.getKey();
			stringValue = event.getValue();
			RIWCMGetOperationRequest rgor = event;
			ReadBebDataMessage readBebDataMessage = new ReadBebDataMessage(self, rid);/*, receiversToArray(), rgor);*/
//			BroadcastReadRiwcm broadcastReadRiwcm = new BroadcastReadRiwcm(readBebDataMessage);
//			trigger(broadcastReadRiwcm, bebPort);
			trigger(readBebDataMessage, bebPort);
		}
	};
	
	private ArrayList<TAddress> receiversToArray(){
		
		ArrayList<TAddress> temp = new ArrayList<>();
		for(TAddress addr: receivers)
			temp.add(addr);
		return temp;
	}

//	private Handler<GetOperationRequest> bebDeliver = new Handler<GetOperationRequest>() {
    ClassMatchedHandler<ReadBebDataMessage, TMessage> bebDeliver = new ClassMatchedHandler<ReadBebDataMessage, TMessage>() {

		@Override
		public void handle(ReadBebDataMessage event, TMessage context) {
			LOG.info("\t\tDelivered BEB message {}", event.getR());
			
			RIWCMDataMessage arMessage = new RIWCMDataMessage(self, event.getR(), ts, wr, val);
			trigger(new TMessage(self, context.getSource(), Transport.TCP , arMessage), network);
		}
	};

//	private Handler<ArDataMessage> arDeliver = new Handler<ArDataMessage>() {
    ClassMatchedHandler<RIWCMDataMessage, TMessage> arDeliver = new ClassMatchedHandler<RIWCMDataMessage, TMessage>() {

		@Override
		public void handle(RIWCMDataMessage event, TMessage context) {
			if (event.getR().equals(rid)) {
				readList.add(new ReadObject(event.getTs(), event.getWr(), event.getVal(), event.getR()));
				
				if (readList.size() > (numberOfNodes / 2)) {
					// Sort of readList based on 'ts' or on 'wr'
					Collections.sort(readList, new Comparator<ReadObject>(){

						@Override
						public int compare(ReadObject obj0, ReadObject obj1) {
							if (obj0.getTs().equals(obj1.getTs())) {
								return obj0.getNodeId() < obj1.getNodeId() ? -1 : 1;
							} else if (obj0.getTs() < obj1.getTs()) {
								return -1;
							} else {
								return 1;
							}
						}
						
					});
					ReadObject highest = readList.get(readList.size() - 1);

					rr = new Integer(highest.getWr());
					readVal = new Integer(highest.getVal());
					maxts = new Integer(highest.getTs());

					readList.clear();

					if (reading) {
						// trigger <beb, Broadcast | [Write, rid, maxts, rr,
						// readval]>;
						wBebMsg = new WriteBebDataMessage(self, receiversToArray(), stringValue, rid, maxts, rr, readVal);
						trigger(new BroadcastWriteRiwcm(wBebMsg), bebPort);
					} else {
						// trigger <beb, Broadcast | [Write, rid, maxts + 1,
						// rank(self ), writeval]>;
						wBebMsg = new WriteBebDataMessage(self, receiversToArray(), stringValue, rid, maxts+1, selfId, writeVal);
						trigger(new BroadcastWriteRiwcm(wBebMsg), bebPort);
					}
				}
			}
		}
	};

//	private Handler<ArWriteRequest> writeRequest = new Handler<ArWriteRequest>() {
//
//		@Override
//		public void handle(ArWriteRequest event) {
//			rid++;
//			writeVal = event.getValue();
//			acks = 0;
//			readList.clear();
//			ReadBebDataMessage bebMessage = new ReadBebDataMessage(self, rid);
//			trigger(new BebBroadcast(bebMessage), beb);
//		}
//	};
//
//	private Handler<WriteBebDataMessage> writeValue = new Handler<WriteBebDataMessage>() {
//
//		@Override
//		public void handle(WriteBebDataMessage event) {
//			//How da fuck Java evaluate expressions
//			/*if ((event.getTs() > ts) || (event.getWr() > wr)) {
//				ts = event.getTs();
//				wr = event.getWr();
//				val = event.getVal();
//			}*/
//			if (event.getTs().equals(ts)) {
//				if (event.getWr() > wr) {
//					ts = event.getTs();
//					wr = event.getWr();
//					val = event.getVal();
//				}
//			} else if (event.getTs() > ts) {
//				ts = event.getTs();
//				wr = event.getWr();
//				val = event.getVal();
//			}
//			// trigger ACK
//			//logger.info("Sending ACK to: {}", event.getSource());
//			trigger(new Pp2pSend(event.getSource(), new AckMessage(self,
//					event.getR())), pp2p);
//		}
//	};
//	
//	private Handler<AckMessage> ackHandler = new Handler<AckMessage>() {
//
//		@Override
//		public void handle(AckMessage event) {
//			if (event.getR().equals(rid)) {
//				acks++;
//				//logger.info("Number of ACKs received: {}", acks);
//				if (acks > (numberOfNodes / 2)) {
//					//logger.info("Gathered the required ACKs!");
//					acks = 0;
//					
//					if (reading) {
//						reading = false;
//						ArReadResponse resp = new ArReadResponse(readVal);
//						//logger.info("Sending ReadResponse with value: {}", readVal);
//						trigger(resp, ar);
//					} else {
//						//logger.info("Sending WriteResponse with value");
//						trigger(new ArWriteResponse(), ar);
//					}
//				}
//			}
//		}
//	};
}