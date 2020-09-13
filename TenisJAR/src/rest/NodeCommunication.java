package rest;


import java.util.List;

import model.AgentCenter;
import responseModel.AgentTypeClass;
import responseModel.AgentsClass;
import responseModel.ResponseClass;

public interface NodeCommunication {

	public ResponseClass contactMaster(AgentCenter ac);
	
	public AgentsClass allAgents();
	
	public ResponseClass checkNode();
	
	public ResponseClass deleteNode(String alias);
	
	public String sendTypes(List<String> agentTypes);

	public String[] getTypes();
	
	public void testTypes();
}
