package rest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.Path;

import dao.CollectorAgentDAO;
import dao.MasterAgentDAO;
import dao.PredictorAgentDAO;
import model.AID;
import model.AgentCenter;
import model.MasterAgent;
import model.PredictorAgent;
import model.AgentType;
import model.CollectorAgent;

@Startup
@Singleton
@Path("")
@LocalBean
public class StartupBean {
	
	@PostConstruct
	public void init() throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getLocalHost();
		System.out.println(inetAddress.getHostAddress());
		AgentCenter ac = new AgentCenter("Master", inetAddress.getHostAddress());
		AgentType at = new AgentType("Master", "Module");
		AID aid = new AID("Agent master", ac, at);
		MasterAgent agentMaster = new MasterAgent();
		agentMaster.setId(aid);
		MasterAgentDAO.getInstance().getAllMasterAgents().add(agentMaster);
		MasterAgentDAO.getInstance().getStartedMasterAgents().add(agentMaster);
		
		AgentCenter ac1 = new AgentCenter("Sakupljac", inetAddress.getHostAddress());
		AgentType at1 = new AgentType("Sakupljac", "Module");
		AID aid1 = new AID("Sakupljac", ac1, at1);
		AID aid4 = new AID("Sakupljac", ac1, at1);
		CollectorAgent collectorAgent = new CollectorAgent();
		CollectorAgent collectorAgent1 = new CollectorAgent();
		collectorAgent.setId(aid1);
		collectorAgent1.setId(aid4);
		CollectorAgentDAO.getInstance().getAllCollectorAgents().add(collectorAgent);
		CollectorAgentDAO.getInstance().getAllCollectorAgents().add(collectorAgent1);
		
		AgentCenter ac2 = new AgentCenter("Predikator", inetAddress.getHostAddress());
		AgentType at2 = new AgentType("Predikator", "Module");
		AID aid2 = new AID("Predikator", ac2, at2);
		PredictorAgent predictorAgent = new PredictorAgent();
		predictorAgent.setId(aid2);
		PredictorAgentDAO.getInstance().getAllPredictorAgents().add(predictorAgent);
		
		AgentCenter ac3 = new AgentCenter("Predikator1", inetAddress.getHostAddress());
		AID aid3 = new AID("Predikator1", ac3, at2);
		PredictorAgent predictorAgent2 = new PredictorAgent();
		predictorAgent2.setId(aid3);
		PredictorAgentDAO.getInstance().getAllPredictorAgents().add(predictorAgent2);
		PredictorAgentDAO.getInstance().getStartedPredictorAgents().add(predictorAgent2);
		
		System.out.println(MasterAgentDAO.getInstance().getAllMasterAgents().size() + " " + 
						   MasterAgentDAO.getInstance().getStartedMasterAgents().size() + " # " + 
						   CollectorAgentDAO.getInstance().getAllCollectorAgents().size() + " " +
						   CollectorAgentDAO.getInstance().getStartedCollectorAgents().size() + " # " +
						   PredictorAgentDAO.getInstance().getAllPredictorAgents().size() + " " + 
						   PredictorAgentDAO.getInstance().getStartedPredictorAgents().size());
	}

}
