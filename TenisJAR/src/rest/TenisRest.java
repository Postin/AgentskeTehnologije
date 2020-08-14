package rest;

import java.util.List;

import dto.PerformativeDTO;
import model.ACLMessage;
import model.AID;
import model.AgentType;
import model.StringRequest;


public interface TenisRest {

	public String runAgent(StringRequest sr, String type, String name);
	
	public String stopAgent(AID aid);
	
	public String test();
	
	public String sendACLMessage(ACLMessage aclMessage);
	
	public String addAgent(AID aid);
	
	public List<PerformativeDTO> getPerformatives();

	public List<AID> activeAgents();
	
	public List<AgentType> activeAgentClasses();

	public List<ACLMessage> getMessages();
}
