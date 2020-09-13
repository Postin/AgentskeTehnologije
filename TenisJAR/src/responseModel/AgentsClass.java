package responseModel;

import java.util.ArrayList;
import java.util.List;

import model.AgentCenter;
import model.CollectorAgent;
import model.MasterAgent;
import model.PingAgent;
import model.PongAgent;
import model.PredictorAgent;

public class AgentsClass {

	private List<MasterAgent> allMasterAgents = new ArrayList<MasterAgent>();
	private List<MasterAgent> startedMasterAgents = new ArrayList<MasterAgent>();
	private List<CollectorAgent> allCollectorAgents = new ArrayList<CollectorAgent>();
	private List<CollectorAgent> startedCollectorAgents = new ArrayList<CollectorAgent>();
	private List<PredictorAgent> allPredictorAgents = new ArrayList<PredictorAgent>();
	private List<PredictorAgent> startedPredictorAgents = new ArrayList<PredictorAgent>();
	private List<PongAgent>  allPongAgents = new ArrayList<PongAgent>();
	private List<PongAgent> startedPongAgents = new ArrayList<PongAgent>();
	private List<PingAgent> allPingAgents = new ArrayList<PingAgent>();
	private List<PingAgent> startedPingAgents = new ArrayList<PingAgent>();
	private List<AgentCenter> agentCenters = new ArrayList<AgentCenter>();

	
	public AgentsClass() {
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

	public List<AgentCenter> getAgentCenters() {
		return agentCenters;
	}

	public void setAgentCenters(List<AgentCenter> agentCenters) {
		this.agentCenters = agentCenters;
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

	public void setStartedPongAgents(List<PongAgent> startedPongAgents) {
		this.startedPongAgents = startedPongAgents;
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
	
	
	
	
	
	
}
