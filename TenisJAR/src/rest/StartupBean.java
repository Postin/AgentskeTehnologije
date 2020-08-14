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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import dao.AgentCenterDAO;
import dao.CollectorAgentDAO;
import dao.MasterAgentDAO;
import dao.PredictorAgentDAO;
import model.AgentCenter;
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
			            if(ip.getHostAddress().contains("192.168.56")) {
			            	ac.setAddress(ip.getHostAddress());
				            if(ip.getHostAddress().contains("192.168.56.1")) {
				            	ac.setAlias("Master");
				            }
				            else {
				            	ac.setAlias(element.getDisplayName());
				            	ResteasyClient client = new ResteasyClientBuilder().build();
				            	ResteasyWebTarget target = client.target("http://192.168.56.1:8080/TenisWAR/rest/node");
				            	Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(ac, MediaType.APPLICATION_JSON));
				            	ResponseClass ret = response.readEntity(ResponseClass.class);
				            	System.out.println(ret.getText());
				            }
			            }
			        }
			    }
			}
        }
		System.out.println(ac);
		AgentCenterDAO.getInstance().getAgentCenters().add(ac);
		System.out.println(MasterAgentDAO.getInstance().getAllMasterAgents().size() + " " + 
						   MasterAgentDAO.getInstance().getStartedMasterAgents().size() + " # " + 
						   CollectorAgentDAO.getInstance().getAllCollectorAgents().size() + " " +
						   CollectorAgentDAO.getInstance().getStartedCollectorAgents().size() + " # " +
						   PredictorAgentDAO.getInstance().getAllPredictorAgents().size() + " " + 
						   PredictorAgentDAO.getInstance().getStartedPredictorAgents().size() + " # " +
						   AgentCenterDAO.getInstance().getAgentCenters().size());
	
	}

}
