package responseModel;

import java.util.ArrayList;
import java.util.List;

import model.AgentType;

public class AgentTypeClass {

	private List<AgentType> agentTypes = new ArrayList<AgentType>();

	public AgentTypeClass() {
		super();
		
	}
	
	public List<AgentType> getAgentTypes() {
		return agentTypes;
	}

	public void setAgentTypes(List<AgentType> agentTypes) {
		this.agentTypes = agentTypes;
	}
	
	
}
