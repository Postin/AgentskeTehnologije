package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.CollectorAgentDAO;
import dao.MasterAgentDAO;
import dao.MessageDAO;
import dao.PredictorAgentDAO;
import model.ACLMessage;
import model.AID;
import model.AgentType;
import model.CollectorAgent;
import model.MasterAgent;
import model.Performative;
import model.PredictorAgent;

@Stateless
@LocalBean
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Remote(TenisRest.class)
@Path("/agents")
public class TenisRestBean implements TenisRest {
	
	@GET
	@Path("/running")
	@Override
	public List<AID> activeAgents() {
		List<CollectorAgent> collectorAgents = CollectorAgentDAO.getInstance().getStartedCollectorAgents();
		List<MasterAgent> masterAgents = MasterAgentDAO.getInstance().getStartedMasterAgents();
		List<PredictorAgent> predictorAgents = PredictorAgentDAO.getInstance().getAllPredictorAgents();
		
		List<AID> activeAgents = new ArrayList<AID>();
		
		for(CollectorAgent ca: collectorAgents) {
			activeAgents.add(ca.getId());
		}
		
		for(MasterAgent ma: masterAgents) {
			activeAgents.add(ma.getId());
		}
		
		for(PredictorAgent pa: predictorAgents) {
			activeAgents.add(pa.getId());
		}
		
		return activeAgents;
	}
	
	@GET
	@Path("/classes")
	@Override
	public List<AgentType> activeAgentClasses() {
		List<AgentType> agentClasses = new ArrayList<AgentType>();
		
		if(!CollectorAgentDAO.getInstance().getStartedCollectorAgents().equals(null)) {
			agentClasses.add(CollectorAgentDAO.getInstance().getAllCollectorAgents().get(0).getId().getType());
		}
		
		if(!MasterAgentDAO.getInstance().getStartedMasterAgents().equals(null)) {
			agentClasses.add(MasterAgentDAO.getInstance().getAllMasterAgents().get(0).getId().getType());
		}
		
		if(!PredictorAgentDAO.getInstance().getStartedPredictorAgents().equals(null)) {
			agentClasses.add(PredictorAgentDAO.getInstance().getAllPredictorAgents().get(0).getId().getType());
		}
		
		return agentClasses;
	}

	@PUT
	@Path("/running/{type}/{name}")
	@Override
	public String runAgent(@PathParam("type") String type, @PathParam("name") String name) {
		// TODO Auto-generated method stub
		if (type.equals("MASTER")) {
			MasterAgent masterAgent = MasterAgentDAO.getInstance().findByName(name);
			if (masterAgent != null && !MasterAgentDAO.getInstance().getStartedMasterAgents().contains(masterAgent)) {
				MasterAgentDAO.getInstance().getStartedMasterAgents().add(masterAgent);
				return "Success";
			}
			else if (MasterAgentDAO.getInstance().getStartedMasterAgents().contains(masterAgent)) {
				return "Already running";
			}
			else 
				return "Error";
		}
		else if (type.equals("COLLECTOR")) {
			CollectorAgent collectorAgent = CollectorAgentDAO.getInstance().findByName(name);
			if (collectorAgent != null && !CollectorAgentDAO.getInstance().getStartedCollectorAgents().contains(collectorAgent)) {
				CollectorAgentDAO.getInstance().getStartedCollectorAgents().add(collectorAgent);
				return "Success";
			}
			else if (CollectorAgentDAO.getInstance().getStartedCollectorAgents().contains(collectorAgent)) {
				return "Already running";
			}
			else 
				return "Error";
		}
		else if (type.equals("PREDICTOR")) {
			PredictorAgent predictorAgent = PredictorAgentDAO.getInstance().findByName(name);
			if (predictorAgent != null && !PredictorAgentDAO.getInstance().getStartedPredictorAgents().contains(predictorAgent)) {
				PredictorAgentDAO.getInstance().getStartedPredictorAgents().add(predictorAgent);
				return "Success";
			}
			else if (PredictorAgentDAO.getInstance().getStartedPredictorAgents().contains(predictorAgent)) {
				return "Already running";
			}
			else 
				return "Error";
		}
		return "Error";
	}

	@DELETE
	@Path("/running")
	@Override
	public String stopAgent(AID aid) {
		// TODO Auto-generated method stub
		if (aid.getType().getName().equals("Master")) {
			MasterAgent masterAgent = MasterAgentDAO.getInstance().findByName(aid.getName());
			if (masterAgent != null) {
				boolean stopped = MasterAgentDAO.getInstance().getStartedMasterAgents().remove(masterAgent);
				if (stopped)
					return "Stopped";
				else 
					return "Already stopped";
			}
			else {
				return "Error";
			}
		}
		else if (aid.getType().getName().equals("Predikator")) {
			PredictorAgent predictorAgent = PredictorAgentDAO.getInstance().findByName(aid.getName());
			if (predictorAgent != null) {
				boolean stopped = PredictorAgentDAO.getInstance().getStartedPredictorAgents().remove(predictorAgent);
				if (stopped)
					return "Stopped";
				else 
					return "Already stopped";
			}
			else {
				return "Error";
			}
		}
		else if (aid.getType().getName().equals("Sakupljac")) {
			System.out.println("####");
			CollectorAgent collectorAgent = CollectorAgentDAO.getInstance().findByName(aid.getName());
			System.out.println(collectorAgent.getId().getName());
			if (collectorAgent != null) {
				boolean stopped = CollectorAgentDAO.getInstance().getStartedCollectorAgents().remove(collectorAgent);
				if (stopped)
					return "Stopped";
				else 
					return "Already stopped";
			}
			
		}
		return "Error";
	}
	
	@POST
	@Path("/messages")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public String sendACLMessage(ACLMessage aclMessage) {
		ACLMessage m = new ACLMessage();
		m.setContent(aclMessage.getContent());
		m.setContentObj(aclMessage.getContentObj());
		m.setConversationId(aclMessage.getConversationId());
		m.setEncoding(aclMessage.getEncoding());
		m.setInReplyTo(aclMessage.getInReplyTo());
		m.setLanguage(aclMessage.getLanguage());
		m.setOntology(aclMessage.getOntology());
		m.setPerformative(aclMessage.getPerformative());
		m.setProtocol(aclMessage.getProtocol());
		m.setReceivers(aclMessage.getReceivers());
		m.setReplyBy(aclMessage.getReplyBy());
		m.setReplyTo(aclMessage.getReplyTo());
		m.setReplyWith(aclMessage.getReplyWith());
		m.setSender(aclMessage.getSender());
		m.setUserArgs(aclMessage.getUserArgs());
		MessageDAO.getInstance().getAllMessages().add(m);
		
		return "Message sent";
	}

	@GET
	@Path("/messages")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<Performative> getPerformatives(){
		List<Performative> performatives = new ArrayList<Performative>();
		Performative[] performativeValues = Performative.values();
		
		for(Performative p : performativeValues)
			performatives.add(p);
		
		
		return performatives;
		
	}
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String test() {
		System.out.println(MasterAgentDAO.getInstance().getAllMasterAgents().size() + " " + 
				   MasterAgentDAO.getInstance().getStartedMasterAgents().size() + " # " + 
				   CollectorAgentDAO.getInstance().getAllCollectorAgents().size() + " " +
				   CollectorAgentDAO.getInstance().getStartedCollectorAgents().size() + " # " +
				   PredictorAgentDAO.getInstance().getAllPredictorAgents().size() + " " + 
				   PredictorAgentDAO.getInstance().getStartedPredictorAgents().size());
		return "test";
	}

}
