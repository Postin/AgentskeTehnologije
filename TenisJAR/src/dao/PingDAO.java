package dao;

import java.util.ArrayList;
import java.util.List;

import model.AID;
import model.AgentCenter;
import model.AgentType;
import model.PingAgent;

public class PingDAO {

	private List<PingAgent> allPingAgents = new ArrayList<PingAgent>();
	private List<PingAgent> startedPingAgents = new ArrayList<PingAgent>();
	private static PingDAO instance;
	private static AgentType agentType = new AgentType("Ping", "Module");
	
	public PingDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<PingAgent> getAllPingAgents() {
		return allPingAgents;
	}

	public void setAllPingAgents(List<PingAgent> allPingAgents) {
		this.allPingAgents = allPingAgents;
	}

	public List<PingAgent> getStartedPingAgents() {
		return startedPingAgents;
	}

	public void setStartedPingAgents(List<PingAgent> startedPingAgents) {
		this.startedPingAgents = startedPingAgents;
	}

	public static PingDAO getInstance() {
		if(instance == null)
			instance = new PingDAO();
		
		return instance;
	}

	public static AgentType getAgentType() {
		return agentType;
	}

	public static void setAgentType(AgentType agentType) {
		PingDAO.agentType = agentType;
	}
	
	public PingAgent findByName(String name) {
		for(PingAgent pingAgent: allPingAgents)
			if(pingAgent.getId().getName().equals(name))
				return pingAgent;
		
		return null;
	}
	
	public void addNewAgent(String name) {
		AgentCenter host = AgentCenterDAO.getInstance().findByNetwork();
		AID aid = new AID(name, host, agentType);
		PingAgent pa = new PingAgent();
		pa.setId(aid);
		allPingAgents.add(pa);
		startedPingAgents.add(pa);
	}
	
	public AID findAID(String name) {
		for(PingAgent pingAgent: allPingAgents)
			if(pingAgent.getId().getName().contains(name))
				return pingAgent.getId();
		return null;
	}
}
