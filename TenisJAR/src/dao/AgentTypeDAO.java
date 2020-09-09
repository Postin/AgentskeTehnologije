package dao;

import java.util.ArrayList;
import java.util.List;

import model.AgentType;

public class AgentTypeDAO {
	
	private List<String> agentTypes = new ArrayList<String>();
	private static AgentTypeDAO instance;
	
	public AgentTypeDAO() {
		super();
		agentTypes.add("Master");
	}

	public static AgentTypeDAO getInstance() {
		if(instance == null)
			instance = new AgentTypeDAO();
		return instance;
	}


	public void setAgentTypes (List<String> agentTypes) {
		this.agentTypes = agentTypes;
	}

	public List<String> getAgentTypes() {
		return agentTypes;
	}



	
	
	
}
