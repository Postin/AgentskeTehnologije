package dao;

import java.util.ArrayList;
import java.util.List;

import model.AID;
import model.AgentCenter;
import model.AgentType;
import model.PongAgent;

public class PongDAO {

	private List<PongAgent> allPongAgents = new ArrayList<PongAgent>();
	private List<PongAgent> startedPongAgents = new ArrayList<PongAgent>();
	private static PongDAO instance;
	private static AgentType agentType = new AgentType("Pong", "Module");
	
	public PongDAO() {
		super();
	}

	public List<PongAgent> getAllPongAgents() {
		return allPongAgents;
	}

	public void setAllPongAgents(List<PongAgent> allPongAgents) {
		this.allPongAgents = allPongAgents;
	}

	public List<PongAgent> getStartedPongAgents() {
		return startedPongAgents;
	}

	public void setStartedPongAgents(List<PongAgent> startedPongAgnets) {
		this.startedPongAgents = startedPongAgnets;
	}

	public static PongDAO getInstance() {
		if(instance == null)
			instance = new PongDAO();
		return instance;
	}



	public static AgentType getAgentType() {
		return agentType;
	}

	public static void setAgentType(AgentType agentType) {
		PongDAO.agentType = agentType;
	}
	
	public PongAgent findByName(String name) {
		for(PongAgent pongAgent: allPongAgents) {
			if(pongAgent.getId().getName().equals(name))
				return pongAgent;
		}
		
		return null;
	}
	
	public void addNewAgent(String name) {
		AgentCenter host = AgentCenterDAO.getInstance().findByNetwork();
		AID aid = new AID(name, host, agentType);
		PongAgent pa = new PongAgent();
		pa.setId(aid);
		allPongAgents.add(pa);
		startedPongAgents.add(pa);
		System.out.println(startedPongAgents.size());
	}
	
	public AID findAID(String name) {
		for(PongAgent pongAgent : allPongAgents)
			if(pongAgent.getId().getName().contains(name))
				return pongAgent.getId();
		return null;
	}
}
