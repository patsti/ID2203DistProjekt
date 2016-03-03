package riwm;

import java.util.ArrayList;
import java.util.HashMap;

import init.InitRIWCM;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.test.TAddress;

public class RIWM extends ComponentDefinition {
	
	private TAddress self;
	private ArrayList<TAddress> replicationGroup;
	
	private int writeTimestamp;
	private int acks;
	private int rid;
	
	HashMap<Integer, ArrayList<TAddress>> readList;
	
	
	
		

	
	
	
	public RIWM(){}
	
	//SIDA 158  Read Impose write majority
	Handler<InitRIWCM> initHandler = new Handler<InitRIWCM>(){
		@Override
		public void handle(InitRIWCM event){
			self = event.getSelf();
			replicationGroup = event.getReplicationGroup();
			
			
		}
	};
	
	//upon event (onar read) do
	
	
	//upon event beb deliver read) do
	
	
	//upon event pl deliver value such that
	
	
	
	
	
}
