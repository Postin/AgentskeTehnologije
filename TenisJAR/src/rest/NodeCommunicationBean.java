package rest;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
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
import dao.CollectorAgentDAO;
import dao.MasterAgentDAO;
import dao.NetworkData;
import dao.PredictorAgentDAO;
import model.AgentCenter;
import model.CollectorAgent;
import model.MasterAgent;
import model.PredictorAgent;
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
		if (!AgentCenterDAO.getInstance().getAgentCenters().contains(ac))
			AgentCenterDAO.getInstance().getAgentCenters().add(ac);
		
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
	@Override
	public void deleteNode(@PathParam("alias") String alias) {
		AgentCenterDAO.getInstance().removeByAlias(alias);
		for (CollectorAgent ca : CollectorAgentDAO.getInstance().getAllCollectorAgents()) {
			if (ca.getId().getHost().getAlias().equals(alias)) {
				CollectorAgentDAO.getInstance().getAllCollectorAgents().remove(ca);
				CollectorAgentDAO.getInstance().getStartedCollectorAgents().remove(ca);
			}
		}
		
		for (PredictorAgent pa : PredictorAgentDAO.getInstance().getAllPredictorAgents()) {
			if (pa.getId().getHost().getAlias().equals(alias)) {
				PredictorAgentDAO.getInstance().getAllPredictorAgents().remove(pa);
				PredictorAgentDAO.getInstance().getStartedPredictorAgents().remove(pa);
			}
		}
		
		for (MasterAgent ma : MasterAgentDAO.getInstance().getAllMasterAgents()) {
			if (ma.getId().getHost().getAlias().equals(alias)) {
				MasterAgentDAO.getInstance().getAllMasterAgents().remove(ma);
				MasterAgentDAO.getInstance().getStartedMasterAgents().remove(ma);
			}
		}
		
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText("Agent center: " + alias + " (" + AgentCenterDAO.getInstance().findByAlias(alias).getAddress() + ")" + "stopped!");
			sender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
				
	}
	
	
	
}
