package rest;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
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

@Startup
@Singleton
@Path("")
@LocalBean
public class StartupBean {
	
	@PostConstruct
	public void init() throws UnknownHostException {
		AgentCenter ac = new AgentCenter();
		Enumeration<NetworkInterface> net = null;
        try { 
            net = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
        	e.printStackTrace();
        }
        
        while(net.hasMoreElements()){
            NetworkInterface element = net.nextElement();
            // loop through and print the IPs
			Enumeration<InetAddress> addresses = element.getInetAddresses();
			while (addresses.hasMoreElements()){
			    InetAddress ip = addresses.nextElement();
			    if (ip instanceof Inet4Address){
			        if (ip.isSiteLocalAddress()){
			            System.out.println(element.getDisplayName() + " - " + ip.getHostAddress());
			            if(ip.getHostAddress().contains("192.168.")) {
			            	ac.setAddress(ip.getHostAddress());
				            if(ip.getHostAddress().contains(NetworkData.MASTER_ADRESS)) {
				            	ac.setAlias("Master");
				        		AgentCenterDAO.getInstance().getAgentCenters().add(ac);
				            }
				            else {
				            	InetAddress inetAddress = InetAddress.getLocalHost();
				            	ac.setAlias(inetAddress.getHostName());
				            }
			            }
			        }
			    }
			}
        }
        if(!ac.getAlias().equals("Master")) {
        	ResteasyClient client = new ResteasyClientBuilder().build();
        	String http = "http://" + NetworkData.MASTER_ADRESS + ":8080/TenisWAR/rest/node";
        	System.out.println(http);
        	ResteasyWebTarget target = client.target(http);
        	Response response = target.request().post(Entity.entity(ac, "application/json"));
        	ResponseClass ret = response.readEntity(ResponseClass.class);
        	System.out.println(ret.getText());
			
			client = new ResteasyClientBuilder().build();
        	http = "http://"+ NetworkData.MASTER_ADRESS +":8080/TenisWAR/rest/node/allAgents";
        	System.out.println(http);
        	target = client.target(http);
        	response = target.request().get();
        	AgentsClass agentsClass = response.readEntity(AgentsClass.class);
        	MasterAgentDAO.getInstance().setAllMasterAgents(agentsClass.getAllMasterAgents());
    		MasterAgentDAO.getInstance().setStartedMasterAgents(agentsClass.getStartedMasterAgents());
    		CollectorAgentDAO.getInstance().setAllCollectorAgents(agentsClass.getAllCollectorAgents());
    		CollectorAgentDAO.getInstance().setStartedCollectorAgents(agentsClass.getStartedCollectorAgents());
    		PredictorAgentDAO.getInstance().setAllPredictorAgents(agentsClass.getAllPredictorAgents());
    		PredictorAgentDAO.getInstance().setStartedPredictorAgents(agentsClass.getStartedPredictorAgents());
    		AgentCenterDAO.getInstance().setAgentCenters(agentsClass.getAgentCenters());
        }
        
		System.out.println(ac);
		System.out.println(MasterAgentDAO.getInstance().getAllMasterAgents().size() + " " + 
						   MasterAgentDAO.getInstance().getStartedMasterAgents().size() + " # " + 
						   CollectorAgentDAO.getInstance().getAllCollectorAgents().size() + " " +
						   CollectorAgentDAO.getInstance().getStartedCollectorAgents().size() + " # " +
						   PredictorAgentDAO.getInstance().getAllPredictorAgents().size() + " " + 
						   PredictorAgentDAO.getInstance().getStartedPredictorAgents().size() + " # " +
						   AgentCenterDAO.getInstance().getAgentCenters().size());
	
	}
	
	

}
