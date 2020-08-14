package dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import model.AID;
import model.AgentCenter;
import model.AgentType;
import model.PredictorAgent;

public class PredictorAgentDAO {
	
	private List<PredictorAgent> allPredictorAgents;
	private List<PredictorAgent> startedPredictorAgents;
	private static PredictorAgentDAO instance;
	private static AgentType agentType = new AgentType("Predictor", "Module");
	
	public PredictorAgentDAO() {
		super();
		// TODO Auto-generated constructor stub
		allPredictorAgents = new ArrayList<PredictorAgent>();
		startedPredictorAgents = new ArrayList<PredictorAgent>();
	}

	public List<PredictorAgent> getAllPredictorAgents() {
		return allPredictorAgents;
	}

	public void setAllPredictorAgents(List<PredictorAgent> allPredictorAgents) {
		this.allPredictorAgents = allPredictorAgents;
	}

	public List<PredictorAgent> getStartedPredictorAgents() {
		return startedPredictorAgents;
	}

	public void setStartedPredictorAgents(List<PredictorAgent> startedPredictorAgents) {
		this.startedPredictorAgents = startedPredictorAgents;
	}

	public static PredictorAgentDAO getInstance() {
		if (instance == null)
			instance = new PredictorAgentDAO();
		
		return instance;
	}

	public PredictorAgent findByName(String name) {
		for (PredictorAgent predictorAgent : allPredictorAgents)
			if (predictorAgent.getId().getName().equals(name))
				return predictorAgent;
		
		return null;
	}

	public void addNewAgent(String name) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getLocalHost();
		AgentCenter host = AgentCenterDAO.getInstance().findByAlias(inetAddress.getHostName());
		AID aid = new AID(name, host, agentType);
		PredictorAgent pa = new PredictorAgent();
		pa.setId(aid);
		allPredictorAgents.add(pa);
		startedPredictorAgents.add(pa);
	}	
	
	

	
}
