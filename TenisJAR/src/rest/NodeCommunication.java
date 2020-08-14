package rest;


import model.AgentCenter;
import responseModel.AgentsClass;
import responseModel.ResponseClass;

public interface NodeCommunication {

	public ResponseClass contactMaster(AgentCenter ac);
	
	public AgentsClass allAgents();
}
