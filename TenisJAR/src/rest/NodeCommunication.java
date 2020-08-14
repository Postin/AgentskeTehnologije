package rest;


import model.AgentCenter;
import responseModel.ResponseClass;

public interface NodeCommunication {

	public ResponseClass contactMaster(AgentCenter ac);
}
