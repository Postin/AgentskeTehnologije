package rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Schedule;
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
import javax.ws.rs.PUT;
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
import dao.MessageDAO;
import dao.NetworkData;
import dao.PingDAO;
import dao.PongDAO;
import dao.PredictorAgentDAO;
import dto.PerformativeDTO;
import model.ACLMessage;
import model.AID;
import model.AgentCenter;
import model.AgentType;
import model.CollectorAgent;
import model.MasterAgent;
import model.Performative;
import model.PingAgent;
import model.PongAgent;
import model.PredictorAgent;
import model.StringRequest;
import responseModel.ResponseClass;

@Stateless
@LocalBean
@Remote(TenisRest.class)
@Path("/agents")
public class TenisRestBean implements TenisRest {
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:jboss/exported/jms/queue/mojQueue")
	private Queue queue;
	
	@GET
	@Path("/running")
	@Override
	public List<AID> activeAgents() {
		List<CollectorAgent> collectorAgents = CollectorAgentDAO.getInstance().getStartedCollectorAgents();
		List<MasterAgent> masterAgents = MasterAgentDAO.getInstance().getStartedMasterAgents();
		List<PredictorAgent> predictorAgents = PredictorAgentDAO.getInstance().getStartedPredictorAgents();
		List<PingAgent> pingAgents = PingDAO.getInstance().getStartedPingAgents();
		List<PongAgent> pongAgents = PongDAO.getInstance().getStartedPongAgents();
		
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
		
		for(PingAgent pa: pingAgents) {
			activeAgents.add(pa.getId());
		}
		
		for(PongAgent pa : pongAgents) {
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
		
		if(!PingDAO.getInstance().getStartedPingAgents().equals(null))
			agentClasses.add(PingDAO.getInstance().getAllPingAgents().get(0).getId().getType());
		
		if(!PongDAO.getInstance().getStartedPongAgents().equals(null))
			agentClasses.add(PongDAO.getInstance().getAllPongAgents().get(0).getId().getType());
		
		return agentClasses;
	}

	@PUT
	@Path("/running/{type}/{name}")
	@Override
	public String runAgent(StringRequest sr, @PathParam("type") String type, @PathParam("name") String name) {
		String retVal = "";
		AID aid = null;
		if (type.equals("Master")) {
			MasterAgent masterAgent = MasterAgentDAO.getInstance().findByName(name);
			if (masterAgent != null && !MasterAgentDAO.getInstance().getStartedMasterAgents().contains(masterAgent)) {
				MasterAgentDAO.getInstance().getStartedMasterAgents().add(masterAgent);
				try {
					QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
					QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
					QueueSender sender = session.createSender(queue);
					// create and publish a message
					TextMessage mess = session.createTextMessage();
					mess.setText("Agent: " + type + " (" + name + ")" + "started!");
					sender.send(mess);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				retVal = "Success";
			}
			else if (MasterAgentDAO.getInstance().getStartedMasterAgents().contains(masterAgent)) {
				retVal = "Already running";
			}
			else {
				MasterAgentDAO.getInstance().addNewAgent(name);
				retVal = "Agent with this data does not exist, so we created a new master agent";
			}
			aid = MasterAgentDAO.getInstance().findAID(name);
		}
		else if (type.equals("Collector")) {
			CollectorAgent collectorAgent = CollectorAgentDAO.getInstance().findByName(name);
			if (collectorAgent != null && !CollectorAgentDAO.getInstance().getStartedCollectorAgents().contains(collectorAgent)) {
				CollectorAgentDAO.getInstance().getStartedCollectorAgents().add(collectorAgent);
				try {
					QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
					QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
					QueueSender sender = session.createSender(queue);
					// create and publish a message
					TextMessage mess = session.createTextMessage();
					mess.setText("Agent: " + type + " " + name + ")" + "started!");
					sender.send(mess);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				retVal = "Success";
			}
			else if (CollectorAgentDAO.getInstance().getStartedCollectorAgents().contains(collectorAgent)) {
				retVal = "Already running";
			}
			else{
				CollectorAgentDAO.getInstance().newAgent(name);
				retVal = "Agent with this data does not exist, so we created a new collector agent";
			}
			aid = CollectorAgentDAO.getInstance().findAID(name);
		}
		else if (type.equals("Predictor")) {
			PredictorAgent predictorAgent = PredictorAgentDAO.getInstance().findByName(name);
			if (predictorAgent != null && !PredictorAgentDAO.getInstance().getStartedPredictorAgents().contains(predictorAgent)) {
				PredictorAgentDAO.getInstance().getStartedPredictorAgents().add(predictorAgent);
				try {
					QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
					QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
					QueueSender sender = session.createSender(queue);
					// create and publish a message
					TextMessage mess = session.createTextMessage();
					mess.setText("Agent: " + type + " " + name + ")" + "started!");
					sender.send(mess);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				retVal = "Success";
			}
			else if (PredictorAgentDAO.getInstance().getStartedPredictorAgents().contains(predictorAgent)) {
				retVal = "Already running";
			}
			else{
				PredictorAgentDAO.getInstance().addNewAgent(name);
				retVal = "Agent with this data does not exist, so we created a new predictor agent";
			}
			aid = PredictorAgentDAO.getInstance().findAID(name);
		} 
		else if(type.equals("Pong")){
			PongAgent pongAgent = PongDAO.getInstance().findByName(name);
			if(pongAgent != null && !PongDAO.getInstance().getStartedPongAgents().contains(pongAgent)) {
				PongDAO.getInstance().getStartedPongAgents().add(pongAgent);
				try {
					QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
					QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
					QueueSender sender = session.createSender(queue);
					// create and publish a message
					TextMessage mess = session.createTextMessage();
					mess.setText("Agent: " + type + " " + name + ")" + "started!");
					sender.send(mess);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				retVal = "Success";
			}
			else if (PongDAO.getInstance().getStartedPongAgents().contains(pongAgent)) {
				retVal = "Already running";
			}
			else{
				PongDAO.getInstance().addNewAgent(name);
				retVal = "Agent with this data does not exist, so we created a new predictor agent";
			}
			aid = PongDAO.getInstance().findAID(name);
		}
		else if(type.equals("Ping")) {
			PingAgent pingAgent = PingDAO.getInstance().findByName(name);
			if(pingAgent != null && !PingDAO.getInstance().getStartedPingAgents().contains(pingAgent)) {
				PingDAO.getInstance().getStartedPingAgents().add(pingAgent);
				try {
					QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
					QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
					QueueSender sender = session.createSender(queue);
					// create and publish a message
					TextMessage mess = session.createTextMessage();
					mess.setText("Agent: " + type + " " + name + ")" + "started!");
					sender.send(mess);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				retVal = "Success";
			}
			else if (PingDAO.getInstance().getStartedPingAgents().contains(pingAgent)) {
				retVal = "Already running";
			}
			else{
				PingDAO.getInstance().addNewAgent(name);
				retVal = "Agent with this data does not exist, so we created a new predictor agent";
			}
			aid = PingDAO.getInstance().findAID(name);
		}
		else {
			return "Something went wrong";
		}
		
		if (aid != null) {
			for (AgentCenter a : AgentCenterDAO.getInstance().getAgentCenters()) {
				ResteasyClient client = new ResteasyClientBuilder().build();
		    	String http = "http://"+ a.getAddress() +":8080/TenisWAR/rest/agents/running";
		    	System.out.println(http);
		    	ResteasyWebTarget target = client.target(http);
		    	Response response = target.request().post(Entity.entity(aid, "application/json"));
		    	String ret = response.readEntity(String.class);
		    	System.out.println(ret);
			}
		}
		
		return retVal;
	}

	@DELETE
	@Path("/running")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String stopAgent(AID aid) {
		String retVal = "";
		if (aid.getType().getName().equals("Master")) {
			MasterAgent masterAgent = MasterAgentDAO.getInstance().findByName(aid.getName());
			if (masterAgent != null) {
				boolean stopped = MasterAgentDAO.getInstance().getStartedMasterAgents().remove(masterAgent);
				if (stopped) {
					try {
						QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
						QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
						QueueSender sender = session.createSender(queue);
						// create and publish a message
						TextMessage mess = session.createTextMessage();
						mess.setText("Agent: " + aid.getType().getName() + " " + aid.getName() 
									+ "(" + aid.getHost().getAddress() + ")" + "stopped!");
						sender.send(mess);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					retVal = "Stopped";
				}
					
				else 
					retVal = "Already stopped";
			}
			else {
				
				return "Error1";
			}
		}
		else if (aid.getType().getName().equals("Predictor")) {
			PredictorAgent predictorAgent = PredictorAgentDAO.getInstance().findByName(aid.getName());
			if (predictorAgent != null) {
				boolean stopped = PredictorAgentDAO.getInstance().getStartedPredictorAgents().remove(predictorAgent);
				if (stopped) {
					try {
						QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
						QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
						QueueSender sender = session.createSender(queue);
						// create and publish a message
						TextMessage mess = session.createTextMessage();
						mess.setText("Agent: " + aid.getType().getName() + " " + aid.getName() 
									+ "(" + aid.getHost().getAddress() + ")" + "stopped!");
						sender.send(mess);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					retVal = "Stopped";
				}
					
				else 
					retVal = "Already stopped";
			}
			else {
				return "Error2";
			}
		}
		else if (aid.getType().getName().equals("Collector")) {
			System.out.println("####");
			CollectorAgent collectorAgent = CollectorAgentDAO.getInstance().findByName(aid.getName());
			System.out.println(collectorAgent.getId().getName());
			if (collectorAgent != null) {
				boolean stopped = CollectorAgentDAO.getInstance().getStartedCollectorAgents().remove(collectorAgent);
				if (stopped) {
					try {
						QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
						QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
						QueueSender sender = session.createSender(queue);
						// create and publish a message
						TextMessage mess = session.createTextMessage();
						mess.setText("Agent: " + aid.getType().getName() + " " + aid.getName() 
									+ "(" + aid.getHost().getAddress() + ")" + "stopped!");
						sender.send(mess);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					retVal = "Stopped";
				}
				else 
					retVal = "Already stopped";
			}
		}else if(aid.getType().getName().equals("Ping")) {
			System.out.println("####");
			PingAgent pingAgent = PingDAO.getInstance().findByName(aid.getName());
			System.out.println(pingAgent.getId().getName());
			if (pingAgent != null) {
				boolean stopped = PingDAO.getInstance().getStartedPingAgents().remove(pingAgent);
				if (stopped) {
					try {
						QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
						QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
						QueueSender sender = session.createSender(queue);
						// create and publish a message
						TextMessage mess = session.createTextMessage();
						mess.setText("Agent: " + aid.getType().getName() + " " + aid.getName() 
									+ "(" + aid.getHost().getAddress() + ")" + "stopped!");
						sender.send(mess);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					retVal = "Stopped";
				}
				else 
					retVal = "Already stopped";
			}
			
		}else if(aid.getType().getName().equals("Pong")) {
			System.out.println("####");
			PongAgent pongAgent = PongDAO.getInstance().findByName(aid.getName());
			System.out.println(pongAgent.getId().getName());
			if (pongAgent != null) {
				boolean stopped = PongDAO.getInstance().getStartedPongAgents().remove(pongAgent);
				if (stopped) {
					try {
						QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
						QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
						QueueSender sender = session.createSender(queue);
						// create and publish a message
						TextMessage mess = session.createTextMessage();
						mess.setText("Agent: " + aid.getType().getName() + " " + aid.getName() 
									+ "(" + aid.getHost().getAddress() + ")" + "stopped!");
						sender.send(mess);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					retVal = "Stopped";
				}
				else 
					retVal = "Already stopped";
			}
		}
		else {
			return "Error4";
		}
		for (AgentCenter a : AgentCenterDAO.getInstance().getAgentCenters()) {
			ResteasyClient client = new ResteasyClientBuilder().build();
	    	String http = "http://"+ a.getAddress() +":8080/TenisWAR/rest/agents/deleted";
	    	System.out.println(http);
	    	ResteasyWebTarget target = client.target(http);
	    	Response response = target.request("application/json").build("DELETE", Entity.entity(aid, "application/json")).invoke();
	    	ResponseClass ret = response.readEntity(ResponseClass.class);
	    	System.out.println(ret.getText());
		}
		return retVal;
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
		m.setReplyBy(aclMessage.getReplyBy());
		m.setReplyTo(aclMessage.getReplyTo());
		m.setReplyWith(aclMessage.getReplyWith());
		
	
		AID sender = aclMessage.getSender();
		List<AID> receivers = new ArrayList<AID>();
		
		//List<AID> activeAgents = new ArrayList<AID>();
		
		List<CollectorAgent> collectorAgents = CollectorAgentDAO.getInstance().getStartedCollectorAgents();
		List<MasterAgent> masterAgents = MasterAgentDAO.getInstance().getStartedMasterAgents();
		List<PredictorAgent> predictorAgents = PredictorAgentDAO.getInstance().getStartedPredictorAgents();
		List<PongAgent> pongAgents = PongDAO.getInstance().getStartedPongAgents();
		List<PingAgent> pingAgents = PingDAO.getInstance().getStartedPingAgents();
		
		for(CollectorAgent a : collectorAgents) {
			if(a.getId().getName().equals(sender.getName()))
			{
				m.setSender(a.getId());
			}
			
			for(int i = 0; i < aclMessage.getReceivers().length; i++) {
				if(a.getId().getName().equals(aclMessage.getReceivers()[i].getName())) {
					receivers.add(a.getId());
				}
			}
		}
		for(MasterAgent a: masterAgents) {
			if(a.getId().getName().equals(sender.getName()))
			{
				m.setSender(a.getId());
			}
			for(int i = 0; i < aclMessage.getReceivers().length; i++) {
				if(a.getId().getName().equals(aclMessage.getReceivers()[i].getName())) {
					receivers.add(a.getId());
				}
			}
		}
		for(PredictorAgent a: predictorAgents) {
			if(a.getId().getName().equals(sender.getName()))
			{
				m.setSender(a.getId());
			}
			for(int i = 0; i < aclMessage.getReceivers().length; i++) {
				if(a.getId().getName().equals(aclMessage.getReceivers()[i].getName())) {
					receivers.add(a.getId());
				}
			}
		}
		
		for(PongAgent a : pongAgents) {
			if(a.getId().getName().equals(sender.getName()))
				m.setSender(a.getId());
			
			for(int i = 0; i < aclMessage.getReceivers().length; i++)
				if(a.getId().getName().equals(aclMessage.getReceivers()[i].getName()))
					receivers.add(a.getId());
		}
		
		for(PingAgent a : pingAgents) {
			if(a.getId().getName().equals(sender.getName()))
				m.setSender(a.getId());
			
			for(int i = 0; i <aclMessage.getReceivers().length; i++)
				if(a.getId().getName().equals(aclMessage.getReceivers()[i].getName()))
					receivers.add(a.getId());
		}
		
		AID[] r = new AID[receivers.size()];
		
		for(int i = 0; i < receivers.size(); i++) {
			r[i] = receivers.get(i);
		}
		m.setReceivers(r);
		
		m.setUserArgs(aclMessage.getUserArgs());
		MessageDAO.getInstance().getAllMessages().add(m);
		
		System.out.println(m.toString());
		
		
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender qsender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText("Message with content " + m.getContent() + " sent");
			qsender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return "Message sent";
	}

	@GET
	@Path("/messages")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<PerformativeDTO> getPerformatives(){
		List<PerformativeDTO> performatives = new ArrayList<PerformativeDTO>();
		Performative[] performativeValues = Performative.values();
		
		for(Performative p : performativeValues)
			performatives.add(new PerformativeDTO(p));
		
		
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
	
	@GET
	@Path("/inbox")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<ACLMessage> getMessages(){
		

	/*	try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender qsender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText("Message");
			qsender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}*/
		
		return MessageDAO.getInstance().getAllMessages();
	}

	@POST
	@Path("/running")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String addAgent(AID aid) {
		System.out.println("============= STARTED AGENT =============");
		if (aid.getType().getName().equals("Master")) {
			MasterAgent ma = MasterAgentDAO.getInstance().findByName(aid.getName());
			if (ma == null) {
				ma = new MasterAgent();
				ma.setId(aid);
				MasterAgentDAO.getInstance().getAllMasterAgents().add(ma);
			}
			if (!MasterAgentDAO.getInstance().getStartedMasterAgents().contains(ma)) {
				MasterAgentDAO.getInstance().getStartedMasterAgents().add(ma);
			}
			
			boolean found = false;
			for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size(); i++) {
				if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Master")) {
					found = true;
					break;
				}
			}
			
			if(!found)
				AgentTypeDAO.getInstance().getAgentTypes().add("Master");
		}
		else if (aid.getType().getName().equals("Collector")) {
			CollectorAgent ca = CollectorAgentDAO.getInstance().findByName(aid.getName());
			if (ca == null) {
				ca = new CollectorAgent();
				ca.setId(aid);
				CollectorAgentDAO.getInstance().getAllCollectorAgents().add(ca);
			}
			if (!CollectorAgentDAO.getInstance().getStartedCollectorAgents().contains(ca)) {
				CollectorAgentDAO.getInstance().getStartedCollectorAgents().add(ca);
			}
			boolean found = false;
			for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size(); i++) {
				if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Collector")) {
					found = true;
					break;
				}
			}
			
			if(!found)
				AgentTypeDAO.getInstance().getAgentTypes().add("Collector");
		}
		else if (aid.getType().getName().equals("Predictor")) {
			PredictorAgent pa = PredictorAgentDAO.getInstance().findByName(aid.getName());
			if (pa == null) {
				pa = new PredictorAgent();
				pa.setId(aid);
				PredictorAgentDAO.getInstance().getAllPredictorAgents().add(pa);
			}
			if (!PredictorAgentDAO.getInstance().getStartedPredictorAgents().contains(pa)) {
				PredictorAgentDAO.getInstance().getStartedPredictorAgents().add(pa);
			}
			boolean found = false;
			for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size(); i++) {
				if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Predictor")) {
					found = true;
					break;
				}
			}
			
			if(!found)
				AgentTypeDAO.getInstance().getAgentTypes().add("Predictor");
		}else if(aid.getType().getName().equals("Ping")) {
			PingAgent pa = PingDAO.getInstance().findByName(aid.getName());
			if(pa == null) {
				pa = new PingAgent();
				pa.setId(aid);
				PingDAO.getInstance().getStartedPingAgents().add(pa);
			}
			if(!PingDAO.getInstance().getStartedPingAgents().contains(pa)) {
				PingDAO.getInstance().getStartedPingAgents().add(pa);
			}
			boolean found = false;
			for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size();i++) {
				if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Ping")) {
					found = true;
					break;
				}
			}
			if(!found)
				AgentTypeDAO.getInstance().getAgentTypes().add("Ping");
		}else if(aid.getType().getName().equals("Pong")) {
			PongAgent pa = PongDAO.getInstance().findByName(aid.getName());
			if(pa == null) {
				pa = new PongAgent();
				pa.setId(aid);
				PongDAO.getInstance().getStartedPongAgents().add(pa);
			}
			if(!PongDAO.getInstance().getStartedPongAgents().contains(pa)) {
				PongDAO.getInstance().getStartedPongAgents().add(pa);
			}
			boolean found = false;
			for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size();i++) {
				if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Pong")) {
					found = true;
					break;
				}
			}
			if(!found)
				AgentTypeDAO.getInstance().getAgentTypes().add("Pong");
		}
		else 
			return null;
		
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender qsender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText("There is a new agent");
			qsender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "Successfully added";
	}

	@DELETE
	@Path("/deleted")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public ResponseClass stopedAgent(AID aid) {
		System.out.println("============== STOPPED AGENT ===============");
		if (aid.getType().getName().equals("Master")) {
			MasterAgent masterAgent = MasterAgentDAO.getInstance().findByName(aid.getName());
			if (masterAgent != null)
				MasterAgentDAO.getInstance().getStartedMasterAgents().remove(masterAgent);
			
			if(MasterAgentDAO.getInstance().getStartedMasterAgents().size()==0) {
				for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size();i++) {
					if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Master")) {
						AgentTypeDAO.getInstance().getAgentTypes().remove(i);
						break;
					}
				}	
			}
				
					
		}
		else if (aid.getType().getName().equals("Collector")) {
			CollectorAgent collectorAgent = CollectorAgentDAO.getInstance().findByName(aid.getName());
			if (collectorAgent != null)
				CollectorAgentDAO.getInstance().getStartedCollectorAgents().remove(collectorAgent);
			if(CollectorAgentDAO.getInstance().getStartedCollectorAgents().size()== 0){
				for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size();i++) {
					if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Collector")) {
						AgentTypeDAO.getInstance().getAgentTypes().remove(i);
						break;
					}
				}	
			}
				
		}
		else if (aid.getType().getName().equals("Predictor")) {
			PredictorAgent predictorAgent = PredictorAgentDAO.getInstance().findByName(aid.getName());
			if (predictorAgent != null)
				PredictorAgentDAO.getInstance().getStartedPredictorAgents().remove(predictorAgent);

			if(PredictorAgentDAO.getInstance().getStartedPredictorAgents().size()== 0){
				for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size();i++) {
					if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Predictor")) {
						AgentTypeDAO.getInstance().getAgentTypes().remove(i);
						break;
					}
				}	
			}
		}else if(aid.getType().getName().equals("Ping")){
			PingAgent pingAgent = PingDAO.getInstance().findByName(aid.getName());
			if(pingAgent !=null)
				PingDAO.getInstance().getStartedPingAgents().remove(pingAgent);
			
			if(PingDAO.getInstance().getStartedPingAgents().size()==0)
				for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size(); i++) {
					if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Ping")) {
						AgentTypeDAO.getInstance().getAgentTypes().remove(i);
						break;
					}
					
				}
		}
		else if(aid.getType().getName().equals("Pong")){
			PongAgent pongAgent = PongDAO.getInstance().findByName(aid.getName());
			if(pongAgent != null)
				PongDAO.getInstance().getStartedPongAgents().remove(pongAgent);
			
			if(PongDAO.getInstance().getStartedPongAgents().size()==0) {
				for(int i = 0; i < AgentTypeDAO.getInstance().getAgentTypes().size(); i++) {
					if(AgentTypeDAO.getInstance().getAgentTypes().get(i).equals("Pong")) {
						AgentTypeDAO.getInstance().getAgentTypes().remove(i);
						break;
					}
				
				}
			}
		}
		else 
			return null;
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueSender qsender = session.createSender(queue);
			// create and publish a message
			TextMessage mess = session.createTextMessage();
			mess.setText("Stopped agent");
			qsender.send(mess);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		ResponseClass rc = new ResponseClass();
		rc.setText("Successfully deleted");
		return rc;
	}
	
	@Lock(LockType.READ)
	@Schedule(hour = "*", minute = "*", second = "*/20", persistent = false)
	public void checkOtherNodes() {
		System.out.println("**** Check other nodes ****");
		for (int i = 0; i < AgentCenterDAO.getInstance().getAgentCenters().size(); i++) {
			AgentCenter ac = AgentCenterDAO.getInstance().getAgentCenters().get(i);
			if (!ac.getAddress().equals(NetworkData.getInstance().getAddress())) {
				System.out.println("**** Usao ****");
				try {
					ResteasyClient client = new ResteasyClientBuilder().build();
			    	String http = "http://" + ac.getAddress() + ":8080/TenisWAR/rest/node";
			    	System.out.println(http);
			    	ResteasyWebTarget target = client.target(http);
			    	Response response = target.request().get();
			    	ResponseClass ret = response.readEntity(ResponseClass.class);
			    	System.out.println(ret.getText());
				}
				catch (Exception e){
					try {
						ResteasyClient client = new ResteasyClientBuilder().build();
				    	String http = "http://" + ac.getAddress() + ":8080/TenisWAR/rest/node";
				    	System.out.println(http);
				    	ResteasyWebTarget target = client.target(http);
				    	Response response = target.request().get();
				    	ResponseClass ret = response.readEntity(ResponseClass.class);
				    	System.out.println(ret.getText());
					}
					catch (Exception ex){
						System.out.println("Delete node");
						System.out.println(ex.getMessage());
						if (NetworkData.getInstance().getAddress().equals(NetworkData.MASTER_ADRESS)) {
							System.out.println("----- Send data for deleting -----");
							for (AgentCenter a : AgentCenterDAO.getInstance().getAgentCenters()) {
								if (a.getAlias().equals(ac.getAlias()))
									continue;
								ResteasyClient client = new ResteasyClientBuilder().build();
						    	String http = "http://" + a.getAddress() + ":8080/TenisWAR/rest/node/" + ac.getAlias();
						    	System.out.println(http);
						    	ResteasyWebTarget target = client.target(http);
						    	Response response = target.request().delete();
						    	ResponseClass ret = response.readEntity(ResponseClass.class);
						    	System.out.println(ret.getText());
						    	
						    	try {
									QueueConnection connection = (QueueConnection) connectionFactory.createConnection("guest", "guest.guest.1");
									QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
									QueueSender sender = session.createSender(queue);
									// create and publish a message
									TextMessage mess = session.createTextMessage();
									mess.setText("Agent center: " + ac.getAlias() + " (" + ac.getAddress() + ") " + "stopped!");
									sender.send(mess);
								} catch (Exception exc) {
									System.out.println("##### Exception #####");
									exc.printStackTrace();
								}
								
						    	
						    	System.out.println(MasterAgentDAO.getInstance().getAllMasterAgents().size() + " " + 
										   MasterAgentDAO.getInstance().getStartedMasterAgents().size() + " # " + 
										   CollectorAgentDAO.getInstance().getAllCollectorAgents().size() + " " +
										   CollectorAgentDAO.getInstance().getStartedCollectorAgents().size() + " # " +
										   PredictorAgentDAO.getInstance().getAllPredictorAgents().size() + " " + 
										   PredictorAgentDAO.getInstance().getStartedPredictorAgents().size() + " # " +
										   AgentCenterDAO.getInstance().getAgentCenters().size());
							}
						}
					}
				}
			}
		}
	}
	

}
