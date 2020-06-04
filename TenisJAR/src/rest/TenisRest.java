package rest;

import java.util.List;

import model.AID;
import model.AgentType;

public interface TenisRest {

	public String runAgent(String type, String name);
	
	public String stopAgent(AID aid);
	
	public String test();
	
	public List<AID> activeAgents();
	
	public List<AgentType> activeAgentClasses();
}
