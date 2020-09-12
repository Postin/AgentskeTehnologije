package rest;


import model.AgentCenter;
import responseModel.AgentTypeClass;
import responseModel.AgentsClass;
import responseModel.ResponseClass;

public interface NodeCommunication {

	public ResponseClass contactMaster(AgentCenter ac);
	
	public AgentsClass allAgents();
	
	public ResponseClass checkNode();
	
	public ResponseClass deleteNode(String alias);
	
	public AgentTypeClass sendTypes();

	public AgentTypeClass getTypes(AgentCenter ac);
	
	public void testTypes();
}
