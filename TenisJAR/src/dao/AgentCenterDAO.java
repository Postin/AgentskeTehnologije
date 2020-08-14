package dao;

import java.util.ArrayList;
import java.util.List;

import model.AgentCenter;

public class AgentCenterDAO {

	private List<AgentCenter> agentCenters = new ArrayList<AgentCenter>();
	private static AgentCenterDAO instance;
	
	public AgentCenterDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<AgentCenter> getAgentCenters() {
		return agentCenters;
	}

	public void setAgentCenters(List<AgentCenter> agentCenters) {
		this.agentCenters = agentCenters;
	}

	public static AgentCenterDAO getInstance() {
		if (instance == null)
			instance = new AgentCenterDAO();
		return instance;
	}

	public AgentCenter findByAlias(String alias) {
		for (AgentCenter a : agentCenters) 
			if (a.getAlias().equals(a.getAlias()))
				return a;
		
		return null;
	}
	
	
	
}
