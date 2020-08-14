package rest;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import dao.AgentCenterDAO;
import dao.CollectorAgentDAO;
import dao.MasterAgentDAO;
import dao.NetworkData;
import dao.PredictorAgentDAO;
import model.AgentCenter;
import responseModel.AgentsClass;
import responseModel.ResponseClass;

@Stateless
@LocalBean
@Remote(NodeCommunication.class)
@Path("")
public class NodeCommunicationBean implements NodeCommunication {

	@POST
	@Path("/node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public ResponseClass contactMaster(AgentCenter ac) {
		System.out.println("=========== REGISTRATION OF NEW AGENT CENTER ===========");
		if (!AgentCenterDAO.getInstance().getAgentCenters().contains(ac))
			AgentCenterDAO.getInstance().getAgentCenters().add(ac);
		
		if (NetworkData.getInstance().getAddress().equals("192.168.56.1")) {
			System.out.println("*****Usao**********");
			for (AgentCenter a : AgentCenterDAO.getInstance().getAgentCenters()) {
				if (a.getAlias().equals("Master") || a.getAlias().equals(ac.getAlias()))
					continue;
				ResteasyClient client = new ResteasyClientBuilder().build();
            	String http = "http://"+ a.getAddress() +":8080/TenisWAR/rest/node";
            	System.out.println(http);
            	ResteasyWebTarget target = client.target(http);
            	Response response = target.request().post(Entity.entity(ac, "application/json"));
            	ResponseClass ret = response.readEntity(ResponseClass.class);
            	System.out.println(ret.getText());
			}
			AgentsClass agentsClass = new AgentsClass();
			agentsClass.setAgentCenters(AgentCenterDAO.getInstance().getAgentCenters());
			agentsClass.setAllMasterAgents(MasterAgentDAO.getInstance().getAllMasterAgents());
			agentsClass.setAllCollectorAgents(CollectorAgentDAO.getInstance().getAllCollectorAgents());
			agentsClass.setStartedMasterAgents(MasterAgentDAO.getInstance().getStartedMasterAgents());
			agentsClass.setStartedCollectorAgents(CollectorAgentDAO.getInstance().getStartedCollectorAgents());
			agentsClass.setStartedPredictorAgents(PredictorAgentDAO.getInstance().getStartedPredictorAgents());
			
			ResteasyClient client = new ResteasyClientBuilder().build();
        	String http = "http://"+ ac.getAddress() +":8080/TenisWAR/rest/node/allAgents";
        	System.out.println(http);
        	ResteasyWebTarget target = client.target(http);
        	Response response = target.request().post(Entity.entity(agentsClass, "application/json"));
        	ResponseClass ret = response.readEntity(ResponseClass.class);
        	System.out.println(ret.getText());
		} 
		
		System.out.println("Number of agent centers: " + AgentCenterDAO.getInstance().getAgentCenters().size());
		ResponseClass rc = new ResponseClass();
		rc.setText("Agent center " + ac.getAlias() + " (" + ac.getAddress() +") succefully registred");
		return rc;
	}

	@POST
	@Path("/node/allAgents")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public ResponseClass allAgents(AgentsClass agentsClass) {
		System.out.println("=========== ALL RUNNING AGENTS ==============");
		MasterAgentDAO.getInstance().setAllMasterAgents(agentsClass.getAllMasterAgents());
		MasterAgentDAO.getInstance().setStartedMasterAgents(agentsClass.getStartedMasterAgents());
		CollectorAgentDAO.getInstance().setAllCollectorAgents(agentsClass.getAllCollectorAgents());
		CollectorAgentDAO.getInstance().setStartedCollectorAgents(agentsClass.getStartedCollectorAgents());
		PredictorAgentDAO.getInstance().setAllPredictorAgents(agentsClass.getAllPredictorAgents());
		PredictorAgentDAO.getInstance().setStartedPredictorAgents(agentsClass.getStartedPredictorAgents());
		AgentCenterDAO.getInstance().setAgentCenters(agentsClass.getAgentCenters());
		ResponseClass rc = new ResponseClass();
		rc.setText("Agents are sent");
		return rc;
	}

	
}
