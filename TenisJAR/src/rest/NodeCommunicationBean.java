package rest;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import dao.AgentCenterDAO;
import dao.AgentTypeDAO;
import dao.CollectorAgentDAO;
import dao.MasterAgentDAO;
import dao.NetworkData;
import dao.PredictorAgentDAO;
import model.AgentCenter;
import model.CollectorAgent;
import model.MasterAgent;
import model.PredictorAgent;
import responseModel.AgentTypeClass;
import responseModel.AgentsClass;
import responseModel.ResponseClass;

@Stateless
@LocalBean
@Remote(NodeCommunication.class)
@Path("")
public class NodeCommunicationBean implements NodeCommunication {
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:jboss/exported/jms/queue/mojQueue")
	private Queue queue;

	@POST
	@Path("/node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public ResponseClass contactMaster(AgentCenter ac) {
		System.out.println("=========== REGISTRATION OF NEW AGENT CENTER ===========");
		if (!AgentCenterDAO.getInstance().getAgentCenters().contains(ac)) {
			AgentCenterDAO.getInstance().getAgentCenters().add(ac);
		}
			
		
		if (NetworkData.getInstance().getAddress().equals(NetworkData.MASTER_ADRESS)) {
			System.out.println("***** Usao *****");
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
			
		} 
		
		
		System.out.println("Number of agent centers: " + AgentCenterDAO.getInstance().getAgentCenters().size());
		ResponseClass rc = new ResponseClass();
		rc.setText("Agent center " + ac.getAlias() + " (" + ac.getAddress() +") succefully registred");
		return rc;
	}

	@GET
	@Path("/node/allAgents")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public AgentsClass allAgents() {
		System.out.println("=========== ALL RUNNING AGENTS ==============");
		AgentsClass agentsClass = new AgentsClass();
		agentsClass.setAgentCenters(AgentCenterDAO.getInstance().getAgentCenters());
		agentsClass.setAllMasterAgents(MasterAgentDAO.getInstance().getAllMasterAgents());
		agentsClass.setAllCollectorAgents(CollectorAgentDAO.getInstance().getAllCollectorAgents());
		agentsClass.setStartedMasterAgents(MasterAgentDAO.getInstance().getStartedMasterAgents());
		agentsClass.setStartedCollectorAgents(CollectorAgentDAO.getInstance().getStartedCollectorAgents());
		agentsClass.setStartedPredictorAgents(PredictorAgentDAO.getInstance().getStartedPredictorAgents());
		return agentsClass;
	}

	@GET
	@Path("/node")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public ResponseClass checkNode() {
		System.out.println("======== CHECK NODE =========");
		return new ResponseClass("Still alive");
	}
	
	@DELETE
	@Path("/node/{alias}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public ResponseClass deleteNode(@PathParam("alias") String alias) {
		for (int i = 0; i < AgentCenterDAO.getInstance().getAgentCenters().size(); i++) {
			if (AgentCenterDAO.getInstance().getAgentCenters().get(i).getAlias().equals(alias)) {
				System.out.println("Found: " + AgentCenterDAO.getInstance().getAgentCenters().get(i).getAddress());
				AgentCenterDAO.getInstance().getAgentCenters().remove(i);
			}
		}
		
		for (int i = 0; i < CollectorAgentDAO.getInstance().getAllCollectorAgents().size(); i++) {
			CollectorAgent ca = CollectorAgentDAO.getInstance().getAllCollectorAgents().get(i);
			if (ca.getId().getHost().getAlias().equals(alias))
				CollectorAgentDAO.getInstance().getAllCollectorAgents().remove(i);
		}
		
		for (int i = 0; i < CollectorAgentDAO.getInstance().getStartedCollectorAgents().size(); i++) {
			CollectorAgent ca = CollectorAgentDAO.getInstance().getStartedCollectorAgents().get(i);
			if (ca.getId().getHost().getAlias().equals(alias))
				CollectorAgentDAO.getInstance().getStartedCollectorAgents().remove(i);
		}
		
		for (int i = 0; i < PredictorAgentDAO.getInstance().getAllPredictorAgents().size(); i++) {
			PredictorAgent pa = PredictorAgentDAO.getInstance().getAllPredictorAgents().get(i);
			if (pa.getId().getHost().getAlias().equals(alias)) {
				PredictorAgentDAO.getInstance().getAllPredictorAgents().remove(i);
			}
		}
		
		for (int i = 0; i < PredictorAgentDAO.getInstance().getStartedPredictorAgents().size(); i++) {
			PredictorAgent pa = PredictorAgentDAO.getInstance().getStartedPredictorAgents().get(i);
			if (pa.getId().getHost().getAlias().equals(alias)) {
				PredictorAgentDAO.getInstance().getStartedPredictorAgents().remove(i);
			}
		}
		
		for (int i = 0; i < MasterAgentDAO.getInstance().getAllMasterAgents().size(); i++) {
			MasterAgent ma = MasterAgentDAO.getInstance().getAllMasterAgents().get(i);
			if (ma.getId().getHost().getAlias().equals(alias)) {
				MasterAgentDAO.getInstance().getAllMasterAgents().remove(i);
			}
		}
		
		for (int i = 0; i < MasterAgentDAO.getInstance().getStartedMasterAgents().size(); i++) {
			MasterAgent ma = MasterAgentDAO.getInstance().getStartedMasterAgents().get(i);
			if (ma.getId().getHost().getAlias().equals(alias)) {
				MasterAgentDAO.getInstance().getStartedMasterAgents().remove(i);
			}
		}
		
		return new ResponseClass("Deleted");
				
	}
	
	@POST
	@Path("/agent/classes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String sendTypes(List<String> agentTypes) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		System.out.println("GETAGENTS");
		for(int i = 0; i < AgentCenterDAO.getInstance().getAgentCenters().size(); i++) {
			System.out.println("Entered for");
			AgentCenter ac = AgentCenterDAO.getInstance().getAgentCenters().get(i);
	    	String http = "http://"+ ac.getAddress() +":8080/TenisWAR/rest/agent/classes";
	    	System.out.println(http);
	    	ResteasyWebTarget target = client.target(http);
	    	Response response = target.request(MediaType.APPLICATION_JSON).get();
	    	String[] ret = response.readEntity(String[].class);
	    	System.out.println(ret);
			
		}
		return "Success";
	}
	
	@GET
	@Path("/agent/classes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String[] getTypes() {
		System.out.println("ENTERED!!!!");
		String[] ret = new String[AgentTypeDAO.getInstance().getAgentTypes().size()];
		for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size(); i++) {
			ret[i] = AgentTypeDAO.getInstance().getAgentTypes().get(i);
		}
		return ret;
	}
	
	@GET
	@Path("/node/test")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public void testTypes() {
		for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size(); i++) {
			System.out.println(AgentTypeDAO.getInstance().getAgentTypes().get(i));
		}
		
	}
	
	
}
