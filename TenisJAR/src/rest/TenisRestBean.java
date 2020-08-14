package rest;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dao.CollectorAgentDAO;
import dao.MasterAgentDAO;
import dao.MessageDAO;
import dao.PredictorAgentDAO;
import dto.PerformativeDTO;
import model.ACLMessage;
import model.AID;
import model.AgentType;
import model.CollectorAgent;
import model.MasterAgent;
import model.Performative;
import model.PredictorAgent;
import model.StringRequest;

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
	public String runAgent(StringRequest sr, @PathParam("type") String type, @PathParam("name") String name) {
		// TODO Auto-generated method stub
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
				return "Success";
			}
			else if (MasterAgentDAO.getInstance().getStartedMasterAgents().contains(masterAgent)) {
				return "Already running";
			}
			else {
				try {
					MasterAgentDAO.getInstance().addNewAgent(name);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "Agent with this data does not exist, so we created a new master agent";
			}
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
				return "Success";
			}
			else if (CollectorAgentDAO.getInstance().getStartedCollectorAgents().contains(collectorAgent)) {
				return "Already running";
			}
			else{
				try {
					CollectorAgentDAO.getInstance().newAgent(name);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "Agent with this data does not exist, so we created a new collector agent";
			}
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
				return "Success";
			}
			else if (PredictorAgentDAO.getInstance().getStartedPredictorAgents().contains(predictorAgent)) {
				return "Already running";
			}
			else{
				try {
					PredictorAgentDAO.getInstance().addNewAgent(name);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "Agent with this data does not exist, so we created a new predictor agent";
			}
		}
		return "Something went wrong";
	}

	@DELETE
	@Path("/running")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String stopAgent(AID aid) {
		// TODO Auto-generated method stub
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
					return "Stopped";
				}
					
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
					return "Stopped";
				}
					
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
					return "Stopped";
				}
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
		m.setReplyBy(aclMessage.getReplyBy());
		m.setReplyTo(aclMessage.getReplyTo());
		m.setReplyWith(aclMessage.getReplyWith());
		
	
		AID sender = aclMessage.getSender();
		List<AID> receivers = new ArrayList<AID>();
		
		//List<AID> activeAgents = new ArrayList<AID>();
		
		List<CollectorAgent> collectorAgents = CollectorAgentDAO.getInstance().getStartedCollectorAgents();
		List<MasterAgent> masterAgents = MasterAgentDAO.getInstance().getStartedMasterAgents();
		List<PredictorAgent> predictorAgents = PredictorAgentDAO.getInstance().getStartedPredictorAgents();
		
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

}
