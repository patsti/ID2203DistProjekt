package init;

import se.sics.kompics.Init;
import se.sics.test.NodeParent;

public class InitNode extends Init<NodeParent> {
	private int id;
	
	public InitNode(){}
	
	public InitNode(int id){
		this.id = id;
	}
}	
