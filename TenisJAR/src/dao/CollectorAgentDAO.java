package dao;

import java.util.ArrayList;
import java.util.List;

import model.CollectorAgent;

public class CollectorAgentDAO {

	private List<CollectorAgent> allCollectorAgents = new ArrayList<CollectorAgent>();
	private List<CollectorAgent> startedCollectorAgents = new ArrayList<CollectorAgent>();
	private static CollectorAgentDAO instance; 
	
	public CollectorAgentDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<CollectorAgent> getAllCollectorAgents() {
		return allCollectorAgents;
	}

	public void setAllCollectorAgents(List<CollectorAgent> allCollectorAgents) {
		this.allCollectorAgents = allCollectorAgents;
	}

	public List<CollectorAgent> getStartedCollectorAgents() {
		return startedCollectorAgents;
	}

	public void setStartedCollectorAgents(List<CollectorAgent> startedCollectorAgents) {
		this.startedCollectorAgents = startedCollectorAgents;
	}

	public static CollectorAgentDAO getInstance() {
		if (instance == null)
			instance = new CollectorAgentDAO();
		
		return instance;
	}

	public CollectorAgent findByName(String name) {
		for (CollectorAgent collectorAgent : allCollectorAgents)
			if (collectorAgent.getId().getName().equals(name))
				return collectorAgent;
		
		return null;
	}	
	
}
