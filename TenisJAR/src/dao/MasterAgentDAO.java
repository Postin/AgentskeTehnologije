package dao;

import java.util.ArrayList;
import java.util.List;

import model.MasterAgent;

public class MasterAgentDAO {

	private List<MasterAgent> allMasterAgents = new ArrayList<MasterAgent>();
	private List<MasterAgent> startedMasterAgents = new ArrayList<MasterAgent>();
	private static MasterAgentDAO instance;
	
	public MasterAgentDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<MasterAgent> getAllMasterAgents() {
		return allMasterAgents;
	}

	public void setAllMasterAgents(List<MasterAgent> allMasterAgents) {
		this.allMasterAgents = allMasterAgents;
	}

	public List<MasterAgent> getStartedMasterAgents() {
		return startedMasterAgents;
	}

	public void setStartedMasterAgents(List<MasterAgent> startedMasterAgents) {
		this.startedMasterAgents = startedMasterAgents;
	}

	public static MasterAgentDAO getInstance() {
		if (instance == null) 
			instance = new MasterAgentDAO();
		
		return instance;
	}

	public MasterAgent findByName(String name) {
		for (MasterAgent masterAgent : allMasterAgents) {
			if (masterAgent.getId().getName().equals(name))
				return masterAgent;
		}
		return null;
	}
	
}
