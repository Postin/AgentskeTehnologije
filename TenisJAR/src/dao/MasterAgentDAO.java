package dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import model.AID;
import model.AgentCenter;
import model.AgentType;
import model.MasterAgent;

public class MasterAgentDAO {

	private List<MasterAgent> allMasterAgents = new ArrayList<MasterAgent>();
	private List<MasterAgent> startedMasterAgents = new ArrayList<MasterAgent>();
	private static MasterAgentDAO instance;
	private static AgentType agentType = new AgentType("Master", "Module");
	
	public MasterAgentDAO() {
		super();
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

	public void addNewAgent(String name) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getLocalHost();
		AgentCenter host = AgentCenterDAO.getInstance().findByAlias(inetAddress.getHostName());
		AID aid = new AID(name, host, agentType);
		MasterAgent ma = new MasterAgent();
		ma.setId(aid);
		allMasterAgents.add(ma);
		startedMasterAgents.add(ma);
	}
	
}
