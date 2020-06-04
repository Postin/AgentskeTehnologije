package rest;

import java.util.List;

import model.ACLMessage;

import model.AID;

import model.Performative;

import model.AgentType;


public interface TenisRest {

	public String runAgent(String type, String name);
	
	public String stopAgent(AID aid);
	
	public String test();
	
	public String sendACLMessage(ACLMessage aclMessage);
	
	public List<Performative> getPerformatives();

	public List<AID> activeAgents();
	
	public List<AgentType> activeAgentClasses();

}
