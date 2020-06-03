package rest;

import java.util.List;

import model.ACLMessage;
import model.AID;
import model.Performative;

public interface TenisRest {

	public String runAgent(String type, String name);
	
	public String stopAgent(AID aid);
	
	public String test();
	
	public String sendACLMessage(ACLMessage aclMessage);
	
	public List<Performative> getPerformatives();
}
